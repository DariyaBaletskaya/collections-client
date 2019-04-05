package onpu.pnit.collectionsclient.DAO;

import android.content.ClipData;

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
    /* НЕЛЬЗЯ НАЗЫВАТЬ МЕТОД INSERT(LIST<ITEM> ITEMS) - INSERTALL, НАДО INSERT. */
    /* С DELETE ТА ЖЕ ИСТОРИЯ, DELETEALL УДАЛЯЕТ ВСЁ*/
    @Insert
    long insertItem(Item item);

    @Delete
    void deleteItem(Item item);

    @Update
    void updateItem(Item item);

    @Update
    void updateItems(List<Item> items);

    @Query("DELETE FROM Items WHERE user_id=:userId")
    void deleteAllItemsForUser(int userId);

    @Delete
    void deleteItems(List<Item> items);


    @Query("SELECT * from Items WHERE user_id=:userId")
    LiveData<List<Item>> getItemsForUser(int userId);

    @Query("SELECT * from Items WHERE item_title=:title")
    LiveData<List<Item>> getItemsByTitle(String title);

    @Query("SELECT * from Items")
    LiveData<List<Item>> getAllItems();

    @Query("SELECT * from Items WHERE item_id=:itemId")
    Item getItemById(int itemId);

    @Insert
    void insertItems(List<Item> items);

    @Query("DELETE FROM items")
    void deleteAllItems();
}
