package shoppingMall.gupang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
public class GupangApplication {

	public static void main(String[] args) {
		SpringApplication.run(GupangApplication.class, args);
	}

}
