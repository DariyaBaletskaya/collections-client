package onpu.pnit.collectionsclient.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import onpu.pnit.collectionsclient.DAO.ItemDao;
import onpu.pnit.collectionsclient.R;
import onpu.pnit.collectionsclient.entities.Collection;
import onpu.pnit.collectionsclient.entities.Item;
import onpu.pnit.collectionsclient.viewmodel.ItemListViewModel;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.r0adkll.slidr.Slidr;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class ItemAddEditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String EXTRA_ID =
            "onpu.pnit.collectionsclient.ui.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "onpu.pnit.collectionsclient.ui.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "onpu.pnit.collectionsclient.ui.EXTRA_DESCRIPTION";
    public static final String EXTRA_ONSALE =
            "onpu.pnit.collectionsclient.ui.EXTRA_ONSALE";
    public static final String EXTRA_PRICE =
            "onpu.pnit.collectionsclient.ui.EXTRA_PRICE";
    public static final String EXTRA_CURRENCY =
            "onpu.pnit.collectionsclient.ui.EXTRA_CURRENCY";

    static final int GALLERY_REQUEST = 1;

    @BindView(R.id.add_edit_item_currency_spinner)
    Spinner currencySpinner;
    private ArrayAdapter<CharSequence> spinnerAdapter;

    // Fields for adding new items
    @BindView(R.id.item_title_input)
    EditText editTextTitle;
    @BindView(R.id.item_description_input)
    EditText editTextDescription;
    @BindView(R.id.item_price_input)
    EditText editTextPrice;

    @BindView(R.id.isItemOnSaleSwitch)
    Switch isItemOnSale;

    @BindView(R.id.item_details_photo)
    ImageView itemImage;

    private ItemListViewModel viewModel;

    private Uri loadedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.item_add_edit);

        ButterKnife.bind(this);
        setTitle(R.string.add_item);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        Slidr.attach(this);

        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.categories_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencySpinner.setAdapter(spinnerAdapter);
        currencySpinner.setOnItemSelectedListener(this);

        itemImage.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
        });
        viewModel = ViewModelProviders.of(this).get(ItemListViewModel.class);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Add new item and check new item's fields
    private void saveItem() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        float price;
        if (!editTextPrice.getText().toString().isEmpty()) {
            price = Float.parseFloat(editTextPrice.getText().toString());
        } else {
            price = 0;
        }

//        String currency = ((TextView) currencySpinner.getSelectedView()).getText().toString();
        Boolean isOnSale = isItemOnSale.isChecked();

        if (title.trim().isEmpty()) {
            Toast.makeText(this, "Please fill the title field", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.insert(new Item(title, description, isOnSale, price, Collection.DEFAULT_USER_ID, loadedImage.getPath()));

        setResult(RESULT_OK);
        finish();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);


        switch (requestCode) {
            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    loadedImage = imageReturnedIntent.getData();

                    Glide.with(this)
                            .load(loadedImage)
                            .into(itemImage);
                }
        }
    }


}
