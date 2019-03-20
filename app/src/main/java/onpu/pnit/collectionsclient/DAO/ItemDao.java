package onpu.pnit.collectionsclient.DAO;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import onpu.pnit.collectionsclient.entities.Item;


@Dao
public interface ItemDao {
    @Insert
    void insertItem(Item item);

    @Delete
    void deleteItem(Item item);

    @Update
    void updateItem(Item item);

    @Update
    void updateItems(Item... items);

    @Query("DELETE FROM Items WHERE userId=:userId")
    void deleteAllItemsForUser(int userId);

    @Query("SELECT * from Items WHERE userId=:userId")
    LiveData<List<Item>> getItemsForUser(int userId);

    @Query("SELECT * from Items WHERE title=:title")
    LiveData<List<Item>> getItemsByTitle(String title);

    @Insert
    void insertAllItems(Item... items);
}
