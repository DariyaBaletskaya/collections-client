package onpu.pnit.collectionsclient.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "collections", foreignKeys =  @ForeignKey(entity = User.class,
        parentColumns = "user_id", childColumns = "user_id", onDelete = CASCADE))
public class Collection implements Serializable {
    public static final int DEFAULT_COLLECTION_ID = 1;
    public static final int DEFAULT_USER_ID = 1;
    @JsonProperty("id")
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "collection_id", index = true)
    private int id;
    @JsonProperty("title")
    @ColumnInfo(name = "collection_title")
    private String title;
    @JsonProperty("category")
    @ColumnInfo(name = "collection_category")
    private String category;
    @JsonProperty("description")
    @ColumnInfo(name = "collection_description")
    private String description;
    @JsonProperty("userid")
    @ColumnInfo(name = "user_id", index = true)
    private int userId;

    public Collection(String title, String category, String description, int userId) {
        this.title = title;
        this.category = category;
        this.description = description;
        this.userId = userId;
    }

    @Ignore
    public Collection() {
    }

    @Ignore
    public Collection(int id, String title, int userId) {
        this.id = id;
        this.title = title;
        this.userId = userId;
    }

    //for test in class AppDatabase
    @Ignore
    public Collection(int id, String title) {
        this.id = id;
        this.title = title;
        this.userId = 0;
    }

    public static Collection getDefaultCollection() {
        return new Collection(DEFAULT_COLLECTION_ID, "All items", DEFAULT_USER_ID);
    }

    public int getId() {
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
