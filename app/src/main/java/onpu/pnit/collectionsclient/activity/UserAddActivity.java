package onpu.pnit.collectionsclient.activity;


import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;
import onpu.pnit.collectionsclient.R;

public class UserAddActivity extends AppCompatActivity {
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private static final String AGE_PATTERN = "[0-9]*";
    private Pattern patternEmail = Pattern.compile(EMAIL_PATTERN);
    private Pattern patternAge = Pattern.compile(AGE_PATTERN);
    private Matcher matcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_add);

        TextInputLayout usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        usernameWrapper.setHint("Username");
        TextInputLayout passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);
        passwordWrapper.setHint("Password");
        TextInputLayout confirmPasswordWrapper = (TextInputLayout) findViewById(R.id.confirmPasswordWrapper);
        confirmPasswordWrapper.setHint("Confirm Password");
        TextInputLayout countryWrapper = (TextInputLayout) findViewById(R.id.countryWrapper);
        countryWrapper.setHint("Country");
        TextInputLayout cityWrapper = (TextInputLayout) findViewById(R.id.cityWrapper);
        cityWrapper.setHint("City");
        TextInputLayout ageWrapper = (TextInputLayout) findViewById(R.id.ageWrapper);
        ageWrapper.setHint("Age");
        TextInputLayout userInfoWrapper = (TextInputLayout) findViewById(R.id.userInfoWrapper);
        userInfoWrapper.setHint("Info");

        Button signUpBtn = (Button) findViewById(R.id.sign_up);
        signUpBtn.setOnClickListener(v->{
            String username = usernameWrapper.getEditText().getText().toString();
            String password = passwordWrapper.getEditText().getText().toString();
            String confirmPassword = confirmPasswordWrapper.getEditText().getText().toString();
            String age = ageWrapper.getEditText().getText().toString();

            if(!validateEmail(username)) {
                usernameWrapper.setError("Not a valid email address!");
            } else if(!validatePassword(password, confirmPassword)){
                passwordWrapper.setError("Passwords must be equal");
            } else if(!validateAge(age)) {
                passwordWrapper.setError("Not a valid age!");
            } else {
                usernameWrapper.setErrorEnabled(false);
                passwordWrapper.setErrorEnabled(false);
                doLogin();
            }

        });
    }

    private boolean validateEmail(String email) {
        matcher = patternEmail.matcher(email);
        return matcher.matches();
    }

    public boolean validatePassword(String password, String confirm) {
        return password.length() > 5 && password.length()<16 && password.equals(confirm);
    }
    private boolean validateAge(String age) {
        matcher = patternAge.matcher(age);
        if(matcher.matches()) {
            if(Integer.parseInt(age) > 0 && Integer.parseInt(age) < 110) {
                return true;
            }
        }

        return false;
    }

    public void doLogin() {
        Toast.makeText(getApplicationContext(), "I'm performing signup.", Toast.LENGTH_SHORT).show();
        // TODO: коннект с БД
    }
}
