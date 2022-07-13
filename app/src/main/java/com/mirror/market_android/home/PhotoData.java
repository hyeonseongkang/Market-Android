package com.mirror.market_android.home;

import android.net.Uri;

public class PhotoData {

    private Uri photoUri;

    public PhotoData(){}
    public PhotoData(Uri uri) {
        this.photoUri = uri;
    }

    public Uri getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }
}
