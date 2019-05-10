package onpu.pnit.collectionsclient.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import onpu.pnit.collectionsclient.R;
import onpu.pnit.collectionsclient.entities.Collection;
import onpu.pnit.collectionsclient.viewmodel.CollectionViewModel;
import onpu.pnit.collectionsclient.viewmodel.EditorCollectionViewModel;

public class CollectionsListAdapter extends ListAdapter<Collection, CollectionsListAdapter.CollectionViewHolder> {

    private EditorCollectionViewModel viewmodel;
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


    public CollectionsListAdapter(Context context, EditorCollectionViewModel viewmodel) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.viewmodel = viewmodel;
    }

    @NonNull
    @Override
    public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collection_card, parent, false);
        return new CollectionViewHolder(itemView);
    }

    @NonNull
    @Override
    public List<Collection> getCurrentList() {
        return super.getCurrentList();
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionViewHolder holder, int position) {
        Collection currentCollection = getItem(position);
        holder.title.setText(currentCollection.getTitle());
        holder.category.setText(currentCollection.getCategory());
        holder.id = currentCollection.getId();
        /*Doesn't work. Maybe we can't use viewmodel in adapter.
        * methods in dao and repo return the first item of collection correctly,
        * viewmodel method doensn't
        * Maybe we can use android data binding
        * NOW IMAGE OF COLLECTION IS HIDDEN
        * TODO: fix it*/
//        Glide.with(context)
//                .load(viewmodel.getFirstItemOfCollection(currentCollection.getId()).getImage())
//                .into(holder.image);
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
            viewBackground = itemView.findViewById(R.id.collections_card_background);
            viewForeground = itemView.findViewById(R.id.collections_card_foreground);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.onCollectionClick(getItem(getAdapterPosition()).getId(), getAdapterPosition());
                    }
                }
            });

        }
        public int getId(){
            return this.id;
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