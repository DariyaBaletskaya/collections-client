package onpu.pnit.collectionsclient.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.r0adkll.slidr.Slidr;

import java.util.Objects;

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
        adapter = new ItemListAdapter();
        adapter.SetOnItemClickListener((ItemId, position) -> {
            //Toast.makeText(ItemsListActivity.this, String.valueOf(ItemId), Toast.LENGTH_SHORT).show();
            Intent i = new Intent(ItemsListActivity.this, MyItemDetailsActivity.class);
            i.putExtra(ITEM_ID, ItemId);
            startActivity(i);
        });
        recyclerViewItems.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerViewItems.setHasFixedSize(true);
        recyclerViewItems.setAdapter(adapter);
        initViewModel();
    }



}
