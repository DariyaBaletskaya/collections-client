package onpu.pnit.collectionsclient.ui;

import androidx.appcompat.app.AppCompatActivity;
import onpu.pnit.collectionsclient.R;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MyItemDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_item_details);
        getSupportActionBar().setTitle(R.string.item_details);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_item_details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_item_details_action_delete:
                deleteItem();
                return true;
            case R.id.my_item_details_action_edit:
                editItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editItem() {
        Toast.makeText(MyItemDetailsActivity.this, "Edit item", Toast.LENGTH_SHORT).show();
    }

    private void deleteItem() {
        Toast.makeText(MyItemDetailsActivity.this, "Delete item", Toast.LENGTH_SHORT).show();
    }
}
