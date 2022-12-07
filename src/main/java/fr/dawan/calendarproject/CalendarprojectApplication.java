package fr.dawan.calendarproject;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import fr.dawan.calendarproject.interceptors.TokenInterceptor;

@SpringBootApplication
@EnableScheduling
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
	
	@Bean("myTasksExecutor")
	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(6);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("asyncThread-");
		executor.initialize();
		return executor;
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
