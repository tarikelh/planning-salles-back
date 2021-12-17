package fr.dawan.calendarproject.tools;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Use this configuration to have some methods asynchrone
 */
@Configuration
@EnableAsync   //activation du mode asynchrone
public class AsyncConfiguration {

	@Bean(name = "taskExecutor")
	public Executor asyncExecutor() {
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(6);
		executor.setMaxPoolSize(6);
		executor.setQueueCapacity(150);
		executor.setThreadNamePrefix("AsyncThread-");
		executor.initialize();
		return executor;
	}
}
