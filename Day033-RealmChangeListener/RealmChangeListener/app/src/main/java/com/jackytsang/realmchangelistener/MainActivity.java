package com.jackytsang.realmchangelistener;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jackytsang.realmchangelistener.realm.SomeObject;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private Realm realm;
    private RealmResults<SomeObject> someObjectRealmResults;
    private RealmChangeListener realmChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm = Realm.getDefaultInstance();
        someObjectRealmResults = realm.where(SomeObject.class).findAllAsync();
        realmChangeListener = new RealmChangeListener<RealmResults<SomeObject>>() {
            @Override
            public void onChange(RealmResults<SomeObject> upOauths) {
                // do something upon change
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        // add listeners
        someObjectRealmResults.addChangeListener(realmChangeListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // remove all registered listeners
        someObjectRealmResults.removeChangeListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Remove all listeners
        realm.removeAllChangeListeners();
        // Close the Realm instance
        realm.close();
    }
}
