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
import onpu.pnit.collectionsclient.entities.ItemCollectionJoin;
import onpu.pnit.collectionsclient.repos.CollectionRepository;
import onpu.pnit.collectionsclient.repos.ItemCollectionJoinRepository;
import onpu.pnit.collectionsclient.repos.ItemRepository;

/*
 * Class which use for creating new collection in DB
 * */
public class EditorCollectionViewModel extends AndroidViewModel {
    private CollectionRepository collectionRepository;
    private ItemRepository itemRepository;
    private Executor executor = Executors.newSingleThreadExecutor();
    private MutableLiveData<Collection> mCollection = new MutableLiveData<>();

    public EditorCollectionViewModel(@NonNull Application application) {
        super(application);
        collectionRepository = CollectionRepository.getInstance(application);
        itemRepository = ItemRepository.getInstance(application);
    }

    public void insertCollection(Collection collection) {
        executor.execute(() -> collectionRepository.insertCollection(collection));
    }

    public void insertCollections(List<Collection> collections) {
        collectionRepository.insertCollections(collections);
    }

    public void insertJoins(List<ItemCollectionJoin> joins) {
        executor.execute(() -> itemRepository.insertJoins(joins));
    }

    public LiveData<List<Collection>> getAllCollections() {
        return collectionRepository.getAllCollections();
    }

    public void deleteCollection(Collection collection) {
        executor.execute(() -> collectionRepository.deleteCollection(collection));
    }

    public void deleteAllCollections() {
        collectionRepository.deleteAllCollections();
    }

    public void updateCollection(Collection collection) {
        collectionRepository.updateCollection(collection);
    }

    public MutableLiveData<Collection> getCollectionById(int collectionId) {
        MutableLiveData<Collection> mCollection = new MutableLiveData<>();
        executor.execute(() -> {
            mCollection.postValue(collectionRepository.getCollectionById(collectionId));
        });
        return mCollection;
    }

    public Item getFirstItemOfCollection(int collectionId) {
        return itemRepository.getFirstItemForCollection(collectionId);
    }

    public List<ItemCollectionJoin> getAllJoinsForCollection(int collectionId) {
        return itemRepository.getAllJoinsForCollection(collectionId);
    }

    public List<ItemCollectionJoin> getAllJoins() {
        return itemRepository.getAllJoins();
    }

    public List<ItemCollectionJoin> getAllJoinsForNotDefaultCollections() {
        return itemRepository.getAllJoinsForNotDefaultCollections();
    }

    public void deleteAllItemsFromCollection(int collectionId) {
        executor.execute(() -> itemRepository.deleteAllItemsFromCollection(collectionId));
    }
}
