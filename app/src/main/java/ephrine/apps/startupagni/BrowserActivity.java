package ephrine.apps.startupagni;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BrowserActivity extends Activity {
    public String TAG = "Startup Agni";
public String url;
public View LoadingView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        // Receive Data
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String s = intent.getStringExtra(MainActivity.URL_MESSAGE);
        Log.d(TAG, "URL is: " + s);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference URLdata = database.getReference("blogpost/" + s + "/url");
        URLdata.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "URL is: " + value);

                if (value != null) {
                    url=value;
                    LoadWeb();


                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.browser_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.share:


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void LoadWeb(){
        WebView myWebView = (WebView) findViewById(R.id.Webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        myWebView.loadUrl(url);
        myWebView.setWebViewClient(new MyWebViewClient());

    }

    private class MyWebViewClient extends WebViewClient {
        /*@Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().equals("www.example.com")) {
                // This is my web site, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }*/
        public void onPageStarted(WebView view, String url, Bitmap favicon ) {
            LoadingView=(View)findViewById(R.id.LoadingView);
            LoadingView.setVisibility(View.VISIBLE);
        }
        public void onPageFinished(WebView view, String url) {
            LoadingView=(View)findViewById(R.id.LoadingView);
            LoadingView.setVisibility(View.INVISIBLE);
        }
    }
}
