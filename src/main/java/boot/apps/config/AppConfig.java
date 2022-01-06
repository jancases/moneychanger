package boot.apps.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


@Configuration
public class AppConfig {
	
	Environment env;
	
	public AppConfig(Environment env) {
		this.env = env;
	}
	
	public String getProperty(String key) {
		return env.getProperty(key);
	}

}
