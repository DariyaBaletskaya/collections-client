package onpu.pnit.collectionsclient.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import onpu.pnit.collectionsclient.R;
import onpu.pnit.collectionsclient.entities.Item;
import onpu.pnit.collectionsclient.repos.ItemRepository;
import onpu.pnit.collectionsclient.viewmodel.EditorCollectionViewModel;
import onpu.pnit.collectionsclient.viewmodel.ItemListViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MyItemDetailsActivity extends AppCompatActivity {

    @BindView(R.id.my_item_details_name)
    public TextView title;
    @BindView(R.id.my_item_details_description)
    public TextView description;
    private ItemListViewModel itemListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_item_details);
        getSupportActionBar().setTitle(R.string.item_details);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        Intent intent = getIntent();
        int id = intent.getIntExtra(ItemsListActivity.ITEM_ID, -1);
        itemListViewModel = ViewModelProviders.of(this).get(ItemListViewModel.class);
        Item item = itemListViewModel.getItemById(id);

        title.setText(item.getTitle());
        description.setText(item.getDescription());

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
