package onpu.pnit.collectionsclient.activity;

import android.os.AsyncTask;
import android.os.Bundle;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import onpu.pnit.collectionsclient.R;
import onpu.pnit.collectionsclient.adapters.CollectionAdapter;
import onpu.pnit.collectionsclient.entities.Collection;
import onpu.pnit.collectionsclient.models.CollectionRestClient;

public class CollectionList extends AppCompatActivity {

    private CollectionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.collection_list);
        RecyclerView recyclerView = findViewById(R.id.list_collections);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        adapter = new CollectionAdapter();
        recyclerView.setAdapter(adapter);
        new HttpRequestAsk().execute();


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
