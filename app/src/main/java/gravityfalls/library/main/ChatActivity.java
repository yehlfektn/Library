package gravityfalls.library.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import gravityfalls.library.R;
import gravityfalls.library.objects.ChatMessage;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by 777 on 12.04.2018.
 */

public class ChatActivity extends AppCompatActivity {

    Unbinder unbinder;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.input)
    EditText input;
    @BindView(R.id.list_of_messages)
    ListView listView;

    private FirebaseListAdapter<ChatMessage> adapter;
    private static String ARG_TITLE = "title";

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser user;

    private String TAG = "ProfileActivity";

    public static Intent newInstance(Context context, String title){
        Intent i = new Intent(context, ChatActivity.class);
        i.putExtra(ARG_TITLE,title);
        return i;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        unbinder = ButterKnife.bind(this);

        ((TextView) mToolbar.findViewById(R.id.title)).setText(getIntent().getExtras().getString(ARG_TITLE));
        setSupportActionBar(mToolbar);
        setTitle("");
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("messages");
        user = FirebaseAuth.getInstance().getCurrentUser();
        displayChatMessages();
    }

    private void displayChatMessages() {
        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference().child("messages")) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = v.findViewById(R.id.message_text);
                TextView messageUser = v.findViewById(R.id.message_user);
                TextView messageTime = v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        listView.setAdapter(adapter);
    }

    @OnClick(R.id.fab)
    void onSendClick(){
        String name;
        if (FirebaseAuth.getInstance()
                .getCurrentUser()
                .getDisplayName() != null){
            name = FirebaseAuth.getInstance()
                    .getCurrentUser()
                    .getDisplayName();
        }else {
            name = FirebaseAuth.getInstance()
                    .getCurrentUser()
                    .getEmail();
        }
        mDatabase.push().setValue(new ChatMessage(input.getText().toString(), name));
        // Clear the input
        input.setText("");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
