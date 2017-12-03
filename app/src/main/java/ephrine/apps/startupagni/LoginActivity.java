package ephrine.apps.startupagni;

import android.app.Activity;

import android.content.Intent;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.support.annotation.NonNull;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;

import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends Activity {

    private static final int RC_SIGN_IN = 123;


    private FirebaseAuth mAuth;
    public String TAG = "Startup Agni";
public CallbackManager mCallbackManager;
public FirebaseAuth.AuthStateListener mAuthListener;

public GoogleSignInClient mGoogleSignInClient;
public GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);

    //    GetHashKey();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
//Account();

                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };

CardView EmailLogin=(CardView)findViewById(R.id.CardViewEmailLogin);
EmailLogin.setVisibility(View.GONE);

View LoginView=(View)findViewById(R.id.ViewLogin);
View SignupView=(View)findViewById(R.id.SignUpView);
LoginView.setVisibility(View.VISIBLE);
SignupView.setVisibility(View.GONE);


        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                View Loading=(View)findViewById(R.id.LoadingView);
                Loading.setVisibility(View.VISIBLE);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Account();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            Log.d(TAG, "User Account True");

            Account();
        }
    }



public void Account(){

    FirebaseAuth.getInstance().getCurrentUser().getProviderId();
    for (UserInfo user: FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
        if (user.getProviderId().equals("facebook.com")) {
            System.out.println("User is signed in with Facebook");
            String ProfileID= Profile.getCurrentProfile().getId().toString();
            String ProfilePicUrl="http://graph.facebook.com/"+ProfileID+"+/picture?type=small";
            // Picasso.with(this).load(ProfilePicUrl).into(ProfilePic);


        }else{
            System.out.println("User is signed in with Email");


        }
    }
finish();
    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
    startActivity(intent);

}

public void EmailLogin(View v){
    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
            R.anim.slideup);

    CardView EmailLogin=(CardView)findViewById(R.id.CardViewEmailLogin);
    EmailLogin.setVisibility(View.VISIBLE);
    EmailLogin.startAnimation(animation);
    LinearLayout LLForgetPassword=(LinearLayout)findViewById(R.id.LLViewPasswordReset);
    LLForgetPassword.setVisibility(View.GONE);
    LinearLayout LLViewLogin=(LinearLayout)findViewById(R.id.LLViewLogin);
    LLViewLogin.setVisibility(View.VISIBLE);


}

public void Emailsignup(View v){

    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
            R.anim.slideleft);
    View LoginView=(View)findViewById(R.id.ViewLogin);
    View SignupView=(View)findViewById(R.id.SignUpView);
    LoginView.setVisibility(View.GONE);
    SignupView.setVisibility(View.VISIBLE);
SignupView.startAnimation(animation);



}
public void EmailLogin1(View v){
    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
            R.anim.slideright);
    View LoginView=(View)findViewById(R.id.ViewLogin);
    View SignupView=(View)findViewById(R.id.SignUpView);
    LoginView.setVisibility(View.VISIBLE);
    SignupView.startAnimation(animation);
    LoginView.startAnimation(animation);

    SignupView.setVisibility(View.GONE);
    LinearLayout LLViewLogin=(LinearLayout)findViewById(R.id.LLViewLogin);
    LLViewLogin.setVisibility(View.VISIBLE);
    LinearLayout LLForgetPassword=(LinearLayout)findViewById(R.id.LLViewPasswordReset);
    LLForgetPassword.setVisibility(View.GONE);

}
    public void EmailLogin2(View v){
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slideright);
        View LoginView=(View)findViewById(R.id.ViewLogin);
        View SignupView=(View)findViewById(R.id.SignUpView);
        LoginView.setVisibility(View.VISIBLE);

        SignupView.setVisibility(View.GONE);

        LinearLayout LLViewLogin=(LinearLayout)findViewById(R.id.LLViewLogin);
        LLViewLogin.setVisibility(View.VISIBLE);
        LinearLayout LLForgetPassword=(LinearLayout)findViewById(R.id.LLViewPasswordReset);
        LLForgetPassword.setVisibility(View.GONE);

    }
public void backButton(View v){
    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
            R.anim.slidedown);
    CardView EmailLogin=(CardView)findViewById(R.id.CardViewEmailLogin);
    EmailLogin.setVisibility(View.GONE);
    EmailLogin.startAnimation(animation);
}
public void PasswordReset(View v){
    LinearLayout LLViewLogin=(LinearLayout)findViewById(R.id.LLViewLogin);
    LLViewLogin.setVisibility(View.GONE);
    LinearLayout LLForgetPassword=(LinearLayout)findViewById(R.id.LLViewPasswordReset);
    LLForgetPassword.setVisibility(View.VISIBLE);
}

public void emailsignin(View v){
    EditText EmailIDET=(EditText)findViewById(R.id.editTextEmail);
    EditText passwordET=(EditText)findViewById(R.id.editTextPassword);
if(EmailIDET.getText().toString()!=null && passwordET.getText().toString()!=null){
    View Loading=(View)findViewById(R.id.LoadingView);
    Loading.setVisibility(View.VISIBLE);

    mAuth = FirebaseAuth.getInstance();
    mAuth.signInWithEmailAndPassword(EmailIDET.getText().toString(), passwordET.getText().toString())
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Account();

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();


                    }


                }
            });


}

    }

public void PasswordResetEmailSend(View v){
    final EditText email=(EditText)findViewById(R.id.editText4);
    FirebaseAuth auth = FirebaseAuth.getInstance();
    final String emailAddress = email.getText().toString();

    auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Email sent.");

TextView tx=(TextView)findViewById(R.id.TxPasswordReset);
tx.setText("Password Reset Email has been send to "+emailAddress);
email.setVisibility(View.GONE);
Button pw=(Button)findViewById(R.id.buttonPasswordReset1);
pw.setVisibility(View.GONE);

                    }
                }
            });
}

public void EmailAccountCreate(View v){
    final EditText UserNameET=(EditText)findViewById(R.id.editText3);
    EditText UserEmailET=(EditText)findViewById(R.id.editText);
    EditText UserPassword=(EditText)findViewById(R.id.editText2);

if(UserNameET.getText().toString()!=null && UserEmailET.getText().toString()!=null && UserPassword.getText().toString()!=null){
    View Loading=(View)findViewById(R.id.LoadingView);
    Loading.setVisibility(View.VISIBLE);

    mAuth.createUserWithEmailAndPassword(UserEmailET.getText().toString(), UserPassword.getText().toString())
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        //  FirebaseUser user = mAuth.getCurrentUser();

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(UserNameET.getText().toString())
                                //   .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                                .build();

                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User profile updated.");
                                             Account();
                                        }
                                    }
                                });

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();

                    }

                    // ...
                }
            });



}

}
public void nulll(View v){

}
public void GetHashKey(){

    try {
        PackageInfo info = getPackageManager().getPackageInfo(
                "ephrine.apps.startupagni",
                PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("Hash Key", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } catch (PackageManager.NameNotFoundException e) {
        Log.d("Hash Key","Failed 1");

    } catch (NoSuchAlgorithmException e) {
        Log.d("Hash Key","Failed 2");
    }
}

}
