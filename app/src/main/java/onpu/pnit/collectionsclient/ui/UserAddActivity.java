package onpu.pnit.collectionsclient.ui;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

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

import androidx.appcompat.app.AppCompatActivity;
import onpu.pnit.collectionsclient.R;
import onpu.pnit.collectionsclient.entities.User;

public class UserAddActivity extends AppCompatActivity {
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";

    private Pattern patternEmail = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;
    private TextInputLayout usernameWrapper;
    private TextInputLayout passwordWrapper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_add);

        usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        usernameWrapper.setHint("Username");
        passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);
        passwordWrapper.setHint("Password");
        TextInputLayout confirmPasswordWrapper = (TextInputLayout) findViewById(R.id.confirmPasswordWrapper);
        confirmPasswordWrapper.setHint("Confirm Password");


        Button signUpBtn = (Button) findViewById(R.id.save_changes);
        signUpBtn.setOnClickListener(v->{
            String username = usernameWrapper.getEditText().getText().toString();
            String password = passwordWrapper.getEditText().getText().toString();
            String confirmPassword = confirmPasswordWrapper.getEditText().getText().toString();


            if(!validateEmail(username)) {
                usernameWrapper.setError("Not a valid email address!");
            } else if(!validatePassword(password, confirmPassword)){
                passwordWrapper.setError("Passwords must be equal");
            } else {
                usernameWrapper.setErrorEnabled(false);
                passwordWrapper.setErrorEnabled(false);
                doLogin();
            }
            doLogin();

        });
    }

    private boolean validateEmail(String email) {
        matcher = patternEmail.matcher(email);
        return matcher.matches();
    }

    public boolean validatePassword(String password, String confirm) {
        return password.length() > 5 && password.length()<16 && password.equals(confirm);
    }


    public void doLogin() {
        new FetchSecuredResourceTask().execute();
    }

    private void displayResponse(User response) {
        Toast.makeText(this, response.toString(), Toast.LENGTH_LONG).show();
    }


    private class FetchSecuredResourceTask extends AsyncTask<Void, Void, User> {

        private String username;

        private String password;

        private ProgressDialog progressDialog;

        private boolean destroyed = false;

        @Override
        protected void onPreExecute() {
            //showLoadingProgressDialog();

            this.username = usernameWrapper.getEditText().getText().toString();
            this.password = passwordWrapper.getEditText().getText().toString();
        }

        @Override
        protected User doInBackground(Void... params) {
            final String url = getString(R.string.base_uri) + "/registration";

            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));


            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            try {
                ResponseEntity<User> responseEntity = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(requestHeaders),User.class);
                return responseEntity.getBody();
            } catch (HttpClientErrorException e) {
                e.printStackTrace();;
                return new User();

            }
        }

        @Override
        protected void onPostExecute(User user) {
            //dismissProgressDialog();
            //displayResponse(user);
        }
        public void showLoadingProgressDialog() {
            this.showProgressDialog("Loading. Please wait...");
        }

        public void showProgressDialog(CharSequence message) {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(getApplicationContext());
                progressDialog.setIndeterminate(true);
            }

            progressDialog.setMessage(message);
            progressDialog.show();
        }

        public void dismissProgressDialog() {
            if (progressDialog != null && !destroyed) {
                progressDialog.dismiss();
            }
        }

    }


}
