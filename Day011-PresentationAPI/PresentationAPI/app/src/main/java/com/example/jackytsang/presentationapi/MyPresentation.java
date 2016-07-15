package com.example.jackytsang.presentationapi;

import android.app.Presentation;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;

public class MyPresentation extends Presentation {
    public MyPresentation(Context outerContext, Display display) {
        super(outerContext, display);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_secondary);
    }
}
