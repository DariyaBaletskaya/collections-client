package onpu.pnit.collectionsclient.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import onpu.pnit.collectionsclient.viewmodel.CollectionListViewModel;
import onpu.pnit.collectionsclient.viewmodel.EditorCollectionViewModel;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NetworkReceiver networkReceiver = new NetworkReceiver();

    // using for adding new collection
    public static final int ADD_COLLECTION_REQUEST = 1;
    // using for edit exist collection
    public static final int EDIT_COLLECTION_REQUEST = 2;

    @BindView(R.id.list_collections)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private CollectionsListAdapter adapter;
    private CollectionListViewModel collectionListViewModel;
    private EditorCollectionViewModel editorCollectionListViewModel;
    public static final String COLLECTION_ID = "adsada";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, CollectionAddEditActivity.class);
                startActivityForResult(i, ADD_COLLECTION_REQUEST);  // add new collection
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                editorCollectionListViewModel.delete(adapter.getCollectionAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Collection deleted", Toast.LENGTH_SHORT).show();
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

    public void initRecyclerView() {
        adapter = new CollectionsListAdapter();
        adapter.setOnCollectionClickListener((collectionId, position) -> {
            Intent i = new Intent(MainActivity.this, MyItemDetailsActivity.class);
            i.putExtra(COLLECTION_ID, collectionId);
            MainActivity.this.startActivity(i);
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
                Toast.makeText(MainActivity.this, "Collection can't be update", Toast.LENGTH_SHORT).show();
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



    }

    private void initViewModel() {
        editorCollectionListViewModel = ViewModelProviders.of(this).get(EditorCollectionViewModel.class);
        editorCollectionListViewModel.getAllCollections().observe(this, new Observer<List<Collection>>() {
            @Override
            public void onChanged(List<Collection> collections) {
                adapter.submitList(collections);
            }
        });
    }
}
