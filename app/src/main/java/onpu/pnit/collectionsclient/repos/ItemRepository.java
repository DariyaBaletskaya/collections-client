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

    public void deleteAll(List<Item> items) {
        executor.execute(() -> itemDao.delete(items));
    }

    public void delete(List<Item> items) {
        executor.execute(() -> itemDao.delete(items));
    }

    public void deleteAll(){
        executor.execute(() -> {
            itemDao.deleteAll();
        });
    }
    public void delete(Item item) {
        executor.execute(() -> itemDao.delete(item));
    }

    public LiveData<List<Item>> getItemsForUser(int userId) {
        return itemDao.getItemsForUser(userId);
    }

    public LiveData<List<Item>> getItemsByTitle(String title) {
        return itemDao.getItemsByTitle(title);
    }

    public void insert(Item item) {
        executor.execute(() -> itemDao.insert(item));
    }

    public void insert(List<Item> items) {
        executor.execute(() -> itemDao.insert(items));
    }

    public void update(Item item) {
        executor.execute(() -> itemDao.update(item));
    }

    public void updateItems(List<Item> items) {
        executor.execute(() -> itemDao.update(items));
    }

    public Item getItemById(int id) {
         return itemDao.getItemById(id);
    }

    public void deleteAllItemsFromCollection(int colletionId) {
        executor.execute(() -> itemCollectionJoinDao.deleteAllItemsFromCollection(colletionId));
    }

    public void insertInCollection(int collectionId, List<Item> items) {
        executor.execute(() -> {
            for (Item i:items) {
                itemCollectionJoinDao.insertInCollection(i.getId(), collectionId);
            }
        });
    }
    public void insertInCollection(int collectionId, Item item) {
        executor.execute(() -> {
            itemCollectionJoinDao.insertInCollection(item.getId(), collectionId);
        });
    }
}
