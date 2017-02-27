package com.jackytsang.realmchangelistener;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.jackytsang.realmchangelistener.realm.SomeObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private Realm realm;
    private RealmResults<SomeObject> someObjectRealmResults;
    private RealmChangeListener<RealmResults<SomeObject>> realmChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        realm = Realm.getDefaultInstance();
        someObjectRealmResults = realm.where(SomeObject.class).findAllAsync();
        realmChangeListener = new RealmChangeListener<RealmResults<SomeObject>>() {
            @Override
            public void onChange(RealmResults<SomeObject> upOauths) {
                // do something upon change
                updateUI(someObjectRealmResults);
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

    @OnClick(R.id.toggle)
    public void toggleText(ToggleButton toggleButton) {
        final SomeObject someObject = new SomeObject();
        someObject.setSomeText(toggleButton.isChecked() ? "ON" : "OFF");
        RealmResults<SomeObject> rr = realm.where(SomeObject.class).findAll();
        Log.e("tag", rr.toString());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(someObject);
            }
        });
        Log.e("tag", rr.toString());
    }

    private void updateUI(RealmResults<SomeObject> someObjectRealmResults) {
        // When a query does not have any matches,
        // the returned RealmResults object will not be null,
        // but the size() method will return 0.
        if (someObjectRealmResults.size() > 0) {
            SomeObject theObject = someObjectRealmResults.get(0);
            theTextView.setText(theObject.getSomeText());
        }
    }
}
