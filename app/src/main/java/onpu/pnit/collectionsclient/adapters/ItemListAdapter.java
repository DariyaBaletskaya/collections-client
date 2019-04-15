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
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import onpu.pnit.collectionsclient.R;
import onpu.pnit.collectionsclient.entities.Item;

public class ItemListAdapter extends ListAdapter<Item, ItemListAdapter.ItemViewHolder> {

    private OnItemClickListener listener;
    private Context context;

    private static final DiffUtil.ItemCallback<Item> DIFF_CALLBACK = new DiffUtil.ItemCallback<Item>() {
        @Override
        public boolean areItemsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.toString().equals(newItem.toString());
        }
    };


    public ItemListAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
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
        Item currentItem = getItem(position);
        holder.title.setText(currentItem.getTitle());

        Glide.with(context)
                .asBitmap()
                .load(currentItem.getImage())
                .error(R.drawable.ic_profile)
                .into(holder.image);

        /*Настраиваем картинку по размеру, иначе при карточка будет сначала маленькой (только текст),
        * а при подгрузке картинки она будет резко меняться, подстраиваясь под нее. А так карточка изначально будет по размеру
        * и если картинка будет долго подгружаться, вьюшка скакать не будет*/
        ConstraintSet set = new ConstraintSet();
        String ratio = String.format("%d:%d", currentItem.getWidth(), currentItem.getHeigth());
        set.clone(holder.constraintLayout);
        set.setDimensionRatio(holder.image.getId(), ratio);
        set.applyTo(holder.constraintLayout);

    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        ConstraintLayout constraintLayout;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.card_item_photo);
            title = itemView.findViewById(R.id.card_item_title);
            constraintLayout = itemView.findViewById(R.id.item_card_constrainlayout);
            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(getAdapterPosition()).getId(), getAdapterPosition());
                }
            });
        }
    }


    public interface OnItemClickListener {
        void onItemClick(int itemId, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public Item getItemAt(int position) {
        return getItem(position);
    }


}
