package onpu.pnit.collectionsclient.viewmodel;

import android.app.Application;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import onpu.pnit.collectionsclient.entities.Collection;
import onpu.pnit.collectionsclient.repos.CollectionRepository;

/*
* Class which use for creating new collection in DB
* */
public class EditorCollectionViewModel extends AndroidViewModel {
    private CollectionRepository repository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public EditorCollectionViewModel(@NonNull Application application) {
        super(application);
        repository = new CollectionRepository(application);
    }



    public void insert(Collection collection) {
        executor.execute(() -> {
            repository.insertCollection(collection);
        });
    }

    public LiveData<List<Collection>> getAllCollections() {
        return repository.getAllCollections();
    }

    public void delete(Collection collection) {
        repository.deleteCollection(collection);
    }

    public void deleteAll() {
        repository.deleteAllCollections();
    }
}
