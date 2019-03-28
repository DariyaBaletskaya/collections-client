package onpu.pnit.collectionsclient.ui;

import android.os.AsyncTask;
import android.os.Bundle;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.OnItemClick;
import onpu.pnit.collectionsclient.CollectionRestClient;
import onpu.pnit.collectionsclient.R;
import onpu.pnit.collectionsclient.adapters.CollectionsListAdapter;
import onpu.pnit.collectionsclient.entities.Collection;

public class CollectionListActivity extends AppCompatActivity {


    @BindView(R.id.list_collections)
    RecyclerView recyclerView;
    private CollectionsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_list);
        setTitle("My collections");

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        adapter = new CollectionsListAdapter();
        recyclerView.setAdapter(adapter);

        new HttpRequestAsk().execute();


    }

    @OnItemClick(R.id.list_collections)
    public void onCollectionClick(int position) {
//        Intent i = new Intent(CollectionListActivity.this, CollectionFrag)
    }

    private class HttpRequestAsk extends AsyncTask<Void, Void, List<Collection>> {

        @Override
        protected List<Collection> doInBackground(Void... params){
            CollectionRestClient collectionRestClient = new CollectionRestClient();
            return collectionRestClient.findAll();
        }

        @Override
        protected void onPostExecute(List<Collection> collections) {
            adapter.submitList(collections);
        }

    }



}
