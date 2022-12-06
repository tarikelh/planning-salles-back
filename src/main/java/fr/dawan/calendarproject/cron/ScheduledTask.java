package fr.dawan.calendarproject.cron;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import fr.dawan.calendarproject.services.SyncService;

@Component
public class ScheduledTask {

	private static Logger LOGGER = LoggerFactory.getLogger(ScheduledTask.class);
	// Autowired des services nécessaires pour réaliser votre traitement

	@Autowired
	SyncService syncService;

	// Seconds Minutes Hours Day Of Month Month Day Of Week Year
	@Scheduled(cron = "00 03 17 1/1 * ?") // chaque jour à 7h du matin
	@Async("myTasksExecutor")
	public void AutoSyncDG2Task() {
		try {
			LOGGER.info(syncService.allDG2Import());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
}
