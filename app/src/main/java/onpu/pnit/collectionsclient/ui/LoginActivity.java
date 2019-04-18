package onpu.pnit.collectionsclient.ui;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import onpu.pnit.collectionsclient.R;
import onpu.pnit.collectionsclient.auth.BusProvider;
import onpu.pnit.collectionsclient.auth.ErrorEvent;
import onpu.pnit.collectionsclient.auth.ServerEvent;
import onpu.pnit.collectionsclient.auth.UserClient;
import onpu.pnit.collectionsclient.entities.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends AppCompatActivity {

    private static final String SERVER_URL = "https://collections-blue.herokuapp.com";
    public static final String USERNAME = "username";

    @BindView(R.id.email)
    AutoCompleteTextView emailField;
    @BindView(R.id.password)
    EditText passwordField;
    @BindView(R.id.email_sign_in_button)
    Button signInButton;
    @BindView(R.id.tv_signUp)
    TextView signUpButton;
    @BindView(R.id.tv_forgotPassword)
    TextView forgotPasswordButton;

    private String username, password;
    private UserClient userClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_form);

        ButterKnife.bind(this);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(SERVER_URL)
                .build();

        userClient = retrofit.create(UserClient.class);

        signInButton.setOnClickListener(v -> {
            username = emailField.getText().toString();
            password = passwordField.getText().toString();

            doLogin(username,password);
        });


        signUpButton.setOnClickListener(v -> {
            Intent i = new Intent(this, UserAddActivity.class);
            startActivity(i);
        });

    }

    //login existing user
    private void doLogin(String username, String password) {

        Call<User> callGet = userClient.getRegistrationPage();
        callGet.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                BusProvider.getInstance().post(new ServerEvent(response.body()));
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // handle execution failures like no internet connectivity
                BusProvider.getInstance().post(new ErrorEvent(-2, t.getMessage()));
            }
        });


        Call<User> callPost = userClient.registerUser(username, password);

            callPost.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    BusProvider.getInstance().post(new ServerEvent(response.body()));
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    // handle execution failures like no internet connectivity
                    BusProvider.getInstance().post(new ErrorEvent(-2, t.getMessage()));
                }
            });

            Call<User> callGetUsername = userClient.getUser(username);

            callGetUsername.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    System.out.println(response.body().getUsername());
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.putExtra(USERNAME, username);
                    startActivity(i);
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    // handle execution failures like no internet connectivity
                    BusProvider.getInstance().post(new ErrorEvent(-2, t.getMessage()));
                }
            });

        Call<User> callLogin = userClient.loginUser(username,password);
        callLogin.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                BusProvider.getInstance().post(new ServerEvent(response.body()));
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // handle execution failures like no internet connectivity
                BusProvider.getInstance().post(new ErrorEvent(-2, t.getMessage()));
            }
        });




    }

    //getting user from the DB to accept credentials
//    private void acceptLogin(String username)  {
//
//        Call<User> callGet = userClient.getUser(username);
//        callGet.enqueue(new Callback<User>() {
//        @Override
//        public void onResponse(Call<User> call, Response<User> response) {
//            BusProvider.getInstance().post(new ServerEvent(response.body()));
//            System.out.println(response.body());
//        }
//
//        @Override
//        public void onFailure(Call<User> call, Throwable t) {
//            // handle execution failures like no internet connectivity
//            BusProvider.getInstance().post(new ErrorEvent(-2, t.getMessage()));
//
//        }
//    });



}



