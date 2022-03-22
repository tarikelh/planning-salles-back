package fr.dawan.calendarproject.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = true, unique = true)
    private long idDg2;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private long fullCapacity;

    private boolean isAvailable;

    @Version
    private int version;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "location_id")
    private Location location;

    public Room() {
    }

    public Room(long id, long idDg2, String name, long fullCapacity , boolean isAvailable, int version, Location location) {
        this.id = id;
        this.idDg2 = idDg2;
        this.name = name;
        this.fullCapacity = fullCapacity;
        this.isAvailable = isAvailable;
        this.version = version;
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdDg2() {
        return idDg2;
    }

    public void setIdDg2(long idDg2) {
        this.idDg2 = idDg2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getFullCapacity() {
        return fullCapacity;
    }

    public void setFullCapacity(long fullCapacity) {
        this.fullCapacity = fullCapacity;
    }

    public long getPartialCapacity() {
        return fullCapacity / 2;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return id == room.id && idDg2 == room.idDg2 && fullCapacity == room.fullCapacity && isAvailable == room.isAvailable && version == room.version && Objects.equals(name, room.name) && Objects.equals(location, room.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idDg2, name, fullCapacity, isAvailable, version, location);
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", idDg2=" + idDg2 +
                ", name='" + name + '\'' +
                ", fullCapacity=" + fullCapacity +
                ", isAvailable=" + isAvailable +
                ", location=" + location +
                '}';
    }
}
