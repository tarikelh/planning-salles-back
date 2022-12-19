package fr.dawan.calendarproject.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SyncReportDto {
	private long id;

	private LocalDateTime startOfSync;

	private LocalDateTime endOfSync;

	private LocalDate start;

	private LocalDate end;

	private String message;

	private boolean failed;

	private long durationInMilli;

	private int version;
	
	public SyncReportDto() {
	}

	public SyncReportDto(long id, LocalDateTime startOfSync, LocalDateTime endOfSync, LocalDate start, LocalDate end,
			String message, boolean failed, long durationInMilli, int version) {
		this.id = id;
		this.startOfSync = startOfSync;
		this.endOfSync = endOfSync;
		this.start = start;
		this.end = end;
		this.message = message;
		this.failed = failed;
		this.durationInMilli = durationInMilli;
		this.version = version;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDateTime getStartOfSync() {
		return startOfSync;
	}

	public void setStartOfSync(LocalDateTime startOfSync) {
		this.startOfSync = startOfSync;
	}

	public LocalDateTime getEndOfSync() {
		return endOfSync;
	}

	public void setEndOfSync(LocalDateTime endOfSync) {
		this.endOfSync = endOfSync;
	}

	public LocalDate getStart() {
		return start;
	}

	public void setStart(LocalDate start) {
		this.start = start;
	}

	public LocalDate getEnd() {
		return end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isFailed() {
		return failed;
	}

	public void setFailed(boolean failed) {
		this.failed = failed;
	}

	public long getDurationInMilli() {
		return durationInMilli;
	}

	public void setDurationInMilli(long durationInMilli) {
		this.durationInMilli = durationInMilli;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}
