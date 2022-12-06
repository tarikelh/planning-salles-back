package fr.dawan.calendarproject.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
public class SyncReport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = true, columnDefinition = "TIMESTAMP")
	private LocalDateTime startOfSync;

	@Column(nullable = true, columnDefinition = "TIMESTAMP")
	private LocalDateTime endOfSync;

	@Column(nullable = false, columnDefinition = "DATE")
	private LocalDate start;

	@Column(nullable = false, columnDefinition = "DATE")
	private LocalDate end;

	@Column(nullable = false)
	private String message;

	@Column(nullable = false)
	private boolean failed = false;
	
	@Column(nullable = false)
	private long durationInMilli;

	@Version
	private int version;

	public SyncReport() {
	}

	public SyncReport(long id, LocalDateTime startOfSync, LocalDateTime endOfSync, LocalDate start, LocalDate end,
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

	@Override
	public String toString() {
		return "SyncReport [id=" + id + ", startOfSync=" + startOfSync + ", endOfSync=" + endOfSync + ", start=" + start
				+ ", end=" + end + ", message=" + message + ", failed=" + failed + ", version=" + version + "]";
	}
}
