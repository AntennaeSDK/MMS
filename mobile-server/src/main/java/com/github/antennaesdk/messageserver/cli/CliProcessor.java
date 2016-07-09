/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.antennaesdk.messageserver.cli;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * <code>CliProcessor</code> parses the command line arguments.
 *
 * <ul>
 *    <li>parses the command line arguments</li>
 *    <li>fetches the values</li>
 *    <li>prints usage</li>
 * </ul>
 */
public class CliProcessor {

    private static final Logger logger = LoggerFactory.getLogger(CliProcessor.class);

    private String[] args;
    private Options options;
    private CommandLine commandLine;

    private boolean isSuccess = false;

    public CliProcessor(String[] args ){

        this.args = args;

        // create the command line parser
        CommandLineParser parser = new DefaultParser();

        // create the Options
        options = new Options();

        Option port = Option.builder("p")
                .argName("port")
                .hasArg(true)
                .longOpt("port")
                .desc("port number to start the application. default is 8080")
                .required(false)
                .build();

        Option configFile = Option.builder("c")
                .argName("config-folder")
                .hasArg(true)
                .longOpt("config")
                .desc(  "by default MMS will look for 'config' folder in the current directory.\n" +
                        "config folder contains all config files needed by MMS " +
                        "such as mms.config file and keystore files.\n" +
                        "mms.config file contains GCM, Database, SSL details. " +
                        "When mms.config file is absent or details are missing, MMS will exit")
                .required(false)
                .build();

        Option generateConfig = Option.builder("g")
                .argName("generate-config")
                .hasArg(false)
                .longOpt("generate-config")
                .desc("generates a sample config folder with config files in it.")
                .required(false)
                .build();

        Option help = Option.builder("h")
                .argName("usage")
                .hasArg(false)
                .longOpt("help")
                .desc("displays usage")
                .required(false)
                .build();


        // add the options
        options.addOption(port);
        options.addOption(configFile);
        options.addOption(generateConfig);
        options.addOption(help);
    }

    public void parse() throws ParseException, FileNotFoundException {

        CommandLineParser parser = new DefaultParser();
        commandLine = parser.parse(options, args);

        InputParameters inputParameters = InputParameters.getInstance();

        // parse help and generate options
        // usually these options are processed right away and program exits
        parseHelpAndGenerateConfig();

        // parse runtime options
        parseConfig(inputParameters);
        parsePort(inputParameters);
    }

    // check whether "-c" or "--config" option is provided
    private void parseConfig( InputParameters inputParameters ) throws FileNotFoundException {

        String config = null;
        if( commandLine.hasOption('c') || commandLine.hasOption("config") ) {
            logger.debug("option -c found");

            config = commandLine.getOptionValue('c');
            if (config == null) {
                config = commandLine.getOptionValue("config");
            }

            if( config == null ){
                throw new FileNotFoundException("config folder provided through -c or --config is null.");
            }
        }else if (config == null){
            // config file is not provided.
            // fallback to be default
            config = "config";
        }

        // check whether "config" folder is available and readable
        boolean configDirFound = false;
        File configDir = new File(config);
        if( configDir.exists() && configDir.isDirectory() && configDir.canRead() ){
            configDirFound = true;
            inputParameters.setConfigDir(configDir.getAbsolutePath());
        }else{
            // error condition
            // -c/--config option is not provided and default config file doesn't exist
            throw new FileNotFoundException("config folder neither provided through -c or --config option nor default 'config' file exists in the current directory.");
        }
    }

    // check whether "-p" or "--port" option is provided
    public void parsePort( InputParameters inputParameters ) throws ParseException {

        if( commandLine.hasOption('p') || commandLine.hasOption("port")){
            logger.debug("option -p or --port found");

            String portStr = commandLine.getOptionValue('p');
            if( portStr == null ){
                portStr = commandLine.getOptionValue("port");
            }

            // convert port to a number
            int port = Integer.parseInt(portStr);
            if( port < 0 ){
                throw new ParseException("Port value cannot be less than 0");
            }
        }
    }

    /**
     * Process help and generate-config.
     * These are not runtime options, but helper functions to help user.
     * The system exits after this method
     */
    public void parseHelpAndGenerateConfig(){

        // "help" or "generate-config" cannot be combined with any other options
        // make sure this is true.
        if(  commandLine.hasOption('g') && commandLine.hasOption("h") ){

            printError("-g or --generate-config cannot be combined with -h or --help");
            printUsage( -1 );
        }
        if( ( commandLine.hasOption('h') && !commandLine.hasOption('g')) &&
                commandLine.getOptions().length > 1 ){
            printError("-h or --help cannot be combined with any other options");
            printUsage( -1 );
        }
        if( ( commandLine.hasOption('g') && !commandLine.hasOption('h')) &&
                commandLine.getOptions().length > 1 ){
            printError("-g or --generate-config cannot be combined with any other options");
            printUsage( -1 );
        }


        // check whether the "-g" or "--generate-config" option is provided
        if( commandLine.hasOption('g') || commandLine.hasOption("generate-config")){
            logger.debug("option -g or --generate-config found");

            // TODO: Generate the sample config
            generateConfig();
        }

        if( commandLine.hasOption('h') || commandLine.hasOption("help") ){
            printUsage(0);
        }
    }

