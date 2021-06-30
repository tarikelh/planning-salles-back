package fr.dawan.calendarproject.dto;

import java.io.Serializable;

public class CountDto implements Serializable {

	private long nb;

	public CountDto() {

	}

	public CountDto(long nb) {
		this.nb = nb;
	}

	public long getNb() {
		return nb;
	}

	public void setNb(long nb) {
		this.nb = nb;
	}

}