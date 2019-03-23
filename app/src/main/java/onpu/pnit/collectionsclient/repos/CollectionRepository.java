package onpu.pnit.collectionsclient.repos;

import android.app.Application;
import android.content.Context;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;
import onpu.pnit.collectionsclient.AppDatabase;
import onpu.pnit.collectionsclient.DAO.CollectionDao;
import onpu.pnit.collectionsclient.entities.Collection;

public class CollectionRepository {

    private CollectionDao collectionDao;
    private static volatile CollectionRepository instance;

    private Executor executor = Executors.newSingleThreadExecutor();

    public CollectionRepository(Application application) {
        collectionDao = AppDatabase.getInstance(application).collectionDao();
    }

    public static CollectionRepository getInstance(Application application) {
        if (instance == null) {
            synchronized (CollectionRepository.class) {
                if (instance == null) {
                    instance = new CollectionRepository(application);
                }
            }
        }
        return instance;
    }

    public LiveData<List<Collection>> getAllCollections() {
        return collectionDao.getAllCollections();
    }

    public LiveData<List<Collection>> getCollectionForUser(int userId) {
        return collectionDao.getCollectionsForUser(userId);
    }

    public LiveData<List<Collection>> getCollectionByTitle(String title) {
        return collectionDao.getCollectionByTitle(title);
    }

    public LiveData<List<Collection>> getCollectionByCategory(String category) {
        return collectionDao.getCollectionsByCategory(category);
    }

    public void deleteAllCollectionsForUser(int userId) {
        executor.execute(() -> collectionDao.deleteAllCollectionsForUser(userId));
    }

    public void deleteCollection(Collection collection) {
        executor.execute(() -> collectionDao.deleteCollection(collection));
    }

    public void updateCollections(Collection... collections) {
        executor.execute(() -> collectionDao.updateCollections(collections));
    }

    public void updateCollection(Collection collection) {
        executor.execute(() -> collectionDao.updateCollection(collection));
    }

    public void insertCollection(Collection collection) {
        executor.execute(() -> collectionDao.insertCollection(collection));
    }

}