    /** process the parameters */
    public void process() throws IOException {

        // get the config folder
        String configDir = InputParameters.getInstance().getConfigDir();

        // get the config file
        String config = InputParameters.getInstance().getConfigFile();

        File configFile = new File(configDir, config);
        if( !configFile.exists() || !configFile.canRead() ){
            throw new FileNotFoundException(configFile.getName() + " doesn't exist or not readable/");
        }

        logger.info("begin loading from config file " + config);
        Properties properties = new Properties();
        InputStream input = new FileInputStream(configFile);

        properties.load(input);

        // read the properties file store the values in InputParameters
        InputParameters inputParameters = InputParameters.getInstance();

        // set the GCM API values
        inputParameters.setGcmApiKey(properties.getProperty("gcm.server.apikey") );
        inputParameters.setGcmSenderId( properties.getProperty("gcm.sender.id"));
        inputParameters.setGcmHost( properties.getProperty("gcm.server.domain"));
        inputParameters.setGcmPreProdEndPoint( properties.getProperty("gcm.endpoint.pre-prod"));
        inputParameters.setGcmProdEndPoint( properties.getProperty("gcm.endpoint.prod"));

        // check whether SSL is enabled
        String sslEnabled = properties.getProperty("ssl.enabled");
        boolean ssl = Boolean.valueOf(sslEnabled);
        inputParameters.setSslEnabled( ssl );

        // set the http/https ports
        if( properties.getProperty("http.port") != null ) {
            int httpPort = Integer.valueOf(properties.getProperty("http.port"));
            inputParameters.setHttpPort( httpPort );
        }
        if( properties.getProperty("https.port") != null ) {
            int httpsPort = Integer.valueOf(properties.getProperty("https.port"));
            inputParameters.setHttpsPort( httpsPort );
        }


        // set H2 port number
        if( properties.getProperty("h2.port") != null ){
            int h2port = Integer.valueOf( properties.getProperty("h2.port"));
            inputParameters.setH2port(h2port);
        }

        // set the SSL values
        inputParameters.setKeyStoreFile(properties.getProperty("keystore.file"));
        inputParameters.setKeyStorePassword(properties.getProperty("keystore.password"));
        inputParameters.setKeyStoreKeyName(properties.getProperty("keystore.keyname"));

        logger.info("input processing complete ");

        // At the end make it success
        isSuccess =true;
    }

    /**
     * Generate a sample configuration file and java keystore files, then exit the program
     */
    public void generateConfig(){

        boolean canCreateFile = false;

        // check whether there is a config folder
        File configDir = new File("config");
        if( !configDir.exists() ){
            if( configDir.mkdir() ){
                canCreateFile = true;
            }else{
                printError("Unable to create \"config\" directory.");
            }
        }else if( configDir.isDirectory() ){
            if( !configDir.canWrite() ){
                printError("\"config\" directory is not writable.");
            }else{
                canCreateFile = true;
            }
        }else if( configDir.isFile() ){
            printError("file named \"config\" already exists. please rename or delete the file.");
        }

        // exit the program.
        if( canCreateFile == false){
            System.exit( -1 );
        }

        // check whether mms.config and keystore.jks files exist
        File mmsConfig = new File( configDir, "mms.config");
        File jksFile = new File( configDir, "keystore.jks");
        if( mmsConfig.exists() && jksFile.exists() ){
            printError("mms.config and keystore.jks files already exist.");
            System.exit( -1 );
        }
        if( mmsConfig.exists() ){
            printError("mms.config file already exists.");
            System.exit( -1 );
        }
        if( jksFile.exists() ){
            printError("keystore.jks file already exists.");
            System.exit( -1 );
        }

        // read and write the files from "sample-config".
        URL configUrl = CliProcessor.class.getResource("/sample-config/mms.config");
        try{

            // Java 8 style stream for reading and writing the config file.
            // read all the lines
            List<String> lines = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(CliProcessor.class.getResourceAsStream("/sample-config/mms.config")))) {
                Stream stream =br.lines();
                stream.forEach(new Consumer() {
                    @Override
                    public void accept(Object o) {
                        lines.add( (String) o);
                    }
                });
            }

            // write all the line
            Files.write( Paths.get(mmsConfig.toURI()), lines);
            printMessage("mms.config generated");


            // Use the old style for reading and writing the keystore file
            InputStream jskis = CliProcessor.class.getResourceAsStream("/sample-config/keystore.jks");
            OutputStream fileOutputStream = new FileOutputStream( jksFile.getAbsoluteFile());

            int byteCount;
            byte[] buffer = new byte[4096];
            while ((byteCount = jskis.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, byteCount);
            }

            printMessage("keystore.jks generated");
            System.exit(0);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean isSuccess() {
        return isSuccess;
    }
    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public void printUsage( int result){
        // automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "java -jar mms.war", options );

        System.exit( result );
    }

    public void printMessage(String msg){
        System.out.println("MESSAGE: "+ msg);
        System.out.flush();
    }
    public void printError( String msg ){
        System.err.println("ERROR: " + msg );
        System.err.flush();
    }
    public void printError( Throwable throwable ){
        System.err.println("Error proceesing input parameters: " + throwable.getMessage() );
        System.err.flush();
    }

    public static void main(String[] args ){

        CliProcessor cliProcessor = new CliProcessor(args);

        cliProcessor.printUsage(0);
    }

    public InputParameters getInputParameters() {

        InputParameters inputParameters = InputParameters.getInstance();
        return inputParameters;
    }
}
