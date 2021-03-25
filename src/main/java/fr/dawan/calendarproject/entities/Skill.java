package fr.dawan.calendarproject.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Version;

@Entity
public class Skill {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(unique = true, nullable = false, length = 255)
	private String title;

//	@ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
//	@JoinTable(name = "user_skill", joinColumns = @JoinColumn(name = "skill_id", referencedColumnName = "id"), 
//	inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
//	private Set<User> users = new HashSet<User>();
	
	@ManyToMany
	@JoinTable(name = "user_skill", joinColumns = @JoinColumn(name = "skill_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> users = new HashSet<User>();
	
//	@ManyToMany(mappedBy = "skills")
//	private Set<User> users = new HashSet<User>();

	@Version
	private int version;

	public Skill() {
	}

	public Skill(String title, Set<User> users) {
		setTitle(title);
		setUsers(users);
	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Skill other = (Skill) obj;
		if (id != other.id)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Skill [id=" + getId() + ", title=" + getTitle() + ", Users=" + getUsers() + "]";
	}

}
