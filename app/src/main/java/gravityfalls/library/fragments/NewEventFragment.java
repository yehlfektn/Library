package gravityfalls.library.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import gravityfalls.library.R;
import gravityfalls.library.main.MapsActivity;
import gravityfalls.library.objects.Book;
import gravityfalls.library.utils.Helper;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewEventFragment extends Fragment{
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



    public NewEventFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * category.
     */
    public static NewEventFragment newInstance(int category) {
        NewEventFragment fragment = new NewEventFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        rootView = inflater.inflate(R.layout.fragment_new_event, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);

        showLoad(false);

        //initialize FireBase and retrieve data
        //initFireBase();

        //loadImages();
    }

    private void loadImages() {
        //Glide.with(this).load(R.drawable.event_background).into(imageTitle);
    }

    private void initFireBase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void showLoad(boolean b) {
        if (progressOverlay != null) {
            Helper.animateView(progressOverlay, b ? View.VISIBLE : View.GONE, 0.4f, 0);
            mainLayout.setVisibility(b ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.chooseLocation)
    void onChooseLocationClicked(){
        startActivity(new Intent(getActivity(), MapsActivity.class));
    }

}