package com.jackytsang.realmchangelistener.realm;

import io.realm.RealmObject;


public class SomeObject extends RealmObject {

    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
