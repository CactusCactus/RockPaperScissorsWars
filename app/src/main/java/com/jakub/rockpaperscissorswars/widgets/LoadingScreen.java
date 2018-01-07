package com.jakub.rockpaperscissorswars.widgets;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jakub.rockpaperscissorswars.R;

/**
 * Created by Emil on 2018-01-07.
 */

public class LoadingScreen extends RelativeLayout {
    Context context;
    public LoadingScreen(Context context) {
        super(context);
        this.context = context;
        init();
    }
    public static LoadingScreen create(Context context) {
        return new LoadingScreen(context);
    }
    private void init() {
        inflate(context, R.layout.loading_layout, this);
    }
}
