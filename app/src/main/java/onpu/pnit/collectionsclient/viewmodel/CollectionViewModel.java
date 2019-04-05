package onpu.pnit.collectionsclient.viewmodel;

import android.app.Application;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import onpu.pnit.collectionsclient.entities.Collection;
import onpu.pnit.collectionsclient.entities.Item;
import onpu.pnit.collectionsclient.repos.CollectionRepository;
import onpu.pnit.collectionsclient.repos.ItemRepository;

public class CollectionViewModel extends AndroidViewModel {
    private ItemRepository itemRepository;
    private CollectionRepository collectionRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public CollectionViewModel(@NonNull Application application) {
        super(application);
        itemRepository = ItemRepository.getInstance(application);
        collectionRepository = CollectionRepository.getInstance(application);
    }

    public LiveData<List<Item>> getAllItems() {
        return itemRepository.getAllItems();
    }

    public LiveData<List<Item>> getItemsForCollection(int collectionId) {
        return itemRepository.getItemsForCollection(collectionId);
    }

    public MutableLiveData<Item> getItemById(int itemId) {
        MutableLiveData<Item> mItem = new MutableLiveData<>();
        executor.execute(() -> {
            mItem.postValue(itemRepository.getItemById(itemId));
        });
        return mItem;
//        return itemRepository.getItemById(itemId);
    }

    public MutableLiveData<Collection> getCollectionById(int collectionId) {
        MutableLiveData<Collection> mCollection = new MutableLiveData<>();
        executor.execute(() -> {
            mCollection.postValue(collectionRepository.getCollectionById(collectionId));
        });
        return mCollection;
//        return collectionRepository.getCollectionById(collectionId);
    }



}
