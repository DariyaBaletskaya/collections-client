package onpu.pnit.collectionsclient;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import onpu.pnit.collectionsclient.DAO.CollectionDao;
import onpu.pnit.collectionsclient.DAO.ItemCollectionJoinDao;
import onpu.pnit.collectionsclient.DAO.ItemDao;
import onpu.pnit.collectionsclient.DAO.UserDao;
import onpu.pnit.collectionsclient.entities.Collection;
import onpu.pnit.collectionsclient.entities.Item;
import onpu.pnit.collectionsclient.entities.ItemCollectionJoin;
import onpu.pnit.collectionsclient.entities.User;

@Database(entities = {Item.class, Collection.class, ItemCollectionJoin.class, User.class},
        version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "appdb.db";
    private static volatile AppDatabase instance;

    public abstract ItemDao itemDao();
    public abstract UserDao userDao();
    public abstract CollectionDao collectionDao();
    public abstract ItemCollectionJoinDao itemCollectionJoinDao();

    public static AppDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = buildDatabase(context);
                }
            }

        }

        return instance;
    }

    private static AppDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        //Executors.newSingleThreadExecutor().execute(() -> getInstance(context).collectionDao().insertCollection(Collection.getDefaultCollection()));
                        new PopularAsyncTask(instance).execute();
                    }
                })
                .build();
    }

    private static class PopularAsyncTask extends AsyncTask<Void, Void, Void> {
        private CollectionDao collectionDao;

        private PopularAsyncTask(AppDatabase db) {
            collectionDao = db.collectionDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            collectionDao.insertCollection(new Collection(1, "Cars"));
            collectionDao.insertCollection(new Collection(2, "Birds"));
            collectionDao.insertCollection(new Collection(3, "Monets"));
            return null;
        }
    }
}
