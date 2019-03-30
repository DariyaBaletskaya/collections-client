package onpu.pnit.collectionsclient.repos;

import android.app.Application;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;
import onpu.pnit.collectionsclient.AppDatabase;
import onpu.pnit.collectionsclient.DAO.ItemCollectionJoinDao;
import onpu.pnit.collectionsclient.DAO.ItemDao;
import onpu.pnit.collectionsclient.entities.Item;

public class ItemRepository {

    private ItemDao itemDao;
    private ItemCollectionJoinDao itemCollectionJoinDao;
    private static volatile ItemRepository instance;

    private Executor executor = Executors.newSingleThreadExecutor();

    public ItemRepository(Application application) {
        itemDao = AppDatabase.getInstance(application).itemDao();
        itemCollectionJoinDao = AppDatabase.getInstance(application).itemCollectionJoinDao();
    }

    public static ItemRepository getInstance(Application application) {
        if (instance == null) {
            synchronized (ItemRepository.class) {
                if (instance == null) {
                    instance = new ItemRepository (application);
                }
            }
        }
        return instance;
    }

    public LiveData<List<Item>> getItemsForCollection(int collectionId) {
        return itemCollectionJoinDao.getItemsForCollection(collectionId);
    }
    public LiveData<List<Item>> getAllItems() {
        return itemDao.getAllItems();
    }

    public void deleteAllItemsForUser(int userId) {
        executor.execute(() -> itemDao.deleteAllItemsForUser(userId));
    }

    public void deleteItem(Item item) {
        executor.execute(() -> itemDao.deleteItem(item));
    }

    public LiveData<List<Item>> getItemsForUser(int userId) {
        return itemDao.getItemsForUser(userId);
    }

    public LiveData<List<Item>> getItemsByTitle(String title) {
        return itemDao.getItemsByTitle(title);
    }

    public void insertItem(Item item) {
        executor.execute(() -> itemDao.insertItem(item));
    }

    public void insertAllItems(Item... items) {
        executor.execute(() -> itemDao.insertAllItems(items));
    }

    public void updateItem(Item item) {
        executor.execute(() -> itemDao.updateItem(item));
    }

    public void updateItems(Item... items) {
        executor.execute(() -> itemDao.updateItems(items));
    }

    public Item getItemById(int id) {
         return itemDao.getItemById(id);
    }
}
