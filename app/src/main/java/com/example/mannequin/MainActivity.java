package com.example.mannequin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   //     getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        // inflate the view
        // demonstrates how this could be dynamic


        Toolbar t = (Toolbar) findViewById(R.id.tool);
        Log.d("DEBUG", "toolbar1");
        if (t == null)
            Log.d("DEBUG", "toolbar2");
      // setSupportActionBar(t);
        if (t.equals(getSupportActionBar()))
            Log.d("DEBUG", "toolbar4");
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //left side button


     //   t.addView(b);
        LinearLayout view_group = (LinearLayout) findViewById(R.id.main_region);
        final DrawingView dv = new DrawingView(this.getBaseContext());

        final Context context = this;
        Rect iconBounds = new Rect(0,0,60,60);  // Adjust this for where you want it

        view_group.addView(dv);
        Button b = new Button(this);
        Toolbar.LayoutParams l1=new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        l1.gravity = Gravity.CENTER_HORIZONTAL;
        b.setLayoutParams(l1);
   //     b.setText("left");
        Drawable resetIcon = getResources().getDrawable(R.drawable.reset);
        resetIcon.setBounds(iconBounds);
        b.setCompoundDrawables(null,resetIcon,null,null);


        b.setEnabled(true);
        t.addView(b,0);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dv.initDoll(context);
                Log.d("DEBUG", "toolbar3");
            }
        });
        Button about = new Button(this);
        Toolbar.LayoutParams l2=new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        l2.gravity = Gravity.CENTER_HORIZONTAL;
        about.setLayoutParams(l2);
        Drawable aboutIcon = getResources().getDrawable(R.drawable.about);
        aboutIcon.setBounds(iconBounds);
        about.setCompoundDrawables(null,aboutIcon,null,null);
        about.setEnabled(true);
        t.addView(about,1);
        t.bringToFront();
        about.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Mannequin")
                        .setMessage("Gabriel House\n20622384")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                    //    .setNegativeButton(android.R.string.no, null)
                   //     .setIcon(R.drawable.torso)
                        .show();
            }
        });
    //    view_group.addView(t);
        /*Button reset = new Button(this.getBaseContext());
        reset.setWidth(200);
        reset.setHeight(100);
        reset.setX(0);
        reset.setY(0);
        reset.setVisibility(View.VISIBLE);
        reset.setText("reset");
        reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });
        view_group.addView(reset,1);

         */
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

}
