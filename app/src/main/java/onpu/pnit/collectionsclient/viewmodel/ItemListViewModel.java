package onpu.pnit.collectionsclient.viewmodel;

import android.app.Application;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import onpu.pnit.collectionsclient.entities.Item;
import onpu.pnit.collectionsclient.repos.ItemRepository;

public class ItemListViewModel extends AndroidViewModel {
    private ItemRepository repository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public ItemListViewModel(@NonNull Application application) {
        super(application);
        repository = new ItemRepository(application);
    }

    public void insert(Item item) {
        executor.execute(() -> {
            repository.insertItem(item);
        });
    }

    public LiveData<List<Item>> getAllItems() {
        return repository.getAllItems();
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

}
