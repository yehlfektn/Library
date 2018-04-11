package gravityfalls.library.main;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import gravityfalls.library.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by 777 on 12.04.2018.
 */

public class MainActivity extends AppCompatActivity {

    Unbinder unbinder;
    Drawer mDrawer;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DrawerBuilder mDrawerBuilder = new DrawerBuilder().withActivity(this).withToolbar(mToolbar)
                .withTranslucentStatusBar(false);

        mDrawerBuilder.addDrawerItems(new PrimaryDrawerItem().withName(R.string.private_cab)
                .withTypeface(Typeface.defaultFromStyle(Typeface.BOLD)).withIcon(R.drawable.boy));
        mDrawerBuilder.addDrawerItems(new PrimaryDrawerItem().withName(R.string.library)
                .withTypeface(Typeface.defaultFromStyle(Typeface.BOLD)).withIcon(R.drawable.library));

        unbinder = ButterKnife.bind(this);
        mDrawer = mDrawerBuilder.build();
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
