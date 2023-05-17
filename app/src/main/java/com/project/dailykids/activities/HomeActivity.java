package com.project.dailykids.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.project.dailykids.utils.TimestampConverter;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int SIZE_OF_NOTICE = 4;
    private View mView;
    private Toolbar toolbar;
    private TextView tvToolbarTitle, tvKinderName, tvNickname, tvNotice[] = new TextView[SIZE_OF_NOTICE], tvDate[] = new TextView[SIZE_OF_NOTICE];
    private int noticeId[] = {R.id.home_tvNotice1, R.id.home_tvNotice2, R.id.home_tvNotice3, R.id.home_tvNotice4}, dateId[] = {R.id.home_tvNoticeDate1, R.id.home_tvNoticeDate2, R.id.home_tvNoticeDate3, R.id.home_tvNoticeDate4};
    private DatabaseReference mDbRef;
    private StorageReference mStorageRef;
    private CircleImageView imgProfile;
    private LinearLayout linearNotice, linearSearch, linearPost, linearShuttle;
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
        linearPost = findViewById(R.id.home_post);
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
        linearPost.setOnClickListener(this);
        linearSearch.setOnClickListener(this);
        fabChat.setOnClickListener(this);
    }

    private void loadNotice() {
        mDbRef.child("Notice").orderByChild("timestampForSorting").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int num = 0;
                for (DataSnapshot item : snapshot.getChildren()) {
                    notice = item.getValue(Notice.class);
                    if (notice.getIsNotice() == 1) {
                        tvNotice[num].setText(notice.getTitle());
                        Log.d("TAG", String.valueOf(notice.getTimestamp()));
                        tvDate[num].setText(TimestampConverter.timestampToDateWithDot(notice.getTimestamp()));
                        num++;
                    }
                    if (num > 3)
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void loadProfile() {
        Glide.with(HomeActivity.this).load(mStorageRef.child("profile_img/").child(uid + ".jpg")).into(imgProfile);
        mDbRef.child("UserData").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                nickname = user.getNickname();
                tvNickname.setText(nickname);
                kinderName = user.getKinderName();
                tvKinderName.setText(kinderName);
                who = user.getWho();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
            case R.id.home_post:
                intent = new Intent(this, PostActivity.class);
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
        if (item.getItemId() == R.id.menu_profile) {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}