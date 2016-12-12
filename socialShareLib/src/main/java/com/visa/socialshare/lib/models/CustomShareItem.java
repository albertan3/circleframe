package com.visa.socialshare.lib.models;

import android.content.Intent;
import android.content.pm.ResolveInfo;

/**
 * Created by qxie on 10/11/16.
 */

public class CustomShareItem {
    private ResolveInfo resolveInfo;
    private Intent shareIntent;

    public CustomShareItem(ResolveInfo resolveInfo, Intent shareIntent){
        this.resolveInfo = resolveInfo;
        this.shareIntent = shareIntent;
    }

    public ResolveInfo getResolveInfo() {
        return resolveInfo;
    }

    public Intent getShareIntent() {
        return shareIntent;
    }

    public void setResolveInfo(ResolveInfo resolveInfo) {
        this.resolveInfo = resolveInfo;
    }

    public void setShareIntent(Intent shareIntent) {
        this.shareIntent = shareIntent;
    }
}
