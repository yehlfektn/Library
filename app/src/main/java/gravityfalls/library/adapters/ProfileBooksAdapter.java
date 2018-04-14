package gravityfalls.library.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import gravityfalls.library.R;
import gravityfalls.library.main.BookDetailsActivity;
import gravityfalls.library.objects.Book;
import mehdi.sakout.fancybuttons.FancyButton;

public class ProfileBooksAdapter extends BaseAdapter {
    private ViewHolder holder;
    private ArrayList<Book> mItems;
    private Context mContext;
    private LayoutInflater mInflater;
    private DatabaseReference mDatabase;
    private BooksListener mListener;

    public ProfileBooksAdapter(ArrayList<Book> items, Context context, DatabaseReference database, BooksListener listener) {
        mItems = items;
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatabase = database;
        mListener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Book item = mItems.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_profile_books_list, parent, false);
            holder.Photo =  convertView.findViewById(R.id.imageView);
            holder.author = convertView.findViewById(R.id.author);
            holder.title = convertView.findViewById(R.id.title);
            holder.remove = convertView.findViewById(R.id.remove_button);
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatabase.child(item.getCategory()).child(String.valueOf(item.getPosition())).child("available").setValue(true);
                    mDatabase.child(item.getCategory()).child(String.valueOf(item.getPosition())).child("onUser").setValue("none");
                    mListener.onBookReturned();
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(item.getTitle());
        holder.author.setText(item.getAuthor());
        Glide.with(mContext).load(item.getImageLink()).into(holder.Photo);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(BookDetailsActivity.getIntent(mContext,item,String.valueOf(position)));
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int pos) {
        return mItems.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    class ViewHolder {
        ImageView Photo;
        TextView title;
        TextView author;
        FancyButton remove;
    }

    public interface BooksListener {
        // you can define any parameter as per your requirement
        void onBookReturned();
    }
}