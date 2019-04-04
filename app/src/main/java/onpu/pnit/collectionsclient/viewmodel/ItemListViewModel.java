package onpu.pnit.collectionsclient.viewmodel;

import android.app.Application;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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


    public ItemListViewModel(@NonNull Application application) {
        super(application);
        itemRepository = ItemRepository.getInstance(application);
        collectionRepository = CollectionRepository.getInstance(application);
    }

    public void insert(Item item) {
        executor.execute(() -> {
            itemRepository.insert(item);
        });
    }

    public LiveData<List<Item>> getAllItems() {
        return itemRepository.getAllItems();
    }

    public LiveData<List<Item>> getItemsForCollection(int collectionId) {
        return itemRepository.getItemsForCollection(collectionId);
    }

    public void delete(Item item) {
        executor.execute(() -> {
            itemRepository.delete(item);
        });
    }

    public void deleteAllFromCollection(int сollectionId) {
        executor.execute(() -> {
            itemRepository.deleteAllItemsFromCollection(сollectionId);
        });
    }

    public void insertItemsInCollection(int collectionId, List<Item> items) {
        executor.execute(() -> {
            itemRepository.insertInCollection(collectionId, items);
        });
    }
    public void delete(List<Item> items) {
        executor.execute(() -> {
            itemRepository.deleteAll(items);
        });
    }

    public void update(Item item) {
        executor.execute(() -> {
            itemRepository.update(item);
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
            itemRepository.insert(items);
        });
    }

    public void deleteAll() {
        executor.execute(() -> {
            itemRepository.deleteAll();
        });
    }
}
