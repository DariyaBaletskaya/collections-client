package onpu.pnit.collectionsclient.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.OnItemClick;
import onpu.pnit.collectionsclient.R;
import onpu.pnit.collectionsclient.entities.Collection;

public class CollectionsListAdapter extends ListAdapter<Collection, CollectionsListAdapter.CollectionViewHolder> {
    private List<Collection> collectionList = new ArrayList<>();

    private static final DiffUtil.ItemCallback<Collection> DIFF_CALLBACK = new DiffUtil.ItemCallback<Collection>() {
        @Override
        public boolean areItemsTheSame(@NonNull Collection oldItem, @NonNull Collection newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Collection oldItem, @NonNull Collection newItem) {
            return oldItem.toString().contentEquals(newItem.toString());
        }
    };

    public CollectionsListAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collection_card, parent, false);
        return new CollectionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionViewHolder holder, int position) {
        Collection currentCollection = getItem(position);
        holder.title.setText(currentCollection.getTitle());
        holder.category.setText(currentCollection.getCategory());
    }

    public void setCollectionList(List<Collection> collections) {
        this.collectionList = collections;
        notifyDataSetChanged();
    }

    public class CollectionViewHolder extends RecyclerView.ViewHolder {
        //TODO: Добавить отображение фото, возможно по URL
        ImageView photo;
        TextView title;
        TextView category;

        public CollectionViewHolder(View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.card_collection_title);
            category = itemView.findViewById(R.id.card_collection_category);
            photo = itemView.findViewById(R.id.card_collection_photo);
        }


    }
}