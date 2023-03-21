/*
package com.project.dailykids;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class temp extends AppCompatActivity {
    private View mView;
    private Toolbar toolbar;
    private TextView tvToolbarTitle, tvCheckEmail, tvPasswordLength, tvCheckPassword, tvCheckNickname;
    private EditText edtEmail, edtPassword, edtCheckPassword, edtNickname;
    private RadioButton rdButton;
    private RadioGroup rdGroup;
    private Button btnCheckEmail, btnCheckNickname, btnNext;
    private String userEmail, userPassword, userNickname;
    private boolean checkEmailForm = false, checkEmailExist = true, checkPasswordLength = false, checkSamePassword = false, checkNicknameLength = false, checkNicknameExist = true;
    private DatabaseReference mRef;
    private SimpleUserDTO simpleUserDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.none);
        setContentView(R.layout.join_step1_layout);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        setToolbar();
        initData();
        setButtonToCheckEmail();
        checkPassword();
        hideKeyboard();

        edtNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userNickname = edtNickname.getText().toString();
                if (userNickname.length() < 2 || userNickname.length() > 8) {
                    checkNicknameLength = false;
                    tvCheckNickname.setTextColor(Color.RED);
                    tvCheckNickname.setText("2-8자 이내로 설정해주세요.");
                    tvCheckNickname.setVisibility(View.VISIBLE);
                } else {
                    checkNicknameLength = true;
                    tvCheckNickname.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        */
