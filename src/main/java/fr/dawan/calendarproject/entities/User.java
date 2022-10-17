package fr.dawan.calendarproject.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javax.persistence.OneToMany;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import fr.dawan.calendarproject.enums.UserCompany;
import fr.dawan.calendarproject.enums.UserType;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE) 
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = true)
	private long idDg2;
	
	@Column(nullable = true)
	private Long employeeIdDg2;

	@Column(nullable = false, length = 255)
	private String firstName;

	@Column(nullable = false, length = 255)
	private String lastName;

	@ManyToOne(cascade = CascadeType.MERGE)
	private Location location;

	@Column(unique = true, nullable = false, length = 255)
	private String email;

	@Column(nullable = false)
	private String password;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "user_skill", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
	private Set<Skill> skills = new HashSet<>();

	@Enumerated(EnumType.STRING)
	private UserType type;

	@Enumerated(EnumType.STRING)
	private UserCompany company;

	@Column(nullable = true, columnDefinition = "TEXT")
	private String imagePath;
	
	@Column(nullable = true)
	private LocalDate endDate;
	
	//ToDo check if we need getter and setter on dto
	@OneToMany(mappedBy = "user")
	Set<InterventionFollowed> interventionsFollowed;

	@Version
	private int version;

	// Constructor important pour la sérialization (exemple Jackson)
	public User() {
		setSkills(new HashSet<>());
	}

	public User(long id, long idDg2, Long employeeIdDg2, String firstName, String lastName, Location location, String email,
			String password, Set<Skill> skills, UserType type, UserCompany company, String imagePath, LocalDate endDate, 
			Set<InterventionFollowed> interventionsFollowed, int version) {
		setId(id);
		setIdDg2(idDg2);
		setEmployeeIdDg2(employeeIdDg2);
		setFirstName(firstName);
		setLastName(lastName);
		setLocation(location);
		setEmail(email);
		setPassword(password);
		setSkills(skills);
		setType(type);
		setCompany(company);
		setImagePath(imagePath);
		setEndDate(endDate);
		setInterventionsFollowed(interventionsFollowed);
		setVersion(version);
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Long getEmployeeIdDg2() {
		return employeeIdDg2;
	}

	public void setEmployeeIdDg2(Long employeeIdDg2) {
		this.employeeIdDg2 = employeeIdDg2;
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

	public long getIdDg2() {
		return idDg2;
	}

	public void setIdDg2(long idDg2) {
		this.idDg2 = idDg2;
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

	public String getEnumCompany() {
		return this.company.toString();
	}

	public void setEnumCompany(String company) {
		this.company = UserCompany.valueOf(company);
	}

	public String getEnumType() {
		return this.type.toString();
	}

	public void setEnumType(String type) {
		this.type = UserType.valueOf(type);
	}

	public Set<InterventionFollowed> getInterventionsFollowed() {
		return interventionsFollowed;
	}

	public void setInterventionsFollowed(Set<InterventionFollowed> interventionsFollowed) {
		this.interventionsFollowed = interventionsFollowed;
	}

	
//	@Override
//	public int hashCode() {
//		return Objects.hash(company, email, lastName, location, skills, type);
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		User other = (User) obj;
//		return company == other.company && Objects.equals(email, other.email)
//				&& Objects.equals(lastName, other.lastName) && Objects.equals(location, other.location)
//				&& Objects.equals(skills, other.skills) && type == other.type;
//	}

	@Override
	public int hashCode() {
		return Objects.hash(company, email, lastName, location, skills, type);
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
		return company == other.company && Objects.equals(email, other.email)
				&& Objects.equals(lastName, other.lastName) && Objects.equals(location, other.location)
				&& Objects.equals(skills, other.skills) && type == other.type;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", idDg2=" + idDg2 + ", employeeIdDg2=" + employeeIdDg2 + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", location=" + location + ", email=" + email + ", password=" + password
				+ ", skills=" + skills + ", type=" + type + ", company=" + company + ", imagePath=" + imagePath
				+ ", endDate=" + endDate + " , version=" + version
				+ "]";
	}

	static public boolean emailIsValid(String email) {
		Pattern emailRegex = Pattern.compile("^(.+)@(.+)$");
		final Matcher matcher = emailRegex.matcher(email);
		return matcher.matches();
	}

	public String getFullname() {
		return this.firstName + " " + this.lastName;
	}
}
