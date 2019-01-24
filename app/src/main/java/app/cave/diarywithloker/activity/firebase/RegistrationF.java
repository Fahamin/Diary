package app.cave.diarywithloker.activity.firebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import app.cave.diarywithloker.R;


public class RegistrationF extends AppCompatActivity {

    AdView adView;
    AdRequest adRequest;

    private EditText inputEmail, inputPassword, etdconfrimpass;
    private Button btnSignIn, btnSignUp;
    private ProgressDialog mProgress;
    private FirebaseAuth auth;
    String email, password, confrimpasword;
    private Button btnResetPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 0);

        setContentView(R.layout.activity_registration_f);



        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

      /*  MobileAds.initialize(getApplicationContext(), getString(R.string.banner_ad_unit_id));
        adView =findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);*/
        auth = FirebaseAuth.getInstance();

       // btnSignIn = (Button) findViewById(R.id.btn_login);
        btnSignUp = (Button) findViewById(R.id.btn_login);
        inputEmail = (EditText) findViewById(R.id.inputEmailEtdId);
        inputPassword = (EditText) findViewById(R.id.inputPassEtdId);


        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = inputEmail.getText().toString().trim();
                password = inputPassword.getText().toString().trim();
             //   confrimpasword = etdconfrimpass.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    inputEmail.setError("Enter Valid Email");
                    inputEmail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    inputEmail.setError("Enter Valid Email");
                    inputEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    inputPassword.setError("Required password");
                    inputPassword.requestFocus();
                    return;
                }

                if (password.length() < 6) {
                    inputPassword.setError("Atleast 6 characters requried");
                    inputPassword.requestFocus();
                    return;
                }

                mProgress.show();
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegistrationF.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (!task.isSuccessful()) {
                                    mProgress.dismiss();
                                    Toast.makeText(RegistrationF.this, "Authentication failed!!\n check internet connection",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    mProgress.dismiss();
                                    Intent i = new Intent(RegistrationF.this, LoginF.class);
                                    i.putExtra("emailS",email);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}