/*btnCheckEmail.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
            checkEmailExist = false;
            userEmail = edtEmail.getText().toString();
            mRef.child("SimpleUserData").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot item : snapshot.getChildren()) {
                        simpleUserDTO = item.getValue(SimpleUserDTO.class);
                        String strEmail = simpleUserDTO.getEmail();
                        if (userEmail.equals(strEmail)) {
                            checkEmailExist = true;
                            break;
                        }
                    }
                    if (!checkEmailExist) {
                        tvCheckEmail.setTextColor(Color.BLUE);
                        tvCheckEmail.setText("사용 가능한 메일입니다.");
                        edtEmail.setTextColor(Color.GRAY);
                        edtEmail.setFocusable(false);
                    } else {
                        tvCheckEmail.setTextColor(Color.RED);
                        tvCheckEmail.setText("이미 존재하는 메일입니다.");
                    }
                    tvCheckEmail.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });*//*


        btnCheckNickname.setOnClickListener(view -> {
            checkNicknameExist = false;
            mRef.child("SimpleUserData").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot item : snapshot.getChildren()) {
                        simpleUserDTO = item.getValue(SimpleUserDTO.class);
                        String strNickname = simpleUserDTO.getNickname();
                        if (userNickname.equals(strNickname)) {
                            checkNicknameExist = true;
                            break;
                        }
                    }
                    if (!checkNicknameExist) {
                        tvCheckNickname.setTextColor(Color.BLUE);
                        tvCheckNickname.setText("사용 가능한 별명입니다.");
                        edtNickname.setTextColor(Color.GRAY);
                        edtNickname.setFocusable(false);
                    } else {
                        tvCheckNickname.setTextColor(Color.RED);
                        tvCheckNickname.setText("이미 존재하는 별명입니다.");
                    }
                    tvCheckNickname.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rdButton = findViewById(rdGroup.getCheckedRadioButtonId());
                if (!checkEmailExist && canUsePassword(checkPasswordLength, checkSamePassword) && canUseNickname(checkNicknameLength, checkNicknameExist) && (rdButton != null)) {
                    Intent intent = new Intent(temp.this, temp.class);
                    intent.putExtra("email", userEmail);
                    intent.putExtra("password", userPassword);
                    intent.putExtra("nickname", userNickname);
                    intent.putExtra("who", rdButton.getText().toString());
                    startActivity(intent);
                } else {
                    Toast.makeText(temp.this, "입력 정보를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initData() {
        // 위젯 지정
        tvCheckEmail = findViewById(R.id.join1_tvCheckEmail);
        tvPasswordLength = findViewById(R.id.join1_tvPasswordLength);
        tvCheckPassword = findViewById(R.id.join1_tvCheckPassword);
        tvCheckNickname = findViewById(R.id.join1_tvCheckNickname);
        edtEmail = findViewById(R.id.join1_edtEmail);
        edtPassword = findViewById(R.id.join1_edtPassword);
        edtCheckPassword = findViewById(R.id.join1_edtCheckPassword);
        edtNickname = findViewById(R.id.join1_edtNickname);
        rdGroup = findViewById(R.id.join1_rdGroup);
        btnCheckEmail = findViewById(R.id.join1_btnCheckEmail);
        btnCheckNickname = findViewById(R.id.join1_btnCheckNickname);
        btnNext = findViewById(R.id.join1_btnNext);
        // 파이어베이스 관련
        mRef = FirebaseDatabase.getInstance().getReference();
    }

    private void setToolbar() {
        // 툴바 설정
        mView = findViewById(R.id.join1_toolbar);
        toolbar = mView.findViewById(R.id.toolbar);
        tvToolbarTitle = mView.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("회원가입");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setButtonToCheckEmail() {
        btnCheckEmail.setOnClickListener(view -> {
            userEmail = edtEmail.getText().toString();
            Pattern pattern = Patterns.EMAIL_ADDRESS;
            if (pattern.matcher(userEmail).matches()) {
                checkEmailExist();
            } else {

            }
        });
    }

    private void checkEmailExist() {
        mRef.child("SimpleUserData").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    simpleUserDTO = item.getValue(SimpleUserDTO.class);
                    String strEmail = simpleUserDTO.getEmail();
                    if (userEmail.equals(strEmail)) {
                        checkEmailExist = true;
                        break;
                    }
                }
                if (!checkEmailExist) {
                    tvCheckEmail.setTextColor(Color.BLUE);
                    tvCheckEmail.setText("사용 가능한 메일입니다.");
                    edtEmail.setTextColor(Color.GRAY);
                    edtEmail.setFocusable(false);
                } else {
                    tvCheckEmail.setTextColor(Color.RED);
                    tvCheckEmail.setText("이미 존재하는 메일입니다.");
                }
                tvCheckEmail.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkPassword() {
        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userPassword = edtPassword.getText().toString();
                if (userPassword.length() < 6) {
                    tvPasswordLength.setVisibility(View.VISIBLE);
                    checkPasswordLength = false;
                } else {
                    tvPasswordLength.setVisibility(View.GONE);
                    checkPasswordLength = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        edtCheckPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvCheckPassword.setVisibility(View.VISIBLE);
                if (edtPassword.getText().toString().equals(edtCheckPassword.getText().toString())) {
                    checkSamePassword = true;
                    tvCheckPassword.setTextColor(Color.BLUE);
                    tvCheckPassword.setText("비밀번호와 일치합니다.");
                } else {
                    checkSamePassword = false;
                    tvCheckPassword.setTextColor(Color.RED);
                    tvCheckPassword.setText("비밀번호와 일치하지 않습니다.");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void findNickname() {
        mRef.child("SimpleUserData").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    simpleUserDTO = item.getValue(SimpleUserDTO.class);
                    String strNickname = simpleUserDTO.getNickname();
                    if (userNickname.equals(strNickname)) {
                        checkNicknameExist = true;
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setButtonToCheckNickname() {
        btnCheckNickname.setOnClickListener(view -> {

            findNickname();
        });
    }

    private boolean canUseNickname(boolean checkNicknameLength, boolean checkNicknameExist) {
        return checkNicknameLength && !checkNicknameExist;
    }

    private boolean canUsePassword(boolean checkPasswordLength, boolean checkSamePassword) {
        return checkPasswordLength && checkSamePassword;
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

    private void hideKeyboard() {
        View v = getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}*/

