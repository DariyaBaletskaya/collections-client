package onpu.pnit.collectionsclient.ui;

import android.content.DialogInterface;
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
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import onpu.pnit.collectionsclient.R;
import onpu.pnit.collectionsclient.adapters.ItemListAdapter;
import onpu.pnit.collectionsclient.entities.Collection;
import onpu.pnit.collectionsclient.entities.Item;
import onpu.pnit.collectionsclient.entities.ItemCollectionJoin;
import onpu.pnit.collectionsclient.viewmodel.ItemListViewModel;

public class CollectionActivity extends AppCompatActivity {

    // using for adding new item
    public static final int ADD_ITEM_REQUEST = 3;
    public static final int DELETE_ITEM_REQUEST = 4;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.items_recyclerView)
    RecyclerView rv;
    @BindView(R.id.collection_add_item_fab)
    FloatingActionButton addItemFab;
    @BindView(R.id.collection_details_description)
    TextView description;
    @BindView(R.id.collection_details_category)
    TextView category;
    @BindView(R.id.collection_frame_layout)
    FrameLayout frameLayout;

    private ItemListAdapter adapter;
    private ItemListViewModel viewModel;
    private int collectionId;
    public static final String ITEM_ID = "item_id";
    public static final int DELETE_COLLECTION_REQUEST = 1;
    public static final int EDIT_COLLECTION_REQUEST = 2;
    private Item cachedItem;
    List<ItemCollectionJoin> cachedJoins = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        Slidr.attach(this);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collectionId = getIntent().getIntExtra(MainActivity.COLLECTION_ID, -1);

        initAdapter();
        initViewModel();
        initRecyclerView();
        initView();

    }

    @OnClick(R.id.collection_add_item_fab)
    void handleFabClick() {
        Intent i = new Intent(CollectionActivity.this, ItemAddEditActivity.class);
        i.putExtra(MainActivity.COLLECTION_ID, collectionId);
        startActivityForResult(i, ADD_ITEM_REQUEST);
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
        if (collectionId != -1) {   // print items which contain in select collection
            viewModel.getItemsForCollection(collectionId).observe(this, items -> adapter.submitList(items));
        }

    }


    private void initAdapter() {
        adapter = new ItemListAdapter(getApplicationContext());
        adapter.setOnItemClickListener((itemId, position) -> {
            cachedItem = adapter.getItemAt(position);
            cachedJoins = viewModel.getAllJoinsForItem(itemId);
            Intent i = new Intent(CollectionActivity.this, MyItemDetailsActivity.class);
            i.putExtra(ITEM_ID, itemId);
            CollectionActivity.this.startActivityForResult(i, DELETE_ITEM_REQUEST);
        });
    }

    private void initRecyclerView() {
        rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
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
                startActivityForResult(i, EDIT_COLLECTION_REQUEST);
                return true;
            case R.id.action_delete_all_items:
                deleteAllItems();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /*Обработка действия "Удалить все" в меню*/
    // TODO: FIX
    private void deleteAllItems() {
        /*Кеш всех удаляемых предметов*/
        List<Item> cachedItems = new ArrayList<>();
        /*Кеш удаляемых связей для всех предметов*/
        /*Проверяем, не пустая ли коллекция*/
        if (adapter.getItemCount() > 0) {
            // Создаем диалог для подтверждения действия
            AlertDialog confirmationDialog = new AlertDialog.Builder(CollectionActivity.this)
                    .setMessage(R.string.q_delete_all_items)
                    // пользователь подтверждает удаление
                    .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) { // Удаление
                            cachedItems.addAll(adapter.getCurrentList()); // Кешируем список предметов (ОК)
                            if (collectionId == Collection.DEFAULT_COLLECTION_ID) { // если удаляем из дефолтной коллекции
                                /*так как удаляем все айтемы, кешируем все связи*/
                                /*не работает*/
                                cachedJoins.addAll(viewModel.getAllJoins());
                                // Если это all items, удаляем вообще все айтемы
                                // используем viewmodel, тк в дефолтной коллекции список айтемов определяется ей
                                viewModel.deleteAllItems();
                            } else {
                                // Если это кастомная коллекция, удаляем айтемы только из нее (удаление связей)
                                viewModel.deleteAllFromCollection(collectionId);
                            }
                            // Снекбар для отмены действия
                            Snackbar.make(CollectionActivity.this.findViewById(R.id.collection_frame_layout), "Deleted!", Snackbar.LENGTH_LONG)
                                    .setAction("Undo", v -> { // отмена удаления
                                        if (collectionId == Collection.DEFAULT_COLLECTION_ID) {
                                            viewModel.insertItems(cachedItems);
                                            viewModel.insertJoins(cachedJoins);
                                            cachedJoins.clear();
                                        } else {
                                            viewModel.insertItemsInCollection(collectionId, cachedItems);
                                        }
                                        cachedItems.clear();
                                    })
                                    .addCallback(new Snackbar.Callback() {
                                        @Override
                                        public void onDismissed(Snackbar transientBottomBar, int event) {
                                            super.onDismissed(transientBottomBar, event);
                                            if (event != DISMISS_EVENT_ACTION)
                                                Toast.makeText(CollectionActivity.this, "All items deleted!", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .show();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel", ((dialog, which) -> dialog.dismiss()))
                    .create();
            confirmationDialog.show();
        } else {
            Toast.makeText(CollectionActivity.this, "Nothing to delete!", Toast.LENGTH_SHORT).show();
        }
    }

    /*Удаление конкретного айтема*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DELETE_ITEM_REQUEST && resultCode == RESULT_OK) {
            if (collectionId == Collection.DEFAULT_COLLECTION_ID) {
                viewModel.deleteItem(cachedItem);
            } else {
                viewModel.deleteItemFromCollection(cachedItem.getId(), collectionId);
            }
            // Снекбар для отмены действия
            Snackbar.make(findViewById(R.id.collection_frame_layout), "Deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo", v -> {
                        Toast.makeText(this, String.valueOf(cachedJoins.size()), Toast.LENGTH_SHORT).show();
                        if (collectionId == Collection.DEFAULT_COLLECTION_ID) {
                            try {
                                /*ошибка где-то тут. cachedItem и cachedJoins - в порядке, вьюмодел их не добавляет в бд*/
                                viewModel.insertItem(cachedItem);
                                viewModel.insertJoins(cachedJoins);
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        } else {
                            viewModel.insertItemInCollection(cachedItem.getId(), collectionId);
                        }
                        cachedJoins.clear();
                    })
                    .show();
        }

    }

}
