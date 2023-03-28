package com.project.dailykids;

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

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private View mView;
    private Toolbar toolbar;
    private TextView tvToolbarTitle, tvNickname;
    private CircleImageView imgProfile;
    private Button btnEditProfile, btnLogout, btnDeleteAccount;
    private String uid, nickname;
    private StorageReference mStorageRef;
    private DatabaseReference mDbRef;
    private Uri photoUri, cropUri;
    private File tempFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.none);
        setContentView(R.layout.profile_layout);

        setToolbar();
        initView();
        initData();
        setClickListener();
        loadProfile();
    }

    private void setToolbar() {
        mView = findViewById(R.id.profile_toolbar);
        toolbar = mView.findViewById(R.id.toolbar);
        tvToolbarTitle = mView.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("프로필");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initView() {
        imgProfile = findViewById(R.id.profile_imgProfile);
        btnEditProfile = findViewById(R.id.profile_btnEditProfile);
        btnLogout = findViewById(R.id.profile_btnLogOut);
        btnDeleteAccount = findViewById(R.id.profile_btnDeleteAccount);
    }

    private void initData() {
        uid = FirebaseAuth.getInstance().getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDbRef = FirebaseDatabase.getInstance().getReference();
    }

    private void setClickListener() {
        imgProfile.setOnClickListener(this);
        btnEditProfile.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnDeleteAccount.setOnClickListener(this);
    }

    private void loadProfile() {
        new Thread(() -> {
            mStorageRef = FirebaseStorage.getInstance().getReference();
            mDbRef = FirebaseDatabase.getInstance().getReference();
            runOnUiThread(() -> {
                mStorageRef.child("profile_img/").child(uid + ".jpg").getDownloadUrl().addOnCompleteListener(task -> {
                    while (!task.isComplete()) ;
                    Glide.with(ProfileActivity.this).load(task.getResult()).into(imgProfile);
                });
                mDbRef.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserDTO userDTO = snapshot.getValue(UserDTO.class);
                        nickname = userDTO.getNickname();
                        tvNickname.setText(nickname);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            });
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_imgProfile:
                break;
            case R.id.profile_btnEditProfile:
                break;
            case R.id.profile_btnLogOut:
                break;
            case R.id.profile_btnDeleteAccount:
                break;
        }
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
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(Intent.createChooser(intent, "사진 선택"), PICK_FROM_ALBUM);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.none, R.anim.fadeout);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.none, R.anim.fadeout);
    }
}
