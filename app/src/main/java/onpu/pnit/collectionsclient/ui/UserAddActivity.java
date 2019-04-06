package onpu.pnit.collectionsclient.ui;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.r0adkll.slidr.Slidr;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import onpu.pnit.collectionsclient.R;
import onpu.pnit.collectionsclient.auth.BusProvider;
import onpu.pnit.collectionsclient.auth.ErrorEvent;
import onpu.pnit.collectionsclient.auth.ServerEvent;
import onpu.pnit.collectionsclient.auth.UserClient;
import onpu.pnit.collectionsclient.entities.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAddActivity extends AppCompatActivity {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private static final int GALLERY_REQUEST = 1;

    private Pattern patternEmail = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;

    @BindView(R.id.usernameWrapper)
    TextInputLayout usernameWrapper;
    @BindView(R.id.passwordWrapper)
    TextInputLayout passwordWrapper;
    @BindView(R.id.confirmPasswordWrapper)
    TextInputLayout confirmPasswordWrapper;
    @BindView(R.id.add_user_photo)
    CircleImageView profilePhoto;

    private String username, password;
    private UserClient userClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_add);
        ButterKnife.bind(this);
        Slidr.attach(this);

        usernameWrapper.setHint("Username");
        passwordWrapper.setHint("Password");
        confirmPasswordWrapper.setHint("Confirm Password");

        profilePhoto.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent();
            photoPickerIntent.setType("image/*");
            photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
        });


        Button signUpBtn = (Button) findViewById(R.id.save_changes);
        signUpBtn.setOnClickListener(v -> {
             username = usernameWrapper.getEditText().getText().toString();
             password = passwordWrapper.getEditText().getText().toString();
            String confirmPassword = confirmPasswordWrapper.getEditText().getText().toString();


            if (!validateEmail(username)) {
                usernameWrapper.setError("Not a valid email address!");
            } else if (!validatePassword(password, confirmPassword)) {
                passwordWrapper.setError("Passwords must be equal");
            } else {
                usernameWrapper.setErrorEnabled(false);
                passwordWrapper.setErrorEnabled(false);
                doRegister(username,password);
            }

        });
    }

    private boolean validateEmail(String email) {
        matcher = patternEmail.matcher(email);
        return matcher.matches();
    }

    public boolean validatePassword(String password, String confirm) {
        return password.length() > 5 && password.length() < 16 && password.equals(confirm);
    }


    public void doRegister(String username, String password) {
        Call<User> call = userClient.registerUser(username,password);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                BusProvider.getInstance().post(new ServerEvent(response.body()));
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // handle execution failures like no internet connectivity
                BusProvider.getInstance().post(new ErrorEvent(-2,t.getMessage()));
            }
        });

        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    Uri loadedImage = imageReturnedIntent.getData();

                    Glide.with(this)
                            .load(loadedImage)
                            .into(profilePhoto);
                }
        }
    }


}