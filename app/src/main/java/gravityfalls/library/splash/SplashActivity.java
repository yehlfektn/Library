package gravityfalls.library.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import gravityfalls.library.R;
import gravityfalls.library.login.LoginActivity;
import gravityfalls.library.main.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.progress_overlay)
    FrameLayout progressOverlay;
    Unbinder unbinder;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash);
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
        //showLoad();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                Intent i;
                if (currentUser != null){
                    i = new Intent(SplashActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                }else {
                    i = new Intent(SplashActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                }
                startActivity(i);
            }
        }, 1000);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
