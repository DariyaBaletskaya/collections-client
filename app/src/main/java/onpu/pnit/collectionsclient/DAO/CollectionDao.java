package onpu.pnit.collectionsclient.DAO;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import onpu.pnit.collectionsclient.entities.Collection;

@Dao
public interface CollectionDao {
    @Insert
    void insertCollection(Collection collection);

    @Delete
    void deleteCollection(Collection collection);

    @Insert
    void insertCollections(List<Collection> collections);

    @Update
    void updateCollection(Collection collection);

    @Update
    void updateCollections(List<Collection> collections);

    @Query("DELETE FROM collections WHERE (user_id=:userId and collection_id > 1)")
    void deleteAllCollectionsForUser(int userId);

    @Query("DELETE FROM collections WHERE  collection_id > 1")
    void deleteAllCollections( );

    @Query("SELECT * from collections WHERE collection_id=:collectionId")
    Collection getCollectionById(int collectionId);

    @Query("SELECT * from collections WHERE user_id=:userId")
    LiveData<List<Collection>> getCollectionsForUser(int userId);

    @Query("SELECT * from collections WHERE collection_title=:title")
    LiveData<List<Collection>> getCollectionByTitle(String title);

    @Query("SELECT * from collections WHERE collection_category=:category")
    LiveData<List<Collection>> getCollectionsByCategory(String category);

    @Query("SELECT * from collections")
    LiveData<List<Collection>> getAllCollections();


}
