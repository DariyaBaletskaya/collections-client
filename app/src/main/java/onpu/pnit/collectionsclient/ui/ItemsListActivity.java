package onpu.pnit.collectionsclient.ui;

import android.content.Intent;
import android.os.Bundle;

import com.r0adkll.slidr.Slidr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import onpu.pnit.collectionsclient.R;
import onpu.pnit.collectionsclient.adapters.ItemListAdapter;
import onpu.pnit.collectionsclient.viewmodel.ItemListViewModel;

public class ItemsListActivity extends AppCompatActivity {

    @BindView(R.id.list_item)
    RecyclerView recyclerViewItems;

    private ItemListAdapter adapter;
    private ItemListViewModel itemListViewModel;
    public static final String ITEM_ID = "item_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);
        setTitle("My items");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        Slidr.attach(this);

        ButterKnife.bind(this);

        recyclerViewItems.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        adapter = new ItemListAdapter();
        adapter.SetOnItemClickListener((ItemId, position) -> {
            Intent i = new Intent(ItemsListActivity.this, ItemDetailsActivity.class);
            i.putExtra(ITEM_ID, ItemId);
            ItemsListActivity.this.startActivity(i);
        });

        recyclerViewItems.setHasFixedSize(true);
        recyclerViewItems.setAdapter(adapter);

        initViewModel();
    }

    private void initViewModel() {
        itemListViewModel = ViewModelProviders.of(this).get(ItemListViewModel.class);
        itemListViewModel.getAllItems().observe(this, items ->
            adapter.submitList(items)
        );
    }


}
