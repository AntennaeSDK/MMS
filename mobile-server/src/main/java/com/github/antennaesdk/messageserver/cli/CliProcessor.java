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
import java.util.Properties;

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
                .hasArg(false)
                .longOpt("config")
                .desc(  "by default MMS will look for 'config' folder in the current directory." +
                        "config folder contains all config files needed by MMS" +
                        "config folder mms.config and keystore files " +
                        "This mms.config contains GCM, Database, SSL details. " +
                        "When mms.config file is absent or details are missing, MMS will exit")
                .required(false)
                .build();

        // add the options

        //options.addOption(port);
        options.addOption(configFile);
    }

    public void parse() throws ParseException, FileNotFoundException {

        CommandLineParser parser = new DefaultParser();
        commandLine = parser.parse(options, args);

        InputParameters inputParameters = InputParameters.getInstance();

        // check whether "-f" option is provided
        String config = null;
        if( commandLine.hasOption("c") || commandLine.hasOption("config") ) {
            logger.debug("option -c found");

            config = commandLine.getOptionValue("c");
            if (config == null) {
                config = commandLine.getOptionValue("config");
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

    public boolean isSuccess() {
        return isSuccess;
    }
    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public void printUsage(){
        // automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "java -jar mms.war", options );

        System.exit(0);
    }

    public void printError( Throwable throwable ){
        System.err.println("Error proceesing input parameters: " + throwable.getMessage() );
        System.err.flush();
    }

    public static void main(String[] args ){

        CliProcessor cliProcessor = new CliProcessor(args);

        cliProcessor.printUsage();
    }

    public InputParameters getInputParameters() {

        InputParameters inputParameters = InputParameters.getInstance();
        return inputParameters;
    }
}
