package gravityfalls.library.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import gravityfalls.library.R;
import gravityfalls.library.adapters.ProfileBooksAdapter;
import gravityfalls.library.objects.Book;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by 777 on 12.04.2018.
 */

public class ProfileActivity extends AppCompatActivity {

    Unbinder unbinder;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.listView)
    ListView listView;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ArrayList<Book> arrayList = new ArrayList<>();
    private ProfileBooksAdapter adapter;

    private String TAG = "ProfileActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        unbinder = ButterKnife.bind(this);

        ((TextView)mToolbar.findViewById(R.id.title)).setText(R.string.private_cab);
        setSupportActionBar(mToolbar);
        setTitle("");
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        adapter = new ProfileBooksAdapter(arrayList,this);
        listView.setAdapter(adapter);

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

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
