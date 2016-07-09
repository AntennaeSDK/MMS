/*
 * Copyright 2015 the original author or authors.
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

package com.github.antennaesdk.messageserver.config;

import com.github.antennaesdk.common.beans.AppInfo;
import com.github.antennaesdk.common.beans.DeviceInfo;
import com.github.antennaesdk.messageserver.cli.InputParameters;
import com.github.antennaesdk.messageserver.ws.WebSocketConfig;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@Import( {H2Config.class, GcmXmppConfig.class, WebSocketConfig.class} )
public class ApplicationConfig {


    public static final Class[] entityClasses = {  DeviceInfo.class, AppInfo.class };
	
	@Bean(name = "viewResolver")
    public InternalResourceViewResolver getViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }
	
    private Properties getHibernateProperties() {
    	Properties properties = new Properties();
    	properties.put("hibernate.show_sql", "true");
    	properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
    	return properties;
    }
    
    @Autowired
    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory(DataSource dataSource) {
    	LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
    	sessionBuilder.addProperties(getHibernateProperties());

        for( Class clz : entityClasses ){
            sessionBuilder.addAnnotatedClass(clz);
        }
    	
    	return sessionBuilder.buildSessionFactory();
    }


	@Autowired
	@Bean(name = "transactionManager")
	public HibernateTransactionManager getTransactionManager( SessionFactory sessionFactory) {
		
		HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);
		return transactionManager;
	}
	
	@Bean
    public ServletContextAware endpointExporterInitializer(final ApplicationContext applicationContext) {
        return new ServletContextAware() {
            @Override
            public void setServletContext(ServletContext servletContext) {
                ServerEndpointExporter exporter = new ServerEndpointExporter();
                exporter.setApplicationContext(applicationContext);
                exporter.afterPropertiesSet();
            }
        };
    }

    @Bean
    public Integer httpPort() {
        //return SocketUtils.findAvailableTcpPort();
        return InputParameters.getInstance().getHttpPort();
    }

    @Bean
    public Integer httpsPort(){
        return InputParameters.getInstance().getHttpsPort();
    }


    // DONT USE PORT 9090 , since that port is used by H2 in-memory DB
    @Bean
    public EmbeddedServletContainerFactory servletContainer() {

        // Standard port is created by the constructor
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory( httpPort() );

        if( InputParameters.getInstance().isSslEnabled()) {
            tomcat.addAdditionalTomcatConnectors(createSslConnector());
        }

        return tomcat;
    }


    private Connector createSslConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        InputParameters inputParameters = InputParameters.getInstance();

        //File keystore = new ClassPathResource("keystore").getFile();
        File keystore = new File( inputParameters.getConfigDir(), inputParameters.getKeyStoreFile());
        //File keystore = new ClassPathResource( inputParameters.getKeyStoreFile()).getFile();

        //File truststore = new ClassPathResource("keystore").getFile();
        File truststore = new File( inputParameters.getConfigDir(), inputParameters.getKeyStoreFile());
        //File truststore = new ClassPathResource(inputParameters.getKeyStoreFile()).getFile();

        connector.setScheme("https");
        connector.setSecure(true);
        connector.setPort( httpsPort() );
        protocol.setSSLEnabled(true);
        protocol.setKeystoreFile(keystore.getAbsolutePath());
        //protocol.setKeystorePass("changeit");
        protocol.setKeyPass( inputParameters.getKeyStorePassword());
        protocol.setTruststoreFile(truststore.getAbsolutePath());
        //protocol.setTruststorePass("changeit");
        protocol.setTruststorePass( inputParameters.getKeyStorePassword());
        //protocol.setKeyAlias("apitester");
        protocol.setKeyAlias( inputParameters.getKeyStoreKeyName());

        return connector;
    }
}
