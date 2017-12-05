package ephrine.apps.startupagni;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.login.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends Activity {
    private FirebaseAuth mAuth;
    public String Login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        mAuth = FirebaseAuth.getInstance();
        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(2 * 1000);

              if (Login.equals("y")) {
                        // After 5 seconds redirect to another intent
                        Intent i = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(i);

                        // Remove activity
                        finish();
                    } else if (Login.equals("n")) {
                        Intent i = new Intent(getBaseContext(), LoginActivity.class);
                      startActivity(i);

                        // Remove activity
    finish();
                    }


                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
if(currentUser!=null){
    Login="y";
}else{
    Login="n";
}
    }
}
