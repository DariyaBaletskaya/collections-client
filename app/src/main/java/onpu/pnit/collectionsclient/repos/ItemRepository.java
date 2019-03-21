package onpu.pnit.collectionsclient.repos;

import android.content.Context;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;
import onpu.pnit.collectionsclient.AppDatabase;
import onpu.pnit.collectionsclient.entities.Item;

public class ItemRepository {

    private static ItemRepository ourInstance;

    private LiveData<List<Item>> items;

    private AppDatabase db;

    private Executor executor = Executors.newSingleThreadExecutor();

    private ItemRepository(Context context) {
        db = AppDatabase.getInstance(context);
        items = getAllItems();
    }

    public static ItemRepository getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new ItemRepository(context);
        }
        return ourInstance;
    }

    private LiveData<List<Item>> getAllItems() {
        return db.itemDao().getAllItems();
    }

    public void deleteAllItemsForUser(int userId) {
        executor.execute(() -> db.itemDao().deleteAllItemsForUser(userId));
    }

    public void deleteItem(Item item) {
        executor.execute(() -> db.itemDao().deleteItem(item));
    }

    public LiveData<List<Item>> getItemsForUser(int userId) {
        return db.itemDao().getItemsForUser(userId);
    }

    public LiveData<List<Item>> getItemsByTitle(String title) {
        return db.itemDao().getItemsByTitle(title);
    }

    public void insertItem(Item item) {
        executor.execute(() -> db.itemDao().insertItem(item));
    }

    public void insertAllItems(Item... items) {
        executor.execute(() -> db.itemDao().insertAllItems(items));
    }

    public void updateItem(Item item) {
        executor.execute(() -> db.itemDao().updateItem(item));
    }

    public void updateItems(Item... items) {
        executor.execute(() -> db.itemDao().updateItems(items));
    }
}
