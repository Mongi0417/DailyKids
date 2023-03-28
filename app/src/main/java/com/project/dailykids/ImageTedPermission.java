package com.project.dailykids;

import android.Manifest;
import android.content.Context;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class ImageTedPermission {
    private Context mContext;

    public ImageTedPermission(Context context) {
        this.mContext = context;
    }

    public void tedPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                return;
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                return;
            }
        };
        TedPermission.with(mContext)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage(mContext.getResources().getString(R.string.permission_files_2))
                .setDeniedMessage(mContext.getResources().getString(R.string.permission_files_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }
}
