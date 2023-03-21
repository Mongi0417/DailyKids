package com.project.dailykids;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Join3Activity extends AppCompatActivity {
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private View mView;
    private Toolbar toolbar;
    private TextView tvToolbarTitle;
    private String userEmail, userPassword, userNickname, userWho;
    private CircleImageView imgProfile;
    private Button btnLoadImg, btnJoin;
    private Uri photoUri, cropUri;
    private File tempFile;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.none);
        setContentView(R.layout.join_step3_layout);

    }

    private void setToolbar() {
        mView = findViewById(R.id.join3_toolbar);
        toolbar = mView.findViewById(R.id.toolbar);
        tvToolbarTitle = mView.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("회원가입");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initData() {
        Intent intent = getIntent();
        userEmail = intent.getStringExtra("email");
        userPassword = intent.getStringExtra("password");
        userNickname = intent.getStringExtra("nickname");
        userWho = intent.getStringExtra("who");
        imgProfile = findViewById(R.id.join3_imgProfile);
        btnLoadImg = findViewById(R.id.join3_btnUploadImg);
        btnJoin = findViewById(R.id.join3_btnJoin);
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    private void setButtonToLoadImage() {
        btnLoadImg.setOnClickListener(view -> {
            tedPermission();
            DialogInterface.OnClickListener cameraListener = (dialogInterface, i) -> takePhoto();
            DialogInterface.OnClickListener albumListener = (dialogInterface, i) -> goToAlbum();
            DialogInterface.OnClickListener cancelListener = (dialogInterface, i) -> dialogInterface.dismiss();
            new AlertDialog.Builder(Join3Activity.this).setTitle("불러올 이미지 선택").setPositiveButton("앨범 선택", albumListener).setNeutralButton("취소", cancelListener).setNegativeButton("사진 촬영", cameraListener).show();
        });
    }

    private void setButtonToJoin() {
        btnJoin.setOnClickListener(view -> {
            createUser(userEmail, userPassword, userNickname, userWho, cropUri);
        });
    }

    private void takePhoto() {

    }

    private void goToAlbum() {

    }

    private void createUser(String email, String password, String nickname, String who, Uri cropUri) {

    }

    private void tedPermission() {
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
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage(getResources().getString(R.string.permission_files_2))
                .setDeniedMessage(getResources().getString(R.string.permission_files_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.none, R.anim.fadeout);
    }
}