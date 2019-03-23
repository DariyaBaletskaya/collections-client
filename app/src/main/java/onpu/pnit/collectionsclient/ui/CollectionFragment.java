package onpu.pnit.collectionsclient.ui;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import onpu.pnit.collectionsclient.R;
import onpu.pnit.collectionsclient.adapters.CollectionsListAdapter;
import onpu.pnit.collectionsclient.entities.Collection;
import onpu.pnit.collectionsclient.CollectionRestClient;
import onpu.pnit.collectionsclient.entities.User;
import onpu.pnit.collectionsclient.viewmodel.CollectionListViewModel;
import onpu.pnit.collectionsclient.viewmodel.EditorCollectionViewModel;
import onpu.pnit.collectionsclient.viewmodel.UserViewModel;

import static android.app.Activity.RESULT_OK;


public class CollectionFragment extends Fragment {

    public static final int ADD_COLLECTION_REQUEST = 1;
    private EditorCollectionViewModel collectionListViewModel;
    private UserViewModel userViewModel;
    private CollectionsListAdapter adapter;


    public FloatingActionButton buttonAddCollectionOrItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.collection_list, container, false);
        buttonAddCollectionOrItem = (FloatingActionButton) view.findViewById(R.id.fab);
        buttonAddCollectionOrItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CollectionAddEditActivity.class);
                startActivityForResult(intent, ADD_COLLECTION_REQUEST);
            }
        });

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.list_collections);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        adapter = new CollectionsListAdapter();
        recyclerView.setAdapter(adapter);





        initViewModel();

//        new HttpRequestAsk().execute();


    }


    private class HttpRequestAsk extends AsyncTask<Void, Void, List<Collection>> {

        @Override
        protected List<Collection> doInBackground(Void... params) {
            CollectionRestClient collectionRestClient = new CollectionRestClient();
            return collectionRestClient.findAll();
        }

        @Override
        protected void onPostExecute(List<Collection> collections) {
            adapter.submitList(collections);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_COLLECTION_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(CollectionAddEditActivity.EXTRA_TITLE);
            String description = data.getStringExtra(CollectionAddEditActivity.EXTRA_DESCRIPTION);
            String category = data.getStringExtra(CollectionAddEditActivity.EXTRA_CATEGORY);

            Collection collection = new Collection(title, category, description, 1);
            collectionListViewModel.insert(collection);
            Toast.makeText(getActivity(), "Collection saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Collection not saved", Toast.LENGTH_SHORT).show();
        }

    }

    private void initViewModel() {
        collectionListViewModel = ViewModelProviders.of(this).get(EditorCollectionViewModel.class);
        collectionListViewModel.getAllCollections().observe(this, new Observer<List<Collection>>() {
            @Override
            public void onChanged(List<Collection> collections) {
                Toast.makeText(getActivity(), "Work collectionListViewModel", Toast.LENGTH_SHORT).show();
                adapter.setCollectionList(collections);
            }
        });
    }




}


