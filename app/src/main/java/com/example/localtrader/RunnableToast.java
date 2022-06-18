package com.example.localtrader;

import android.content.Context;
import android.widget.Toast;

public class RunnableToast implements Runnable {
    private Context ctx;
    private String text;
    private int duration;

    public RunnableToast(Context ctx, int resId, int duration) {
        this.ctx = ctx;
        this.text = ctx.getResources().getString(resId);
        this.duration = duration;
    }

    public RunnableToast(Context ctx, String text, int duration) {
        this.ctx = ctx;
        this.text = text;
        this.duration = duration;
    }

    @Override
    public void run() {
        Toast.makeText(this.ctx, this.text, this.duration).show();
    }
}
