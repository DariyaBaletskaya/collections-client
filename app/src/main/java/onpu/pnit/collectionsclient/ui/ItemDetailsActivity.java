package onpu.pnit.collectionsclient.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.r0adkll.slidr.Slidr;

import androidx.appcompat.app.AppCompatActivity;
import onpu.pnit.collectionsclient.R;

public class ItemDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);
        getSupportActionBar().setTitle(R.string.item_details);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        Slidr.attach(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_details_action_share:
                shareItemDetails();
                return true;
            case R.id.item_details_action_favourites:
                addToFavourites();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addToFavourites() {
        Toast.makeText(ItemDetailsActivity.this, "Add to favourites", Toast.LENGTH_SHORT).show();
    }

    private void shareItemDetails() {
        Toast.makeText(ItemDetailsActivity.this, "Share item details", Toast.LENGTH_SHORT).show();
    }
}
