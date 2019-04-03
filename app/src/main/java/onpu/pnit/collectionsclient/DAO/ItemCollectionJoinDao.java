package onpu.pnit.collectionsclient.DAO;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;
import onpu.pnit.collectionsclient.entities.Collection;
import onpu.pnit.collectionsclient.entities.Item;


@Dao
public interface ItemCollectionJoinDao {
    @Query("SELECT * FROM collections c WHERE c.collection_id IN " +
            "(SELECT collection_id FROM item_collection_join ic " +
            "WHERE ic.item_id=:itemId AND ic.collection_id=c.collection_id)")
    LiveData<List<Collection>> getCollectionsForItem(int itemId);

    @Query("SELECT * FROM items i WHERE i.item_id IN " +
            "(SELECT item_id FROM item_collection_join ic " +
            "WHERE ic.collection_id=:collectionId AND ic.item_id=i.item_id)")
    LiveData<List<Item>> getItemsForCollection(int collectionId);

    @Query("DELETE FROM item_collection_join WHERE collection_id=:collectionId")
    void deleteAllItemsFromCollection(int collectionId);
}
