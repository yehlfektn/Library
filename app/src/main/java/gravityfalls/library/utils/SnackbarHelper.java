package gravityfalls.library.utils;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import gravityfalls.library.R;

public class SnackbarHelper {

    public static Snackbar getSnackBar(View view, String msg, int duration, View.OnClickListener listener, String actionMessage) {
        Snackbar snackbar = Snackbar.make(view, msg, duration).setDuration(duration).setActionTextColor(Color.RED);
        if (!(listener == null || TextUtils.isEmpty(actionMessage))) {
            snackbar.setAction(actionMessage, listener);
        }
        snackbar.getView().setBackgroundColor(Color.DKGRAY);
        TextView tv = snackbar.getView().findViewById(R.id.snackbar_text);
        ((TextView) snackbar.getView().findViewById(R.id.snackbar_text)).setTypeface(null, 1);
        tv.setTextColor(Color.WHITE);
        return snackbar;
    }

    public static Snackbar getSnackBar(View view, String msg) {
        return getSnackBar(view, msg, Snackbar.LENGTH_LONG, null, null);
    }
}
