package com.infinityco.notebookcam.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.infinityco.notebookcam.Adapters.ColorsAdapter;
import com.infinityco.notebookcam.Adapters.FoldersAdapter;
import com.infinityco.notebookcam.Adapters.SubjectAdapter;
import com.infinityco.notebookcam.Object.Folder;
import com.infinityco.notebookcam.Object.Subject;
import com.infinityco.notebookcam.Object.SubjectList;
import com.infinityco.notebookcam.R;

import java.util.Timer;
import java.util.TimerTask;

public class FolderActivity extends AppCompatActivity implements View.OnClickListener{

    //Layout
    private RecyclerView rvSubjects;
    private Button tvFolderName,btFolderBack,btNewSubject;
    private Typeface tittle_type;
    private ImageView folderColor;
    private CardView newSubject;

    //Objects
    private SubjectList subjects;
    private AlertDialog alertDialog; //createFolderAlertDialog()
    private InputMethodManager imm;  //createFolderAlertDialog()
    private int color;  //createFolderAlertDialog()
    private String FolderName;
    private Folder folder;



    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(45);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        getFolder();
        Init();
        SetUpTypeFace();
        SetUp();
    }

    private void getFolder(){
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            FolderName= null;
        } else {
            FolderName= extras.getString("Folder");
        }
    }
    private void Init(){
        //Layout
        rvSubjects = (RecyclerView) findViewById(R.id.rvSubjects);
        btFolderBack = (Button) findViewById(R.id.btFolderBack);
        btNewSubject = (Button) findViewById(R.id.btNewSubject);
        tvFolderName = (Button) findViewById(R.id.tv_folder_name);
        folderColor = (ImageView) findViewById(R.id.iv_folder_color);
        newSubject = (CardView) findViewById(R.id.card_view_new_subject);

        //Objects
        folder = new Folder(this,FolderName);
        subjects= new SubjectList(this,FolderName);
    }

    private void SetUpTypeFace(){
        Typeface tittle_type_bold = MainActivity.getTyperFaceBold();
        tittle_type = MainActivity.getTyperFace();

        tvFolderName.setTypeface(tittle_type_bold);
        tvFolderName.setText(FolderName);
    }

    private void SetUp(){

        switch (folder.getColor()){
            case 1:
                color = getResources().getColor(R.color.folderColor1);
                folderColor.setBackgroundColor(getResources().getColor(R.color.folderColor1));
                break;
            case 2:
                color = getResources().getColor(R.color.folderColor2);
                folderColor.setBackgroundColor(getResources().getColor(R.color.folderColor2));
                break;
            case 3:
                color = getResources().getColor(R.color.folderColor3);
                folderColor.setBackgroundColor(getResources().getColor(R.color.folderColor3));
                break;
            case 4:
                color = getResources().getColor(R.color.folderColor4);
                folderColor.setBackgroundColor(getResources().getColor(R.color.folderColor4));
                break;
            case 5:
                color = getResources().getColor(R.color.folderColor5);
                folderColor.setBackgroundColor(getResources().getColor(R.color.folderColor5));
                break;
            case 6:
                color = getResources().getColor(R.color.folderColor6);
                folderColor.setBackgroundColor(getResources().getColor(R.color.folderColor6));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }

        newSubject.setCardBackgroundColor(color);

        LoadSubjects(false);


        btFolderBack.setOnClickListener(this);
        tvFolderName.setOnClickListener(this);
        btNewSubject.setOnClickListener(this);
    }

    private void LoadSubjects(Boolean fromDelete){
        subjects.LoadSubjects();
        if(!subjects.isEmpty() || fromDelete) {
            rvSubjects.setHasFixedSize(true);
            rvSubjects.setLayoutManager(new LinearLayoutManager(this));
            rvSubjects.setAdapter(new SubjectAdapter(this, subjects,color,tittle_type));
            if(!fromDelete) {
                setClickListner();
            }
        }
    }

    private void setClickListner(){
        if(!subjects.isEmpty()) {
            ((SubjectAdapter) rvSubjects.getAdapter()).setOnItemClickListener(new SubjectAdapter.MyClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    Intent i = new Intent(FolderActivity.this, SubjectActivity.class);
                    i.putExtra("Folder", FolderName);
                    i.putExtra("Subject",subjects.get(position).getSubjectName());
                    overridePendingTransition(R.anim.right_to_left_open, R.anim.left_to_right_open);
                    startActivity(i);
                    overridePendingTransition(R.anim.right_to_left_open, R.anim.left_to_right_open);
                }
                @Override
                public void onLongClick(int position, View v){
                    EditSubjectAlertDialog(subjects.get(position));
                }
            });
        }
    }

    private void createSubjectAlertDialog(){
        final AlertDialog.Builder RecDialog = new AlertDialog.Builder(this);

        final EditText input = new EditText(this);
        input.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        input.setHint(R.string.NewSubjectAlertDialogHint);
        input.setTypeface(tittle_type);

        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if(!Character.isLetterOrDigit(c)){
                        if(String.valueOf(c).hashCode()!=32) {
                            return "";
                        }
                    }
                }
                return null;
            }
        };
        input.setFilters(new InputFilter[] { filter });

        TextView dialogTittle = new TextView(this);
        dialogTittle.setText(R.string.NewSubjectAlertDialog);
        dialogTittle.setTypeface(tittle_type);
        dialogTittle.setTextColor(Color.WHITE);
        dialogTittle.setPadding(20,10,10,10);
        dialogTittle.setTextSize(25);
        dialogTittle.setBackgroundColor(MainActivity.getAppColor());




        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout editTextLayout = new LinearLayout(this);
        editTextLayout.setOrientation(LinearLayout.HORIZONTAL);
        editTextLayout.setPadding(30,30,30,30);
        editTextLayout.addView(input);

        dialogLayout.addView(dialogTittle);
        dialogLayout.addView(editTextLayout);

        RecDialog.setView(dialogLayout);

        RecDialog.setPositiveButton(R.string.Create,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Subject newSubject = new Subject(FolderActivity.this,input.getText().toString(),FolderName);
                        newSubject.saveSubject();
                        LoadSubjects(false);
                        setClickListner();
                    }
                });

        RecDialog.setNegativeButton(R.string.Cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


        alertDialog = RecDialog.create();
        alertDialog.getWindow().setTitleColor(MainActivity.getAppColor());
        alertDialog.show();


        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(MainActivity.getAppColor());


    }
    private void EditSubjectAlertDialog(final Subject subject){
        final AlertDialog.Builder RecDialog = new AlertDialog.Builder(this);

        final EditText input = new EditText(this);
        input.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        input.setText(subject.getSubjectName());
        input.setTypeface(tittle_type);
        input.setFocusable(true);

        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if(!Character.isLetterOrDigit(c)){
                        if(String.valueOf(c).hashCode()!=32) {
                            return "";
                        }
                    }
                }
                return null;
            }
        };
        input.setFilters(new InputFilter[] { filter });

        TextView dialogTittle = new TextView(this);
        dialogTittle.setText(R.string.EditSubjectAlertDialog);
        dialogTittle.setTypeface(tittle_type);
        dialogTittle.setTextColor(Color.WHITE);
        dialogTittle.setPadding(20,10,10,10);
        dialogTittle.setTextSize(25);
        dialogTittle.setBackgroundColor(MainActivity.getAppColor());




        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout editTextLayout = new LinearLayout(this);
        editTextLayout.setOrientation(LinearLayout.HORIZONTAL);
        editTextLayout.setPadding(30,30,30,30);
        editTextLayout.addView(input);

        dialogLayout.addView(dialogTittle);
        dialogLayout.addView(editTextLayout);

        RecDialog.setView(dialogLayout);

        RecDialog.setPositiveButton(R.string.Edit,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        subject.EditSubject(input.getText().toString());
                        LoadSubjects(false);
                        setClickListner();
                    }
                });

        RecDialog.setNeutralButton(R.string.Cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        RecDialog.setNegativeButton(R.string.Delete,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });


        alertDialog = RecDialog.create();
        alertDialog.getWindow().setTitleColor(MainActivity.getAppColor());
        alertDialog.show();


        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);

        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color=0;
                alertDialog.dismiss();
            }
        });

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteSubjectDialog(subject,alertDialog);
            }
        });

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.folderColor6));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(MainActivity.getAppColor());

    }
    private void DeleteSubjectDialog(final Subject subject, final AlertDialog alertDialogMother){
        final AlertDialog.Builder RecDialog = new AlertDialog.Builder(this);

        TextView dialogTittle = new TextView(this);
        dialogTittle.setText(R.string.DeleteADTittle);
        dialogTittle.setTypeface(tittle_type);
        dialogTittle.setTextColor(Color.WHITE);
        dialogTittle.setPadding(20,10,10,10);
        dialogTittle.setTextSize(25);
        dialogTittle.setBackgroundColor(MainActivity.getAppColor());

        final TextView input = new TextView(this);
        input.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        input.setText(R.string.DeleteSubjectADMensage);
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
                        subject.delete();
                        alertDialogMother.dismiss();
                        alertDialog.dismiss();
                        LoadSubjects(true);
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


        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);

        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.folderColor6));
    }


    @Override
    public void onClick(View v){
        Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        switch (v.getId()){
            case R.id.btFolderBack:
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                vibrator.vibrate(45);
                break;
            case R.id.tv_folder_name:
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                vibrator.vibrate(45);
                break;
            case R.id.btNewSubject:
                createSubjectAlertDialog();
                break;

        }
    }
}
