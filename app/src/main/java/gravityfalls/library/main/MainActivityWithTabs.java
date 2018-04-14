package gravityfalls.library.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import gravityfalls.library.R;
import gravityfalls.library.adapters.SectionsPagerAdapter;
import gravityfalls.library.login.LoginActivity;

public class MainActivityWithTabs extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private AccountHeaderBuilder headerBuilder;
    DrawerBuilder mDrawerBuilder;
    Toolbar mToolbar;

    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_with_tabs);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initDrawer();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity_with_tabs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initDrawer() {

        mDrawerBuilder = new DrawerBuilder().withActivity(MainActivityWithTabs.this).withToolbar(mToolbar)
                .withTranslucentStatusBar(false);
        headerBuilder = new AccountHeaderBuilder()
                .withActivity(MainActivityWithTabs.this)
                .withHeaderBackground(R.drawable.book)
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(i);
                        return false;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .withSelectionListEnabledForSingleProfile(false);

        if (user != null) {
           /* Log.e(TAG, "user is not null");
            Log.e(TAG, "userID: " + user.getUid());*/
            if (user.getPhotoUrl() != null) {
                DrawerWithProfilePhoto();
            } else {
                DrawerWithoutProfilePhoto();
                makeDrawer();
                //closeLoad();
            }
        }
    }

    private void makeDrawer() {
        mDrawerBuilder.withAccountHeader(headerBuilder.build()).addDrawerItems(new ProfileDrawerItem().withName(R.string.library)
                        .withTypeface(Typeface.defaultFromStyle(Typeface.BOLD)).withIcon(R.drawable.library), new DividerDrawerItem(),
                new PrimaryDrawerItem().withName(R.string.exit)
                        .withTypeface(Typeface.defaultFromStyle(Typeface.BOLD)).withIcon(R.drawable.exit)).
                withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position) {
                            case 3:
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityWithTabs.this);
                                builder.setMessage(getString(R.string.wanna_exit));
                                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User clicked OK button
                                        if (mAuth != null) {
                                            mAuth.signOut();
                                            Intent i = new Intent(MainActivityWithTabs.this, LoginActivity.class);
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
    }

    private void DrawerWithProfilePhoto() {
        if (headerBuilder != null) {
            Glide.with(this).asBitmap().load(user.getPhotoUrl()).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    headerBuilder.addProfiles(
                            new ProfileDrawerItem().withName(user.getDisplayName()).withEmail(user.getEmail()).withIcon(resource)
                    );
                    makeDrawer();
                    //closeLoad();
                }
            });
        }
    }

    private void DrawerWithoutProfilePhoto() {
        if (headerBuilder != null) {
            headerBuilder.addProfiles(
                    new ProfileDrawerItem().withName(user.getDisplayName()).withEmail(user.getEmail()).withIcon(getResources().getDrawable(R.drawable.boy))
            );
        }
    }
}
