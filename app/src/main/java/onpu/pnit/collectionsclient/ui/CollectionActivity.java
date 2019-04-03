package onpu.pnit.collectionsclient.ui;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.r0adkll.slidr.Slidr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import onpu.pnit.collectionsclient.R;
import onpu.pnit.collectionsclient.adapters.ItemListAdapter;
import onpu.pnit.collectionsclient.entities.Collection;
import onpu.pnit.collectionsclient.entities.Item;
import onpu.pnit.collectionsclient.viewmodel.CollectionViewModel;
import onpu.pnit.collectionsclient.viewmodel.ItemListViewModel;

public class CollectionActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.items_recyclerView)
    RecyclerView rv;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.collection_details_description)
    TextView description;
    @BindView(R.id.collection_details_category)
    TextView category;

    private ItemListAdapter adapter;
    private ItemListViewModel viewModel;
    private int collectionId;
    public static final String ITEM_ID = "item_id";
    public static final int DELETE_REQUEST = 1;
    public static final int EDIT_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        Slidr.attach(this);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collectionId = getIntent().getIntExtra(MainActivity.COLLECTION_ID, -1);

        initRecyclerView();
        initViewModel();
        initView();

        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }

    private void initView() {
        if (collectionId == Collection.DEFAULT_COLLECTION_ID) {
            setTitle("All items");
        } else if (collectionId != -1) {
            viewModel.getCollectionById(collectionId).observe(this, collection -> {
                setTitle(collection.getTitle());
                if (!collection.getDescription().isEmpty()) {
                    description.setVisibility(View.VISIBLE);
                    description.setText(collection.getDescription());
                }
                if (!collection.getCategory().isEmpty()) {
                    category.setVisibility(View.VISIBLE);
                    category.setText(collection.getCategory());
                }
            });
        }
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ItemListViewModel.class);
        if (collectionId == Collection.DEFAULT_COLLECTION_ID) {
            viewModel.getAllItems().observe(this, items -> adapter.submitList(new ArrayList<>(items)));
        } else if (collectionId != -1) {
            viewModel.getItemsForCollection(collectionId).observe(this, items -> adapter.submitList(new ArrayList<>(items)));
        }
    }

    private void initRecyclerView() {
        adapter = new ItemListAdapter(getApplicationContext());
        adapter.setOnItemClickListener((ItemId, position) -> {
            Intent i = new Intent(CollectionActivity.this, MyItemDetailsActivity.class);
            i.putExtra(ITEM_ID, ItemId);
            startActivityForResult(i, DELETE_REQUEST);
        });
        rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.collection_menu, menu);
        if (collectionId == Collection.DEFAULT_COLLECTION_ID) {
            menu.getItem(0).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_collection:
                Intent i = new Intent(CollectionActivity.this, CollectionAddEditActivity.class);
                i.putExtra(MainActivity.COLLECTION_ID, collectionId);
                startActivityForResult(i, EDIT_REQUEST);
                return true;
            case R.id.action_delete_all_items:
                // TODO: AlertDialog with buttons
                viewModel.getAllItems().observe(this, items -> viewModel.deleteAll(items));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_REQUEST && resultCode == RESULT_OK) {
            initView();
        }
    }
}
