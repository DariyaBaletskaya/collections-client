package onpu.pnit.collectionsclient.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import onpu.pnit.collectionsclient.R;
import onpu.pnit.collectionsclient.entities.Collection;
import onpu.pnit.collectionsclient.viewmodel.CollectionListViewModel;
import onpu.pnit.collectionsclient.viewmodel.EditorCollectionViewModel;
import onpu.pnit.collectionsclient.viewmodel.ItemListViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CollectionAddEditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.category_spinner)
    Spinner categorySpinner;
    private ArrayAdapter<CharSequence> spinnerAdapter;

    // Fields for adding new collections
    @BindView(R.id.edit_text_title)
    EditText editTextTitle;
    @BindView(R.id.edit_text_description)
    EditText editTextDescription;
    private EditorCollectionViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_add_edit);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        ButterKnife.bind(this);

        initSpinners();
        viewModel = ViewModelProviders.of(this).get(EditorCollectionViewModel.class);

        if (getIntent().hasExtra(MainActivity.COLLECTION_ID)) {        // Use for edit exist collection
            setTitle(R.string.edit_exist_collection);
//            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
//            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
//            int spinnerPosition = spinnerAdapter.getPosition(intent.getStringExtra(EXTRA_CATEGORY));
//            categorySpinner.setSelection(spinnerPosition);
        } else {        // Use for create new collection
            setTitle(R.string.create_collection);
        }
    }

    private void initSpinners() {
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.categories_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);
        categorySpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                saveCollection();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Add new collection and check new collection's fields
    private void saveCollection() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        String category = categorySpinner.getSelectedItem().toString();

        if(title.trim().isEmpty()) {
            Toast.makeText(this, "Please enter the title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (getIntent().hasExtra(MainActivity.COLLECTION_ID)) {
            int id = getIntent().getIntExtra(MainActivity.COLLECTION_ID, -1);
            if (id != -1) {
                viewModel.update(new Collection(id, title, category, description, "https://cdn.shopify.com/s/files/1/0414/6957/products/2018_2_Unc_Coin_OBV1_a63e6dae-0c68-4455-889f-5992224da64a_2048x.jpg?v=1532311472", Collection.DEFAULT_USER_ID));
            }
        } else {
            viewModel.insert(new Collection(title, category, description, "https://cdn.shopify.com/s/files/1/0414/6957/products/2018_2_Unc_Coin_OBV1_a63e6dae-0c68-4455-889f-5992224da64a_2048x.jpg?v=1532311472", Collection.DEFAULT_USER_ID));
        }

        setResult(RESULT_OK);
        finish();
    }
}
