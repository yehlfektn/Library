package gravityfalls.library.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import gravityfalls.library.R;
import gravityfalls.library.objects.Book;

public class ProfileBooksAdapter extends BaseAdapter {

    private ViewHolder holder;
    private ArrayList<Book> mItems;
    private Context mContext;
    private LayoutInflater mInflater;

    public ProfileBooksAdapter(ArrayList<Book> items, Context context) {
        mItems = items;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Book item = mItems.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.profile_books_list_item, parent, false);
            holder.Photo =  convertView.findViewById(R.id.imageView);
            holder.author = convertView.findViewById(R.id.author);
            holder.title = convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(item.getTitle());
        holder.author.setText(item.getAuthor());
        
        Glide.with(mContext).load(item.getImageLink()).into(holder.Photo);
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
        TextView available;
        Button remove;
    }
}