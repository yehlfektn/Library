package gravityfalls.library.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import gravityfalls.library.R;
import gravityfalls.library.objects.Book;

public class BookDetailsActivity extends AppCompatActivity {

    @BindView(R.id.imageview)
    ImageView imageView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
 /*   @BindView(R.id.txt_title)
    TextView title;
    @BindView(R.id.txt_author)
    TextView author;*/

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


        if (getIntent().getExtras() != null) {
            Book book = getIntent().getExtras().getParcelable("data");
            if (book!=null) {
                Log.e(TAG, "Book: " + book.getAuthor());
                Glide.with(this).load(book.getImageLink()).into(imageView);
                /*title.setText(book.getTitle());
                author.setText(book.getAuthor());*/
                setTitle(book.getTitle());
            }
        }

    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
