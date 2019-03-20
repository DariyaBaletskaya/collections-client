package onpu.pnit.collectionsclient;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import onpu.pnit.collectionsclient.DAO.CollectionDao;
import onpu.pnit.collectionsclient.DAO.ItemCollectionJoinDao;
import onpu.pnit.collectionsclient.DAO.ItemDao;
import onpu.pnit.collectionsclient.DAO.UserDao;
import onpu.pnit.collectionsclient.entities.Collection;
import onpu.pnit.collectionsclient.entities.Item;
import onpu.pnit.collectionsclient.entities.ItemCollectionJoin;
import onpu.pnit.collectionsclient.entities.User;

@Database(entities = {Item.class, Collection.class, ItemCollectionJoin.class, User.class},
        version = 1)
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
                    instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }

        }

        return instance;
    }
}
