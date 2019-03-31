package onpu.pnit.collectionsclient.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import onpu.pnit.collectionsclient.R;
import onpu.pnit.collectionsclient.viewmodel.ItemListViewModel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
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
    @BindView(R.id.my_item_details_category)
    TextView category;
    @BindView(R.id. my_item_details_price)
    TextView price;
    @BindView(R.id.my_item_details_is_on_sale)
    Switch isOnSale;
    private ItemListViewModel itemListViewModel;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_item_details);
        setTitle(R.string.item_details);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        ButterKnife.bind(this);
        Slidr.attach(this);
        initViewModel();




    }

    private void initViewModel() {
        itemListViewModel = ViewModelProviders.of(this).get(ItemListViewModel.class);

        id = getIntent().getIntExtra(ItemsListActivity.ITEM_ID, -1);
        itemListViewModel.getItemById(id);

        itemListViewModel.getItem().observe(this, item -> {
            if (item != null) {
                photo.setImageURI(Uri.parse(item.getImage()));
                title.setText(item.getTitle());
                description.setText(item.getDescription());
                price.setText(String.valueOf(item.getPrice()));
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
        itemListViewModel.getItem().observe(this, item -> {
            if (item != null) {
               i.putExtra(ITEM_ID, item.getId());
            }
        });
        startActivityForResult(i, EDIT_ITEM_REQUEST);
        onBackPressed();
    }

    //TODO: добавить возможность отмены удаления, UNDO как при удалении коллекции
    // Пока непонятно, как это сделать. Нам надо каким-то образом передавать обратно в активити списка айтемов удаленную инфу
    // Чтобы по нажатию на снекбар айтем заново добавлялся. Это можно провернуть, если айтем будет parcelable, но для этого надо поправить вьюмодел.
    private void deleteItem() {
//        Toast.makeText(MyItemDetailsActivity.this, "Delete item", Toast.LENGTH_SHORT).show();
//        Intent data = new Intent();
//        data.putExtra(DELETED_ITEM, (Parcelable) itemListViewModel.getItem());
//        setResult(RESULT_OK, data);
//        finish();
//        itemListViewModel.getItem().observe(this, item -> {
//            if (item != null) {
//                itemListViewModel.delete(item);
//            }
//        });
//        onBackPressed();
        itemListViewModel.delete(itemListViewModel.getItem().getValue());
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_ITEM_REQUEST && resultCode == RESULT_OK) {
            Toast.makeText(MyItemDetailsActivity.this, "Item has been changed", Toast.LENGTH_SHORT).show();
            initViewModel();
        }
    }
}
