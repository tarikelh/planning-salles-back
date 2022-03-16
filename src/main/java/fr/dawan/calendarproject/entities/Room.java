package fr.dawan.calendarproject.entities;

import javax.persistence.*;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private long fullCapacity;

    private long partialCapacity;

    private boolean isAvailable;

    @Version
    private int version;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    public Room() {
    }

    public Room(long id, String name, long fullCapacity, long partialCapacity, boolean isAvailable, int version, Location location) {
        this.id = id;
        this.name = name;
        this.fullCapacity = fullCapacity;
        this.partialCapacity = partialCapacity;
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
        return partialCapacity;
    }

    public void setPartialCapacity() {
        this.partialCapacity = this.fullCapacity / 2;
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
}
