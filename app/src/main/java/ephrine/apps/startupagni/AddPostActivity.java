package ephrine.apps.startupagni;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AddPostActivity extends AppCompatActivity {
    public String TAG = "Startup Agni";
    public String Total;
    public int TotalInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference totalData = database.getReference("blogpost/total");
        totalData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if (value != null) {
                    Total = value;
                    TotalInt = Integer.parseInt(Total) + 1;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }

    public void postit(View v) {


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference TitleData = database.getReference("blogpost/" + Total + "/title");
        DatabaseReference DescData = database.getReference("blogpost/" + Total + "/desc");
        DatabaseReference UrlData = database.getReference("blogpost/" + Total + "/img");
        DatabaseReference BlogUrlData = database.getReference("blogpost/" + Total + "/url");

        DatabaseReference TotalData = database.getReference("blogpost/total");

        EditText TitleET = (EditText) findViewById(R.id.editTextTitle);
        EditText DescET = (EditText) findViewById(R.id.editTextDescription);
        EditText URLET = (EditText) findViewById(R.id.editTextURL);
        EditText BlogPostURL=(EditText)findViewById(R.id.EditTextBlogPostURL);

            TitleData.setValue(TitleET.getText().toString());
            DescData.setValue(DescET.getText().toString());
BlogUrlData.setValue(BlogPostURL.getText().toString());
        UrlData.setValue(URLET.getText().toString());

               // UrlData.setValue(" ");
                //UrlData.setValue("https://s3.amazonaws.com/culturesurvey.greatplacetowork.com/public/prd_photos_v11/GHQ_Building_WWT_060-1800x1200_caphoto22999.jpg");


            TotalData.setValue(String.valueOf(TotalInt));
            Success();

        }


        public void Success(){
            EditText TitleET = (EditText) findViewById(R.id.editTextTitle);
            EditText DescET = (EditText) findViewById(R.id.editTextDescription);
            EditText URLET = (EditText) findViewById(R.id.editTextURL);
            EditText BlogPostURL=(EditText)findViewById(R.id.EditTextBlogPostURL);


  /*         BlogPostURL.setText(" ");
            TitleET.setText(" ");
            DescET.setText(" ");
            URLET.setText(" ");
*/

            BlogPostURL.setVisibility(View.INVISIBLE);
            TitleET.setVisibility(View.INVISIBLE);
            DescET.setVisibility(View.INVISIBLE);
            URLET.setVisibility(View.INVISIBLE);

            TextView tx=(TextView)findViewById(R.id.textView13Success);
            tx.setVisibility(View.VISIBLE);
            tx.setText("New Blog Post has been added Successfully :)");

            tx.setTextColor(Color.parseColor("#ff669900"));
    }
    }

