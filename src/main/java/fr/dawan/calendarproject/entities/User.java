package fr.dawan.calendarproject.entities;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import fr.dawan.calendarproject.enums.UserCompanie;
import fr.dawan.calendarproject.enums.UserType;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false, length = 255)
	private String firstName;

	@ManyToOne(cascade = CascadeType.PERSIST)
	private Location location;

	@Column(unique = true, nullable = false, length = 255)
	private String mail;
	
	@Column(nullable = false, length = 150)
	private String password;

	@Column(nullable = true)
	private LocalDate dateEntry;

	// OU - @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
	@Column(nullable = true)
	private LocalDate dateExit;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable(name = "user_skill", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "skill_id", referencedColumnName = "id"))
	private Set<Skill> skills;

	@Enumerated(EnumType.STRING)
	private UserType type;

	@Enumerated(EnumType.STRING)
	private UserCompanie companie;
	
	@Column(nullable = true)
	private String imagePath;

	@Version
	private int version;

	// @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
	// private List<Intervention> interventions = new ArrayList<Intervention>();

	// Constructor important pour la sérialization (exemple Jackson)
	public User() {
	}

	public User(long contact, Location location, String firstName, String mail, LocalDate dateEntry, LocalDate dateExit,
			long planningOrder, Set<Skill> skills, UserType type, int version) {
//		setContact(contact);
		setLocation(location);
		setFirstName(firstName);
		setMail(mail);
		setDateEntry(dateEntry);
		setDateExit(dateExit);
		setSkills(skills);
		setType(type);
	}

	public long getId() {
		return id;
	}

//	public long getContact() {
//		return contact;
//	}
//
//	public void setContact(long contact) {
//		this.contact = contact;
//	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public LocalDate getDateEntry() {
		return dateEntry;
	}

	public void setDateEntry(LocalDate dateEntry) {
		this.dateEntry = dateEntry;
	}

	public LocalDate getDateExit() {
		return dateExit;
	}

	public void setDateExit(LocalDate dateExit) {
		this.dateExit = dateExit;
	}

	public Set<Skill> getSkills() {
		return skills;
	}

	public void setSkills(Set<Skill> skills) {
		this.skills = skills;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public UserCompanie getCompanie() {
		return companie;
	}

	public void setCompanie(UserCompanie companie) {
		this.companie = companie;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + getId() + ", location=" + getLocation() + ", firstName=" + getFirstName() + ", mail="
				+ getMail() + ", dateEntry=" + getDateEntry() + ", dateExit=" + getDateEntry() + ", skills="
				+ getSkills() + ", type=" + getType() + ", version=" + getVersion() + "]";
	}

}
