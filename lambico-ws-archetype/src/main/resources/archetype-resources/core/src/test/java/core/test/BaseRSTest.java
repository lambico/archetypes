#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * Copyright (C) 2013 Lambico Team <info@lambico.org>
 *
 * This file is part of Lambico WS Archetype Template - Core.
 */
package ${package}.core.test;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import java.util.ArrayList;
import java.util.List;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.RequestHandler;
import org.apache.cxf.jaxrs.lifecycle.ResourceProvider;
import org.apache.cxf.transport.local.LocalConduit;
import org.junit.After;
import org.junit.Before;
import org.lambico.test.spring.hibernate.junit4.AbstractBaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

/**
 * A base class for web service tests.
 *
 * @author Lucio Benfante <lucio@benfante.com>
 */
@ContextConfiguration(locations = {"/${packageInPathFormat}/core/rest/client-beans-test.xml"})
public abstract class BaseRSTest extends AbstractBaseTest {

    public static final String DEFAULT_ACCEPT_TYPE = "application/xml";
    private Server server;
    @Autowired(required = false)
    @Qualifier("securityFilter")
    private RequestHandler securityFilter;

    @Before
    public void startServer() throws Exception {
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setProviders(getServerProviders());
        sf.setResourceClasses(getServerResourceClasses());
        sf.setResourceProviders(getServerResourceProviders());
        sf.setAddress(getServerEndpointAddress());
        sf.getOutInterceptors().add(new LoggingOutInterceptor());
        sf.getInInterceptors().add(new LoggingInInterceptor());
        this.server = sf.create();
    }

    @After
    public void stopServer() throws Exception {
        server.stop();
        server.destroy();
    }
    
    protected List<Object> getServerProviders() {
        List<Object> providers = new ArrayList<Object>();
        if (securityFilter != null) {
            providers.add(securityFilter);
        }
        providers.add(new JacksonJaxbJsonProvider());
        return providers;
    }
    
    abstract protected String getServerEndpointAddress();
    
    abstract protected List<Class<?>> getServerResourceClasses();
    
    abstract protected List<ResourceProvider> getServerResourceProviders();

    private String buildBasicAuthorizationHeader(String username, String password) {
        String authorizationHeader = "Basic "
                + org.apache.cxf.common.util.Base64Utility.encode((username + ":" + password).getBytes());
        return authorizationHeader;
    }

    protected void setAuthorizationHeader(String username, String password, Client client) {
        String authorizationHeader = buildBasicAuthorizationHeader(username, password);
        client.header("Authorization", authorizationHeader);
    }

    protected WebClient createWebClient(String endpointAddress, String username, String password, String acceptType) {
        WebClient client = WebClient.create(endpointAddress);
        configureClient(client, username, password, acceptType);
        return client;
    }

    protected <T> T createProxyClient(Class<T> resourceClass, String endpointAddress, String username, String password, String acceptType) {
        List<Object> providers = new ArrayList<Object>();
        providers.add(new JacksonJaxbJsonProvider());
        T proxy = JAXRSClientFactory.create(endpointAddress, resourceClass, providers);
        Client client = WebClient.client(proxy);
        configureClient(client, username, password, acceptType);
        return proxy;
    }

    protected void configureClient(Client client, String username, String password, String acceptType) {
        configureClientDirectDispatch(client, isDirectlyDispatched());
        configureClientAcceptType(client, acceptType);
        configureClientAuthorization(client, username, password);
    }

    protected void configureClientDirectDispatch(Client client, boolean directlyDispatched) {
        WebClient.getConfig(client).getRequestContext().put(LocalConduit.DIRECT_DISPATCH, directlyDispatched);
    }

    protected boolean isDirectlyDispatched() {
        return true;
    }

    protected void configureClientAcceptType(Client client, String acceptType) {
        if (acceptType == null) {
            acceptType = DEFAULT_ACCEPT_TYPE;
        }
        client.accept(acceptType);
    }

    protected void configureClientAuthorization(Client client, String username, String password) {
        if (username != null && password != null) {
            setAuthorizationHeader(username, password, client);
        }
    }
}
