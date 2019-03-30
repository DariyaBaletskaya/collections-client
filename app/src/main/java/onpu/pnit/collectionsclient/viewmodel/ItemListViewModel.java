package onpu.pnit.collectionsclient.viewmodel;

import android.app.Application;

import java.text.CollationElementIterator;
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
    private ItemRepository repository;
    private CollectionRepository collectionRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public ItemListViewModel(@NonNull Application application) {
        super(application);
        repository = new ItemRepository(application);
        collectionRepository = new CollectionRepository(application);
    }

    public void insert(Item item) {
        executor.execute(() -> {
            repository.insertItem(item);
        });
    }


    public LiveData<List<Item>> getAllItems() {
        return repository.getAllItems();
    }

    public LiveData<List<Item>> getItemsForCollection(int collectionId) {
        return repository.getItemsForCollection(collectionId);
    }

    public void delete(Item item) {
        executor.execute(() -> {
            repository.deleteItem(item);
        });
    }

    public void deleteAll(int userId) {
        executor.execute(() -> {
            repository.deleteAllItemsForUser(userId);
        });
    }

    public void update(Item item) {
        executor.execute(() -> {
            repository.updateItem(item);
        });
    }

    public Item getItemById(int id) {
        return repository.getItembyId(id);
    }

}
