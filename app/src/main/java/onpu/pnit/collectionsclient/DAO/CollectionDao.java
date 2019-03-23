package onpu.pnit.collectionsclient.DAO;

import java.util.List;

import androidx.lifecycle.LiveData;
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

    @Update
    void updateCollections(Collection... collections);

    @Update
    void updateCollection(Collection collection);

    @Query("DELETE FROM collections WHERE user_id=:userId")
    void deleteAllCollectionsForUser(int userId);

    @Query("SELECT * from collections WHERE user_id=:userId")
    LiveData<List<Collection>> getCollectionsForUser(int userId);

    @Query("SELECT * from collections WHERE collection_title=:title")
    LiveData<List<Collection>> getCollectionByTitle(String title);

    @Query("SELECT * from collections WHERE collection_category=:category")
    LiveData<List<Collection>> getCollectionsByCategory(String category);

    @Query("SELECT * from collections")
    LiveData<List<Collection>> getAllCollections();

}
