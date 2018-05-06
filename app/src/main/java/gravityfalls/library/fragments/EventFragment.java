package gravityfalls.library.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
import gravityfalls.library.objects.Book;
import gravityfalls.library.utils.Helper;

/**
 * A placeholder fragment containing a simple view.
 */
public class EventFragment extends Fragment implements OnMapReadyCallback{
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
    @BindView(R.id.image_1)
    ImageView imageTitle;

    private DatabaseReference mDatabase;
    private ArrayList<Book> arrayList = new ArrayList<>();
    private String TAG = "EventFragment";
    private FirebaseUser user;

    private OnImageClickListener mListener;

    MapView mMapView;
    private GoogleMap googleMap;


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
        unbinder = ButterKnife.bind(this, rootView);

        mMapView =  rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);

        showLoad(false);

        //initialize FireBase and retrieve data
        initFireBase();

        loadImages();

    }


    private void loadImages() {
        Glide.with(this).load(R.drawable.event_background).into(imageTitle);
    }

    private void initFireBase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();

     /*   ValueEventListener booksListener = new ValueEventListener() {
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
        };mDatabase.child("books").addValueEventListener(booksListener);*/
    }

    private void showLoad(boolean b) {
        if (progressOverlay != null) {
            Helper.animateView(progressOverlay, b ? View.VISIBLE : View.GONE, 0.4f, 0);
            mainLayout.setVisibility(b ? View.GONE : View.VISIBLE);
        }
    }

    @OnClick(R.id.chats_layout)
    void onChatsClick(){
        if (mListener!=null){
            mListener.onImageClick(1);
        }
    }

    @OnClick(R.id.organization_layout)
    void onOrganizationClick(){
        if (mListener!=null){
            mListener.onImageClick(2);
        }
    }

   /* @OnClick(R.id.guests2)
    void onMapClicked(){
        startActivity(new Intent(getActivity(),MapsActivity.class));
    }
*/
    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // Add a marker in Sydney and move the camera
        LatLng astana = new LatLng(51.0939563, 71.3987141);
        googleMap.addMarker(new MarkerOptions().position(astana).title("Технопарк"));
        //googleMap.animateCamera(CameraUpdateFactory.newLatLng(astana));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(astana,15));
        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }

    public interface OnImageClickListener {
        void onImageClick(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnImageClickListener) {
            mListener = (OnImageClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @OnClick(R.id.map_button)
    void onMapButtonClicked(){
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr=51.0939563,71.3987141"));
        startActivity(intent);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}