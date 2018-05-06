package gravityfalls.library.main;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import gravityfalls.library.R;
import gravityfalls.library.objects.Book;
import gravityfalls.library.objects.User;
import gravityfalls.library.utils.SnackbarHelper;
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
    @BindView(R.id.main_layout)
    LinearLayout mainLayout;
    @BindView(R.id.profile_email)
    TextView email;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.family_name)
    TextView family;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private ArrayList<Book> arrayList = new ArrayList<>();
    //private ProfileBooksAdapter adapter;

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
        user = FirebaseAuth.getInstance().getCurrentUser();

        email.setText(user.getEmail());

        populateUserData();

        setAdapter();
        updateListView();
    }

    private void populateUserData() {
        ValueEventListener booksListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null){
                    if (user.getName() != null)
                        ((TextView) findViewById(R.id.name)).setText(user.getName());
                    if (user.getFamily_name() != null){
                        ((TextView) findViewById(R.id.family_name)).setText(user.getFamily_name());
                    }
                    if (user.getRole() != null){
                        ((TextView) findViewById(R.id.profile_role)).setText(user.getRole());
                    }
                    if (user.getFamily_name() != null){
                        ((TextView) findViewById(R.id.profile_about)).setText(user.getAbout());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error: " + databaseError.getMessage());
                if (mainLayout != null) {
                    SnackbarHelper.getSnackBar(mainLayout, getString(R.string.can_not_load_data));
                }
            }
        };
        mDatabase.child("users").child(user.getUid()).addValueEventListener(booksListener);
    }

    private void setAdapter() {
 /*       try {
            adapter = new ProfileBooksAdapter(arrayList, this, new ProfileBooksAdapter.BooksListener() {
                @Override
                public void onBookReturned(int position) {
                    mDatabase.child("books").child(String.valueOf(position)).child("available").setValue(true);
                    mDatabase.child("books").child(String.valueOf(position)).child("onUser").setValue("none");
                    //updateListView();
                }
            });
            listView.setAdapter(adapter);
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }

    private void updateListView() {
      /*  ValueEventListener booksListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Book book = child.getValue(Book.class);
                    if (book != null) {
                        Log.e(TAG,"Book: ");
                        if (book.getOnUser().equals(user.getUid()))
                            arrayList.add(book);
                    }
                }
                Log.e(TAG, "Array list size: " + arrayList.size());
                setAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error: " + databaseError.getMessage());
                if (mainLayout != null)
                    SnackbarHelper.getSnackBar(mainLayout,getString(R.string.can_not_load_data));
            }
        };
        mDatabase.child("books").addValueEventListener(booksListener);*/
    }

    @OnClick(R.id.name_layout)
    void onNameChangeClicked(){
        showAlertDialog("Изменить Имя", "name");
    }

    @OnClick(R.id.family_layout)
    void onFamilyNameChanged(){
        showAlertDialog("Изменить Фамилию", "family_name");
    }

    @OnClick(R.id.role_layout)
    void onRoleNameChanged(){
        showAlertDialog("Изменить Роль", "role");
    }

    @OnClick(R.id.about_layout)
    void onAboutNameChanged(){
        showAlertDialog("Изменить информацию о себе", "about");
    }



    private void showAlertDialog(String title, final String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDatabase.child("users").child(user.getUid()).child(type).setValue(input.getText().toString());
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
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
