package onpu.pnit.collectionsclient.ui;

import android.animation.Animator;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import onpu.pnit.collectionsclient.NetworkReceiver;
import onpu.pnit.collectionsclient.R;
import onpu.pnit.collectionsclient.adapters.CollectionsListAdapter;
import onpu.pnit.collectionsclient.entities.Collection;
import onpu.pnit.collectionsclient.viewmodel.EditorCollectionViewModel;
import onpu.pnit.collectionsclient.viewmodel.ItemListViewModel;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    public static final String COLLECTION_ID = "collection_id";
    //Broadcast receiver for internet connection
    NetworkReceiver networkReceiver = new NetworkReceiver();
    // using for adding new collection
    public static final int ADD_COLLECTION_REQUEST = 1;
    // using for edit exist collection
    public static final int EDIT_COLLECTION_REQUEST = 2;
    // using for adding new item
    public static final int ADD_ITEM_REQUEST = 3;

    //control over Fab Menu
    private boolean isFabMenuOpened = false;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.list_collections)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.fab_layout_collection)
    LinearLayout fabLayoutCollection;
    @BindView(R.id.fab_collection)
    FloatingActionButton fabCollection;
    @BindView(R.id.fab_layout_item)
    LinearLayout fabLayoutItem;
    @BindView(R.id.fab_item)
    FloatingActionButton fabItem;



    private CollectionsListAdapter adapter;
    private EditorCollectionViewModel editorCollectionListViewModel;
    private ItemListViewModel itemListViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);

        //setting username in drawer
        View headerView = navigationView.getHeaderView(0);
        TextView drawerUsername = (TextView) headerView.findViewById(R.id.drawer_username);

        Intent getUsername = getIntent();
        drawerUsername.setText(getUsername.getStringExtra(LoginActivity.USERNAME));

        fab.setOnClickListener(v -> {
                    if (!isFabMenuOpened) {
                        showFabMenu();
                    } else {
                        closeFabMenu();
                    }
                }
        );

        fabCollection.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, CollectionAddEditActivity.class);
            startActivityForResult(i, ADD_COLLECTION_REQUEST);  // add new collection
        });

        fabItem.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, ItemAddEditActivity.class);
            i.putExtra(COLLECTION_ID, Collection.DEFAULT_COLLECTION_ID);
            startActivityForResult(i, ADD_ITEM_REQUEST);  // add new item
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);


        initRecyclerView();
        initViewModel();
        initItemSwipes();

    }

    //control over Fab Menu
    private void showFabMenu() {
        isFabMenuOpened = true;
        fabLayoutItem.setVisibility(View.VISIBLE);
        fabLayoutCollection.setVisibility(View.VISIBLE);
        fab.animate().rotationBy(45).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (fab.getRotation() != 45) {
                    fab.setRotation(45);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        fabLayoutCollection.animate().translationY(-getResources().getDimension(R.dimen.fab_collection));
        fabLayoutItem.animate().translationY(-getResources().getDimension(R.dimen.fab_item));
    }

    private void closeFabMenu() {
        isFabMenuOpened = false;
        fab.animate().rotationBy(90).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (fab.getRotation() != 90) {
                    fab.setRotation(90);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

        fabLayoutItem.setVisibility(View.GONE);
        fabLayoutCollection.setVisibility(View.GONE);
        fabLayoutCollection.animate().translationY(0);
        fabLayoutItem.animate().translationY(0);
    }


    public void initRecyclerView() {
        adapter = new CollectionsListAdapter(getApplicationContext());
        adapter.setOnCollectionClickListener((collectionId, position) -> {
            Intent i = new Intent(MainActivity.this, CollectionActivity.class);
            i.putExtra(COLLECTION_ID, collectionId);
            MainActivity.this.startActivity(i);
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    private void initViewModel() {
        editorCollectionListViewModel = ViewModelProviders.of(this).get(EditorCollectionViewModel.class);
        itemListViewModel = ViewModelProviders.of(this).get(ItemListViewModel.class);
        editorCollectionListViewModel.getAllCollections().observe(this, collections -> adapter.submitList(collections));
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback;

    //actions with swipes
    public void initItemSwipes() {
         itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }




    //callback when recycler view is swiped
    //item will be removed on swiped
    //undo option will be provided in snackbar to restore the item
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CollectionsListAdapter.CollectionViewHolder) {
            // get the removed item name to display it in snack bar
            String title = adapter.getCollectionAt(viewHolder.getAdapterPosition()).getTitle();

            // backup of removed item for undo purpose
            final Collection swipedCollection = adapter.getCollectionAt(viewHolder.getAdapterPosition());

            //for deleting
            if(direction == ItemTouchHelper.RIGHT) {
                if (adapter.getCollectionAt(viewHolder.getAdapterPosition()).getId() != Collection.DEFAULT_COLLECTION_ID) {
                    // remove the item from recycler view
                    editorCollectionListViewModel.delete(adapter.getCollectionAt(viewHolder.getAdapterPosition()));
                }
                closeFabMenu();
                // showing snack bar with Undo option
                Snackbar snackbar = Snackbar
                        .make(recyclerView, title + " removed!", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", v ->
                        // undo is selected, restore the deleted item
                        editorCollectionListViewModel.insert(swipedCollection)
                );
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            } else if(direction == ItemTouchHelper.LEFT) {
                closeFabMenu();
                Intent i = new Intent(MainActivity.this, CollectionAddEditActivity.class);
                i.putExtra(COLLECTION_ID, swipedCollection.getId());// needed for setting correct title in activity Edit
                adapter.notifyItemChanged(position); // to reset viewholder after swiping (otherwise if we close editing window we'll see an option viewholder
                startActivityForResult(i, EDIT_COLLECTION_REQUEST);

            }
        }
    }


    @Override
    protected void onPause() {
        closeFabMenu();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (isFabMenuOpened) {
            closeFabMenu();
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.collection_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.collections_delete_all) {     //delete all collections
            editorCollectionListViewModel.deleteAll();
            Toast.makeText(MainActivity.this, "All collections deleted", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //Fragment fragment = null;

        if (id == R.id.nav_search) {

        } else if (id == R.id.nav_profile) {
            Intent i = new Intent(MainActivity.this, ViewProfile.class);
            startActivity(i);

        } else if (id == R.id.nav_favorites) {
            Intent i = new Intent(MainActivity.this, CollectionActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_signout) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_help) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkReceiver);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_COLLECTION_REQUEST && resultCode == RESULT_OK) {     //  Create new collection
//            String title = data.getStringExtra(CollectionAddEditActivity.EXTRA_TITLE);
//            String description = data.getStringExtra(CollectionAddEditActivity.EXTRA_DESCRIPTION);
//            String category = data.getStringExtra(CollectionAddEditActivity.EXTRA_CATEGORY);
//
//            Collection collection = new Collection(title, category, description, 1, "https://cdn.shopify.com/s/files/1/0414/6957/products/2018_2_Unc_Coin_OBV1_a63e6dae-0c68-4455-889f-5992224da64a_2048x.jpg?v=1532311472");
//            editorCollectionListViewModel.insert(collection);
            Toast.makeText(MainActivity.this, "Collection saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_COLLECTION_REQUEST && resultCode == RESULT_OK) {      //Edit exist collection
//            int id = data.getIntExtra(CollectionAddEditActivity.EXTRA_ID, -1);

//            if (id == -1) {
//                Toast.makeText(MainActivity.this, "Collection can't be updated", Toast.LENGTH_SHORT).show();
//                return;
//            }

//            String title = data.getStringExtra(CollectionAddEditActivity.EXTRA_TITLE);
//            String description = data.getStringExtra(CollectionAddEditActivity.EXTRA_DESCRIPTION);
//            String category = data.getStringExtra(CollectionAddEditActivity.EXTRA_CATEGORY);

//            Collection updateCollection = new Collection(title, category, description, "https://cdn.shopify.com/s/files/1/0414/6957/products/2018_2_Unc_Coin_OBV1_a63e6dae-0c68-4455-889f-5992224da64a_2048x.jpg?v=1532311472", Collection.DEFAULT_USER_ID);
//            updateCollection.setId(id);
//            editorCollectionListViewModel.update(updateCollection);
            Toast.makeText(MainActivity.this, "Collection updated", Toast.LENGTH_SHORT).show();
            } else if (requestCode == ADD_ITEM_REQUEST && resultCode == RESULT_OK) { //create new items
//            String title = data.getStringExtra(ItemAddEditActivity.EXTRA_TITLE);
//            String description = data.getStringExtra(ItemAddEditActivity.EXTRA_DESCRIPTION);
//            String price = data.getStringExtra(ItemAddEditActivity.EXTRA_PRICE);
//            Boolean isOnSale = data.getBooleanExtra(ItemAddEditActivity.EXTRA_ONSALE, false);
//
//            Item newItem = new Item(title, description, isOnSale, Float.parseFloat(price), 1);
//            itemListViewModel.insert(newItem);
            Toast.makeText(MainActivity.this, "Item saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
        }

    }

}
