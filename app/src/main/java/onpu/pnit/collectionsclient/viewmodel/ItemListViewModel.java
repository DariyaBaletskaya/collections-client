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
import onpu.pnit.collectionsclient.repos.CollectionRepository;
import onpu.pnit.collectionsclient.repos.ItemRepository;

public class ItemListViewModel extends AndroidViewModel {
    private ItemRepository itemRepository;
    private CollectionRepository collectionRepository;
    private Executor executor = Executors.newSingleThreadExecutor();
    private ExecutorService executorService = Executors.newSingleThreadExecutor();


    public ItemListViewModel(@NonNull Application application) {
        super(application);
        itemRepository = ItemRepository.getInstance(application);
        collectionRepository = CollectionRepository.getInstance(application);
    }

    public long insert(Item item) throws ExecutionException, InterruptedException {
        Callable<Long> callable = new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return itemRepository.insertItem(item);
            }
        };
        Future<Long> future = executorService.submit(callable);
        return  future.get();
    }

    public LiveData<List<Item>> getAllItems() {
        return itemRepository.getAllItems();
    }

    public LiveData<List<Item>> getItemsForCollection(int collectionId) {
        return itemRepository.getItemsForCollection(collectionId);
    }

    public void delete(Item item) {
        executor.execute(() -> {
            itemRepository.deleteItem(item);
        });
    }

    public void deleteAllFromCollection(int collectionId) {
        executor.execute(() -> {
            itemRepository.deleteAllItemsFromCollection(collectionId);
        });
    }
    
    public void delete(List<Item> items) {
        executor.execute(() -> {
            itemRepository.deleteItems(items);
        });
    }


    public void update(Item item) {
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

    public void insert(List<Item> items) {
        executor.execute(() -> {
            itemRepository.insertItems(items);
        });
    }

    public void deleteAllItems() {
        executor.execute(() -> {
            itemRepository.deleteAllItems();
        });
    }

    public void insertItemsInCollection(int collectionId, List<Item> items) {
        executor.execute(() -> {
            itemRepository.insertInCollection(collectionId, items);
        });
    }
}
