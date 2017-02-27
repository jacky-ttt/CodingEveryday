package com.jackytsang.realmchangelistener.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class SomeObject extends RealmObject {

    @PrimaryKey
    private int index; // if not set, it is default 0
    private String someText;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getSomeText() {
        return someText;
    }

    public void setSomeText(String someText) {
        this.someText = someText;
    }
}
