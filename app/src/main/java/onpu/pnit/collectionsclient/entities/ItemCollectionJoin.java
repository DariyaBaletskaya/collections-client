package onpu.pnit.collectionsclient.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "item_collection_join", primaryKeys = {"item_id", "collection_id"}, indices = {@Index("item_id"), @Index("collection_id")},
        foreignKeys = {@ForeignKey(entity = Item.class, parentColumns = "item_id", childColumns = "item_id"),
                @ForeignKey(entity = Collection.class, parentColumns = "collection_id", childColumns = "collection_id")})
public class ItemCollectionJoin {
    @ColumnInfo(name = "item_id")
    private int itemId;
    @ColumnInfo(name = "collection_id")
    private int collectionId;

    public ItemCollectionJoin(int itemId, int collectionId) {
        this.itemId = itemId;
        this.collectionId = collectionId;
    }

    @Ignore
    public ItemCollectionJoin() {
    }

    public int getItemId() {
        return itemId;
    }

    public int getCollectionId() {
        return collectionId;
    }
}
