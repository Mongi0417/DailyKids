package com.project.dailykids;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int SIZE_OF_NOTICE = 4;
    private View mView;
    private Toolbar toolbar;
    private TextView tvToolbarTitle, tvKinderName, tvNickname, tvPreNotice[] = new TextView[SIZE_OF_NOTICE], tvnDate[] = new TextView[SIZE_OF_NOTICE];
    private int noticeId[] = {R.id.tvNotice1, R.id.tvNotice2, R.id.tvNotice3, R.id.tvNotice4}, dateId[] = {R.id.tvNoticeDate1, R.id.tvNoticeDate2, R.id.tvNoticeDate3, R.id.tvNoticeDate4};
    private DatabaseReference mDbRef;
    private StorageReference mStorageRef;
    private CircleImageView imgProfile;
    private LinearLayout linearNotice, linearApply, linearBoard, linearShuttle;
    private FloatingActionButton fabChat;
    private long waitTime = 0;
    private String uid = "", nickname = "", kinderName = "", who = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.none);
        setContentView(R.layout.home_layout);

        setToolbar();
        initData();
        setClickListener();

        // 데이터 불러오기 및 프로필 설정(유치원 이름, 닉네임, 프로필 사진)
        new Thread(() -> runOnUiThread(() -> {
            mStorageRef.child("profile_img/").child(uid + ".jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    GlideApp.with(HomeActivity.this).load(task.getResult()).into(imgProfile);
                }
            });
            mDbRef.child("UserData").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserDTO userDTO = snapshot.getValue(UserDTO.class);
                    nickname = userDTO.getNickname();
                    kinderName = userDTO.getKinderName();
                    who = userDTO.getWho();
                    tvKinderName.setText(kinderName);
                    tvNickname.setText(nickname);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        })).start();
    }

    private void setToolbar() {
        mView = findViewById(R.id.home_toolbar);
        toolbar = mView.findViewById(R.id.toolbar);
        tvToolbarTitle = mView.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText(R.string.app_name_eng);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initData() {
        uid = FirebaseAuth.getInstance().getUid();
        mDbRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        // 위젯 연결
        tvKinderName = findViewById(R.id.home_tvKinderName);
        tvNickname = findViewById(R.id.home_tvNickname);
        for (int i = 0; i < SIZE_OF_NOTICE; i++) {
            tvPreNotice[i] = findViewById(noticeId[i]);
            tvnDate[i] = findViewById(dateId[i]);
        }
        imgProfile = findViewById(R.id.home_imgProfile);
        linearNotice = findViewById(R.id.home_notice);
        linearApply = findViewById(R.id.home_apply);
        linearBoard = findViewById(R.id.home_board);
        linearShuttle = findViewById(R.id.home_shuttle);
        fabChat = findViewById(R.id.home_fabChat);
    }

    private void setClickListener() {
        linearShuttle.setOnClickListener(this);
        linearNotice.setOnClickListener(this);
        linearBoard.setOnClickListener(this);
        linearApply.setOnClickListener(this);
        fabChat.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.home_fabChat:
                intent = new Intent(HomeActivity.this, ChatActivity.class);
                intent.putExtra("nickname", nickname);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - waitTime >= 2000) {
            waitTime = System.currentTimeMillis();
            Toast.makeText(HomeActivity.this, "뒤로 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }
}