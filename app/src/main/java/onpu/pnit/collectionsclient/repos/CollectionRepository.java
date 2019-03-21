package onpu.pnit.collectionsclient.repos;

import android.content.Context;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;
import onpu.pnit.collectionsclient.AppDatabase;
import onpu.pnit.collectionsclient.entities.Collection;

public class CollectionRepository {

    private static CollectionRepository ourInstance;

    private LiveData<List<Collection>> collections;

    private AppDatabase db;

    private Executor executor = Executors.newSingleThreadExecutor();

    public CollectionRepository(Context context) {
        db = AppDatabase.getInstance(context);
        collections = getAllItems();
    }

    public static CollectionRepository getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new CollectionRepository(context);
        }
        return ourInstance;
    }

    private LiveData<List<Collection>> getAllItems() {
        return db.collectionDao().getAllCollection();
    }

    public LiveData<List<Collection>> getCollectionForUser(int userId) {
        return db.collectionDao().getCollectionForUser(userId);
    }

    public LiveData<List<Collection>> getCollectionByTitle(String title) {
        return db.collectionDao().getCollectionByTitle(title);
    }

    public LiveData<List<Collection>> getCollectionByCategory(String category) {
        return db.collectionDao().getCollectionByCategory(category);
    }

    public void deleteAllCollectionsForUser(int userId) {
        executor.execute(() -> db.collectionDao().deleteAllCollectionsForUser(userId));
    }

    public void deleteCollection(Collection collection) {
        executor.execute(() -> db.collectionDao().deleteCollection(collection));
    }

    public void updateCollections(Collection... collections) {
        executor.execute(() -> db.collectionDao().updateCollections(collections));
    }

    public void updateCollection(Collection collection) {
        executor.execute(() -> db.collectionDao().updateCollection(collection));
    }

    public void insertCollection(Collection collection) {
        executor.execute(() -> db.collectionDao().insertCollection(collection));
    }

}
