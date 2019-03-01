package onpu.pnit.collectionsclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ItemDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);
        getSupportActionBar().setTitle(R.string.item_details);

    }
}
