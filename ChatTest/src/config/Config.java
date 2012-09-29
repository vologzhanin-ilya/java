package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class Config {
	private static final Logger log = Logger.getLogger( Config.class.getName() );
	
	// param connect
	private String port;
	
	public Config(String filename) throws IOException {
		Properties properties = new Properties();
		FileInputStream fis = new FileInputStream(filename);
		log.info("InputStream properties is: " + filename);
		properties.load(fis);
		fis.close();

		//configuration for connect 
		port = properties.getProperty("port");
		
		log.info("\nRead configuration for connection:\n port = " + port);
	}
	
	public String getPort() {
		return port;
	}
}
