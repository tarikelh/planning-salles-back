package fr.dawan.calendarproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import fr.dawan.calendarproject.interceptors.TokenInterceptor;

//Classe de démarrage >> Spring scannera fr.dawan.calendarproject et tout ces sous packages

@SpringBootApplication
public class CalendarprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalendarprojectApplication.class, args);
	}

	@Autowired
	private TokenInterceptor tokenInterceptor;

	@Bean
	public WebMvcConfigurer myMvcConfigurer() {

		return new WebMvcConfigurer() {

			// CROSS ORIGIN
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedHeaders("*").allowedMethods("GET", "POST", "PUT", "DELETE").allowedOrigins("*");
			}

			// CONVERTERS
			@Override
			public void addFormatters(FormatterRegistry registry) {
				registry.addConverter(new fr.dawan.calendarproject.dto.StringToUserDtoConverter());
			}

			// Intercepteurs
			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				//registry.addInterceptor(tokenInterceptor);
			}

			// MATRIX
		};
	}
}