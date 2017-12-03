package ephrine.apps.startupagni;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    public static final String URL_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public String TAG = "Startup Agni";
    public int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);

        GetTotal();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.profile:


                return true;
            case R.id.chat:
                Intent intent = new Intent(this, ChatActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void NewPost(View v) {
        Intent intent = new Intent(this, AddPostActivity.class);
        startActivity(intent);
    }

    public void GetTotal() {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference totalData = database.getReference("blogpost/total");
// Read from the database
        totalData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    total = Integer.parseInt(value);
                    CreateView();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void CreateView() {
        LinearLayout myLayout = findViewById(R.id.LLFeedView);

        View.OnClickListener clicks = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String S = String.valueOf(v.getId());
                Intent intent = new Intent(MainActivity.this, BrowserActivity.class);
                intent.putExtra(URL_MESSAGE, S);
                startActivity(intent);
               /*
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference URLdata = database.getReference("blogpost/" + S + "/url");
                URLdata.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        String value = dataSnapshot.getValue(String.class);
                        Log.d(TAG, "URL is: " + value);

                        if (value != null) {
                            Intent intent = new Intent(MainActivity.this, BrowserActivity.class);
                            intent.putExtra(URL_MESSAGE, value);
                            startActivity(intent);

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });

                */
            }
        };


        for (int j = 1; j < total; j++) {

            final CardView
                    mcard = new CardView(MainActivity.this);
            mcard.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            mcard.setCardElevation(10);
            mcard.setUseCompatPadding(true);
            mcard.setId(j + 0);
            mcard.setOnClickListener(clicks);
            //  mcard.setOnClickListener(clicks);
            //mcard.setText("Button"+(i+1));
            // btn.setLayoutParams(lprams);

            myLayout.addView(mcard);

            LinearLayout LL = new LinearLayout(MainActivity.this);
            LL.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            LL.setOrientation(LinearLayout.VERTICAL);
            LL.setPadding(30, 20, 30, 20);
            mcard.addView(LL);

            final ImageView img = new ImageView(MainActivity.this);

            // LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
            img.setScaleType(ImageView.ScaleType.FIT_XY);
            //img.setLayoutParams(layoutParams);
            //img.getLayoutParams().height = 200;
            //img.getLayoutParams().width = 200;

            img.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            LL.addView(img);

            LinearLayout LL1 = new LinearLayout(MainActivity.this);
            LL1.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            LL1.setOrientation(LinearLayout.VERTICAL);
            LL.addView(LL1);

            final TextView tx = new TextView(MainActivity.this);
            tx.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));


            tx.setTypeface(null, Typeface.BOLD);
            tx.setTextSize(18);
            tx.setText("Loading....");
            LL1.addView(tx);


            final TextView tx1 = new TextView(MainActivity.this);
            tx1.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            tx1.setText("Loading....");
            LL1.addView(tx1);

            // Set Title
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference titleData = database.getReference("blogpost/" + j + "/title");
            titleData.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = dataSnapshot.getValue(String.class);
                    Log.d(TAG, "Value is: " + value);
                    if (value != null) {
                        tx.setText(value);
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });

            //Set Description Text
            DatabaseReference descData = database.getReference("blogpost/" + j + "/desc");
            descData.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = dataSnapshot.getValue(String.class);
                    Log.d(TAG, "Value is: " + value);
                    if (value != null) {
                        tx1.setText(value);
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });


            //Set Thumb Image
            DatabaseReference imgData = database.getReference("blogpost/" + j + "/img");
            imgData.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = dataSnapshot.getValue(String.class);
                    Log.d(TAG, "Value is: " + value);
                    if (value != null) {
                        Glide.with(MainActivity.this).load(value).into(img);

                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });


        }


    }

}

