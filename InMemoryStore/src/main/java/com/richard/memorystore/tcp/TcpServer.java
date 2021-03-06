/*
 * Copyright 2002-2012 the original author or authors.
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
package com.richard.memorystore.tcp;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.util.TestingUtilities;
import org.springframework.integration.test.util.SocketUtils;

import com.richard.memorystore.processor.InputProcessor;
import com.richard.memorystore.rest.KeyValue;
import com.richard.memorystore.rest.KeyValueController;

public final class TcpServer {

	/**
	 * Load the Spring Integration Application Context
	 *
	 * @param args - command line arguments
	 */
	public static void main() {

		final Scanner scanner = new Scanner(System.in);

		System.out.println("\n========================================================="
						+ "\n                                                         "
						+ "\n    Welcome to the Spring Integration                    "
						+ "\n          TCP-Client-Server Sample!                      "
						+ "\n                                                         "
						+ "\n    For more information please visit:                   "
						+ "\n    http://www.springintegration.org/                    "
						+ "\n                                                         "
						+ "\n=========================================================" );

		final GenericXmlApplicationContext context = TcpServer.setupContext();
		final SimpleGateway gateway = context.getBean(SimpleGateway.class);
		final AbstractServerConnectionFactory crLfServer = context.getBean(AbstractServerConnectionFactory.class);

		System.out.print("Waiting for server to accept connections...");
		TestingUtilities.waitListening(crLfServer, 10000L);
		System.out.println("running.\n\n");

		System.out.println("Please enter some text and press <enter>: ");
		System.out.println("\tNote:");
		System.out.println("\t- Entering FAIL will create an exception");
		System.out.println("\t- Entering q will quit the application");
		System.out.print("\n");
		System.out.println("\t--> Please also check out the other samples, " +
						"that are provided as JUnit tests.");
		System.out.println("\t--> You can also connect to the server on port '" + crLfServer.getPort() + "' using Telnet.\n\n");

		InputProcessor lineProcessor = new InputProcessor();
		
		while (true) {

			final String input = scanner.nextLine();
			
			System.out.println("GOT THIS!!!" + input);
			
			KeyValueController keyValueController = new KeyValueController();
			
			KeyValue keyValue = lineProcessor.processLine(input);
			
			keyValueController.addToMemoryStore(keyValue.getKey(), keyValue.getValue());

			if("q".equals(input.trim())) {
				break;
			}
			else {
				final String result = gateway.send(input);
				System.out.println(result);
			}
		}

		System.out.println("Exiting application...bye.");
		System.exit(0);

	}

	public static GenericXmlApplicationContext setupContext() {
		final GenericXmlApplicationContext context = new GenericXmlApplicationContext();

		System.out.print("Detect open server socket...");
		int availableServerSocket = SocketUtils.findAvailableServerSocket(5678);

		final Map<String, Object> sockets = new HashMap<String, Object>();
		sockets.put("availableServerSocket", availableServerSocket);

		final MapPropertySource propertySource = new MapPropertySource("sockets", sockets);

		context.getEnvironment().getPropertySources().addLast(propertySource);

		System.out.println("using port " + context.getEnvironment().getProperty("availableServerSocket"));

		context.load("classpath:tcpClientServerDemo-context.xml");
		context.registerShutdownHook();
		context.refresh();

		return context;
	}
}
