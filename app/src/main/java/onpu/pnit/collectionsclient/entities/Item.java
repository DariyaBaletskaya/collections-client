package onpu.pnit.collectionsclient.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "items", foreignKeys =  @ForeignKey(entity = User.class,
        parentColumns = "user_id", childColumns = "user_id", onDelete = CASCADE))
public class Item  implements Parcelable {
    @JsonProperty("id")
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id", index = true)
    private int id;
    @JsonProperty("title")
    @ColumnInfo(name="item_title")
    private String title;
    @JsonProperty("description")
    @ColumnInfo(name = "item_description")
    private String description;
    @JsonProperty("isOnSale")
    @ColumnInfo(name = "is_item_on_sale")
    private boolean isOnSale;
    @ColumnInfo(name = "item_price")
    @JsonProperty("price")
    private float price;
    @JsonProperty("currency")
    @ColumnInfo(name = "currency")
    private String currency;
    @ColumnInfo(name = "image")
    @JsonProperty("image")
    private String image;
    @JsonProperty("userId")
    @ColumnInfo(name = "user_id", index = true)
    private int userId;


    public Item(int id, String title, String description, boolean isOnSale, float price, String currency, int userId, String image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isOnSale = isOnSale;
        this.price = price;
        this.currency = currency;
        this.userId = userId;
        this.image = image;
    }

    @Ignore
    public Item() {
    }

    @Ignore
    public Item(String title, String description, boolean isOnSale, float price, String currency, int userId, String image) {
        this.title = title;
        this.description = description;
        this.isOnSale = isOnSale;
        this.price = price;
        this.currency = currency;
        this.userId = userId;
        this.image = image;
    }

    protected Item(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        isOnSale = in.readByte() != 0;
        price = in.readFloat();
        currency = in.readString();
        image = in.readString();
        userId = in.readInt();
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", isOnSale=" + isOnSale +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                ", image='" + image + '\'' +
                ", userId=" + userId +
                '}';
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description){
        this.description= description;
    }

    public boolean isOnSale() {
        return isOnSale;
    }

    public void setOnSale(boolean onSale) {
        isOnSale = onSale;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getUserId() {
        return userId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency){
        this.currency = currency;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeByte((byte) (isOnSale ? 1 : 0));
        dest.writeFloat(price);
        dest.writeString(currency);
        dest.writeString(image);
        dest.writeInt(userId);
    }


}
