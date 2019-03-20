package onpu.pnit.collectionsclient.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "collections", foreignKeys =  @ForeignKey(entity = User.class,
        parentColumns = "id", childColumns = "userId", onDelete = CASCADE))
public class Collection implements Serializable {
    @JsonProperty("id")
    @PrimaryKey(autoGenerate = true)
    private int id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("category")
    private String category;
    @JsonProperty("description")
    private String description;
    @JsonProperty("userid")
    private int userId;

    public Collection(int id, String title, String category, String description, int userId) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.userId = userId;
    }

    @Ignore
    public Collection() {
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUserId() {
        return userId;
    }
}
