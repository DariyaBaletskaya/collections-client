package onpu.pnit.collectionsclient.repos;

import android.app.Application;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;
import onpu.pnit.collectionsclient.AppDatabase;
import onpu.pnit.collectionsclient.DAO.ItemCollectionJoinDao;
import onpu.pnit.collectionsclient.entities.Item;
import onpu.pnit.collectionsclient.entities.ItemCollectionJoin;


public class ItemCollectionJoinRepository {
    private ItemCollectionJoinDao itemCollectionJoinDao;
    private static volatile ItemCollectionJoinRepository instance;

    private Executor executor = Executors.newSingleThreadExecutor();

    public ItemCollectionJoinRepository(Application application) {
        itemCollectionJoinDao = AppDatabase.getInstance(application).itemCollectionJoinDao();
    }

    public static ItemCollectionJoinRepository getInstance(Application application) {
        if (instance == null) {
            synchronized (ItemCollectionJoinRepository.class) {
                if (instance == null) {
                    instance = new ItemCollectionJoinRepository(application);
                }
            }
        }
        return instance;
    }

    public void insertItemCollectionJoin(int itemId, int collectionId) {
        executor.execute(() -> {
            itemCollectionJoinDao.insert(new ItemCollectionJoin(itemId, collectionId));
        });
    }

    public LiveData<List<Item>> getItemsForCollection(int collectionId) {
        return itemCollectionJoinDao.getItemsForCollection(collectionId);
    }


}
