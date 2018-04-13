package gravityfalls.library.main;

import android.app.Application;

import gravityfalls.library.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by 777 on 12.04.2018.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

}
