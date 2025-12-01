package com.jwplayer.rnjwplayer;

import android.annotation.TargetApi;
import android.os.Parcel;
import android.text.TextUtils;
import android.util.Log;

import androidx.media3.exoplayer.drm.ExoMediaDrm;
import com.jwplayer.pub.api.media.drm.MediaDrmCallback;

import java.io.IOException;
import java.util.UUID;

@TargetApi(18)
public class WidevineCallback implements MediaDrmCallback {

    private static final String TAG = "WidevineCallback";
    private final String defaultUri;

    public WidevineCallback(String drmAuthUrl) {
        defaultUri = drmAuthUrl;
        Log.d(TAG, "🔐 WidevineCallback created with URL: " + drmAuthUrl);
    }

    protected WidevineCallback(Parcel in) {
        defaultUri = in.readString();
    }

    public static final Creator<WidevineCallback> CREATOR = new Creator<WidevineCallback>() {
        @Override
        public WidevineCallback createFromParcel(Parcel in) {
            return new WidevineCallback(in);
        }

        @Override
        public WidevineCallback[] newArray(int size) {
            return new WidevineCallback[size];
        }
    };

    @Override
    public byte[] executeProvisionRequest(UUID uuid, ExoMediaDrm.ProvisionRequest request) throws IOException {
        String url = request.getDefaultUrl() + "&signedRequest=" + new String(request.getData());
        Log.d(TAG, "🔐 executeProvisionRequest - URL: " + url);
        try {
            byte[] response = Util.executePost(url, null, null);
            Log.d(TAG, "🔐 executeProvisionRequest - SUCCESS, response size: " + (response != null ? response.length : 0));
            return response;
        } catch (IOException e) {
            Log.e(TAG, "🔐 executeProvisionRequest - ERROR: " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public byte[] executeKeyRequest(UUID uuid, ExoMediaDrm.KeyRequest request) throws IOException {
        String url = request.getLicenseServerUrl();
        if (TextUtils.isEmpty(url)) {
            url = defaultUri;
            Log.d(TAG, "🔐 executeKeyRequest - Using default URI: " + url);
        } else {
            Log.d(TAG, "🔐 executeKeyRequest - Using manifest URL: " + url);
        }
        
        Log.d(TAG, "🔐 executeKeyRequest - Request data size: " + (request.getData() != null ? request.getData().length : 0));
        
        try {
            byte[] response = Util.executePost(url, request.getData(), null);
            Log.d(TAG, "🔐 executeKeyRequest - SUCCESS, response size: " + (response != null ? response.length : 0));
            return response;
        } catch (IOException e) {
            Log.e(TAG, "🔐 executeKeyRequest - ERROR: " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(defaultUri);
    }
}