package gravityfalls.library.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import gravityfalls.library.R;
import gravityfalls.library.adapters.MainAdapter;
import gravityfalls.library.objects.Book;
import gravityfalls.library.utils.Helper;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by 777 on 12.04.2018.
 */

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.main_layout)
    LinearLayout mainLayout;
    @BindView(R.id.progress_overlay)
    FrameLayout progressOverlay;
    private Unbinder unbinder;
    private Drawer mDrawer;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private MainAdapter adapter;
    private String TAG = "MainActivity";
    private ArrayList<Book> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set layout of activity
        setContentView(R.layout.activity_main);

        //BindViews
        unbinder = ButterKnife.bind(this);

        //show Loading View
        showLoad();

        //initialize toolbar and bind views with Butterknife
        initToolbar();

        //initialize FireBase and retrieve data
        initFireBase();

        //set up RecyclerView
        setUpRecyclerView();

        //initialize Drawer and populate with user data
        initDrawer();
    }

    private void initFireBase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        ValueEventListener booksListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Book book = child.getValue(Book.class);
                    if (book != null) {
                        arrayList.add(book);
                        //Log.e(TAG, "Array list size: " + arrayList.size());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error: " + databaseError.getMessage());
            }
        };
        mDatabase.child("books").addValueEventListener(booksListener);
    }

    private void initDrawer() {
        if (user != null) {
            Log.e(TAG,"user is not null");
            Log.e(TAG,"userID: "+user.getUid());
            if (user.getPhotoUrl() != null){
                DrawerWithProfilePhoto();
            }else {
                DrawerWithoutProfilePhoto();
                closeLoad();
            }
        }
    }

    private void DrawerWithProfilePhoto() {
        Glide.with(this).asBitmap().load(user.getPhotoUrl()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                DrawerBuilder mDrawerBuilder = new DrawerBuilder().withActivity(MainActivity.this).withToolbar(mToolbar)
                        .withTranslucentStatusBar(false);
                AccountHeader headerResult = new AccountHeaderBuilder()
                        .withActivity(MainActivity.this)
                        .withHeaderBackground(R.drawable.book)
                        .addProfiles(
                                new ProfileDrawerItem().withName(user.getDisplayName()).withEmail(user.getEmail()).withIcon(resource)
                        )
                        .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                            @Override
                            public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                                return false;
                            }
                        })
                        .withSelectionListEnabledForSingleProfile(false)
                        .build();

                mDrawerBuilder.withAccountHeader(headerResult).addDrawerItems(new ProfileDrawerItem().withName(R.string.library)
                        .withTypeface(Typeface.defaultFromStyle(Typeface.BOLD)).withIcon(R.drawable.library))
                        .addDrawerItems(new PrimaryDrawerItem().withName(R.string.exit)
                                .withTypeface(Typeface.defaultFromStyle(Typeface.BOLD)).withIcon(R.drawable.exit)).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        return false;
                    }
                });
                mDrawer = mDrawerBuilder.build();
                closeLoad();
            }
        });
    }

    private void DrawerWithoutProfilePhoto(){
        DrawerBuilder mDrawerBuilder = new DrawerBuilder().withActivity(this).withToolbar(mToolbar)
                .withTranslucentStatusBar(false);
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.book)
                .addProfiles(
                        new ProfileDrawerItem().withName(user.getDisplayName()).withEmail(user.getEmail()).withIcon(getResources().getDrawable(R.drawable.boy))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .withSelectionListEnabledForSingleProfile(false)
                .build();

        mDrawerBuilder.withAccountHeader(headerResult).addDrawerItems(new ProfileDrawerItem().withName(R.string.library)
                .withTypeface(Typeface.defaultFromStyle(Typeface.BOLD)).withIcon(R.drawable.library))
                .addDrawerItems(new PrimaryDrawerItem().withName(R.string.exit)
                        .withTypeface(Typeface.defaultFromStyle(Typeface.BOLD)).withIcon(R.drawable.exit)).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                return false;
            }
        });
        mDrawer = mDrawerBuilder.build();
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpRecyclerView() {
        CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new MainAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new CenterScrollListener());
    }

    private void showLoad() {
        mainLayout.setVisibility(View.GONE);
        Helper.animateView(progressOverlay, View.VISIBLE, 0.4f, 200);
    }

    private void closeLoad() {
        Helper.animateView(progressOverlay, View.GONE, 0.4f, 0);
        mainLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
