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
package com.github.antennaesdk.messageserver.db.H2;

import com.github.antennaesdk.messageserver.db.DBConnectionProperties;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.tool.hbm2ddl.DatabaseMetadata;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.Table;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.github.antennaesdk.messageserver.config.ApplicationConfig.entityClasses;

/**
 * Created by snambi on 6/25/16.
 */
@Component
public class H2Database {

    private static final Logger logger = LoggerFactory.getLogger(H2Database.class);

    private EmbeddedDatabase embeddedDatabase = null;

    public H2Database(){

        // TODO: move these to a common connection properties
        // TODO: add the ability to be passed from CLI
        String dbName = "mms";
        String dbUser = "sa";
        String dbPassword = "";
        String dbFileUrl = "jdbc:h2:file:~/." + dbName +"/" + dbName + ";AUTO_SERVER=TRUE;DB_CLOSE_ON_EXIT=FALSE;AUTO_SERVER_PORT=9090";
        String dbMemUrl = "jdbc:h2:mem:" + dbName + ";AUTO_SERVER=TRUE;DB_CLOSE_ON_EXIT=FALSE;AUTO_SERVER_PORT=9090";

        // check whether the DB is already created.
        String dbPath = System.getProperty("user.home");
        File dbFile = new File( dbPath + File.separator + "." + dbName + File.separator + dbName + ".mv.db");

        boolean isDbCreated = false;
        if( dbFile.exists() == true && dbFile.isFile() ==true ){
            isDbCreated = true;
        }

        // create new DB only if it doesn't exist
        if( isDbCreated == true ){
            dbFileUrl = dbFileUrl + ";IFEXISTS=TRUE";
        }

        H2SimpleDriverDatasourceFactory dataSourceFactory = new H2SimpleDriverDatasourceFactory();

        DBConnectionProperties connectionProperties = new DBConnectionProperties();
        connectionProperties.setUrl( dbFileUrl );
        connectionProperties.setDriverClass(org.h2.Driver.class);
        connectionProperties.setUsername( dbUser);
        connectionProperties.setPassword(dbPassword);

        org.h2.Driver driver = new org.h2.Driver();
        SimpleDriverDataSource datasource = new SimpleDriverDataSource( driver, dbFileUrl, dbUser, dbPassword );

        dataSourceFactory.setConnectionProperties(connectionProperties);
        dataSourceFactory.setDataSource(datasource);

        try {
            Connection h2connection = datasource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();

        builder.setType(EmbeddedDatabaseType.H2);
        builder.setDataSourceFactory( dataSourceFactory);
        builder.setName(dbName);

        // the current conf always recreates the DB, so create tables during startup

        if( isDbCreated == false ){
            logger.info("Creating the database");
//			builder.addScript("/db/h2/create-db.sql");
//			builder.addScript("/db/h2/insert-data.sql");
        }else{
            logger.info("Database found");
            // TODO: check whether tables are present
            // TODO: if the tables are not present create the tables
            generateSchemaAndCreateTables( datasource);
        }

        embeddedDatabase = builder.build();
    }

    @PostConstruct
    public void init(){

    }

    @PreDestroy
    public void shutdown(){
        logger.info("shutting down H2 database");
        embeddedDatabase.shutdown();
    }

    public EmbeddedDatabase getEmbeddedDatabase() {
        return embeddedDatabase;
    }

    public void setEmbeddedDatabase(EmbeddedDatabase embeddedDatabase) {
        this.embeddedDatabase = embeddedDatabase;
    }

    public void generateSchemaAndCreateTables(SimpleDriverDataSource dataSource){

        // Get the tables that are already in the DATABASE
        List<String> tables = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();
            DatabaseMetaData databaseMetadata =  connection.getMetaData();
            ResultSet resultSet = databaseMetadata.getTables(null, null, null, new String[]{"TABLE"});
            while( resultSet.next() ){
                String table = resultSet.getString(3);
                logger.info("Table : " + table + " ... exists");
                tables.add(table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        // Get the tables that are needed from Entity Classes
        List<Class> tablesToCreate = new ArrayList<>();
        for(Class<?> c : entityClasses ){
            // get the table names
            Table table = c.getAnnotation(Table.class);

            logger.info("Entity: " + c.getName() + " , Table: " + table.name() );
            boolean isExisting = false;
            for( String dbTable : tables) {
                if( dbTable.equals(table.name())){
                    isExisting = true;
                    break;
                }
            }

            if( !isExisting){
                // these tables must be created
                tablesToCreate.add(c);
            }
        }


        // Check whether the tables need to be created...
        if( tablesToCreate.size() == 0 ){
            logger.info("Tables already exist... ");
            return;
        }else{
            logger.info("Creating tables...");
        }


        //create a minimal configuration
        org.hibernate.cfg.Configuration cfg = new org.hibernate.cfg.Configuration();
        cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        cfg.setProperty("hibernate.hbm2ddl.auto", "create");

        // create a temporary file to write the DDL
        File ddlFile = null;
        try {
            File dir = getDirectoryFromClasspath();
            ddlFile = File.createTempFile("H2_", ".SQL", dir);
            ddlFile.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // add the tables to be created
        for( Class c: tablesToCreate ) {
            cfg.addAnnotatedClass(c);
        }

        //build all the mappings, before calling the AuditConfiguration
        cfg.buildMappings();
        cfg.getProperties().setProperty(AvailableSettings.HBM2DDL_IMPORT_FILES, ddlFile.getName());

        cfg.getProperties().setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        cfg.getProperties().setProperty("hibernate.connection.url", dataSource.getUrl());
        cfg.getProperties().setProperty("hibernate.connection.username", dataSource.getUsername());
        cfg.getProperties().setProperty("hibernate.connection.password", dataSource.getPassword());


        //execute the export
        SchemaExport export = new SchemaExport(cfg);

        export.setDelimiter(";");
        export.setFormat(true);
        // create the tables in the DB and show the DDL in console
        export.create(true, true);
    }

    private File getDirectoryFromClasspath(){

        File result=null;
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        URL[] urls = ((URLClassLoader)cl).getURLs();

        for( URL url : urls ){
            File f = new File( url.getFile());

            if(f.isDirectory() && f.canWrite() ){
                result = f;
                break;
            }
        }

        return result;
    }
}
