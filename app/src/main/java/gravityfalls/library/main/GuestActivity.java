package gravityfalls.library.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import gravityfalls.library.R;
import gravityfalls.library.objects.Guest;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class GuestActivity extends AppCompatActivity {

    Unbinder unbinder;
    @BindView(R.id.guest_list)
    LinearLayout mList;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        unbinder = ButterKnife.bind(this);

        setTitle("");
        ((TextView) mToolbar.findViewById(R.id.title)).setText("Список гостей");
        setSupportActionBar(mToolbar);
        initList();
    }

    private void initList() {
        for (int i = 0; i < 10; i++) {
            Guest g = new Guest("Арман", "Жаксыбаев", 33, "+77475455050", "17:00, 13 Maя", "Hilton Garden Inn Astana");
            LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.it_guest, mList, false);
            ((TextView) view.findViewById(R.id.name)).setText(g.getName() + " " + g.getFamily_name());
            view.findViewById(R.id.lesson_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), GuestDetailsActivity.class);
                }
            });
            mList.addView(view);
        }
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
        getMenuInflater().inflate(R.menu.guests_menu, menu);
        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
