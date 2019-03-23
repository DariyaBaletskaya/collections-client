package onpu.pnit.collectionsclient.ui;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import onpu.pnit.collectionsclient.R;

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

    public static final String EXTRA_TITLE =
            "onpu.pnit.collectionsclient.ui.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "onpu.pnit.collectionsclient.ui.EXTRA_DESCRIPTION";
    public static final String EXTRA_CATEGORY =
            "onpu.pnit.collectionsclient.ui.EXTRA_CATEGORY";


    private Spinner categorySpinner;
    private ArrayAdapter<CharSequence> spinnerAdapter;

    // Fields for adding new collections
    private EditText editTextTitle;
    private EditText editTextDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_add_edit);
        getSupportActionBar().setTitle(R.string.create_collection);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        categorySpinner = findViewById(R.id.category_spinner);
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.categories_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);
        categorySpinner.setOnItemSelectedListener(this);

        editTextDescription = findViewById(R.id.edit_text_description);
        editTextTitle = findViewById(R.id.edit_text_title);
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
        inflater.inflate(R.menu.collection_add_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_collection_save:
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
        TextView textView = (TextView) categorySpinner.getSelectedView();
        String category = textView.getText().toString();

        if(title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please fill all field", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_CATEGORY, category);

        setResult(RESULT_OK, data);
        finish();
    }
}
