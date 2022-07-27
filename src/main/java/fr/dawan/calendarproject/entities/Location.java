package fr.dawan.calendarproject.entities;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE) 
public class Location {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = true)
	private long idDg2;

	@Column(nullable = false, length = 255, unique = true)
	private String city;
	
	@Column(nullable = false, length = 4)
	private String countryCode;

	@Column(nullable = true, length = 9)
	private String color;

	private boolean published;
	
	@Version
	private int version;

	public Location() {
	}

	public Location(long id, String city, String contryCode, String color, boolean published,  int version) {
		setId(id);
		setCity(city);
		setCountryCode(contryCode);
		setColor(color);
		setPublished(published);
		setVersion(version);
	}

//	public Location(long id, String city, String contryCode, String color, long idDg2, int version) {
//		setId(id);
//		setCity(city);
//		setCountryCode(contryCode);
//		setColor(color);
//		setIdDg2(idDg2);
//		setVersion(version);
//	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String contryCode) {
		this.countryCode = contryCode;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public long getIdDg2() {
		return idDg2;
	}

	public void setIdDg2(long idDg2) {
		this.idDg2 = idDg2;
	}

	@Override
	public String toString() {
		return "Location [id=" + getId() + ", city=" + getCity() + ", color=" + getColor() + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(city, color, countryCode, idDg2, published, version);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		return Objects.equals(city, other.city) && Objects.equals(color, other.color)
				&& Objects.equals(countryCode, other.countryCode) && idDg2 == other.idDg2
				&& published == other.published && version == other.version;
	}


}
