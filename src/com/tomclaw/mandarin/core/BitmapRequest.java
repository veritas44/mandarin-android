package com.tomclaw.mandarin.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.Pair;
import com.tomclaw.mandarin.im.AccountRoot;
import com.tomclaw.mandarin.util.HttpUtil;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: solkin
 * Date: 12/5/13
 * Time: 1:55 PM
 */
public abstract class BitmapRequest<A extends AccountRoot> extends HttpRequest<A> {

    private String url;

    private transient String hash;

    public BitmapRequest() {
    }

    public BitmapRequest(String url) {
        this.url = url;
    }

    @Override
    protected final String getUrl() {
        return url;
    }

    @Override
    protected final List<Pair<String, String>> getParams() {
        return Collections.emptyList();
    }

    @Override
    protected final String getHttpRequestType() {
        hash = HttpUtil.getUrlHash(url);
        return HttpUtil.GET;
    }

    @Override
    protected final int parseResponse(InputStream httpResponseStream) throws Throwable {
        if (parseBitmap(httpResponseStream)) {
            onBitmapSaved(hash);
        }
        // Remove request in any case, except Network errors.
        // In McDonald's case or when avatar if bad, we'll
        // drop current task and try to download second time
        // presence or roster is coming.
        return REQUEST_DELETE;
    }

    private boolean parseBitmap(InputStream inputStream) {
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        // Checking for bitmap can be decoded. McDonald's etc.
        if (bitmap != null) {
            // Yeah, avatar is good.
            Log.d(Settings.LOG_TAG, "Ready to save bitmap for URL: " + url);
            return saveBitmap(bitmap);
        } else {
            Log.d(Settings.LOG_TAG, "Invalid bitmap for URL: " + url);
        }
        return false;
    }

    private boolean saveBitmap(Bitmap bitmap) {
        return BitmapCache.getInstance().saveBitmapSync(hash, bitmap);
    }

    protected abstract void onBitmapSaved(String hash);
}
