package ephrine.apps.startupagni;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.login.Login;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);


        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(5 * 1000);

  /*                  if (Login.equals("y")) {
                        // After 5 seconds redirect to another intent
                        //Intent i = new Intent(getBaseContext(), MainActivity.class);
                        //startActivity(i);

                        // Remove activity
                        finish();
                    } else if (Login.equals("n")) {
                      //  Intent i = new Intent(getBaseContext(), LoginActivity.class);
                        //startActivity(i);

                        // Remove activity
//                        finish();
                    }
*/

                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();
    }
}
