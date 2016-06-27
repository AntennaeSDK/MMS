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

/**
 * <code>CliParser</code> parses the command line arguments.
 *
 * <ul>
 *    <li>parses the command line arguments</li>
 *    <li>fetches the values</li>
 *    <li>prints usage</li>
 * </ul>
 */
public class CliParser {

    private String[] args;
    private Options options;
    private CommandLine commandLine;

    public CliParser( String[] args ){

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
                .desc("Property file that contains GCM, DB, SSL details.\n" +
                        "When not provided the application will look for config.mms in the current directory" +
                        "\nIf the default config.mms is not found, then it would exit and print usage")
                .required(true)
                .build();

        // add the options

        //options.addOption(port);
        options.addOption(configFile);
    }

    public void parse() throws ParseException {
        CommandLineParser parser = new DefaultParser();
        commandLine = parser.parse(options, args);
    }

    public void printUsage(){
        // automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "java -jar mms.war", options );
    }

    public static void main(String[] args ){

        CliParser cliParser = new CliParser(args);

        cliParser.printUsage();
    }
}
