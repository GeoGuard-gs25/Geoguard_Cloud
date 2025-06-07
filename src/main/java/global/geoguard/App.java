package global.geoguard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableCaching
@OpenAPIDefinition(info = @Info(title = "GeoGuard API", description = "This API is used to make notification, keep data and users crud", version = "v1"))
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}
