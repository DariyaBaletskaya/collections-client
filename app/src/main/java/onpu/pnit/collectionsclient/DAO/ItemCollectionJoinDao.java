package onpu.pnit.collectionsclient.DAO;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import onpu.pnit.collectionsclient.entities.Collection;


@Dao
public interface ItemCollectionJoinDao {

    @Query("SELECT * FROM collections INNER JOIN item_collection_join ON collections.id=item_collection_join.collectionId " +
            "WHERE item_collection_join.collectionId=:collectionId")
    LiveData<List<Collection>> getCollectionsForItem(int collectionId);

    @Query("SELECT * FROM Items INNER JOIN item_collection_join ON Items.id=item_collection_join.itemId " +
            "WHERE item_collection_join.itemId=:itemId")
    LiveData<List<Collection>> getItemsForCollection(int itemId);
}
