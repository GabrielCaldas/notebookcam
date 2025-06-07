package com.infinityco.notebookcam.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.infinityco.notebookcam.Adapters.ColorsAdapter;
import com.infinityco.notebookcam.Adapters.FoldersAdapter;
import com.infinityco.notebookcam.Adapters.MenuAdapter;
import com.infinityco.notebookcam.Object.Folder;
import com.infinityco.notebookcam.Object.FolderList;
import com.infinityco.notebookcam.Object.PhotoAlbum;
import com.infinityco.notebookcam.Object.PhotoList;
import com.infinityco.notebookcam.Object.Settings_values;
import com.infinityco.notebookcam.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    //Layout
    private static Typeface tittle_type,tittle_type_bold;
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView rvFolders,rvMenu;
    private TextView tvNoFoldersMensage,tvAppName,tvMenu;
    private Button btNewFoto,btMenu;
    private DrawerLayout drawer;
    private ImageView ivTopColor;
    private CardView cvNewFolder;

    //Objects
    public static boolean reloadUI = false;
    private static int appColor;
    private FolderList folders;
    private AlertDialog alertDialog; //createFolderAlertDialog()
    private InputMethodManager imm;  //createFolderAlertDialog()
    private int color;  //createFolderAlertDialog()
    private RecyclerView rv; //createFolderAlertDialog()
    private boolean delet=false;



    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(45);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(reloadUI){
            startActivity(new Intent(this,Launcher.class));
            Toast.makeText(this, R.string.ReloadUIMensage, Toast.LENGTH_SHORT).show();
            finish();
            reloadUI = false;
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(isFirst()) {
            welcomeDialog();
        }
        Init();
        SetUpUIValues();
        SetUp();
    }

    private void Init(){
        //Layout
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.mainActivityCoordinatorLayout);
        rvFolders = (RecyclerView) findViewById(R.id.rvFolder);
        rvMenu = (RecyclerView) findViewById(R.id.rvMenu);
        btNewFoto = (Button) findViewById(R.id.btNewFolder);
        btMenu = (Button) findViewById(R.id.bt_menu);
        tvNoFoldersMensage = (TextView) findViewById(R.id.tvNoFoldersMensage);
        tvAppName = (TextView) findViewById(R.id.tv_app_name);
        tvMenu = (TextView) findViewById(R.id.tvMenu);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ivTopColor = (ImageView) findViewById(R.id.ivTopBarColor);
        cvNewFolder = (CardView) findViewById(R.id.cvNewFolder);
        //Objects
        folders = new FolderList(this);
    }

    private void SetUpUIValues(){
        Settings_values settings_values = new Settings_values(this);

        //Typeface
        int typefaceValue;
        try {
            typefaceValue = settings_values.getTypeFace();
        }
        catch (Exception e){
            typefaceValue = 1;
        }

        if(typefaceValue==1) {
            tittle_type_bold = Typeface.createFromAsset(getAssets(), "tittle_font_bold.ttf");
            tittle_type = Typeface.createFromAsset(getAssets(), "tittle_font.ttf");
        }
        else{
            tittle_type_bold = Typeface.createFromAsset(getAssets(), "DroidSans-Bold.ttf");
            tittle_type = Typeface.createFromAsset(getAssets(), "DroidSans.ttf");
        }

        tvAppName.setTypeface(tittle_type_bold);
        tvNoFoldersMensage.setTypeface(tittle_type);
        tvMenu.setTypeface(tittle_type_bold);

        //Color
        try{
            switch (settings_values.getAppColor()) {
                case 1:
                    appColor = getResources().getColor(R.color.folderColor1);
                    break;
                case 2:
                    appColor = getResources().getColor(R.color.folderColor2);
                    break;
                case 3:
                    appColor = getResources().getColor(R.color.folderColor3);
                    break;
                case 4:
                    appColor = getResources().getColor(R.color.folderColor4);
                    break;
                case 5:
                    appColor = getResources().getColor(R.color.folderColor5);
                    break;
                case 6:
                    appColor = getResources().getColor(R.color.folderColor6);
                    break;
            }
        }
        catch (Exception e){
            appColor = getResources().getColor(R.color.folderColor1);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(appColor);
        }
        ivTopColor.setBackgroundColor(appColor);
        cvNewFolder.setCardBackgroundColor(appColor);
    }

    private void SetUp(){

        rvMenu.setHasFixedSize(true);
        rvMenu.setLayoutManager(new LinearLayoutManager(this));
        rvMenu.setAdapter(new MenuAdapter(tittle_type));


        LoadFolders();

        if(folders.isEmpty()) {
            //Colocar Imagem
            tvNoFoldersMensage.setText(getString(R.string.NoFoldersMensage));
        }
        btNewFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFolderAlertDialog();
            }
        });

    }

    private void LoadFolders(){
        folders.LoadFolders();
        if(!folders.isEmpty()||delet) {
            tvNoFoldersMensage.setText("");
            rvFolders.setHasFixedSize(true);
            rvFolders.setLayoutManager(new LinearLayoutManager(this));
            rvFolders.setAdapter(new FoldersAdapter(this, folders,tittle_type));
            delet=false;
        }
        else {
            Settings_values isFirst = new Settings_values(this);
            if(isFirst.isFirstTime()) {
                WelcomeDialog();
                isFirst.setFirsTime();
            }
        }
        setClickListner();
    }

    private void setClickListner(){
        if(!folders.isEmpty()) {
            ((FoldersAdapter) rvFolders.getAdapter()).setOnItemClickListener(new FoldersAdapter.MyClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    Intent i = new Intent(MainActivity.this, FolderActivity.class);
                    i.putExtra("Folder", folders.get(position).getFolderName());

                    overridePendingTransition(R.anim.right_to_left_open, R.anim.left_to_right_open);
                    startActivity(i);
                    overridePendingTransition(R.anim.right_to_left_open, R.anim.left_to_right_open);
                }

                @Override
                public void onLongClick(int position, View v){
                    EditFolderAlertDialog(folders.get(position));
                }
            });
        }
        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer != null) {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        ((MenuAdapter) rvMenu.getAdapter()).setOnItemClickListener(new MenuAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                drawer.closeDrawer(GravityCompat.START);
                switch (position){
                    case 0:
                        Intent i = new Intent(MainActivity.this, OptionsActivity.class);

                        overridePendingTransition(R.anim.right_to_left_open, R.anim.left_to_right_open);
                        startActivity(i);
                        overridePendingTransition(R.anim.right_to_left_open, R.anim.left_to_right_open);
                        break;
                    case 1:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.Youtube))));
                        break;
                    case 2:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=7608743603828072629")));
                        break;

                }
            }
        });

    }

    private void createFolderAlertDialog(){
        final AlertDialog.Builder RecDialog = new AlertDialog.Builder(this);

        final EditText input = new EditText(this);
        input.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        input.setHint(R.string.NewFolderAlertDialogHint);
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
        dialogTittle.setText(R.string.NewFolderAlertDialog);
        dialogTittle.setTypeface(tittle_type);
        dialogTittle.setTextColor(Color.WHITE);
        dialogTittle.setPadding(20,10,10,10);
        dialogTittle.setTextSize(25);
        dialogTittle.setBackgroundColor(appColor);




        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout editTextLayout = new LinearLayout(this);
        editTextLayout.setOrientation(LinearLayout.HORIZONTAL);
        editTextLayout.setPadding(30,30,30,30);
        editTextLayout.addView(input);

        dialogLayout.addView(dialogTittle);
        dialogLayout.addView(editTextLayout);

        LinearLayout rvLayout = new LinearLayout(this);
        rvLayout.setOrientation(LinearLayout.HORIZONTAL);
        rvLayout.setGravity(Gravity.CENTER);

        rv = new RecyclerView(this);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ColorsAdapter colorsAdapter = new ColorsAdapter(this);
        rv.setAdapter(colorsAdapter);

        color = colorsAdapter.getColor();

        rvLayout.addView(rv);

        TextView selectColor = new TextView(this);
        selectColor.setTypeface(tittle_type);
        selectColor.setText(R.string.NewFolderSelectColor);
        selectColor.setPadding(33,0,0,0);

        dialogLayout.addView(selectColor);
        dialogLayout.addView(rvLayout);
        RecDialog.setView(dialogLayout);

        color = 1;

        ((ColorsAdapter) rv.getAdapter()).setOnItemClickListener(new ColorsAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                color = position +1;
                rv.getAdapter().notifyDataSetChanged();
            }
        });

        RecDialog.setPositiveButton(R.string.Create,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Folder newfolder = new Folder(MainActivity.this,input.getText().toString(),color);
                        newfolder.saveFolder();
                        LoadFolders();
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
        alertDialog.getWindow().setTitleColor(appColor);
        alertDialog.show();


        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color=0;
                alertDialog.dismiss();
            }
        });

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(appColor);

    }
    private void EditFolderAlertDialog(final Folder folder){
        final AlertDialog.Builder RecDialog = new AlertDialog.Builder(this);

        final EditText input = new EditText(this);
        input.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        input.setText(folder.getFolderName());
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
        dialogTittle.setText(R.string.EditFolderAlertDialog);
        dialogTittle.setTypeface(tittle_type);
        dialogTittle.setTextColor(Color.WHITE);
        dialogTittle.setPadding(20,10,10,10);
        dialogTittle.setTextSize(25);
        dialogTittle.setBackgroundColor(appColor);




        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout editTextLayout = new LinearLayout(this);
        editTextLayout.setOrientation(LinearLayout.HORIZONTAL);
        editTextLayout.setPadding(30,30,30,30);
        editTextLayout.addView(input);

        dialogLayout.addView(dialogTittle);
        dialogLayout.addView(editTextLayout);

        LinearLayout rvLayout = new LinearLayout(this);
        rvLayout.setOrientation(LinearLayout.HORIZONTAL);
        rvLayout.setGravity(Gravity.CENTER);

        rv = new RecyclerView(this);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ColorsAdapter colorsAdapter = new ColorsAdapter(this,folder.getColor()-1);
        rv.setAdapter(colorsAdapter);

        color = folder.getColor();

        rvLayout.addView(rv);

        TextView selectColor = new TextView(this);
        selectColor.setTypeface(tittle_type);
        selectColor.setText(R.string.NewFolderSelectColor);
        selectColor.setPadding(33,0,0,0);

        dialogLayout.addView(selectColor);
        dialogLayout.addView(rvLayout);
        RecDialog.setView(dialogLayout);

        color = 1;

        ((ColorsAdapter) rv.getAdapter()).setOnItemClickListener(new ColorsAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                color = position +1;
                rv.getAdapter().notifyDataSetChanged();
            }
        });

        RecDialog.setPositiveButton(R.string.Edit,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        folder.EditFolder(input.getText().toString(),color);
                        LoadFolders();
                        setClickListner();
                    }
                });

        RecDialog.setNeutralButton(R.string.Cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

        RecDialog.setNegativeButton(R.string.Delete,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });


        alertDialog = RecDialog.create();
        alertDialog.getWindow().setTitleColor(appColor);
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
                DeleteFolderDialog(folder,alertDialog);
            }
        });

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.folderColor6));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(appColor);

    }
    private void DeleteFolderDialog(final Folder folder, final AlertDialog alertDialogMother){
        final AlertDialog.Builder RecDialog = new AlertDialog.Builder(this);

        TextView dialogTittle = new TextView(this);
        dialogTittle.setText(R.string.DeleteADTittle);
        dialogTittle.setTypeface(tittle_type);
        dialogTittle.setTextColor(Color.WHITE);
        dialogTittle.setPadding(20,10,10,10);
        dialogTittle.setTextSize(25);
        dialogTittle.setBackgroundColor(appColor);

        final TextView input = new TextView(this);
        input.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        input.setText(R.string.DeleteFolderADMensage);
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
                        delet=true;
                        folder.delete();
                        color=0;
                        alertDialogMother.dismiss();
                        alertDialog.dismiss();
                        LoadFolders();
                        setClickListner();
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
    private void WelcomeDialog(){
        final AlertDialog.Builder RecDialog = new AlertDialog.Builder(this);

        Typeface tittle_type = Typeface.createFromAsset(getAssets(),"tittle_font.ttf");

        TextView dialogTittle = new TextView(this);
        dialogTittle.setText(R.string.WelcomeTittle);
        dialogTittle.setTypeface(tittle_type);
        dialogTittle.setTextColor(Color.WHITE);
        dialogTittle.setPadding(20,10,10,10);
        dialogTittle.setTextSize(25);
        dialogTittle.setBackgroundColor(appColor);

        final TextView input = new TextView(this);
        input.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        input.setText(R.string.WelcomeMensage);
        input.setTypeface(tittle_type);

        final TextView tip = new TextView(this);
        tip.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        tip.setPadding(0,10,0,0);
        tip.setText(R.string.WelcomeTip);
        tip.setTypeface(tittle_type);

        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout editTextLayout = new LinearLayout(this);
        editTextLayout.setOrientation(LinearLayout.VERTICAL);
        editTextLayout.setPadding(30,30,30,30);
        editTextLayout.addView(input);
        editTextLayout.addView(tip);

        dialogLayout.addView(dialogTittle);
        dialogLayout.addView(editTextLayout);

        RecDialog.setView(dialogLayout);

        RecDialog.setNeutralButton(R.string.Tutorial,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.Youtube))));
                    }
                });

        RecDialog.setPositiveButton(R.string.Ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });



        alertDialog = RecDialog.create();
        alertDialog.getWindow().setTitleColor(ContextCompat.getColor(this, R.color.folderColor6));
        alertDialog.show();


        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(ContextCompat.getColor(this, R.color.folderColor6));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(appColor);
    }

    public static int getAppColor(){
        return appColor;
    }

    public static Typeface getTyperFace(){
        return tittle_type;
    }
    public static Typeface getTyperFaceBold(){
        return tittle_type_bold;
    }


    private boolean isFirst() {
        try {
            File file = getFilesDir();
            File mainFile = new File(file + "/NotebookCam.config");

            if(mainFile.exists()) {
                FileInputStream input = openFileInput("NotebookCam.config");
                byte[] buffer = new byte[(int) mainFile.length()];

                input.read(buffer);
                String result = new String(buffer);
                if (result.equals(getPackageManager().getPackageInfo(getPackageName(), 0).versionName)) {
                    return false;
                }
                else {
                    setFirst();
                    return true;
                }
            }
            else {
                setFirst();
                return true;
            }
        }
        catch (Exception e){
            return false;
        }
    }
    private boolean setFirst(){

        try {
            FileOutputStream out = openFileOutput("NotebookCam.config", Context.MODE_PRIVATE);
            out.write(getPackageManager().getPackageInfo(getPackageName(), 0).versionName.getBytes());
            out.flush();
            out.close();
            return true;

        } catch (Exception e) {
            return false;
        }
    }
    private void welcomeDialog() {
        final AlertDialog.Builder RecDialog = new AlertDialog.Builder(this);

        //Typeface tittle_type = MainActivity.getTyperFace();

        TextView dialogTittle = new TextView(this);
        dialogTittle.setText(R.string.WelcomeTittle);
        //dialogTittle.setTypeface(tittle_type);
        dialogTittle.setTextColor(Color.WHITE);
        dialogTittle.setPadding(20, 10, 10, 10);
        dialogTittle.setTextSize(25);
        dialogTittle.setBackgroundResource(R.color.folderColor2);


        final TextView Message = new TextView(this);
        Message.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        Message.setText(getString(R.string.WelcomeMensage));
        Message.setPadding(30,10,30,0);
        //Directory.setTypeface(tittle_type);


        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        dialogLayout.addView(dialogTittle);
        dialogLayout.addView(Message);


        RecDialog.setView(dialogLayout);


        RecDialog.setPositiveButton(getString(R.string.Ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });


        alertDialog = RecDialog.create();
        alertDialog.show();


        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);


        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTransformationMethod(null);
    }
}
