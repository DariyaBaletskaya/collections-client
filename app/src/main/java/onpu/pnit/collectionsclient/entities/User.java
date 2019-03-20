package onpu.pnit.collectionsclient.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User implements Serializable {

    @JsonProperty("user_id")
    @PrimaryKey(autoGenerate = true)
    private int id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("age")
    private String age;
    @JsonProperty("city")
    private String city;
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("active")
    private boolean active;
    @JsonIgnoreProperties("collections")
    private List<Collection> collections;

    public User(int id, String name, String age, String city, String username, String password, boolean active, List<Collection> collections) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.city = city;
        this.username = username;
        this.password = password;
        this.active = active;
        this.collections = collections;
    }

    @Ignore
    public User() {
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Collection> getCollections() {
        return collections;
    }

    public void setCollections(List<Collection> collections) {
        this.collections = collections;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

}
