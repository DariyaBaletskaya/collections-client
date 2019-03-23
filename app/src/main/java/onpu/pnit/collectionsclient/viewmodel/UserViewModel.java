package onpu.pnit.collectionsclient.viewmodel;

import android.app.Application;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import onpu.pnit.collectionsclient.entities.User;
import onpu.pnit.collectionsclient.repos.UserRepository;

public class UserViewModel extends AndroidViewModel {
    private UserRepository repository;
    private Executor executor = Executors.newSingleThreadExecutor();
    private MutableLiveData<User> user = new MutableLiveData<>();

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    public void getUser(int id) {
        executor.execute(() -> {
            user.postValue(repository.getUserById(id));
        });
    }

    public void insert(User user) {
       executor.execute(() -> {
           repository.insertUser(user);
       });
    }

}
