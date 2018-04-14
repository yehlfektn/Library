package gravityfalls.library.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import gravityfalls.library.R;
import gravityfalls.library.objects.Book;
import gravityfalls.library.utils.Helper;
import gravityfalls.library.utils.SnackbarHelper;
import mehdi.sakout.fancybuttons.FancyButton;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Nurdaulet Kenges on 12.04.2018.
 */

public class BookDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "id";
    public static final String EXTRA_DATA = "data";

    @BindView(R.id.imageview)
    ImageView imageView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txt_author)
    TextView author;
    @BindView(R.id.txt_year)
    TextView year;
    @BindView(R.id.txt_description)
    TextView description;
    @BindView(R.id.txt_language)
    TextView language;
    @BindView(R.id.txt_pages)
    TextView pages;
    @BindView(R.id.txt_country)
    TextView country;
    @BindView(R.id.txt_status)
    TextView status;
    @BindView(R.id.get_book)
    FancyButton get_book;
    @BindView(R.id.return_book)
    FancyButton return_book;

    @BindView(R.id.progress_overlay)
    FrameLayout progressOverlay;
    @BindView(R.id.main_layout)
    LinearLayout mainLayout;

    private String category;
    private String id;
    private Unbinder unbinder;
    private DatabaseReference mDatabase;
    private FirebaseUser user;


    private String TAG = "BookDetailsActivity";


    public static Intent getIntent(Context context, Book book, String id) {
        Intent intent = new Intent(context, BookDetailsActivity.class);
        intent.putExtra(EXTRA_ID, id);
        intent.putExtra(EXTRA_DATA, book);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        unbinder = ButterKnife.bind(this);

        id = getIntent().getStringExtra(EXTRA_ID);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null) {
            Book book = getIntent().getExtras().getParcelable(EXTRA_DATA);
            category = book.getCategory();
            parseData(book);
            loadData();
        }
        showLoad(false);

    }

    private void parseData(Book book) {
        if (book != null) {
            setTitle(book.getTitle());
            try {
                Glide.with(this).load(book.getImageLink()).into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable glideDrawable, @Nullable Transition<? super Drawable> transition) {
                        double ratio = (double) glideDrawable.getIntrinsicWidth() / (double) glideDrawable.getIntrinsicHeight();
                        imageView.setImageDrawable(glideDrawable);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                (int) Helper.convertDpToPixel((int) (200.0 * ratio), getApplicationContext()),
                                (int) Helper.convertDpToPixel(200, getApplicationContext()));
                        layoutParams.setMargins(0, (int) Helper.convertDpToPixel(40, getApplicationContext()),
                                0, (int) Helper.convertDpToPixel(70, getApplicationContext()));
                        imageView.setLayoutParams(layoutParams);
                    }
                });


                //CalligraphyUtils.applyFontToTextView(this, title, "fonts/mono_bold.ttf");
                author.setText(getString(R.string.books_author, book.getAuthor()));
                year.setText(getString(R.string.books_year, book.getYear()));
                language.setText(getString(R.string.books_language, book.getLanguage()));
                pages.setText(getString(R.string.books_pages, book.getPages()));
                country.setText(getString(R.string.books_country, book.getCountry()));
                description.setText(book.getShort_description());
                status.setText(book.isAvailable() ? getString(R.string.books_status, "Доступен") : getString(R.string.books_status, "Не доступен"));
                get_book.setVisibility(book.isAvailable() ? View.VISIBLE : View.GONE);
                if (book.getOnUser().equals(user.getUid()))
                    return_book.setVisibility(View.VISIBLE);
                else return_book.setVisibility(View.GONE);
                category = book.getCategory();
                Log.e(TAG, "onUser: " + book.getOnUser());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.get_book)
    void onGetBookClicked() {
        Log.e(TAG, "OnGetBook was clicked!");
        Log.e(TAG, "category: " + category + ", id: " + id);
        mDatabase.child(category).child(id).child("available").setValue(false);
        mDatabase.child(category).child(id).child("onUser").setValue(user.getUid());
    }

    @OnClick(R.id.return_book)
    void onBookReturnClicked(){
        mDatabase.child(category).child(id).child("available").setValue(true);
        mDatabase.child(category).child(id).child("onUser").setValue("none");
    }

    private void loadData() {
        ValueEventListener booksListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Book book = dataSnapshot.getValue(Book.class);
                if (book != null) {
                    try {
                        status.setText(book.isAvailable() ? getString(R.string.books_status, "Доступен") : getString(R.string.books_status, "Не доступен"));
                        get_book.setVisibility(book.isAvailable() ? View.VISIBLE : View.GONE);
                        if (book.getOnUser().equals(user.getUid()))
                            return_book.setVisibility(View.VISIBLE);
                        else return_book.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                showLoad(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error: " + databaseError.getMessage());
                if (mainLayout != null) {
                    SnackbarHelper.getSnackBar(mainLayout, getString(R.string.can_not_load_data));
                }
                showLoad(false);
            }
        };
        mDatabase.child(category).child(id).addValueEventListener(booksListener);
    }

    private void showLoad(boolean b) {
        if (progressOverlay != null) {
            Helper.animateView(progressOverlay, b ? View.VISIBLE : View.GONE, 0.4f, 0);
            mainLayout.setVisibility(b ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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
