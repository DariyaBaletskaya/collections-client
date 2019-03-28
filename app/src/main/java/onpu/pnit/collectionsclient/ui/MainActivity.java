package onpu.pnit.collectionsclient.ui;

import android.animation.Animator;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
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
import onpu.pnit.collectionsclient.entities.Item;
import onpu.pnit.collectionsclient.viewmodel.CollectionListViewModel;
import onpu.pnit.collectionsclient.viewmodel.EditorCollectionViewModel;
import onpu.pnit.collectionsclient.viewmodel.ItemListViewModel;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
    public static final String COLLECTION_ID = "collection_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);


        fab.setOnClickListener(v -> {
                if(!isFabMenuOpened){
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
            startActivityForResult(i, ADD_ITEM_REQUEST);  // add new collection
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction ft = fragmentManager.beginTransaction();
//        Fragment fragment = new CollectionFragment();
//        ft.replace(R.id.screen_area, fragment);
//        ft.commit();


        initRecyclerView();
        initViewModel();


        // reaction on swipe in right. Collection deleted
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {
            private boolean swipeRight = false;
            private void setTouchListener (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
                recyclerView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(adapter.getCollectionAt(viewHolder.getAdapterPosition()).getId() == Collection.DEFAULT_COLLECTION_ID) {
                            swipeRight = true;
                        }
                        return false;
                    }
                });
            }

            @Override
            public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                    setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
                super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public int convertToAbsoluteDirection(int flags, int layoutDirection) {
                if(swipeRight) {
                    swipeRight = false;
                    return 0;
                }
                return super.convertToAbsoluteDirection(flags, layoutDirection);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if(adapter.getCollectionAt(viewHolder.getAdapterPosition()).getId() != Collection.DEFAULT_COLLECTION_ID) {
                    editorCollectionListViewModel.delete(adapter.getCollectionAt(viewHolder.getAdapterPosition()));
                    Toast.makeText(MainActivity.this, "Collection deleted", Toast.LENGTH_SHORT).show();
                }
            }
        }).attachToRecyclerView(recyclerView);

        // reaction on swipe in left. Collection edit
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Collection editCollection = adapter.getCollectionAt(viewHolder.getAdapterPosition());
                Intent intent = new Intent(MainActivity.this, CollectionAddEditActivity.class);
                intent.putExtra(CollectionAddEditActivity.EXTRA_ID, editCollection.getId());
                intent.putExtra(CollectionAddEditActivity.EXTRA_TITLE, editCollection.getTitle());
                intent.putExtra(CollectionAddEditActivity.EXTRA_DESCRIPTION, editCollection.getDescription());
                intent.putExtra(CollectionAddEditActivity.EXTRA_CATEGORY, editCollection.getCategory());
                startActivityForResult(intent, EDIT_COLLECTION_REQUEST);
            }
        }).attachToRecyclerView(recyclerView);

    }

    //control over Fab Menu
    private void showFabMenu(){
        isFabMenuOpened = true;
        fabLayoutItem.setVisibility(View.VISIBLE);
        fabLayoutCollection.setVisibility(View.VISIBLE);
        fab.animate().rotationBy(45).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}

            @Override
            public void onAnimationEnd(Animator animator) {
                if (fab.getRotation() != 45) {
                    fab.setRotation(45);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {}

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
        fabLayoutCollection.animate().translationY(-getResources().getDimension(R.dimen.fab_collection));
        fabLayoutItem.animate().translationY(-getResources().getDimension(R.dimen.fab_item));
    }

    private void closeFabMenu(){
        isFabMenuOpened = false;
        fab.animate().rotationBy(90).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}

            @Override
            public void onAnimationEnd(Animator animator) {
                if (fab.getRotation() != 90) {
                    fab.setRotation(90);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {}

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });

        fabLayoutItem.setVisibility(View.GONE);
        fabLayoutCollection.setVisibility(View.GONE);
        fabLayoutCollection.animate().translationY(0);
        fabLayoutItem.animate().translationY(0);
    }


    public void initRecyclerView() {
        adapter = new CollectionsListAdapter();
        adapter.setOnCollectionClickListener((collectionId, position) -> {
            Intent i = new Intent(MainActivity.this, ItemsListActivity.class);
            i.putExtra(COLLECTION_ID, collectionId);
            MainActivity.this.startActivity(i);
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if(isFabMenuOpened){
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
        getMenuInflater().inflate(R.menu.main, menu);
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

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_signout) {

        } else if (id == R.id.nav_help) {

        }

//        if(fragment != null){
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction ft = fragmentManager.beginTransaction();
//
//            ft.replace(R.id.screen_area, fragment);
//
//            ft.commit();
//        }

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
            String title = data.getStringExtra(CollectionAddEditActivity.EXTRA_TITLE);
            String description = data.getStringExtra(CollectionAddEditActivity.EXTRA_DESCRIPTION);
            String category = data.getStringExtra(CollectionAddEditActivity.EXTRA_CATEGORY);

            Collection collection = new Collection(title, category, description, 1);
            editorCollectionListViewModel.insert(collection);
            Toast.makeText(MainActivity.this, "Collection saved", Toast.LENGTH_SHORT).show();
        } if (requestCode == EDIT_COLLECTION_REQUEST && resultCode == RESULT_OK) {      //Edit exist collection
            int id = data.getIntExtra(CollectionAddEditActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(MainActivity.this, "Collection can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(CollectionAddEditActivity.EXTRA_TITLE);
            String description = data.getStringExtra(CollectionAddEditActivity.EXTRA_DESCRIPTION);
            String category = data.getStringExtra(CollectionAddEditActivity.EXTRA_CATEGORY);

            Collection updateCollection = new Collection(title, category, description, 1);
            updateCollection.setId(id);
            editorCollectionListViewModel.update(updateCollection);
            Toast.makeText(MainActivity.this, "Collection update", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(MainActivity.this, "Collection not saved", Toast.LENGTH_SHORT).show();
        }

        if(requestCode == ADD_ITEM_REQUEST && resultCode == RESULT_OK) { //create new items
            String title = data.getStringExtra(ItemAddEditActivity.EXTRA_TITLE);
            String description = data.getStringExtra(ItemAddEditActivity.EXTRA_DESCRIPTION);
            String price = data.getStringExtra(ItemAddEditActivity.EXTRA_PRICE);
            Boolean isOnSale = data.getBooleanExtra(ItemAddEditActivity.EXTRA_ONSALE,false);

            Item newItem = new Item(title,description,isOnSale,Float.parseFloat(price),1);
            itemListViewModel.insert(newItem);
            Toast.makeText(MainActivity.this, "Item saved", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(MainActivity.this, "Item not saved", Toast.LENGTH_SHORT).show();
        }

    }

    private void initViewModel() {
        editorCollectionListViewModel = ViewModelProviders.of(this).get(EditorCollectionViewModel.class);
        itemListViewModel = ViewModelProviders.of(this).get(ItemListViewModel.class);
        editorCollectionListViewModel.getAllCollections().observe(this, new Observer<List<Collection>>() {
            @Override
            public void onChanged(List<Collection> collections) {
                adapter.submitList(collections);
            }
        });
    }
}
