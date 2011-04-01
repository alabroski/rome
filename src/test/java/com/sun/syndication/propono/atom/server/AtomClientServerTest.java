/*
 * Copyright 2007 Sun Microsystems, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.sun.syndication.propono.atom.server;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level; 
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.mortbay.http.HttpContext;
import org.mortbay.http.HttpServer;
import org.mortbay.http.SocketListener;
import org.mortbay.jetty.servlet.ServletHandler;
 

/**
 * Test Propono Atom Client against Atom Server via Jetty. Extends 
 * <code>AtomClientTest</code> to start Jetty server, run tests and then stop
 * the Jetty server.
 */
public class AtomClientServerTest { // extends AtomClientTest {

    private HttpServer server;
    public static final int TESTPORT = 8283;
    public static final String ENDPOINT = "http://localhost:" + TESTPORT + "/rome/app";
    public static final String USERNAME = "admin";
    public static final String PASSWORD = "admin";

    public AtomClientServerTest(String s) {
        //super(s);
    }

    public String getEndpoint() {
        return ENDPOINT;
    }

    public String getUsername() {
        return USERNAME;
    }

    public String getPassword() {
        return PASSWORD;
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(AtomClientServerTest.class);
        return suite;
    }

    protected HttpServer getServer() {
        return server;
    }

    protected void setUp() throws Exception {
        ConsoleHandler handler = new ConsoleHandler();
        Logger logger = Logger.getLogger("com.sun.syndication.propono");
        logger.setLevel(Level.FINEST);
        logger.addHandler(handler);

        setupServer();
        HttpContext context = createContext();
        ServletHandler servlets = createServletHandler();
        context.addHandler(servlets);
        server.addContext(context);
        server.start();
    }

    private void setupServer() throws InterruptedException {
        // Create the server
        if (server != null) {
            server.stop();
            server = null;
        }
        server = new HttpServer();

        // Create a port listener
        SocketListener listener = new SocketListener();
        listener.setPort(TESTPORT);
        server.addListener(listener);
    }

    private ServletHandler createServletHandler() {
        System.setProperty(
                "com.sun.syndication.propono.atom.server.AtomHandlerFactory",
                "com.sun.syndication.propono.atom.server.TestAtomHandlerFactory");
        ServletHandler servlets = new ServletHandler();
        servlets.addServlet(
                "app", "/app/*",
                "com.sun.syndication.propono.atom.server.AtomServlet");
        return servlets;
    }

    private HttpContext createContext() {
        HttpContext context = new HttpContext();
        context.setContextPath("/rome/*");
        return context;
    }

    protected void tearDown() throws Exception {
        if (server != null) {
            server.stop();
            server.destroy();
            server = null;
        }
    }
}


