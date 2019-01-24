package app.cave.diarywithloker.activity.firebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import app.cave.diarywithloker.R;
import app.cave.diarywithloker.activity.Diary;


public class LoginF extends AppCompatActivity {
    private EditText inputEmail, inputPassword, confrimpass;
    private Button btnSignup, btnLogin;
    private ProgressDialog mProgress;
    private FirebaseAuth auth;
    TextView btnReset;
    SharedPreferences preferences;
    String email, password, confrimpasword, hello = "";
    FirebaseUser user;

    String keys = "mail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_f);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Toast.makeText(this, "please check internet connection", Toast.LENGTH_LONG).show();


        initView();

        intvarible();
        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

/*
        preferences = getSharedPreferences("diary", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(keys, email);
        editor.commit();
        // sharepreference();
        LOAD();*/

    }

    private void sharepreference() {
    }

    private void LOAD() {
        if (preferences.contains(keys)) {
            inputEmail.setText(preferences.getString(keys, ""));
        } else {
            startActivity(new Intent(this, Diary.class));
            finish();
        }


       /* if (auth.getCurrentUser() == null) {
            startActivity(new Intent(this, Diary.class));
            finish();
        }
        else{

        }*/
    }

    private void intvarible() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user != null) {
            hello = hello.concat("").concat(user.getEmail());
        } else
        {
           // hello = "";
            startActivity(new Intent(this, Diary.class));
            finish();
        }


        inputEmail.setText(hello);

    }

    private void initView() {
        inputEmail = (EditText) findViewById(R.id.inputEmailEtdId);
        inputPassword = (EditText) findViewById(R.id.inputPassEtdId);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = findViewById(R.id.btn_reset_password);


        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginF.this, ResetPassword.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();


                if (TextUtils.isEmpty(email)) {
                    inputEmail.setError("Enter Email");
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
                    inputPassword.setError("password check");
                    inputPassword.requestFocus();
                    return;
                }

                mProgress.show();
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    mProgress.dismiss();
                                    startActivity(new Intent(LoginF.this, Diary.class));

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mProgress.dismiss();
                        Toast.makeText(LoginF.this, "Login Failed!! \n Try again", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }


    public void RegisterBTN(View view) {
        startActivity(new Intent(LoginF.this, RegistrationF.class));

    }
}
