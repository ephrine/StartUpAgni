package ephrine.apps.startupagni;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
public void ephrine(View v){
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse("https://ephrine.blogspot.com"));
    startActivity(intent);
}
}
