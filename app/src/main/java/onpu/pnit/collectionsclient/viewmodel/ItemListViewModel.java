package onpu.pnit.collectionsclient.viewmodel;

import android.app.Application;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import onpu.pnit.collectionsclient.entities.Collection;
import onpu.pnit.collectionsclient.entities.Item;
import onpu.pnit.collectionsclient.entities.ItemCollectionJoin;
import onpu.pnit.collectionsclient.repos.CollectionRepository;
import onpu.pnit.collectionsclient.repos.ItemCollectionJoinRepository;
import onpu.pnit.collectionsclient.repos.ItemRepository;

public class ItemListViewModel extends AndroidViewModel {
    private ItemRepository itemRepository;
    private CollectionRepository collectionRepository;
    private ItemCollectionJoinRepository itemCollectionJoinRepository;
    private Executor executor = Executors.newSingleThreadExecutor();
    private ExecutorService executorService = Executors.newSingleThreadExecutor();


    public ItemListViewModel(@NonNull Application application) {
        super(application);
        itemRepository = ItemRepository.getInstance(application);
        collectionRepository = CollectionRepository.getInstance(application);
        itemCollectionJoinRepository = ItemCollectionJoinRepository.getInstance(application);
    }

    public long insertItem(Item item) throws ExecutionException, InterruptedException {
        Callable<Long> callable = () -> itemRepository.insertItem(item);
        Future<Long> future = executorService.submit(callable);
        return future.get();
    }

    public LiveData<List<Item>> getAllItems() {
        return itemRepository.getAllItems();
    }

    public LiveData<List<Item>> getItemsForCollection(int collectionId) {
        return itemRepository.getItemsForCollection(collectionId);
    }

    public List<Collection> getCollectionsForItem(int itemId) {
        return itemRepository.getCollectionsForItem(itemId);
    }

    public void deleteItem(Item item) {
        executor.execute(() -> {
            itemRepository.deleteItem(item);
        });
    }

    public void deleteAllFromCollection(int collectionId) {
        itemRepository.deleteAllItemsFromCollection(collectionId);
    }

    public void deleteItems(List<Item> items) {
        executor.execute(() -> {
            itemRepository.deleteItems(items);
        });
    }


    public void updateItem(Item item) {
        executor.execute(() -> {
            itemRepository.updateItem(item);
        });
    }

    public MutableLiveData<Item> getItemById(int id) {
        MutableLiveData<Item> mItem = new MutableLiveData<>();
        executor.execute(() -> {
            mItem.postValue(itemRepository.getItemById(id));
        });
        return mItem;
    }

    public MutableLiveData<Collection> getCollectionById(int collectionId) {
        MutableLiveData<Collection> mCollection = new MutableLiveData<>();
        executor.execute(() -> {
            mCollection.postValue(collectionRepository.getCollectionById(collectionId));
        });
        return mCollection;
    }

    public void insertItems(List<Item> items) {
        executor.execute(() -> {
            itemRepository.insertItems(items);
        });
    }

    public void deleteAllItems() {
        executor.execute(() -> {
            itemRepository.deleteAllItems();
        });
    }

    public void deleteItemFromCollection(int itemId, int collectionId) {
        itemCollectionJoinRepository.deleteItemFromCollection(itemId, collectionId);
    }

    public void insertItemsInCollection(int collectionId, List<Item> items) {
        itemRepository.insertInCollection(collectionId, items);
    }

    public List<ItemCollectionJoin> getAllJoinsForItem(int itemId) {
        return itemRepository.getAllJoinsForItem(itemId);
    }

    public void insertJoins(List<ItemCollectionJoin> joins) {
        itemRepository.insertJoins(joins);
    }

    public List<ItemCollectionJoin> getAllJoins() {
        return itemCollectionJoinRepository.getAllJoins();
    }

    public void insertItemInCollection(int itemId, int collectionId) {
        itemCollectionJoinRepository.insertItemCollectionJoin(itemId, collectionId);
    }

    public List<ItemCollectionJoin> getAllJoinsForCollection(int collectionId) {
        return itemRepository.getAllJoinsForCollection(collectionId);
    }
}
