package tgbots.shelterbot.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Volunteer {

    @Id
    @Column(name = "id_volunteer")
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "name_volunteer")
    private String name;

    @Column(name = "status_volunteer")
    private boolean free;

    public Volunteer(Long id, String userName, String name, boolean free) {
        this.id = id;
        this.userName = userName;
        this.name = name;
        this.free = free;
    }

    public Volunteer() {

    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getName() {
        return name;
    }

    public boolean isFree() {
        return free;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Volunteer volunteer = (Volunteer) o;
        return id.equals(volunteer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
