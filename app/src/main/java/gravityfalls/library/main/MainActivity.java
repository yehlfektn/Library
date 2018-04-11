package gravityfalls.library.main;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerBuilder mDrawerBuilder = new DrawerBuilder().withActivity(this);

        mDrawerBuilder.addDrawerItems(new SectionDrawerItem().withName(R.string.private_cab).withTypeface(Typeface.defaultFromStyle(Typeface.BOLD)));
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
