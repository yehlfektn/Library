package gravityfalls.library.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import gravityfalls.library.R;
import gravityfalls.library.objects.Book;

/**
 * Created by Nurdaulet Kenges on 12.04.2018.
 */

public class BookDetailsActivity extends AppCompatActivity {

    @BindView(R.id.imageview)
    ImageView imageView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txt_title)
    TextView title;
    @BindView(R.id.txt_author)
    TextView author;
    @BindView(R.id.txt_year)
    TextView year;
    @BindView(R.id.txt_description)
    TextView description;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
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
        toolbar.setTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this,R.color.white));

        if (getIntent().getExtras() != null) {
            Book book = getIntent().getExtras().getParcelable("data");
            if (book!=null) {
                setTitle(book.getTitle());
                Glide.with(this).load(book.getImageLink()).into(imageView);
                title.setText(book.getTitle());
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
}
