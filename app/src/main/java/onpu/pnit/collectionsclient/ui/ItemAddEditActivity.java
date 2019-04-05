package onpu.pnit.collectionsclient.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import onpu.pnit.collectionsclient.R;
import onpu.pnit.collectionsclient.entities.Collection;
import onpu.pnit.collectionsclient.entities.Item;
import onpu.pnit.collectionsclient.viewmodel.ItemCollectionJoinViewModel;
import onpu.pnit.collectionsclient.viewmodel.ItemListViewModel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.r0adkll.slidr.Slidr;

import java.util.concurrent.ExecutionException;

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
    // Fields for adding new items
    @BindView(R.id.item_title_input)
    EditText editTextTitle;
    @BindView(R.id.item_description_input)
    EditText editTextDescription;
    @BindView(R.id.item_price_input)
    EditText editTextPrice;

    @BindView(R.id.isItemOnSaleSwitch)
    Switch isItemOnSaleSwitch;

    @BindView(R.id.item_details_photo)
    ImageView itemImage;

    private ItemListViewModel viewModel;
    private ItemCollectionJoinViewModel itemCollectionJoinViewModel;

    private Uri loadedImage;
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private int collectionId = 0;
    private int editableItemId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_add_edit);
        Slidr.attach(this);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = ViewModelProviders.of(this).get(ItemListViewModel.class);
        itemCollectionJoinViewModel = ViewModelProviders.of(this).get(ItemCollectionJoinViewModel.class);

        initView();

    }

    private void initView() {
        initSpinner();
        if (getIntent().hasExtra(MyItemDetailsActivity.ITEM_ID)) {
            // Если это изменение айтема
            setTitle(R.string.edit_item);
            editableItemId = getIntent().getIntExtra(MyItemDetailsActivity.ITEM_ID, -1);
            viewModel.getItemById(editableItemId).observe(this, item -> {
                editTextTitle.setText(item.getTitle());
                editTextTitle.setSelection(editTextTitle.getText().length());
                editTextDescription.setText(item.getDescription());
                editTextPrice.setText(String.valueOf(item.getPrice()));
                isItemOnSaleSwitch.setChecked(item.isOnSale());
                currencySpinner.setSelection(spinnerAdapter.getPosition(item.getCurrency()));
            });

        } else if (getIntent().hasExtra(MainActivity.COLLECTION_ID)) {
            // если это добавление
            setTitle(R.string.add_item);
            collectionId = getIntent().getIntExtra(MainActivity.COLLECTION_ID, -1);
        }
    }

    @OnClick(R.id.item_details_photo)
    void handleImageClick() {
        Intent photoPickerIntent = new Intent();
        photoPickerIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        photoPickerIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        photoPickerIntent.setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        photoPickerIntent.setFlags(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
        photoPickerIntent.setType("image/*");
        photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    private void initSpinner() {
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.currencies_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencySpinner.setAdapter(spinnerAdapter);
        currencySpinner.setOnItemSelectedListener(this);
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
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //     Add new item and check new item's fields
    private void saveItem() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        float price;
        if (!editTextPrice.getText().toString().isEmpty()) {
            price = Float.parseFloat(editTextPrice.getText().toString());
        } else {
            price = 0;
        }
        String currency = currencySpinner.getSelectedItem().toString();
        boolean isOnSale = isItemOnSaleSwitch.isChecked();
        if (!title.isEmpty() && loadedImage != null) {
            if (getIntent().hasExtra(CollectionActivity.ITEM_ID)) {
                viewModel.update(new Item(editableItemId, title, description, isOnSale, price, currency, Collection.DEFAULT_USER_ID, loadedImage.getPath()));
            } else if (getIntent().hasExtra(MainActivity.COLLECTION_ID)) {
                int addedItemId = 0;
                try {
                    addedItemId = ((int) viewModel.insert(new Item(title, description, isOnSale, price, currency, Collection.DEFAULT_USER_ID, loadedImage.getPath())));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (collectionId == Collection.DEFAULT_COLLECTION_ID) {
                    itemCollectionJoinViewModel.insertItemCollectionJoin(addedItemId, Collection.DEFAULT_COLLECTION_ID);
                } else {
                    itemCollectionJoinViewModel.insertItemCollectionJoin(addedItemId, Collection.DEFAULT_COLLECTION_ID);
                    itemCollectionJoinViewModel.insertItemCollectionJoin(addedItemId, collectionId);
                }
            }

            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Please enter title and choose image!", Toast.LENGTH_SHORT).show();
        }

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
