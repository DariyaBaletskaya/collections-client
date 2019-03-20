package onpu.pnit.collectionsclient.DAO;

import android.content.ClipData;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import onpu.pnit.collectionsclient.entities.Items;


@Dao
public interface ItemDAO {
    @Insert
    void insertItem(Items items);

    @Delete
    void deleteItem(Items items);

    @Update
    void updateItem(Items... items);

    @Query("DELETE FROM items WHERE userId=:userId")
    void deleteAllItemsForUser(int userId);

    @Query("SELECT * from items WHERE userId=:userId")
    LiveData<List<Items>> getItemsForUser(int userId);

    @Query("SELECT * from items WHERE title=:title")
    LiveData<List<Items>> getItemsByTitle(String title);

}
