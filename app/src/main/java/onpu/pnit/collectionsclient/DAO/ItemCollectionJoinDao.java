package onpu.pnit.collectionsclient.DAO;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import onpu.pnit.collectionsclient.entities.Collection;
import onpu.pnit.collectionsclient.entities.Item;


@Dao
public interface ItemCollectionJoinDao {
    @Query("SELECT * FROM collections c INNER JOIN item_collection_join ic ON c.collection_id=ic.collection_id " +
            "WHERE ic.item_id=:itemId")
    LiveData<List<Collection>> getCollectionsForItem(int itemId);

    @Query("SELECT * FROM items i INNER JOIN item_collection_join ic ON i.item_id=ic.item_id " +
            "WHERE ic.collection_id=:collectionId")
    LiveData<List<Item>> getItemsForCollection(int collectionId);
}
