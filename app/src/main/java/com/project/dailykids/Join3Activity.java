package com.project.dailykids;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Join3Activity extends AppCompatActivity {
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private View mView;
    private Toolbar toolbar;
    private TextView tvToolbarTitle;
    private String userEmail = "", userPassword = "", userNickname = "", userWho = "";
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

        setToolbar();
        initData();
        setButtonToLoadImage();
        setButtonToJoin();
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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            tempFile = createImageFile();
        } catch (Exception e) {
            Toast.makeText(this, "이미지 처리 오류, 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (tempFile != null) {
            photoUri = FileProvider.getUriForFile(this, "{com.project.dailykids}.fileprovider", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, PICK_FROM_CAMERA);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.KOREA).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = new File(getCacheDir() + "/img");
        if (!storageDir.exists())
            storageDir.mkdir();
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
    }

    private void goToAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            if (tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        tempFile = null;
                    }
                }
            }
            return;
        }

        switch (requestCode) {
            case PICK_FROM_CAMERA:
                photoUri = Uri.fromFile(tempFile);
                cropImage(photoUri);
                break;
            case PICK_FROM_ALBUM:
                photoUri = data.getData();
                cropImage(photoUri);
                break;
            case Crop.REQUEST_CROP:
                setImage();
                break;
        }
    }

    private void cropImage(Uri photoUri) {
        if (tempFile == null) {
            try {
                tempFile = createImageFile();
            } catch (IOException e) {
                Toast.makeText(this, "이미지 처리 오류, 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
                finish();
                e.printStackTrace();
            }
        }

        cropUri = Uri.fromFile(tempFile);
        Crop.of(photoUri, cropUri).asSquare().start(this);
    }

    private void setImage() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        imgProfile.setImageBitmap(originalBm);
    }

    private void createUser(String email, String password, String nickname, String who, Uri cropUri) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String uid = task.getResult().getUser().getUid();
                String simpleDTOKey = mRef.child("SimpleUserData").push().getKey();
                String kinderName = "무소속";
                UserDTO userDTO = new UserDTO(uid, email, nickname, who, kinderName, simpleDTOKey);
                SimpleUserDTO simpleUserDTO = new SimpleUserDTO(email, nickname);
                mRef.child("SimpleUserData").child(simpleDTOKey).setValue(simpleUserDTO);
                mRef.child("UserData").child(uid).setValue(userDTO);
                mStorageRef.child("profile_img/").child(uid + ".jpg").putFile(cropUri);
                tempFile.delete();
                finishAffinity();
                Intent intent = new Intent(Join3Activity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                try {
                    throw task.getException();
                } catch (Exception e) {
                    Toast.makeText(this, "계정을 다시 확인해 주세요", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(e -> e.printStackTrace());
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