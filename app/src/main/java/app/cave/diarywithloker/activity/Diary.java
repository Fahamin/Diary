package app.cave.diarywithloker.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import app.cave.diarywithloker.R;
import app.cave.diarywithloker.activity.firebase.LoginF;
import app.cave.diarywithloker.activity.firebase.RegistrationF;
import app.cave.diarywithloker.adapter.DataAdapter;
import app.cave.diarywithloker.dataparser.ItemDecorate;
import app.cave.diarywithloker.helper.DatabaseHelper;
import app.cave.diarywithloker.model.DataModel;

public class Diary extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    RecyclerView recyclerView;
    TextView messageTV;
    FloatingActionButton fab;
    List<DataModel> diaryModelList;
    DataModel diaryModel;
    DataAdapter adapter;
    DatabaseHelper diaryDatabase;
    private LinearLayoutManager layoutManager;

    AdView adView;
    AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MobileAds.initialize(getApplicationContext(), getString(R.string.appID));
        adView = findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        init();
        diaryModelList = diaryDatabase.getAllitem();

        if (diaryModelList.size() > 0) {

            prepareForView();


        } else {

            recyclerView.setVisibility(View.GONE);
            messageTV.setVisibility(View.VISIBLE);

        }


    }


    private void init() {

        fab = (FloatingActionButton) findViewById(R.id.fab_add);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        messageTV = (TextView) findViewById(R.id.messageTV);

        diaryModelList = new ArrayList<>();
        diaryModel = new DataModel();
        diaryDatabase = new DatabaseHelper(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Diary.this, MainActivity.class));
            }
        });

    }

    private void prepareForView() {


        messageTV.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        diaryModelList.clear();
        diaryModelList = diaryDatabase.getAllitem();


        adapter = new DataAdapter(this, diaryModelList, recyclerView, adapter, "", messageTV, this);

        RecyclerView.LayoutManager layoutManagerBeforeMeal = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManagerBeforeMeal);
        recyclerView.addItemDecoration(new ItemDecorate(1, dpToPx(2), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void singoutaccount(MenuItem item) {

        auth.signOut();
        startActivity(new Intent(Diary.this, LoginF.class));
        finish();

       /* FirebaseAuth.AuthStateListener stateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {

                }
            }
        };*/
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;

            case R.id.share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Install now");
                String app_url = "";
                shareIntent.putExtra(Intent.EXTRA_TEXT, app_url);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
                break;

            case R.id.lockID:
                startActivity(new Intent(this, RegistrationF.class));
                finish();
           // case R.id.removelockID:
//                deleteAccount();
           /* case R.id.rate:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
                startActivity(intent);
                break;
            case R.id.review:
                Intent inter = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
                startActivity(inter);
                break;
            case R.id.moreapp:
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
                startActivity(i);
                break;
            case R.id.exit:
                finish();
                System.exit(0);
                break;*/
        }
        return super.onOptionsItemSelected(item);

    }

    private void deleteAccount() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               // Log.e(TAG,"Ocurrio un error durante la eliminación del usuario", e);
            }
        });
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        diaryModelList.clear();
        diaryModelList = diaryDatabase.getAllitem();

        if (diaryModelList.size() > 0) {

            prepareForView();


        } else {

            recyclerView.setVisibility(View.GONE);
            messageTV.setVisibility(View.VISIBLE);

        }
    }
}
