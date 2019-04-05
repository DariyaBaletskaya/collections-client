package onpu.pnit.collectionsclient.viewmodel;

import android.app.Application;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import onpu.pnit.collectionsclient.entities.Item;
import onpu.pnit.collectionsclient.repos.ItemCollectionJoinRepository;

public class ItemCollectionJoinViewModel extends AndroidViewModel {
    private ItemCollectionJoinRepository itemCollectionJoinRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public ItemCollectionJoinViewModel(@NonNull Application application) {
        super(application);
        itemCollectionJoinRepository = ItemCollectionJoinRepository.getInstance(application);
    }

    public void insertItemCollectionJoin(int itemId, int collectionId) {
        executor.execute(() -> {
            itemCollectionJoinRepository.insertItemCollectionJoin(itemId, collectionId);
        });
    }

    public LiveData<List<Item>> getItemsForCollection(int collectionId) {
        return itemCollectionJoinRepository.getItemsForCollection(collectionId);
    }


}
