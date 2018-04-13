package gravityfalls.library.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import gravityfalls.library.R;
import gravityfalls.library.objects.Book;
import gravityfalls.library.utils.Helper;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import uk.co.chrisjenx.calligraphy.CalligraphyUtils;

/**
 * Created by Nurdaulet Kenges on 12.04.2018.
 */

public class BookDetailsActivity extends AppCompatActivity {

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

    Unbinder unbinder;
    private String TAG = "BookDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        unbinder = ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null) {
            Book book = getIntent().getExtras().getParcelable("data");
            if (book!=null) {
                setTitle(book.getTitle());
                Glide.with(this).load(book.getImageLink()).into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable glideDrawable, @Nullable Transition<? super Drawable> transition) {
                        double ratio = (double)glideDrawable.getIntrinsicWidth()/(double)glideDrawable.getIntrinsicHeight();
                        imageView.setImageDrawable(glideDrawable);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                (int)Helper.convertDpToPixel((int)(200.0 * ratio), getApplicationContext()),
                                (int)Helper.convertDpToPixel(200, getApplicationContext()));
                        layoutParams.setMargins(0, (int)Helper.convertDpToPixel(40, getApplicationContext()),
                                0, (int)Helper.convertDpToPixel(70, getApplicationContext()));
                        imageView.setLayoutParams(layoutParams);
                    }
                });


                //CalligraphyUtils.applyFontToTextView(this, title, "fonts/mono_bold.ttf");
                author.setText(book.getAuthor());
                year.setText(book.getYear());
                description.setText(book.getShort_description());
            }
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
