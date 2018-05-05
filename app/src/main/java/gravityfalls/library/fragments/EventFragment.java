package gravityfalls.library.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import gravityfalls.library.objects.Book;
import gravityfalls.library.utils.Helper;
import gravityfalls.library.utils.SnackbarHelper;

/**
 * A placeholder fragment containing a simple view.
 */
public class EventFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_CATEGORY = "category";

    View rootView;
    Unbinder unbinder;


    @BindView(R.id.progress_overlay)
    FrameLayout progressOverlay;
    @BindView(R.id.main_layout)
    LinearLayout mainLayout;

    private DatabaseReference mDatabase;
    private ArrayList<Book> arrayList = new ArrayList<>();
    private String TAG = "EventFragment";
    private FirebaseUser user;


    public EventFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * category.
     */
    public static EventFragment newInstance(int category) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        rootView = inflater.inflate(R.layout.fragment_main_activity_with_tabs, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this,rootView);

        showLoad(false);

        //initialize FireBase and retrieve data
        initFireBase();

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
                        switch (getArguments().getInt(ARG_CATEGORY)){
                            case 0:
                                if (book.isAvailable())
                                    arrayList.add(book);
                                break;
                            case 1:
                                arrayList.add(book);
                                break;
                            case 2:
                                if (user != null && book.getOnUser().equals(user.getUid()))
                                    arrayList.add(book);
                                break;
                            case 3:
                                if (book.getCategory().equals("fantasy"))
                                    arrayList.add(book);
                                break;
                            case 4:
                                if (book.getCategory().equals("detective"))
                                    arrayList.add(book);
                                break;
                            case 5:
                                if (book.getCategory().equals("fairy"))
                                    arrayList.add(book);
                                break;
                            case 6:
                                if (book.getCategory().equals("history"))
                                    arrayList.add(book);
                                break;
                            case 7:
                                if (book.getCategory().equals("love"))
                                    arrayList.add(book);
                                break;
                            default:
                                break;
                        }
                        //Log.e(TAG, "Array list size: " + arrayList.size());
                    }
                }
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
        };mDatabase.child("books").addValueEventListener(booksListener);
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
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}