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
    private SomeObject someObject;
    private RealmChangeListener<RealmResults<SomeObject>> objectRrChangeListener;
    private RealmChangeListener objectChangeListener;

    @BindView(R.id.the_text)
    TextView theTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        realm = Realm.getDefaultInstance();
        someObjectRealmResults = realm.where(SomeObject.class).findAllAsync();
        objectRrChangeListener = new RealmChangeListener<RealmResults<SomeObject>>() {
            @Override
            public void onChange(RealmResults<SomeObject> someObjectRealmResults) {
                // do something upon change
                updateUI(someObjectRealmResults);
            }
        };

        someObject = realm.where(SomeObject.class).equalTo("someText", "ON").findFirstAsync();
        objectChangeListener = new RealmChangeListener<SomeObject>() {

            @Override
            public void onChange(SomeObject element) {
                updateUI2();
            }
        };
    }


    @Override
    protected void onStart() {
        super.onStart();

        // add listeners
        someObjectRealmResults.addChangeListener(objectRrChangeListener);
        someObject.addChangeListener(objectChangeListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // remove all registered listeners
        someObjectRealmResults.removeChangeListeners();
        someObject.removeChangeListeners();
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
        someObject.setSomeText(toggleButton.isChecked() ? "ON!!" : "OFF!!");
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

    private void updateUI2() {
        // at start up, no someObject can be found,
        // use if (someObject!=null) will not work,
        // should use isValid() instead
        if (someObject.isValid())
            theTextView.setText(someObject.getSomeText());
    }
}
