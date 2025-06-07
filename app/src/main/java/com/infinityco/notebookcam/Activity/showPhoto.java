package com.infinityco.notebookcam.Activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Layout;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.infinityco.notebookcam.Adapters.ImageGalleryViewPageAdapter;
import com.infinityco.notebookcam.Object.ClickableViewPager;
import com.infinityco.notebookcam.Object.PhotoList;
import com.infinityco.notebookcam.Object.Subject;
import com.infinityco.notebookcam.R;

import java.util.ArrayList;

import static com.infinityco.notebookcam.Activity.SubjectActivity.getPhotos;

public class showPhoto extends AppCompatActivity {

    //Layout
    private ClickableViewPager vpPhotos;
    private Button back,deletPhoto,editPhoto;
    private TextView info;
    private RelativeLayout infoLayout,buttons;

    //Objects
    private PhotoList photos;
    private int pos;
    private AlertDialog alertDialog; //deleteAlertDialog()


    @TargetApi(19)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (android.os.Build.VERSION.SDK_INT >= 19) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(45);
    }
    @Override
    public void onResume(){
        super.onResume();


        if(!Editing) {
            setImage(pos);
        }
        else {
            Editing = false;
            setUpPhotos();
            setImage(pos);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);

        Init();
        getPosition();
        setUpPhotos();
        setUpClickListener();
    }

    private void getPosition(){
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            pos= 0;
        } else {
            pos =  extras.getInt("Position");
        }
    }
    private void Init(){
        back = (Button) findViewById(R.id.btBack);
        deletPhoto = (Button) findViewById(R.id.btPhotoDelete);
        vpPhotos = (ClickableViewPager) findViewById(R.id.vpPhotos);
        info = (TextView) findViewById(R.id.tv_photo_info);
        infoLayout = (RelativeLayout) findViewById(R.id.infoLayout);
        buttons = (RelativeLayout) findViewById(R.id.rlButtons);
        editPhoto = findViewById(R.id.btPhotoEdit);
        photos = getPhotos();
    }

    private void setUpPhotos(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        vpPhotos.setAdapter(new ImageGalleryViewPageAdapter(this,photos,buttons,infoLayout));

        setUpVpListener();
    }
    private void setImage(int pos){
        vpPhotos.setCurrentItem(pos,false);
    }

    private void setUpVpListener() {
        vpPhotos.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                info.setText(photos.get(position).getInfo());
                pos = position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void DeletePhotoDialog(){
        final AlertDialog.Builder RecDialog = new AlertDialog.Builder(this);

        Typeface tittle_type = MainActivity.getTyperFace();

        TextView dialogTittle = new TextView(this);
        dialogTittle.setText(R.string.DeleteADTittle);
        dialogTittle.setTypeface(tittle_type);
        dialogTittle.setTextColor(Color.WHITE);
        dialogTittle.setPadding(20,10,10,10);
        dialogTittle.setTextSize(25);
        dialogTittle.setBackgroundColor(MainActivity.getAppColor());

        final TextView input = new TextView(this);
        input.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        input.setText(R.string.DeletePhotoADMensage);
        input.setTypeface(tittle_type);

        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout editTextLayout = new LinearLayout(this);
        editTextLayout.setOrientation(LinearLayout.HORIZONTAL);
        editTextLayout.setPadding(30,30,30,30);
        editTextLayout.addView(input);

        dialogLayout.addView(dialogTittle);
        dialogLayout.addView(editTextLayout);

        RecDialog.setView(dialogLayout);


        RecDialog.setPositiveButton(R.string.Delete,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletePhoto();
                        alertDialog.dismiss();
                    }
                });

        RecDialog.setNeutralButton(R.string.Cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });


        alertDialog = RecDialog.create();
        alertDialog.getWindow().setTitleColor(ContextCompat.getColor(this, R.color.folderColor6));
        alertDialog.show();



        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.folderColor6));
    }

    private void deletePhoto(){

        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        slide_up.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {


                if(pos==0){
                    if(photos.size()>1){
                        photos.deletPhoto(pos);
                        setUpPhotos();
                        setImage(0);
                    }
                    else {
                        photos.deletPhoto(pos);
                        finish();
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                        Vibrator vibrator = (Vibrator) showPhoto.this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(45);
                    }
                }
                else {
                    photos.deletPhoto(pos);
                    setUpPhotos();
                    setImage(pos-1);
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });

        vpPhotos.startAnimation(slide_up);

    }

    private boolean Editing = false;
    private void setUpClickListener(){

        deletPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DeletePhotoDialog();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(45);
            }
        });

        editPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(showPhoto.this,EditPhotoActivity.class);
                i.putExtra("Photo", photos.get(pos).getPath());
                startActivity(i);
                Editing = true;
            }
        });
    }
}
