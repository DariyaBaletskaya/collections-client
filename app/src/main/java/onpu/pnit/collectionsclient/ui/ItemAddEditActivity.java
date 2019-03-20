package onpu.pnit.collectionsclient.ui;

import androidx.appcompat.app.AppCompatActivity;
import onpu.pnit.collectionsclient.R;

import android.os.Bundle;

public class ItemAddEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_add_edit);
        getSupportActionBar().setTitle(R.string.add_item);
    }
}
