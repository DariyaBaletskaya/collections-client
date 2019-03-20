package onpu.pnit.collectionsclient.entities;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "items", foreignKeys =  @ForeignKey(entity = User.class,
        parentColumns = "id", childColumns = "userId", onDelete = CASCADE))
public class Item {

    @JsonProperty("id")
    @PrimaryKey(autoGenerate = true)
    private int id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("isOnSale")
    private boolean isOnSale;
    @JsonProperty("price")
    private float price;
    @JsonProperty("userId")
    private int userId;

    public Item(int id, String title, String description, boolean isOnSale, float price, int userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isOnSale = isOnSale;
        this.price = price;
        this.userId = userId;
    }

    @Ignore
    public Item() {
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isOnSale() {
        return isOnSale;
    }

    public float getPrice() {
        return price;
    }

    public int getUserId() {
        return userId;
    }
}
