package com.visa.socialshare.lib.managers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.visa.socialshare.lib.R;
import com.visa.socialshare.lib.adapters.CustomChooserAdapter;
import com.visa.socialshare.lib.models.CustomShareItem;
import com.visa.socialshare.lib.utils.ShareProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qxie on 10/11/16.
 */

public class ShareManager {
    private static final String TAG = ShareManager.class.getSimpleName();

    private Context mContext;
    private String title;
    private String content;
    private String subject;
    private Uri uri;
    private List<String> shareProviders;
    private boolean useDefaultDialog;

    public ShareManager() {
        shareProviders = new ArrayList<>();
        shareProviders.add(ShareProvider.FACEBOOK);
        shareProviders.add(ShareProvider.ANDROID_NATIVE_MESSAGE);
        shareProviders.add(ShareProvider.GMAIL);
        shareProviders.add(ShareProvider.GOOGLE_MESSAGE);
        shareProviders.add(ShareProvider.SAMSUNG_EMAIL);
        shareProviders.add(ShareProvider.TWITTER);
        useDefaultDialog = false;
    }

    public ShareManager setContext(Context mContext) {
        this.mContext = mContext;
        return this;
    }

    public ShareManager setTitle(String title) {
        this.title = title;
        return this;
    }

    public ShareManager setContent(String content) {
        this.content = content;
        return this;
    }

    public ShareManager setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public ShareManager setUri(Uri uri) {
        this.uri = uri;
        return this;
    }

    public ShareManager setShareProviders(List<String> shareProviders) {
        this.shareProviders = shareProviders;
        return this;
    }

    public ShareManager useDefaultDialog(boolean b){
        this.useDefaultDialog = b;
        return this;
    }

    public void share() {
        if(!useDefaultDialog) {
            List<CustomShareItem> customShareItems = getChooserForTargetApps();
            showCustomChooserDialog(customShareItems);
        }else{
            List<Intent> shareIntents = getChooserForTargetAppsDefault();
            Intent chooserIntent = Intent.createChooser(shareIntents.get(0),mContext.getString(R.string.share_via));
            shareIntents.remove(0);
            Intent[] extraIntents = shareIntents.toArray(new Intent[shareIntents.size()]);
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,extraIntents);
            mContext.startActivity(chooserIntent);
        }
    }

    private List<Intent> getChooserForTargetAppsDefault(){
        try {
            List<Intent> shareIntents = new ArrayList<>();
            Intent getInfoIntent = new Intent(Intent.ACTION_SEND);
            getInfoIntent.setType(getShareType());
            List<ResolveInfo> resInfoList = mContext.getPackageManager().queryIntentActivities(getInfoIntent, 0);
            if (!resInfoList.isEmpty()) {
                for (ResolveInfo appInfo : resInfoList) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType(getShareType());
                    if (isTargetApp(appInfo.activityInfo.name.toLowerCase(), shareProviders)) {
//                        if (appInfo.activityInfo.name.toLowerCase().equals(Constants.ACTIVITY_SHARE_ITEM_NAME)
//                                || appInfo.activityInfo.name.toLowerCase().equals(Constants.ACTIVITY_SHARE_PHOTO_NAME))
//                            shareIntent.putExtra(Constants.SHARE_ITEM_KEY, shareItem);
                        if(uri!=null)
                            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
//                        if(appInfo.activityInfo.name.toLowerCase().contains(Constants.TWITTER_NAME))
//                            shareIntent.putExtra(Intent.EXTRA_TEXT,"#visario @Visa " + content);
//                        else
                        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                        shareIntent.setPackage(appInfo.activityInfo.packageName);
                        shareIntent.setClassName(appInfo.activityInfo.packageName, appInfo.activityInfo.name);
                        shareIntents.add(shareIntent);
                    }
                }
                return shareIntents;
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private List<CustomShareItem> getChooserForTargetApps(){
        try {
            List<CustomShareItem> customShareItems = new ArrayList<>();
            Intent getInfoIntent = new Intent(Intent.ACTION_SEND);
            getInfoIntent.setType(getShareType());
            List<ResolveInfo> resInfoList = mContext.getPackageManager().queryIntentActivities(getInfoIntent, 0);
            if (!resInfoList.isEmpty()) {
                for (ResolveInfo appInfo : resInfoList) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType(getShareType());
                    if (isTargetApp(appInfo.activityInfo.name.toLowerCase(), shareProviders)) {
//                        if (appInfo.activityInfo.name.toLowerCase().equals(Constants.ACTIVITY_SHARE_ITEM_NAME)
//                                || appInfo.activityInfo.name.toLowerCase().equals(Constants.ACTIVITY_SHARE_PHOTO_NAME))
//                            shareIntent.putExtra(Constants.SHARE_ITEM_KEY, shareItem);
                        if(uri!=null)
                            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
//                        if(appInfo.activityInfo.name.toLowerCase().contains(Constants.TWITTER_NAME))
//                            shareIntent.putExtra(Intent.EXTRA_TEXT,"#visario @Visa " + content);
//                        else
                        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                        shareIntent.setPackage(appInfo.activityInfo.packageName);
                        shareIntent.setClassName(appInfo.activityInfo.packageName, appInfo.activityInfo.name);
                        customShareItems.add(new CustomShareItem(appInfo, shareIntent));
                    }
                }
                return customShareItems;
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private String getShareType(){
        return uri==null? "text/plain" : "image/jpeg";
    }

    private void showCustomChooserDialog(final List<CustomShareItem> shareItems) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        Dialog alertDialog;
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_custom_chooser, null);
        dialogBuilder.setView(dialogView);

        ListView lv_chooser = (ListView) dialogView.findViewById(R.id.layout_custom_chooser_lv);
        LinearLayout mainLayout = (LinearLayout) dialogView.findViewById(R.id.layout_custom_chooser_main_lay);
        ImageView closeImageView = (ImageView) dialogView.findViewById(R.id.layout_custom_chooser_img_close);

        CustomChooserAdapter adapter = new CustomChooserAdapter(mContext, shareItems);
        lv_chooser.setAdapter(adapter);

        alertDialog = dialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams WMLP = alertDialog.getWindow().getAttributes();
        WMLP.gravity = Gravity.CENTER;
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setAttributes(WMLP);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        final Dialog finalAlertDialog = alertDialog;

        lv_chooser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent shareIntent = shareItems.get(position).getShareIntent();
                mContext.startActivity(shareIntent);
                finalAlertDialog.dismiss();
            }
        });

        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalAlertDialog.dismiss();
            }
        });

        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalAlertDialog.dismiss();
            }
        });
    }

    private boolean isTargetApp(String targetAppName, List<String> appNames) {
        for (String appName : appNames) {
            if (targetAppName.contains(appName))
                return true;
        }
        return false;
    }

}
