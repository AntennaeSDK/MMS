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
package com.github.antennaesdk.messageserver;

import com.github.antennaesdk.messageserver.cli.CliProcessor;
import com.github.antennaesdk.messageserver.config.ApplicationConfig;
import org.apache.commons.cli.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * <code>MessageServerApplication</code> starts the "Mobile Messaging Server".
 *
 * It does the following,
 *
 * <ol>
 *     <li>reads the CLI arguments</li>
 *     <li>processes the CLI arguments</li>
 *     <li>Stores the arguments for spring beans to use during bean initialization (optional) </li>
 *     <li>Prints usage</li>
 * </ol>
 */
@SpringBootApplication
@EnableAutoConfiguration
@Import(ApplicationConfig.class)
@ComponentScan( basePackages = { "com.github.antennaesdk.common", "com.github.antennaesdk.messageserver"})
public class MessageServerApplication {
    public static void main(String[] args) {

        // validate and process input arguments
        processInputs(args);

        // start MMS
        SpringApplication.run( MessageServerApplication.class, args);
    }

    private static void processInputs( String[] args ){

        // TODO: parse the CLI and print USAGE if necessary
        // TODO: ability to pass GCM user,password, projectId
        // TODO: ability to change the port
        // TODO: ability add SSL connection
        // TODO: ability to pass DB connection details
        CliProcessor cliProcessor = new CliProcessor(args);

        try {

            cliProcessor.parse();
            cliProcessor.process();

        } catch (ParseException e) {
            cliProcessor.printError( e);
            cliProcessor.printUsage(-1);
        } catch (FileNotFoundException e) {
            cliProcessor.printError( e);
            cliProcessor.printUsage( -2);
        } catch (IOException e) {
            cliProcessor.printError( e);
            cliProcessor.printUsage(-3);
        }
    }
}
