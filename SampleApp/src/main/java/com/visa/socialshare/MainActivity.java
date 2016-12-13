package com.visa.socialshare;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.visa.socialshare.lib.managers.ShareManager;
import com.visa.socialshare.lib.utils.FileManager;
import com.visa.socialshare.lib.utils.ShareProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_STORAGE = 1111;

    @BindView(R.id.tv_content_share)
    TextView tv_content_share;
    @BindView(R.id.tv_subject_share)
    TextView tv_subject_share;
    @BindView(R.id.tv_title_share)
    TextView tv_title_share;
    @BindView(R.id.iv_share_image)
    ImageView iv_share_image;
    @BindView(R.id.bt_share_custom)
    Button bt_share_custom;
    @BindView(R.id.bt_share_default)
    Button bt_share_default;
    @BindView(R.id.bt_share_customized_apps)
    Button bt_share_customized_apps;

    private String title, content, subject;
    private Uri uri;
    private int viewId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        title = tv_title_share.getText().toString();
        content = tv_content_share.getText().toString();
        subject = tv_subject_share.getText().toString();
        bt_share_custom.setOnClickListener(this);
        bt_share_default.setOnClickListener(this);
        bt_share_customized_apps.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        viewId = view.getId();
        int hasWrittenStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWrittenStoragePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_STORAGE);
        } else {
            Drawable shareDrawable = iv_share_image.getDrawable();
            Bitmap bmp = ((BitmapDrawable) shareDrawable).getBitmap();
            uri = FileManager.getBitmapUri(this, bmp);
            share(viewId);
        }
    }

    private void share(int id){
        switch (id){
            case R.id.bt_share_custom:
                new ShareManager().setContext(this)
                        .setContent(content)
                        .setSubject(subject)
                        .setTitle(title)
                        .setUri(uri)
                        .useDefaultDialog(false)
                        .share();
                break;
            case R.id.bt_share_default:
                new ShareManager().setContext(this)
                        .setContent(content)
                        .setSubject(subject)
                        .setTitle(title)
                        .setUri(uri)
                        .useDefaultDialog(true)
                        .share();
                break;
            case R.id.bt_share_customized_apps:
                List<String> mShareProviders = new ArrayList<>();
                mShareProviders.add(ShareProvider.TWITTER);
                mShareProviders.add(ShareProvider.GMAIL);
                mShareProviders.add(ShareProvider.FACEBOOK);
                new ShareManager().setContext(this)
                        .setContent(content)
                        .setSubject(subject)
                        .setTitle(title)
                        .setUri(uri)
                        .useDefaultDialog(false)
                        .setShareProviders(mShareProviders)
                        .share();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case PERMISSION_REQUEST_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Drawable shareDrawable = iv_share_image.getDrawable();
                    Bitmap bmp = ((BitmapDrawable) shareDrawable).getBitmap();
                    uri = FileManager.getBitmapUri(this, bmp);
                    share(viewId);
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        showPermissionsDialog(getString(R.string.storage_permission_request_message), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                    } else {
                        showPermissionsDialog(getString(R.string.storage_permission_denied_message), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                    }
                }
                break;
        }
    }

    private void showPermissionsDialog(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
    }
}
