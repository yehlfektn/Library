package gravityfalls.library.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gigamole.navigationtabstrip.NavigationTabStrip;
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
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import gravityfalls.library.R;
import gravityfalls.library.adapters.SectionsPagerAdapter;
import gravityfalls.library.login.LoginActivity;
import gravityfalls.library.objects.User;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Nurdaulet Kenges on 12.04.2018.
 */

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    DrawerBuilder mDrawerBuilder;
    SectionsPagerAdapter mSectionsPagerAdapter;
    private Unbinder unbinder;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String TAG = "MainActivity";
    private AccountHeaderBuilder headerBuilder;
    private ViewPager mViewPager;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set layout of activity
        setContentView(R.layout.activity_main);

        //BindViews
        unbinder = ButterKnife.bind(this);

        //initialize View Pager
        initViewPager();

        //initialize toolbar and bind views with Butterknife
        initToolbar();

        //initialize fireBase
        initFireBase();

        //initialize Drawer and populate with user data
        initDrawer();
    }

    private void initFireBase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    private void initViewPager() {
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        NavigationTabStrip tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(tabLayout);
        tabLayout.setViewPager(mViewPager);
    }


    private void initDrawer() {

        mDrawerBuilder = new DrawerBuilder().withActivity(MainActivity.this).withToolbar(mToolbar)
                .withTranslucentStatusBar(false);
        headerBuilder = new AccountHeaderBuilder()
                .withActivity(MainActivity.this)
                .withHeaderBackground(R.drawable.book)
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivityForResult(i,69);
                        return false;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .withSelectionListEnabledForSingleProfile(false);

        if (user != null) {
            Log.e(TAG, "user is not null");
            Log.e(TAG, "userID: " + user.getUid());
            if (user.getPhotoUrl() != null) {
                DrawerWithProfilePhoto();
            } else {
                DrawerWithoutProfilePhoto();
                makeDrawer();
            }
        }
    }

    private void makeDrawer() {
        try {
            mDrawerBuilder.withAccountHeader(headerBuilder.build()).addDrawerItems(new ProfileDrawerItem().withName(R.string.library)
                            .withTypeface(Typeface.defaultFromStyle(Typeface.BOLD)).withIcon(R.drawable.library), new DividerDrawerItem(),
                    new PrimaryDrawerItem().withName(R.string.exit)
                            .withTypeface(Typeface.defaultFromStyle(Typeface.BOLD)).withIcon(R.drawable.exit)).
                    withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                            switch (position) {
                                case 3:
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setMessage(getString(R.string.wanna_exit));
                                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // User clicked OK button
                                            if (mAuth != null) {
                                                mAuth.signOut();
                                                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        }
                                    });
                                    builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // User cancelled the dialog
                                        }
                                    });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                    break;
                                default:
                                    break;

                            }
                            return false;
                        }
                    }).build();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void DrawerWithProfilePhoto() {
        if (headerBuilder != null) {
            Glide.with(this).asBitmap().load(user.getPhotoUrl()).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    retrieveUserName(resource);
                }
            });
        }
    }

    private void retrieveUserName(@Nullable final Bitmap resource) {
        if (resource != null) {
            ValueEventListener booksListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user_data = dataSnapshot.getValue(User.class);
                    String name = "";
                    if (user_data != null) {
                        if (user_data.getName() != null)
                            name += user_data.getName();
                        if (user_data.getFamily_name() != null)
                            name += " "+user_data.getFamily_name();
                    }
                    headerBuilder.addProfiles(
                            new ProfileDrawerItem().withName(name).withEmail(user.getEmail()).withIcon(resource)
                    );
                    makeDrawer();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "Error: " + databaseError.getMessage());
                }
            };
            mDatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(booksListener);
        } else {
            headerBuilder.addProfiles(
                    new ProfileDrawerItem().withName(user.getDisplayName()).withEmail(user.getEmail()).withIcon(getResources().getDrawable(R.drawable.boy))
            );
        }
    }

    private void DrawerWithoutProfilePhoto() {
        if (headerBuilder != null) {
           retrieveUserName(null);
        }
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 69){
            initDrawer();
        }
    }
}
