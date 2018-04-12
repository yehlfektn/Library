package gravityfalls.library.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import gravityfalls.library.R;
import gravityfalls.library.objects.Book;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private ArrayList<Book> mItems;
    private Context mContext;

    public MainAdapter(ArrayList<Book> myDataset, Context context) {
        mItems = myDataset;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Book item = mItems.get(position);
        holder.mText.setText(item.getTitle());
        Glide.with(mContext).load(item.getImageLink()).into(holder.mView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mText;
        private ImageView mView;

        private ViewHolder(View view) {
            super(view);
            mText =  view.findViewById(R.id.text_1);
            mView =  view.findViewById(R.id.image_1);
        }
    }
}
