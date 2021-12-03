package fr.dawan.calendarproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import fr.dawan.calendarproject.interceptors.TokenInterceptor;

@SpringBootApplication
public class CalendarprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalendarprojectApplication.class, args);
	}

	@Autowired
	private TokenInterceptor tokenInterceptor;

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public WebMvcConfigurer myMvcConfigurer() {

		return new WebMvcConfigurer() {

			// CROSS ORIGIN
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "PUT", "DELETE",
						"OPTIONS");
			}

			// CONVERTERS
			@Override
			public void addFormatters(FormatterRegistry registry) {
				registry.addConverter(new fr.dawan.calendarproject.dto.StringToUserDtoConverter());
			}

			// INTERCEPTORS
			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				registry.addInterceptor(tokenInterceptor);
			}

			// MATRIX
		};
	}
}
