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
public interface CollectionDAO {
    @Insert
    void insertCollection(Collection collection);

    @Delete
    void deleteCollection(Collection collection);

    @Update
    void updateColection(Collection... collections);

    @Query("DELETE FROM collections WHERE userId=:userId")
    void deleteAllCollectionsForUser(int userId);

    @Query("SELECT * from collections WHERE userId=:userId")
    LiveData<List<Collection>> getCollectionForUser(int userId);

    @Query("SELECT * from collections WHERE title=:title")
    LiveData<List<Collection>> getCollectionByTitle(String title);

    @Query("SELECT * from collections WHERE category=:category")
    LiveData<List<Collection>> getCollectionByCategory(String category);
}