/*package com.project.dailykids;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class temp extends AppCompatActivity {
    private View mView;
    private Toolbar toolbar;
    private TextView tvToolbarTitle;
    private EditText edtPhone, edtCode;
    private Button btnRequest, btnNext;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String userEmail, userPassword, userNickname, userWho, userPhone;
    private String verificationId;
    private FirebaseAuth mAuth;
    private boolean authOk = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.none);
        setContentView(R.layout.join_step2_layout);

        // 파이어베이스 관련
        mAuth = FirebaseAuth.getInstance();
        // 데이터 수신
        Intent intent = getIntent();
        userEmail = intent.getStringExtra("email");
        userPassword = intent.getStringExtra("password");
        userNickname = intent.getStringExtra("nickname");
        userWho = intent.getStringExtra("who");
        // 툴바 설정
        mView = findViewById(R.id.join2_toolbar);
        toolbar = mView.findViewById(R.id.toolbar);
        tvToolbarTitle = mView.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("회원가입");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // 위젯 지정
        edtPhone = findViewById(R.id.join2_edtPhone);
        edtCode = findViewById(R.id.join2_edtCode);
        btnRequest = findViewById(R.id.join2_btnRequest);
        btnNext = findViewById(R.id.join2_btnNext);
        // 전화 인증 콜백
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.d("TAG", "Success");
                final String code = phoneAuthCredential.getSmsCode();
                if (code != null) {
                    edtCode.setText(code);
                    verifyCode(code);
                }
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d("TAG", e.toString());
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                verificationId = s;
            }
        };

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPhone = edtPhone.getText().toString();
                if (userPhone.length() == 11) {
                    sendVerificationCode("+82" + userPhone.substring(1));
                } else
                    Toast.makeText(temp.this, "전화번호를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (authOk) {
                    Intent intent = new Intent(temp.this, Join3Activity.class);
                    intent.putExtra("email", userEmail);
                    intent.putExtra("password", userPassword);
                    intent.putExtra("nickname", userNickname);
                    intent.putExtra("who", userWho);
                    intent.putExtra("phone", "01050120682");
                    startActivity(intent);
                } else {
                    Toast.makeText(temp.this, "전화번호 인증을 완료해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    authOk = true;
                    edtPhone.setFocusable(false);
                    edtCode.setFocusable(false);
                    mAuth.getCurrentUser().delete();
                    mAuth.signOut();
                } else {
                    Log.d("TAG", task.getException().toString());
                }
            }
        });
    }

    public void sendVerificationCode(String number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
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
}*/

/*package com.project.dailykids;

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

public class temp extends AppCompatActivity {
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
    private Button btnUploadImg, btnjoin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.none);
        setContentView(R.layout.join_step3_layout);

        // 데이터 수신
        Intent intent = getIntent();
        userEmail = intent.getStringExtra("email");
        userPassword = intent.getStringExtra("password");
        userNickname = intent.getStringExtra("nickname");
        userWho = intent.getStringExtra("who");
        userPhone = intent.getStringExtra("phone");

        // 파이어베이스 관련
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        // 툴바 설정
        mView = findViewById(R.id.join3_toolbar);
        toolbar = mView.findViewById(R.id.toolbar);
        tvToolbarTitle = mView.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("회원가입");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 위젯 지정
        imgProfile = findViewById(R.id.join3_imgProfile);
        btnUploadImg = findViewById(R.id.join3_btnUploadImg);
        btnjoin = findViewById(R.id.join3_btnJoin);

        btnUploadImg.setOnClickListener(view -> {
            tedPermission();
            DialogInterface.OnClickListener cameraListener = (dialogInterface, i) -> takePhoto();
            DialogInterface.OnClickListener albumListener = (dialogInterface, i) -> goToAlbum();
            DialogInterface.OnClickListener cancelListener = (dialogInterface, i) -> dialogInterface.dismiss();
            new AlertDialog.Builder(temp.this).setTitle("불러올 이미지 선택").setPositiveButton("앨범 선택", albumListener).setNeutralButton("취소", cancelListener).setNegativeButton("사진 촬영", cameraListener).show();
        });

        btnjoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser(userEmail, userPassword, userNickname, userWho, userPhone, cropUri);
            }
        });
    }

    public void takePhoto() {
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

    public void goToAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.KOREA).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = new File(getCacheDir() + "/img");
        if (!storageDir.exists())
            storageDir.mkdirs();
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
    }

    public void cropImage(Uri photoUri) {
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

    public void setImage() {
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

    public void createUser(String userEmail, String userPassword, String userNickname, String userWho, String userPhone, Uri cropUri) {
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String uid = task.getResult().getUser().getUid();
                    String simpleDTOKey = mRef.child("SimpleUserData").push().getKey();
                    String kinderName = "무소속";
                    UserDTO userDTO = new UserDTO(uid, userEmail, userNickname, userWho, kinderName, userPhone, simpleDTOKey);
                    SimpleUserDTO simpleUserDTO = new SimpleUserDTO(userEmail, userNickname);
                    mRef.child("SimpleUserData").child(simpleDTOKey).setValue(simpleUserDTO);
                    mRef.child("users").child(uid).setValue(userDTO);
                    mStorageRef.child("profile_img/").child(uid + ".jpg").putFile(cropUri);
                    tempFile.delete();
                    finishAffinity();
                    Intent intent = new Intent(temp.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    try {
                        throw task.getException();
                    } catch (Exception e) {
                        Toast.makeText(temp.this, "계정을 다시 확인해 주세요.", Toast.LENGTH_SHORT).show();
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
*/