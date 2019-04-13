package onpu.pnit.collectionsclient.DAO;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import onpu.pnit.collectionsclient.entities.Collection;
import onpu.pnit.collectionsclient.entities.Item;
import onpu.pnit.collectionsclient.entities.ItemCollectionJoin;


@Dao
public interface ItemCollectionJoinDao {
    @Insert
    void insert(ItemCollectionJoin itemCollectionJoin);

    @Insert
    void insert(List<ItemCollectionJoin> itemCollectionJoins);

    @Query("SELECT * FROM collections c WHERE c.collection_id IN " +
            "(SELECT collection_id FROM item_collection_join ic " +
            "WHERE ic.item_id=:itemId)")
    List<Collection> getCollectionsForItem(int itemId);

    @Query("SELECT * FROM items i WHERE i.item_id IN " +
            "(SELECT item_id FROM item_collection_join ic " +
            "WHERE ic.collection_id=:collectionId)")
    LiveData<List<Item>> getItemsForCollection(int collectionId);


    @Query("DELETE FROM item_collection_join WHERE collection_id=:collectionId")
    void deleteAllItemsFromCollection(int collectionId);

    @Query("DELETE FROM item_collection_join WHERE item_id=:itemId AND collection_id=:collectionId")
    void deleteItemFromCollection(int itemId, int collectionId);

//    @Query("INSERT INTO item_collection_join VALUES (:itemId, :collectionId)")
//    void insertItemInCollection(int itemId, int collectionId);

    @Query("SELECT * FROM item_collection_join WHERE item_id=:itemId")
    List<ItemCollectionJoin> getJoinsForItem(int itemId);

    @Query("SELECT * FROM item_collection_join")
    List<ItemCollectionJoin> getAllJoins();
}
