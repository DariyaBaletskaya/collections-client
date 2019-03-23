package onpu.pnit.collectionsclient.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import onpu.pnit.collectionsclient.entities.Collection;
import onpu.pnit.collectionsclient.repos.CollectionRepository;

public class CollectionListViewModel extends AndroidViewModel {
    private CollectionRepository repository;
    private LiveData<List<Collection>> collections;
    public CollectionListViewModel(@NonNull Application application) {
        super(application);
        repository = CollectionRepository.getInstance(application);
        collections = repository.getAllCollections();
    }

    public LiveData<List<Collection>> getAllCollections(){return collections;}
}
