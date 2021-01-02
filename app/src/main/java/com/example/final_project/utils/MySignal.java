package com.example.final_project.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.Toast;

public class MySignal {
    private static MySignal instance;
    private Context appContext;
    private MediaPlayer mp;
    private Toast toast;

    public static MySignal getInstance() {
        return instance;
    }

    private MySignal(Context appContext) {
        this.appContext = appContext.getApplicationContext();
    }

    public static void init(Context appContext) {
        if (instance == null) {
            instance = new MySignal(appContext);
        }
    }

    public void playSound(int rawSound) {
        if (!MySP.getInstance().getBoolean(MySP.KEYS.SOUND_ENABLE, true)) {
            return;
        }

        if (mp != null) {
            try {
                mp.reset();
                mp.release();
            } catch (Exception ex) {
            }
        }
        mp = null;

        try {
            mp = MediaPlayer.create(appContext, rawSound);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    mp.release();
                }
            });

            mp.start();
        } catch (Exception ex) {
        }
    }

    public void toast(String message) {
        if (toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(appContext, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
