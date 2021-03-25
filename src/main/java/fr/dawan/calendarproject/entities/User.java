package fr.dawan.calendarproject.entities;

import java.util.HashSet;
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

import fr.dawan.calendarproject.enums.UserCompany;
import fr.dawan.calendarproject.enums.UserType;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false, length = 255)
	private String firstName;

	@Column(nullable = false, length = 255)
	private String lastName;

	@ManyToOne(cascade = CascadeType.MERGE)
	private Location location;

	@Column(unique = true, nullable = false, length = 255)
	private String email;

	@Column(nullable = false, length = 150)
	private String password;

//	@ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
//	@JoinTable(name = "user_skill", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), 
//	inverseJoinColumns = @JoinColumn(name = "skill_id", referencedColumnName = "id"))
//	private Set<Skill> skills = new HashSet<Skill>();
	
	@ManyToMany
	@JoinTable(name = "user_skill", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
	private Set<Skill> skills = new HashSet<Skill>();

	
	@Enumerated(EnumType.STRING)
	private UserType type;

	@Enumerated(EnumType.STRING)
	private UserCompany company;

	@Column(nullable = true)
	private String imagePath;

	@Version
	private int version;

	// @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
	// private List<Intervention> interventions = new ArrayList<Intervention>();

	// Constructor important pour la sérialization (exemple Jackson)
	public User() {
	}

	public User(long contact, Location location, String firstName, String email, long planningOrder, Set<Skill> skills,
			UserType type, int version) {
//		setContact(contact);
		setLocation(location);
		setFirstName(firstName);
		setEmail(email);
		setSkills(skills);
		setType(type);
	}

	// CHANGE FOR SETTER
	public User(long id, String firstName, String lastName, Location location, String email,
			String password, Set<Skill> skills, UserType type, UserCompany company, String imagePath, int version) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.location = location;
		this.email = email;
		this.password = password;
		this.skills = skills;
		this.type = type;
		this.company = company;
		this.imagePath = imagePath;
		this.version = version;
	}

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

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public UserCompany getCompany() {
		return company;
	}

	public void setCompany(UserCompany company) {
		this.company = company;
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
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", location=" + location
				+ ", email=" + email + ", password=" + password + ", skills=" + skills + ", type=" + type + ", company="
				+ company + ", imagePath=" + imagePath + ", version=" + version + "]";
	}
}
