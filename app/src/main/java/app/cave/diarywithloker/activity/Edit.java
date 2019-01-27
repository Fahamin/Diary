package app.cave.diarywithloker.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import app.cave.diarywithloker.R;
import app.cave.diarywithloker.activity.firebase.LoginF;
import app.cave.diarywithloker.activity.firebase.RegistrationF;
import app.cave.diarywithloker.helper.DatabaseHelper;
import app.cave.diarywithloker.model.DataModel;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class Edit extends AppCompatActivity {

    Button saveBTN;

    String title, description, times, date;
    Calendar calendar;
    int id, flag;
    EmojiconEditText titleET, descriptionET;
    ImageView imageView;
    ImageView submitBtn;
    DatabaseHelper diaryDatabase;
    DataModel diaryModel;
    EmojIconActions emojIconActions;
    AdRequest adRequest;
    AdView adView;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MobileAds.initialize(getApplicationContext(), getString(R.string.appID));
        adView = findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        flag = intent.getIntExtra("flag", -1);

        diaryModel = new DataModel();
        diaryDatabase = new DatabaseHelper(this);

        diaryModel = diaryDatabase.selectWithId(String.valueOf(id));

        init();
        setData(diaryModel);

        emojIconActions = new EmojIconActions(this, rootView, titleET, imageView);
        emojIconActions.ShowEmojIcon();
        emojIconActions.setIconsIds(R.drawable.smiley, R.drawable.ic_action_keyboard);
        emojIconActions.setUseSystemEmoji(true);
        emojIconActions.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {

            }

            @Override
            public void onKeyboardClose() {

            }
        });

    }

    private void setData(DataModel diaryModel) {

        titleET.setText(diaryModel.getTitle());
        descriptionET.setText(diaryModel.getDescription());
    }


    private void init() {

        titleET = findViewById(R.id.title_ET);
        descriptionET = findViewById(R.id.description_ET);
        rootView = findViewById(R.id.root_view);
        saveBTN = (Button) findViewById(R.id.save_BTN);
        imageView = findViewById(R.id.emoji_btn);


        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkValidity()) {

                    title = titleET.getText().toString();
                    description = descriptionET.getText().toString();

                    Calendar cal = Calendar.getInstance();
                    Date chosenDate = cal.getTime();

                    DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
                    date = dateFormat.format(chosenDate);

                    calendar = Calendar.getInstance();

                    SimpleDateFormat sdf24 = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat sdf12 = new SimpleDateFormat("hh:mm a");
                    String strTime = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
                    Date time = null;
                    try {
                        time = sdf24.parse(strTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    times = sdf12.format(time);


                    DataModel model = new DataModel();
                    model.setId(id);
                    model.setTitle(title);
                    model.setDescription(description);
                    model.setDate(date);
                    model.setTime(times);


                    diaryDatabase.editelement(model);
                    if (flag == 1) {

                        startActivity(new Intent(Edit.this, Diary.class));
                        Toast.makeText(Edit.this, "Successfully edited", Toast.LENGTH_SHORT).show();
                        finish();

                    } else {

                        Edit.super.onBackPressed();
                        Toast.makeText(Edit.this, "Successfully edited", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void singoutaccount(MenuItem item) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        startActivity(new Intent(Edit.this, LoginF.class));
        finish();
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
                String app_url = "https://play.google.com/store/apps/details?id=app.cave.diarywithloker";
                shareIntent.putExtra(Intent.EXTRA_TEXT, app_url);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
                break;
            case R.id.rate:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=app.cave.diarywithloker"));
                startActivity(intent);
                break;
            case R.id.review:
                Intent inter = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=app.cave.diarywithloker"));
                startActivity(inter);
                break;
            case R.id.moreapp:
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Cave+of+app"));
                startActivity(i);
                break;
            case R.id.lockID:
                startActivity(new Intent(this, RegistrationF.class));
                finish();
                break;
            case R.id.exit:
                finish();
                System.exit(0);
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private boolean checkValidity() {

        if (titleET.getText().toString().equals("")) {

            titleET.setError("This field is required !!!");
            return false;

        } else if (descriptionET.getText().toString().equals("")) {

            descriptionET.setError("This field is required !!!");
            return false;

        } else {

            return true;
        }
    }

}
