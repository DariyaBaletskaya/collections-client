package onpu.pnit.collectionsclient.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import onpu.pnit.collectionsclient.R;


public class UserEditActivity extends AppCompatActivity {


    private TextInputLayout Username;
    private TextInputLayout PlaceOfLiving;
    int CANCEL_REQUEST_CODE = 1;

    @OnClick(R.id.save_changes)
    public void saveClick(View view){


        finish();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_edit);

        getSupportActionBar().setTitle(R.string.nav_profile);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
}
