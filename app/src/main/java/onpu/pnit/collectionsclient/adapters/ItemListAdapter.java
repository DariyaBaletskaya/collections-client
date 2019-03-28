package onpu.pnit.collectionsclient.adapters;

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
import onpu.pnit.collectionsclient.R;
import onpu.pnit.collectionsclient.entities.Item;

public class ItemListAdapter extends ListAdapter<Item,ItemListAdapter.ItemViewHolder> {

    private List<Item> itemList = new ArrayList<>();
    private OnItemClickListener listener;

    private static final DiffUtil.ItemCallback<Item> DIFF_UTIL_FOR_ITEMS = new DiffUtil.ItemCallback<Item>() {
        @Override
        public boolean areItemsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.toString().equals(newItem.toString());
        }
    };

    public ItemListAdapter (){
        super(DIFF_UTIL_FOR_ITEMS);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item currentCollection = getItem(position);
        holder.title.setText(currentCollection.getTitle());
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView title;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.card_item_title);
            img = itemView.findViewById(R.id.card_item_photo);

            itemView.setOnClickListener(v ->{
                if(listener!= null && getAdapterPosition() != RecyclerView.NO_POSITION){
                    listener.onItemClick(getItem(getAdapterPosition()).getId(),getAdapterPosition());
                }
            });
        }
    }


    public interface OnItemClickListener{
        void onItemClick(int ItemId,int position);
    }

    public void SetOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public Item GetItemFrom(int position){
        return getItem(position);
    }
}
