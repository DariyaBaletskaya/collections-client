package onpu.pnit.collectionsclient.repos;

import android.content.Context;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;
import onpu.pnit.collectionsclient.AppDatabase;
import onpu.pnit.collectionsclient.entities.User;

public class UserRepository {

    private static UserRepository ourInstance;

    private LiveData<List<User>> users;

    private AppDatabase db;

    private Executor executor = Executors.newSingleThreadExecutor();

    public UserRepository(Context context) {
        db = AppDatabase.getInstance(context);
        users = getAllUsers();
    }

    public static UserRepository getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new UserRepository(context);
        }
        return ourInstance;
    }

    private LiveData<List<User>> getAllUsers() {
        return db.userDao().getAllUsers();
    }

    public User getUserByUsername(String username) {
        return db.userDao().getUserByUsername(username);
    }

    public User getUserById(int id) {
        return db.userDao().getUserById(id);
    }

    public void deleteUser(User user) {
        executor.execute(() -> db.userDao().deleteUser(user));
    }

    public void deleteUsers(List<User> users) {
        executor.execute(() -> db.userDao().deleteUsers(users));
    }

    public void insertUser(User user) {
        executor.execute(() -> db.userDao().insertUser(user));
    }

    public void insertUsers(List<User> users) {
        executor.execute(() -> db.userDao().insertUsers(users));
    }

    public void updateUser(User user) {
        executor.execute(() -> db.userDao().updateUser(user));
    }

    public void updateUsers(List<User> users) {
        executor.execute(() -> db.userDao().updateUsers(users));
    }

}
