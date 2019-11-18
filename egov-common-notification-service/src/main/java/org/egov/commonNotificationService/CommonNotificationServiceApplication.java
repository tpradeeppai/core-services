package org.egov.commonNotificationService;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.TimeZone;

@SpringBootApplication
@ComponentScan(basePackages = { "org.egov.commonNotificationService","org.egov.commonNotificationService.service","org.egov.commonNotificationService.consumer"})
@EntityScan("com.egov.commonNotificationService.models")
@EnableJpaRepositories("com.egov.commonNotificationService.repository")
@Import({ TracerConfiguration.class })
public class CommonNotificationServiceApplication {

	@Value("${app.timezone}")
	private String timeZone;

	@Bean
	public ObjectMapper objectMapper(){
		return new ObjectMapper()
				.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.setTimeZone(TimeZone.getTimeZone(timeZone));
	}


	public static void main(String[] args) {
		SpringApplication.run(CommonNotificationServiceApplication.class, args);
	}

}
