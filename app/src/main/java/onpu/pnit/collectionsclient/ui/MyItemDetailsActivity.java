package onpu.pnit.collectionsclient.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import onpu.pnit.collectionsclient.R;
import onpu.pnit.collectionsclient.viewmodel.ItemListViewModel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.r0adkll.slidr.Slidr;

public class MyItemDetailsActivity extends AppCompatActivity {

    // using for edit exist item
    public static final int EDIT_ITEM_REQUEST = 1;
    public static final String ITEM_ID = "item_id";
    public static final String DELETED_ITEM = "deleted_item";

    @BindView(R.id.my_item_details_photo)
    ImageView photo;
    @BindView(R.id.my_item_details_name)
    TextView title;
    @BindView(R.id.my_item_details_description)
    TextView description;
    @BindView(R.id.my_item_details_price)
    TextView price;
    @BindView(R.id.my_item_details_is_on_sale)
    Switch isOnSale;
    private ItemListViewModel itemListViewModel;
    private int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_item_details);
        setTitle(R.string.item_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        Slidr.attach(this);
        initViewModel();

    }

    private void initViewModel() {
        itemListViewModel = ViewModelProviders.of(this).get(ItemListViewModel.class);

        itemId = getIntent().getIntExtra(CollectionActivity.ITEM_ID, -1);

        itemListViewModel.getItemById(itemId).observe(this, item -> {
            if (item != null) {
                photo.setImageURI(Uri.parse(item.getImage()));
                title.setText(item.getTitle());
                description.setText(item.getDescription());
                price.setText(String.valueOf(item.getPrice()) + " " + item.getCurrency());
                isOnSale.setChecked(item.isOnSale());
            }
        });


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
            case android.R.id.home:
                finish();
                return true;
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
        Intent i = new Intent(MyItemDetailsActivity.this, ItemAddEditActivity.class);
        i.putExtra(ITEM_ID, itemId);
        startActivityForResult(i, EDIT_ITEM_REQUEST);
    }


    private void deleteItem() {
        AlertDialog confirmationDialog = new AlertDialog.Builder(MyItemDetailsActivity.this)
                .setMessage("Delete item?")
                // пользователь подтверждает удаление
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    dialog.dismiss();
                    setResult(RESULT_OK);
                    finish();
                })
                .setNegativeButton("Cancel", ((dialog, which) -> dialog.dismiss()))
                .create();
        confirmationDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_ITEM_REQUEST && resultCode == RESULT_OK) {
            Toast.makeText(MyItemDetailsActivity.this, "Item changed!", Toast.LENGTH_SHORT).show();
            // TODO: починить обновление вьюшки после изменения, инициировать вьюмодел каждый раз не оч
            initViewModel();
        } else {
            Toast.makeText(MyItemDetailsActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }
}
