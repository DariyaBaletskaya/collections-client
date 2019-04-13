package onpu.pnit.collectionsclient.viewmodel;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import onpu.pnit.collectionsclient.entities.Item;
import onpu.pnit.collectionsclient.entities.ItemCollectionJoin;
import onpu.pnit.collectionsclient.repos.ItemCollectionJoinRepository;

public class ItemCollectionJoinViewModel extends AndroidViewModel {
    private ItemCollectionJoinRepository itemCollectionJoinRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public ItemCollectionJoinViewModel(@NonNull Application application) {
        super(application);
        itemCollectionJoinRepository = ItemCollectionJoinRepository.getInstance(application);
    }

    public void insertJoins(List<ItemCollectionJoin> joins) {
        itemCollectionJoinRepository.insertItemCollectionJoins(joins);
    }

    public void insertJoin(ItemCollectionJoin join) {
        itemCollectionJoinRepository.insertItemCollectionJoin(join);
    }

    public void insertJoin(int itemId, int collectionId) {
        itemCollectionJoinRepository.insertItemCollectionJoin(itemId, collectionId);
    }

    public LiveData<List<Item>> getItemsForCollection(int collectionId) {
        return itemCollectionJoinRepository.getItemsForCollection(collectionId);
    }

    public List<ItemCollectionJoin> getAllJoinsForItems(List<Item> items) {
        List<ItemCollectionJoin> joins = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            joins.addAll(itemCollectionJoinRepository.getJoinsForItem(items.get(i).getId()));
        }
        return joins;
    }

    public List<ItemCollectionJoin> getAllJoins() {
        return itemCollectionJoinRepository.getAllJoins();
    }

    public void deleteItemFromCollection(int itemId, int collectionId) {
        itemCollectionJoinRepository.deleteItemFromCollection(itemId, collectionId);
    }


}
