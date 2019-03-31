package onpu.pnit.collectionsclient.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.r0adkll.slidr.Slidr;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import onpu.pnit.collectionsclient.R;
import onpu.pnit.collectionsclient.adapters.ItemListAdapter;
import onpu.pnit.collectionsclient.entities.Collection;
import onpu.pnit.collectionsclient.entities.Item;
import onpu.pnit.collectionsclient.viewmodel.ItemListViewModel;

public class ItemsListActivity extends AppCompatActivity {

    @BindView(R.id.list_item)
    RecyclerView recyclerViewItems;

    private ItemListAdapter adapter;
    private ItemListViewModel viewModel;
    public static final String ITEM_ID = "item_id";
    public static final int DELETE_REQUEST = 1;
    private int collectionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        Slidr.attach(this);
        ButterKnife.bind(this);

        collectionId = getIntent().getIntExtra(MainActivity.COLLECTION_ID, -1);
        if (collectionId == Collection.DEFAULT_COLLECTION_ID) {
            setTitle("All items");
        } else if (collectionId != -1) {
            setTitle("Collection");
        }

        initRecyclerView();
        initViewModel();
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ItemListViewModel.class);
        if (collectionId == Collection.DEFAULT_COLLECTION_ID) {
            viewModel.getAllItems().observe(this, items ->
                    adapter.submitList(items)
            );
        } else if (collectionId != -1) {
            viewModel.getItemsForCollection(collectionId).observe(this, items ->
                    adapter.submitList(items)
            );
        }
    }

    private void initRecyclerView(){
        adapter = new ItemListAdapter(getApplicationContext());
        adapter.SetOnItemClickListener((ItemId, position) -> {
            Intent i = new Intent(ItemsListActivity.this, MyItemDetailsActivity.class);
            i.putExtra(ITEM_ID, ItemId);
            startActivityForResult(i, DELETE_REQUEST);
        });
        recyclerViewItems.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerViewItems.setHasFixedSize(true);
        recyclerViewItems.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DELETE_REQUEST && resultCode == RESULT_OK) {
            Snackbar.make(findViewById(R.id.item_list_constraintlayout), "Item deleted", Snackbar.LENGTH_LONG).show();
//                    .setAction("Undo", v -> {
//                        viewModel.insert(data.getParcelableExtra(MyItemDetailsActivity.DELETED_ITEM));
//                    })
//                    .addCallback(new Snackbar.Callback() {
//                        @Override
//                        public void onDismissed(Snackbar transientBottomBar, int event) {
//                            super.onDismissed(transientBottomBar, event);
//                            if (event != DISMISS_EVENT_ACTION)
//                                Toast.makeText(StudentListActivity.this, R.string.all_students_deleted, Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .show();
        }
    }
}
