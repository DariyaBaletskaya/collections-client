package onpu.pnit.collectionsclient.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User implements Serializable {

    private static final int DEFAULT_USER_ID = 1;
    @JsonProperty("user_id")
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id", index = true)
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
    @ColumnInfo(name = "is_user_active")
    private boolean active;

    public User(int id, String name, String age, String city, String username, String password, boolean active) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.city = city;
        this.username = username;
        this.password = password;
        this.active = active;
    }

    @Ignore
    public User() {}

    @Ignore
    public User(int id) {
        this.id = id;
    }

    @Ignore
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static User getDefaultUser() {
        return new User(DEFAULT_USER_ID);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
