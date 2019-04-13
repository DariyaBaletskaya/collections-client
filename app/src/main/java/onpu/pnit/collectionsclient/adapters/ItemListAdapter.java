package onpu.pnit.collectionsclient.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import onpu.pnit.collectionsclient.R;
import onpu.pnit.collectionsclient.entities.Item;
import onpu.pnit.collectionsclient.ui.CollectionActivity;
import onpu.pnit.collectionsclient.ui.ItemAddEditActivity;
import onpu.pnit.collectionsclient.ui.MainActivity;

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
                .into(holder.img);


    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView title;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.card_item_photo);
            title = itemView.findViewById(R.id.card_item_title);

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
