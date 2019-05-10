package onpu.pnit.collectionsclient.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import onpu.pnit.collectionsclient.R;
import onpu.pnit.collectionsclient.entities.Collection;
import onpu.pnit.collectionsclient.viewmodel.EditorCollectionViewModel;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.r0adkll.slidr.Slidr;

public class CollectionAddEditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.category_spinner)
    Spinner categorySpinner;

    // Fields for adding new collections
    @BindView(R.id.edit_text_title)
    EditText editTextTitle;
    @BindView(R.id.edit_text_description)
    EditText editTextDescription;

    private EditorCollectionViewModel viewModel;
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private int editableCollectionId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_add_edit);
        Slidr.attach(this);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initSpinner();

        viewModel = ViewModelProviders.of(this).get(EditorCollectionViewModel.class);

        if (getIntent().hasExtra(MainActivity.COLLECTION_ID)) {// Use for edit exist collection
            setTitle(R.string.edit_exist_collection);
            editableCollectionId = getIntent().getIntExtra(MainActivity.COLLECTION_ID, -1);
            if (editableCollectionId != -1) {
                viewModel.getCollectionById(editableCollectionId).observe(this, collection -> {
                    editTextTitle.setText(collection.getTitle());
                    editTextTitle.setSelection(editTextTitle.getText().length());
                    categorySpinner.setSelection(spinnerAdapter.getPosition(collection.getCategory()));
                    editTextDescription.setText(collection.getDescription());
                });
            }
        } else {
            setTitle(R.string.create_collection);
        }
    }

    private void initSpinner() {
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
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Add new collection and check new collection's fields
    private void saveCollection() {
        String title = String.valueOf(editTextTitle.getText()).trim();
        String description = String.valueOf(editTextDescription.getText()).trim();
        String category = categorySpinner.getSelectedItem().toString();

        if(title.isEmpty()) {
            Toast.makeText(this, "Please enter the title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (getIntent().hasExtra(MainActivity.COLLECTION_ID)) {
            if (editableCollectionId != -1) {
                viewModel.updateCollection(new Collection(editableCollectionId, title.trim(), category.trim(), description.trim(), Collection.DEFAULT_USER_ID));
            }
        } else {
            viewModel.insertCollection(new Collection(title.trim(), category.trim(), description.trim(), Collection.DEFAULT_USER_ID));
        }

        setResult(RESULT_OK);
        finish();
    }
}
