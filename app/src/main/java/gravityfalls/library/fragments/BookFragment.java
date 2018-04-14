package gravityfalls.library.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import gravityfalls.library.R;
import gravityfalls.library.adapters.MainAdapter;
import gravityfalls.library.objects.Book;
import gravityfalls.library.utils.Helper;
import gravityfalls.library.utils.SnackbarHelper;

/**
 * A placeholder fragment containing a simple view.
 */
public class BookFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_CATEGORY = "category";

    View rootView;
    Unbinder unbinder;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.title_main)
    TextView title;
    @BindView(R.id.txt_description)
    TextView description;
    @BindView(R.id.txt_author)
    TextView author;
    @BindView(R.id.txt_year)
    TextView year;

    @BindView(R.id.progress_overlay)
    FrameLayout progressOverlay;
    @BindView(R.id.main_layout)
    LinearLayout mainLayout;

    private DatabaseReference mDatabase;
    private ArrayList<Book> arrayList = new ArrayList<>();
    private String TAG = "BookFragment";
    private MainAdapter adapter;

    private Handler handler = new Handler();
    private Runnable runnable;

    private String category;

    public BookFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * category.
     */
    public static BookFragment newInstance(int category) {
        BookFragment fragment = new BookFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main_activity_with_tabs, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this,rootView);

        showLoad(true);

        //initialize FireBase and retrieve data
        initFireBase();

        //set up RecyclerView
        setUpRecyclerView();

        //initialize Handler
        initHandler();
    }

    private void initFireBase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ValueEventListener booksListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Book book = child.getValue(Book.class);
                    if (book != null) {
                        arrayList.add(book);
                        //Log.e(TAG, "Array list size: " + arrayList.size());
                    }
                }
                adapter.notifyDataSetChanged();
                showLoad(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error: " + databaseError.getMessage());
                if (mainLayout!=null) {
                    SnackbarHelper.getSnackBar(mainLayout, getString(R.string.can_not_load_data));
                }
                showLoad(false);
            }
        };
        switch (getArguments().getInt(ARG_CATEGORY)){
            case 3:
                category = "fantasy";
                break;
            case 4:
                category = "detective";
                break;
            default:
                category = "books";
        }
        mDatabase.child(category).addValueEventListener(booksListener);
    }

     private void setUpRecyclerView() {
        CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new MainAdapter(arrayList, getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new CenterScrollListener());
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
             @Override
             public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                 int action = e.getAction();
                 switch (action) {
                     case MotionEvent.ACTION_MOVE:
                         rv.getParent().requestDisallowInterceptTouchEvent(true);
                         break;
                 }
                 return false;
             }

             @Override
             public void onTouchEvent(RecyclerView rv, MotionEvent e) {

             }

             @Override
             public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

             }
         });
    }

    private void initHandler() {
        runnable = new Runnable() {
            @Override
            public void run() {
                //Log.e(TAG,"Runnable started");
                if (arrayList.size()!=0) {
                    //Log.e(TAG, "size of ArrayList: " + arrayList.size());
                    if (title!=null) {
                        final int position = ((CarouselLayoutManager) recyclerView.getLayoutManager()).getCenterItemPosition();
                        //Log.e(TAG,"inside arrayList"+position);
                        if (position != -1) {
                            title.setText(arrayList.get(position).getTitle());
                            description.setText(arrayList.get(position).getShort_description());
                            author.setText(arrayList.get(position).getAuthor());
                            year.setText(arrayList.get(position).getYear());
                        }
                    }
                }
                if (handler!=null)
                    handler.postDelayed(this,1000);
            }
        };
        handler = new Handler();
        handler.postDelayed(runnable,1000);
    }

    private void showLoad(boolean b) {
        if (progressOverlay!=null) {
            Helper.animateView(progressOverlay, b ? View.VISIBLE : View.GONE, 0.4f, 0);
            mainLayout.setVisibility(b ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onPause() {
        super.onPause();
        handler = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        handler = new Handler();
        handler.postDelayed(runnable,1000);
    }
}