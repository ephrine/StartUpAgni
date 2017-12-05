package ephrine.apps.startupagni;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    public static final String URL_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public String TAG = "Startup Agni";
    public int total;
    public String BlogURL;
    public String BlogTitle;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        GetTotal();
        AdsLoad();
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
            case R.id.signout:
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
               // deleteAppData();
                if(mInterstitialAd.isLoaded()){

                    mInterstitialAd.show();

                }else {
                    deleteAppData();
                    finish();

                }


                return true;
            case R.id.chat:
                Intent intent1 = new Intent(this, ChatActivity.class);
                startActivity(intent1);
                return true;

            case R.id.about:
                Intent intent2 = new Intent(this, AboutActivity.class);
                startActivity(intent2);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void deleteAppData() {
        try {
            // clearing app data
            String packageName = getApplicationContext().getPackageName();
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("pm clear "+packageName);
            Log.d(TAG, "App Data Cleared !!");

        } catch (Exception e) {
            e.printStackTrace();
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


        View.OnClickListener ShareClicks = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();

                Toast toast = Toast.makeText(context, "Sharing...", Toast.LENGTH_SHORT);
                toast.show();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final String S = String.valueOf(v.getId());
                DatabaseReference URLdata = database.getReference("blogpost/" + S + "/url");
                DatabaseReference Titledata = database.getReference("blogpost/" + S + "/title");

                URLdata.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        String value = dataSnapshot.getValue(String.class);
                        Log.d(TAG, "URL is: " + value);

                        if (value != null) {
                            BlogURL=value;
if(BlogTitle!=null){
    share();

}

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
                Titledata.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        String value = dataSnapshot.getValue(String.class);
                        Log.d(TAG, "URL is: " + value);

                        if (value != null) {
                            BlogTitle=value;
if(BlogURL!=null){
    share();
}
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });




            }
        };



        for (int j = total-1; j >=1; j-=1) {

            final CardView
                    mcard = new CardView(MainActivity.this);
            mcard.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
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
           // img.setScaleType(ImageView.ScaleType.FIT_XY);
            //img.setLayoutParams(layoutParams);
            //img.getLayoutParams().height = 200;
            //img.getLayoutParams().width = 200;

            img.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            LL.addView(img);

            LinearLayout LL1 = new LinearLayout(MainActivity.this);
            LL1.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
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

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                final Drawable ShareDraw = getDrawable(R.drawable.ic_menu_share);
int w=100;
int h=100;
                ImageView ShareButton=new ImageView((MainActivity.this));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, h);
                params.gravity = Gravity.RIGHT;
                ShareButton.setLayoutParams(params);
                ShareButton.setImageDrawable(ShareDraw);
                ShareButton.setId(j);
                ShareButton.setOnClickListener(ShareClicks);
                LL1.addView(ShareButton);
            }else {
                Button ShareButton=new Button((MainActivity.this));
                /*ShareButton.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
               */
                ShareButton.setText("Share");
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.RIGHT;
                ShareButton.setLayoutParams(params);
                ShareButton.setId(j);
                ShareButton.setOnClickListener(ShareClicks);
                LL1.addView(ShareButton);

            }


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

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            Log.d(TAG, "User Account True");
AdminConsole();
            if(currentUser.getUid().toString().equals("")){
                LinearLayout LLConsole=(LinearLayout)findViewById(R.id.LLConsole);
LLConsole.setVisibility(View.VISIBLE);
            }
            FirebaseAuth.getInstance().getCurrentUser().getProviderId();
            for (UserInfo user: FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
                if (user.getProviderId().equals("facebook.com")) {
                    System.out.println("User is signed in with Facebook");
                    String ProfileID= Profile.getCurrentProfile().getId().toString();
                    //   String ProfilePicUrl="http://graph.facebook.com/"+ProfileID+"/picture?type=large&width=720&height=720";
                    // Picasso.with(this).load(ProfilePicUrl).into(ProfilePic);
                    ProfilePictureView profilePictureView;
                    profilePictureView = (ProfilePictureView) findViewById(R.id.friendProfilePicture);
                    profilePictureView.setProfileId(ProfileID);

                }
            }
            TextView UserName=(TextView)findViewById(R.id.textViewName);
            TextView Email=(TextView)findViewById(R.id.textView7Email);

            UserName.setText(currentUser.getDisplayName());
            Email.setText(currentUser.getEmail());
        }

    SDM();
    }


    public void AdminConsole(){

        FirebaseUser currentUser = mAuth.getCurrentUser();
// Write a message to the database
       final  String UID=currentUser.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("admin");
// Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Admin is: " + value);
                if(UID.equals(value)){
                    LinearLayout LLConsole=(LinearLayout)findViewById(R.id.LLConsole);
                    LLConsole.setVisibility(View.VISIBLE);
                }else {
                    LinearLayout LLConsole=(LinearLayout)findViewById(R.id.LLConsole);
                    LLConsole.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }

    public void SDM(){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("sdm");
// Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "SDM is: " + value);
                if(value.equals("1")){
                    LinearLayout LLConsole=(LinearLayout)findViewById(R.id.LoadingView);
LLConsole.setVisibility(View.GONE);
                }else {

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void share(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, BlogTitle+" "+BlogURL+" . Shared via StartUpAgni App https://goo.gl/suxeTk");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
public void AdsLoad(){
    MobileAds.initialize(this, getString(R.string.Ads_app_id));
    mInterstitialAd = new InterstitialAd(this);
    mInterstitialAd.setAdUnitId(getString(R.string.Ads_app_int_unit_id));
    mInterstitialAd.loadAd(new AdRequest.Builder().build());
    mInterstitialAd.setAdListener(new AdListener() {
        @Override
        public void onAdLoaded() {
            // Code to be executed when an ad finishes loading.
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            // Code to be executed when an ad request fails.
            //finish();
        }

        @Override
        public void onAdOpened() {
            // Code to be executed when the ad is displayed.
        }

        @Override
        public void onAdLeftApplication() {
            // Code to be executed when the user has left the app.
            //finish();
        }

        @Override
        public void onAdClosed() {
            finish();
            // Code to be executed when when the interstitial ad is closed.
        }
    });

    mAdView = (AdView) findViewById(R.id.adView);
    AdRequest adRequest = new AdRequest.Builder().build();
    mAdView.loadAd(adRequest);
}
    @Override
    public void onBackPressed() {


            if(mInterstitialAd.isLoaded()){

                mInterstitialAd.show();

            }else{
                Log.d("TAG", "The interstitial wasn't loaded yet.");
                super.onBackPressed();


        }


        super.onBackPressed();
    }

}

