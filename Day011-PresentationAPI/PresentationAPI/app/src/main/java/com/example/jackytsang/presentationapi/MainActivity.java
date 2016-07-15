package com.example.jackytsang.presentationapi;

import android.annotation.TargetApi;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

import java.lang.annotation.Target;

public class MainActivity extends AppCompatActivity {

    MyPresentation mPresentation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            multiInit();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void multiInit() {
        DisplayManager dm = (DisplayManager) getSystemService(DISPLAY_SERVICE);
        if (dm == null)
            return;

        Display[] displays = dm.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);

        for (Display display : displays) {
            mPresentation = new MyPresentation(this, display);
            mPresentation.show();
        }
    }

}
