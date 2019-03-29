package onpu.pnit.collectionsclient.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import onpu.pnit.collectionsclient.R;
import onpu.pnit.collectionsclient.entities.Collection;

public class CollectionsListAdapter extends ListAdapter<Collection, CollectionsListAdapter.CollectionViewHolder> {

    private Context context;
    private OnCollectionClickListener listener;

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


    public CollectionsListAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
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
        holder.id = currentCollection.getId();
        holder.title.setText(currentCollection.getTitle());
        holder.category.setText(currentCollection.getCategory());

        Glide.with(context)
                .load(currentCollection.getImage())
                .into(holder.image);
    }

    public class CollectionViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView title;
        public TextView category;
        public ConstraintLayout viewBackground,
                         viewForeground;
        private int id;

        public CollectionViewHolder(View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.card_collection_title);
            category = itemView.findViewById(R.id.card_collection_category);
            image = itemView.findViewById(R.id.card_collection_photo);
            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onCollectionClick(getItem(getAdapterPosition()).getId(), getAdapterPosition());
                }
            });

            viewBackground = itemView.findViewById(R.id.collections_card_background);
            viewForeground = itemView.findViewById(R.id.collections_card_foreground);

        }

        public int getId() {
            return id;
        }
    }

    public interface OnCollectionClickListener {
        void onCollectionClick(int collectionId, int position);
    }

    public void setOnCollectionClickListener(OnCollectionClickListener listener) {
        this.listener = listener;
    }

    public Collection getCollectionAt(int position) {
        return getItem(position);
    }

}