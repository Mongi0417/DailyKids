package com.project.dailykids.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.dailykids.R;
import com.project.dailykids.models.Notice;
import com.project.dailykids.models.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int SIZE_OF_NOTICE = 4;
    private View mView;
    private Toolbar toolbar;
    private TextView tvToolbarTitle, tvKinderName, tvNickname, tvNotice[] = new TextView[SIZE_OF_NOTICE], tvDate[] = new TextView[SIZE_OF_NOTICE];
    private int noticeId[] = {R.id.tvNotice1, R.id.tvNotice2, R.id.tvNotice3, R.id.tvNotice4}, dateId[] = {R.id.tvNoticeDate1, R.id.tvNoticeDate2, R.id.tvNoticeDate3, R.id.tvNoticeDate4};
    private DatabaseReference mDbRef;
    private StorageReference mStorageRef;
    private CircleImageView imgProfile;
    private LinearLayout linearNotice, linearSearch, linearBoard, linearShuttle;
    private FloatingActionButton fabChat;
    private long waitTime = 0;
    private String uid = "", nickname = "", kinderName = "", who = "";
    private Notice notice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.none);
        setContentView(R.layout.layout_home);

        setToolbar();
        initView();
        initData();
        setClickListener();
        loadProfile();
        loadNotice();
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

    private void initView() {
        tvKinderName = findViewById(R.id.home_tvKinderName);
        tvNickname = findViewById(R.id.home_tvNickname);
        for (int i = 0; i < SIZE_OF_NOTICE; i++) {
            tvNotice[i] = findViewById(noticeId[i]);
            tvDate[i] = findViewById(dateId[i]);
        }
        imgProfile = findViewById(R.id.home_imgProfile);
        linearNotice = findViewById(R.id.home_notice);
        linearSearch = findViewById(R.id.home_search);
        linearBoard = findViewById(R.id.home_board);
        linearShuttle = findViewById(R.id.home_shuttle);
        fabChat = findViewById(R.id.home_fabChat);
    }

    private void initData() {
        uid = FirebaseAuth.getInstance().getUid();
        mDbRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    private void setClickListener() {
        linearShuttle.setOnClickListener(this);
        linearNotice.setOnClickListener(this);
        linearBoard.setOnClickListener(this);
        linearSearch.setOnClickListener(this);
        fabChat.setOnClickListener(this);
    }

    private void loadNotice() {
        mDbRef.child("Notice").orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int num = 0; num < 4;) {
                    for (DataSnapshot item : snapshot.getChildren()) {
                        notice = item.getValue(Notice.class);
                        if (notice.getNotice() == 1) {
                            tvNotice[num].setText(notice.getTitle());
                            tvDate[num].setText(notice.getMonth() + notice.getDate());
                            num++;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void loadProfile() {
        /*new Thread(() -> runOnUiThread(() -> {
            mDbRef.child("UserData").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    nickname = user.getNickname();
                    kinderName = user.getKinderName();
                    who = user.getWho();
                    tvKinderName.setText(kinderName);
                    tvNickname.setText(nickname);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        })).start();*/
        new Thread(() -> {
            mDbRef.child("UserData").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    nickname = user.getNickname();
                    kinderName = user.getKinderName();
                    who = user.getWho();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            runOnUiThread(() -> {
                Glide.with(HomeActivity.this).load(mStorageRef).into(imgProfile);
                tvKinderName.setText(kinderName);
                tvNickname.setText(nickname);
            });
        }).start();
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.home_fabChat:
                intent = new Intent(this, ChatActivity.class);
                intent.putExtra("nickname", nickname);
                startActivity(intent);
                break;
            case R.id.home_search:
                intent = new Intent(this, SearchKinderActivity.class);
                intent.putExtra("nickname", nickname);
                intent.putExtra("who", who);
                startActivity(intent);
                break;
            case R.id.home_notice:
                intent = new Intent(this, NoticeActivity.class);
                intent.putExtra("nickname", nickname);
                intent.putExtra("who", who);
                startActivity(intent);
                break;
            case R.id.home_shuttle:
                intent = new Intent(this, ShuttleActivity.class);
                intent.putExtra("who", who);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_profile:
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}