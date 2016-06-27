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

import java.io.File;
import java.io.FileNotFoundException;

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

        Option configFile = Option.builder("f")
                .argName("config-file")
                .hasArg(false)
                .longOpt("file")
                .desc("By default MMS looks for mms.config in the current directory. " +
                        "This Property file contains GCM, Database, SSL details. " +
                        "If the default mms.config is not found, then, it must be provided by -f or --file option. "+
                        "When both are absent, MMS will exit")
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
        String file = null;
        if( commandLine.hasOption("f") || commandLine.hasOption("file") ) {
            logger.debug("option -f found");

            file = commandLine.getOptionValue("f");
            if (file == null) {
                file = commandLine.getOptionValue("file");
            }
        }else if (file == null){
            // config file is not provided.
            // fallback to be default
            file = "mms.config";
        }

        // check whether "mms.config" file is available in the current directory
        boolean configFileFound = false;
        File configFile = new File(file);
        if( configFile.exists() && configFile.canRead() ){
            configFileFound = true;
        }else{
            // error condition
            // -f/--file option is not provided and default config file doesn't exist
            throw new FileNotFoundException("config file neither provided through -f or --file option nor default 'mms.config' file exists in the current directory.");
        }
    }

    /** process the parameters */
    public void process(){


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
