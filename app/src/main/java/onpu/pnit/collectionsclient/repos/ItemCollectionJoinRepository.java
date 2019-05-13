package onpu.pnit.collectionsclient.repos;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import onpu.pnit.collectionsclient.AppDatabase;
import onpu.pnit.collectionsclient.DAO.ItemCollectionJoinDao;
import onpu.pnit.collectionsclient.entities.Collection;
import onpu.pnit.collectionsclient.entities.Item;
import onpu.pnit.collectionsclient.entities.ItemCollectionJoin;


public class ItemCollectionJoinRepository {
    private ItemCollectionJoinDao itemCollectionJoinDao;
    private static volatile ItemCollectionJoinRepository instance;

    private Executor executor = Executors.newSingleThreadExecutor();

    private ItemCollectionJoinRepository(Application application) {
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

    public void insertItemCollectionJoin(ItemCollectionJoin join) {
        executor.execute(() -> itemCollectionJoinDao.insert(join));
    }

    public void insertItemCollectionJoins(List<ItemCollectionJoin> joins) {
        executor.execute(() -> itemCollectionJoinDao.insert(joins));
    }
    public void insertItemCollectionJoin(int itemId, int collectionId) {
        executor.execute(() -> {
            itemCollectionJoinDao.insert(new ItemCollectionJoin(itemId, collectionId));
        });
    }

    public LiveData<List<Item>> getItemsForCollection(int collectionId) {
        return itemCollectionJoinDao.getItemsForCollection(collectionId);
    }

    public List<ItemCollectionJoin> getAllJoins() {
        List<ItemCollectionJoin> joins = new ArrayList<>();
        executor.execute(() -> joins.addAll(itemCollectionJoinDao.getAllJoins()));
        return joins;
    }

    public List<ItemCollectionJoin> getJoinsForItem(int itemId) {
        List<ItemCollectionJoin> joins = new ArrayList<>();
        executor.execute(() -> joins.addAll(itemCollectionJoinDao.getJoinsForItem(itemId)));
        return joins;
    }

    public void deleteItemFromCollection(int itemId, int collectionId){
        executor.execute(() -> itemCollectionJoinDao.deleteItemFromCollection(itemId, collectionId));
    }


}
