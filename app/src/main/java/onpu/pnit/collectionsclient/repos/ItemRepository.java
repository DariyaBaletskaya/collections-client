package onpu.pnit.collectionsclient.repos;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

import androidx.lifecycle.LiveData;
import onpu.pnit.collectionsclient.AppDatabase;
import onpu.pnit.collectionsclient.DAO.ItemCollectionJoinDao;
import onpu.pnit.collectionsclient.DAO.ItemDao;
import onpu.pnit.collectionsclient.entities.Collection;
import onpu.pnit.collectionsclient.entities.Item;
import onpu.pnit.collectionsclient.entities.ItemCollectionJoin;

public class ItemRepository {

    private ItemDao itemDao;
    private ItemCollectionJoinDao itemCollectionJoinDao;
    private static volatile ItemRepository instance;

    private Executor executor = Executors.newSingleThreadExecutor();
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

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

    public Item getFirstItemForCollection(int collectionId) {
        AtomicReference<Item> item = new AtomicReference<>();
        executor.execute(() -> item.set(itemCollectionJoinDao.getFirstItemForCollection(collectionId)));
        return item.get();
    }

    public List<Collection> getCollectionsForItem(int itemId) {
        List<Collection> collections = new ArrayList<>();
        executor.execute(() -> collections.addAll(itemCollectionJoinDao.getCollectionsForItem(itemId)));
        return collections;
    }

    public LiveData<List<Item>> getAllItems() {
        return itemDao.getAllItems();
    }

    public void deleteItems(List<Item> items) {
        executor.execute(() -> itemDao.deleteItems(items));
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

    public long insertItem(Item item) throws ExecutionException, InterruptedException {
        Callable<Long> callable = () -> itemDao.insertItem(item);
        Future<Long> future = executorService.submit(callable);
        return  future.get();
    }

    public void insertItems(List<Item> items) {
        executor.execute(() -> itemDao.insertItems(items));
    }

    public void updateItem(Item item) {
        executor.execute(() -> itemDao.updateItem(item));
    }

    public void updateItems(List<Item> items) {
        executor.execute(() -> itemDao.updateItems(items));
    }

    public Item getItemById(int id) {
         return itemDao.getItemById(id);
    }

    public void deleteAllItemsFromCollection(int collectionId) {
        executor.execute(() -> itemCollectionJoinDao.deleteAllItemsFromCollection(collectionId));
    }

    public void deleteAllItems() {
        executor.execute(() -> {
            itemDao.deleteAllItems();
        });
    }

    public void insertInCollection(int collectionId, List<Item> items) {
        executor.execute(() -> {
            for (Item i:items) {
                itemCollectionJoinDao.insert(new ItemCollectionJoin(i.getId(), collectionId));
            }
        });
    }

    public void insertInCollection(int collectionId, Item item) {
        executor.execute(() -> {
            itemCollectionJoinDao.insert(new ItemCollectionJoin(item.getId(), collectionId));
        });
    }

    public List<ItemCollectionJoin> getAllJoinsForItem(int itemId) {
        List<ItemCollectionJoin> joins = new ArrayList<>();
        executor.execute(() -> {
            joins.addAll(itemCollectionJoinDao.getJoinsForItem(itemId));
        });

        return joins;
    }

    public List<ItemCollectionJoin> getAllJoinsForCollection(int collectionId) {
        List<ItemCollectionJoin> joins = new ArrayList<>();
        executor.execute(() -> {
            joins.addAll(itemCollectionJoinDao.getAllJoinsForCollection(collectionId));
        });

        return joins;
    }

    public void insertJoins(List<ItemCollectionJoin> joins) {
        executor.execute(() -> itemCollectionJoinDao.insert(joins));
    }

    public List<ItemCollectionJoin> getAllJoinsForNotDefaultCollections() {
        List<ItemCollectionJoin> joins = new ArrayList<>();
        executor.execute(() -> {
            joins.addAll(itemCollectionJoinDao.getAllJoinsForNotDefaultCollections());
        });

        return joins;
    }

    public List<ItemCollectionJoin> getAllJoins() {
        List<ItemCollectionJoin> joins = new ArrayList<>();
        executor.execute(() -> {
            joins.addAll(itemCollectionJoinDao.getAllJoins());
        });

        return joins;
    }
}
