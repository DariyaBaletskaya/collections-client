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

    @Delete
    void deleteUser(User user);

    @Query("SELECT * from users WHERE id=:id")
    User getUserById(int id);

    @Query("SELECT * from users WHERE username=:username")
    User getUserByUsername(String username);

    @Update
    void updateUsers(User... users);

    @Query("SELECT * from users")
    LiveData<List<User>> getAllUsers();
}
