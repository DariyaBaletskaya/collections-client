package onpu.pnit.collectionsclient.DAO;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import onpu.pnit.collectionsclient.entities.User;

@Dao
public interface UserDao {

    @Insert
    void insertUser(User user);

    @Insert
    void insertUsers(User... users);

    @Delete
    void deleteUser(User user);

    @Delete
    void deleteUsers(User... users);

    @Query("SELECT * from users WHERE user_id=:userId")
    User getUserById(int userId);

    @Query("SELECT * from users WHERE username=:username")
    User getUserByUsername(String username);

    @Update
    void updateUsers(User... users);

    @Update
    void updateUser(User user);

    @Query("SELECT * from users")
    LiveData<List<User>> getAllUsers();
}
