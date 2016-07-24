package com.example.jackytsang.presentationapi;

import android.annotation.TargetApi;
import android.app.Presentation;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class MyPresentation extends Presentation {

    @BindView(R.id.dynamic_text)
    TextView dynamicText;

    public MyPresentation(Context outerContext, Display display) {
        super(outerContext, display);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary);

        ButterKnife.bind(this);
    }

    public void setDynamicText(String text) {
        dynamicText.setText(text);
    }
}
