package onpu.pnit.collectionsclient.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "item_collection_join", primaryKeys = {"itemId, collectionId"},
        foreignKeys = {@ForeignKey(entity = Item.class, parentColumns = "id", childColumns = "itemId"),
                       @ForeignKey(entity = Collection.class, parentColumns = "id", childColumns = "collectionId")})
public class ItemCollectionJoin {
    private int itemId;
    private int collectionId;
}
