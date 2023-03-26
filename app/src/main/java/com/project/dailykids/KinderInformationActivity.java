package com.project.dailykids;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import org.json.JSONArray;
import org.json.JSONObject;

public class KinderInformationActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1000;
    private static final String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private View mView;
    private Toolbar toolbar;
    private TextView tvToolbarTitle, tvKinderName, tvKinderAddress, tvKinderTel, tvKinderClCnt, tvKinderFpCnt;
    private String kinderName, establish, addr, telno, clcnt3, clcnt4, clcnt5, mixclcnt, ag3fpcnt, ag4fpcnt, ag5fpcnt, mixfpcnt, ppcnt3, ppcnt4, ppcnt5, mixppcnt;
    private String uid = "", nickname = "", who = "";
    private Button btnApply;
    private JSONObject obj;
    private JSONArray kinderArray;
    private FusedLocationSource locationSource;
    private String latitude, longitude;
    private Double doubleLatitude, doubleLongitude;
    private Marker marker;
    //private MyThread myThread;
    private NaverMap naverMap;
    private DatabaseReference mDbRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.none);
        setContentView(R.layout.kinder_information_layout);

        setToolbar();
        initView();
        initData();
    }

    private void setToolbar() {
        mView = findViewById(R.id.information_toolbar);
        toolbar = mView.findViewById(R.id.toolbar);
        tvToolbarTitle = mView.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("유치원 정보");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initView() {
        tvKinderName = findViewById(R.id.information_tvKinderName);
        tvKinderAddress = findViewById(R.id.information_tvKinderAddress);
        tvKinderTel = findViewById(R.id.information_tvKinderTel);
        tvKinderClCnt = findViewById(R.id.information_tvKinderClCnt);
        tvKinderFpCnt = findViewById(R.id.information_tvKinderFpCnt);
        btnApply = findViewById(R.id.information_btnApply);
    }

    private void initData() {
        nickname = getIntent().getStringExtra("nickname");
        who = getIntent().getStringExtra("who");
        uid = FirebaseAuth.getInstance().getUid();
        kinderName = getIntent().getStringExtra("kinderName");
        mDbRef = FirebaseDatabase.getInstance().getReference();
        try {
            obj = new JSONObject(getIntent().getStringExtra("kinderInfo"));
            kinderArray = obj.getJSONArray("kinderInfo");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
