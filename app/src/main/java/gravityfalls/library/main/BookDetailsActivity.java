package gravityfalls.library.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import gravityfalls.library.R;
import gravityfalls.library.objects.Book;
import gravityfalls.library.objects.User;
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
    @BindView(R.id.date_layout)
    LinearLayout date_layout;
    @BindView(R.id.txt_date)
    TextView date;

    @BindView(R.id.progress_overlay)
    FrameLayout progressOverlay;
    @BindView(R.id.main_layout)
    LinearLayout mainLayout;

    private String id;
    private Unbinder unbinder;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private String uID;


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
                author.setText(book.getAuthor());
                year.setText(book.getYear());
                language.setText(book.getLanguage());
                pages.setText(book.getPages() + "");
                country.setText(book.getCountry());
                description.setText(book.getShort_description());
                status.setText(book.isAvailable() ? "Доступен" : "Не доступен");
                status.setTextColor(book.isAvailable() ? ContextCompat.getColor(this, R.color.colorAccent) : ContextCompat.getColor(this, R.color.colorPrimary));
                get_book.setVisibility(book.isAvailable() ? View.VISIBLE : View.GONE);
                uID = book.getOnUser();
                if (book.getOnUser().equals(user.getUid()))
                    return_book.setVisibility(View.VISIBLE);
                else return_book.setVisibility(View.GONE);
                Log.e(TAG, "onUser: " + book.getOnUser());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.get_book)
    void onGetBookClicked() {
        Log.e(TAG, "OnGetBook was clicked!");
        mDatabase.child("books").child(id).child("available").setValue(false);
        mDatabase.child("books").child(id).child("onUser").setValue(user.getUid());
        String date = new SimpleDateFormat("dd MMMM yyyy", new Locale("ru")).format(Calendar.getInstance().getTime());
        mDatabase.child("books").child(id).child("date_taken").setValue(date);
        loadData();
    }

    @OnClick(R.id.return_book)
    void onBookReturnClicked(){
        mDatabase.child("books").child(id).child("available").setValue(true);
        mDatabase.child("books").child(id).child("onUser").setValue("none");
        loadData();
    }

    private void loadData() {
        ValueEventListener booksListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Book book = dataSnapshot.getValue(Book.class);
                if (book != null) {
                    try {
                        status = findViewById(R.id.txt_status);
                        status.setTextColor(book.isAvailable() ? ContextCompat.getColor(getApplicationContext(), R.color.colorAccent) : ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                        get_book.setVisibility(book.isAvailable() ? View.VISIBLE : View.GONE);
                        if (book.getOnUser().equals(user.getUid()))
                            return_book.setVisibility(View.VISIBLE);
                        else return_book.setVisibility(View.GONE);
                        if (!book.isAvailable()) {
                            Log.e(TAG,"Update status called");
                            updateStatus();
                            updateDate();
                        }
                        else
                            status.setText("Доступна");
                            date_layout.setVisibility(View.GONE);
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
        mDatabase.child("books").child(id).addListenerForSingleValueEvent(booksListener);
    }

    private void updateDate() {
        ValueEventListener booksListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String date_ = dataSnapshot.getValue(String.class);
                if (date_!=null){
                    date_layout.setVisibility(View.VISIBLE);
                    date.setText(date_);
                }else {
                    date_layout.setVisibility(View.GONE);
                }
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
        mDatabase.child("books").child(id).child("date_taken").addListenerForSingleValueEvent(booksListener);
    }

    private void updateStatus() {
        ValueEventListener booksListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user_data = dataSnapshot.getValue(User.class);
                String name = "";
                if (user_data!=null){
                    if (user_data.getName()!=null){
                        name += user_data.getName();
                    }
                    if (user_data.getFamily_name() !=null){
                        name += " "+user_data.getFamily_name();
                    }
                    Log.e(TAG,"Status text: ");
                    if (status!=null)
                    status.setText(getString(R.string.book_is_currently_in,name));
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
        Log.e(TAG, "Database Called");
        mDatabase.child("users").child(uID).addListenerForSingleValueEvent(booksListener);
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
        mDatabase = null;
        super.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
