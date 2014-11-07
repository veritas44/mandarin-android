package com.tomclaw.mandarin.main;

import com.tomclaw.mandarin.util.HttpUtil;

import java.io.Serializable;

/**
* Created by solkin on 07.11.14.
*/
public class PhotoEntry implements Serializable {
    public int bucketId;
    public int imageId;
    public long dateTaken;
    public String path;
    public int orientation;

    public String hash;

    public PhotoEntry(int bucketId, int imageId, long dateTaken, String path, int orientation) {
        this.bucketId = bucketId;
        this.imageId = imageId;
        this.dateTaken = dateTaken;
        this.path = path;
        this.orientation = orientation;
        this.hash = HttpUtil.getUrlHash(path);
    }
}
