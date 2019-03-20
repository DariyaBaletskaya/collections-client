package onpu.pnit.collectionsclient.ui;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import onpu.pnit.collectionsclient.R;
import onpu.pnit.collectionsclient.adapters.CollectionAdapter;
import onpu.pnit.collectionsclient.entities.Collection;
import onpu.pnit.collectionsclient.models.CollectionRestClient;


public class CollectionFragment extends Fragment {


    private CollectionAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.collection_list, null);
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.list_collections);
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


