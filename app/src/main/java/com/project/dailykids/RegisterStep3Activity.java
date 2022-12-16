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
import android.util.Log;
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

public class RegisterStep3Activity extends AppCompatActivity {
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private View mView;
    private Toolbar toolbar;
    private TextView tvToolbarTitle;
    private String userEmail, userPassword, userNickname, userWho, userPhone;
    private CircleImageView imgProfile;
    private Uri photoUri, cropUri;
    private File tempFile;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private StorageReference mStorageRef;
    private Button btnUploadImg, btnRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.none);
        setContentView(R.layout.register_step3_layout);

        // 데이터 수신
        Intent intent = getIntent();
        userEmail = intent.getStringExtra("email");
        userPassword = intent.getStringExtra("password");
        userNickname = intent.getStringExtra("nickname");
        userWho = intent.getStringExtra("who");
        userPhone = intent.getStringExtra("phone");
        Log.d("TAG", userEmail);
        Log.d("TAG", userPassword);
        Log.d("TAG", userNickname);
        Log.d("TAG", userWho);
        Log.d("TAG", userPhone);

        // 파이어베이스 관련
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        // 툴바 설정
        mView = findViewById(R.id.register3_toolbar);
        toolbar = mView.findViewById(R.id.toolbar);
        tvToolbarTitle = mView.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("회원가입");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 위젯 지정
        imgProfile = findViewById(R.id.register3_imgProfile);
        btnUploadImg = findViewById(R.id.register3_btnUploadImg);
        btnRegister = findViewById(R.id.register3_btnRegister);

        btnUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tedPermission();
                DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        takePhoto();
                    }
                };
                DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        goToAlbum();
                    }
                };
                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                };
                new AlertDialog.Builder(RegisterStep3Activity.this).setTitle("업로드할 이미지 선택").setPositiveButton("앨범 선택", albumListener).setNeutralButton("취소", cancelListener).setNegativeButton("사진 촬영", cameraListener).show();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser(userEmail, userPassword, userNickname, userWho, userPhone, cropUri);
            }
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

    private void goToAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.KOREA).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = new File(getCacheDir() + "/img");
        if (!storageDir.exists())
            storageDir.mkdirs();
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
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

    private void createUser(String userEmail, String userPassword, String userNickname, String userWho, String userPhone, Uri cropUri) {
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String uid = task.getResult().getUser().getUid();
                    String simpleDTOKey = mRef.child("checkExist").push().getKey();
                    String kinderName = "nowhere";
                    UserDTO userDTO = new UserDTO(uid, userEmail, userNickname, userWho, kinderName, userPhone, simpleDTOKey);
                    SimpleUserDTO simpleUserDTO = new SimpleUserDTO(userEmail, userNickname);
                    mRef.child("checkExist").child(simpleDTOKey).setValue(simpleUserDTO);
                    mRef.child("users").child(uid).setValue(userDTO);
                    mStorageRef.child("profile_img/").child(uid + ".jpg").putFile(cropUri);
                    finishAffinity();
                    Intent intent = new Intent(RegisterStep3Activity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    try {
                        throw task.getException();
                    } catch (Exception e) {
                        Toast.makeText(RegisterStep3Activity.this, "계정을 다시 확인해 주세요.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
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
