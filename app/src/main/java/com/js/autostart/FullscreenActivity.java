package com.js.autostart;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Toast;

import com.jit.autostart.util.MobileInfoUtils;

import xyz.kumaraswamy.autostart.Autostart;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);



        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
    }


    public void gotoSelfStartingManager(View view) throws Exception {
        Autostart autostart = new Autostart(this);

        Autostart.State state = autostart.getAutoStartState();

        if (state == Autostart.State.DISABLED) {
            // now we are sure that autostart is disabled
            // ask user to enable it manually in the settings app
        } else if (state == Autostart.State.ENABLED) {
            // now we are also sure that autostart is enabled
        }

        Toast.makeText(this,state+"",Toast.LENGTH_LONG).show();

        jumpStartInterface();
    }

    /**
     * Jump Start Interface
     * 提示是否跳转设置自启动界面
     */
    private void jumpStartInterface() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.app_user_auto_start);
            builder.setPositiveButton("立即设置",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MobileInfoUtils.jumpStartInterface(FullscreenActivity.this);
                        }
                    });
            builder.setNegativeButton("暂时不设置",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.setCancelable(false);
            builder.create().show();
        } catch (Exception e) {
        }
    }
}