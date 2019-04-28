package br.com.siscomanda.config.jpa;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PersistenceProperties {
	
	private static final String JDBC_URL = "JDBC_URL";
	private static final String JDBC_USER = "JDBC_USER";
	private static final String JDBC_PASSWORD = "JDBC_PASSWORD";
	
	public Properties get() {
		Properties props = new Properties();
		try {
			props.putAll(userHomeJDBCFile());
			props.putAll(systemEnv());
			props.putAll(javaPropertyJDBCFile());
			props.putAll(javaProperties());
			return props;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	private Properties userHomeJDBCFile() throws Exception {
        Properties props = new Properties();
        
        File fileProperties = new File(System.getProperty("user.home"), "jdbc.properties");
        
        if (fileProperties.exists()) {
            props.load(new FileInputStream(fileProperties));
        }
        
        return props;
    }
	
	private Properties systemEnv() {
        Properties props = new Properties();
        
        if (System.getenv().containsKey(JDBC_URL)) {
            props.put("javax.persistence.jdbc.url", System.getenv(JDBC_URL));
        }
        
        if (System.getenv().containsKey(JDBC_USER)) {
            props.put("javax.persistence.jdbc.user", System.getenv(JDBC_USER));
        }
        
        if (System.getenv().containsKey(JDBC_PASSWORD)) {
            props.put("javax.persistence.jdbc.password", System.getenv(JDBC_PASSWORD));
        }
        
        return props;
    }
	
	private Properties javaPropertyJDBCFile() throws Exception {
        Properties props = new Properties();
        
        if (!System.getProperties().containsKey("jdbc-file")) {
            return props;
        }
        
        File fileProperties = new File(System.getProperty("jdbc-file"));
        
        if (fileProperties.exists()) {
            props.load(new FileInputStream(fileProperties));
        }
        
        return props;
    }
	
	private Properties javaProperties() {
        Properties props = new Properties();
        
        if (System.getProperties().containsKey(JDBC_URL)) {
            props.put("javax.persistence.jdbc.url", System.getProperty(JDBC_URL));
        }
        
        if (System.getProperties().containsKey(JDBC_USER)) {
            props.put("javax.persistence.jdbc.user", System.getProperty(JDBC_USER));
        }
        
        if (System.getProperties().containsKey(JDBC_PASSWORD)) {
            props.put("javax.persistence.jdbc.password", System.getProperty(JDBC_PASSWORD));
        }
        
        return props;
    }
}
