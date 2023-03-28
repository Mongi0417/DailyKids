package com.project.dailykids;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

public class ShuttleActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    private static final int PERMISSION_REQUEST_CODE = 1000;
    private static final String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private NaverMap naverMap;
    private Marker marker;
    private View mView;
    private Toolbar toolbar;
    private TextView tvToolbarTitle, tvNumPlate, tvRunState;
    private FloatingActionButton fabShare, fabStart, fabEnd;
    private FusedLocationSource fusedLocationSource;
    private DatabaseReference mDbRef;
    private ShuttleDTO shuttleDTO;
    private String uid, who;
    private String runState, getRunState;
    private double latitude, longitude, getLatitude, getLongitude;
    private boolean fabState = false, shareState = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.none);
        setContentView(R.layout.shuttle_layout);

        setToolbar();
        initView();
        initData();
        setMapFragment();

        if (who.equals("교사")) {
            setClickListener();
            setFabButtonVisibility();
        }
    }

    private void setToolbar() {
        mView = findViewById(R.id.shuttle_toolbar);
        toolbar = mView.findViewById(R.id.toolbar);
        tvToolbarTitle = mView.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("셔틀버스 위치");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initView() {
        tvNumPlate = findViewById(R.id.shuttle_tvNumPlate);
        tvRunState = findViewById(R.id.shuttle_tvRunState);
        fabShare = findViewById(R.id.shuttle_fabShare);
        fabStart = findViewById(R.id.shuttle_fabStart);
        fabEnd = findViewById(R.id.shuttle_fabEnd);
    }

    private void initData() {
        uid = FirebaseAuth.getInstance().getUid();
        who = getIntent().getStringExtra("who");
        mDbRef = FirebaseDatabase.getInstance().getReference();
    }

    private void setClickListener() {
        fabShare.setOnClickListener(this);
        fabStart.setOnClickListener(this);
        fabEnd.setOnClickListener(this);
    }

    private void setFabButtonVisibility() {
        fabShare.setVisibility(View.VISIBLE);
        fabStart.setVisibility(View.VISIBLE);
        fabEnd.setVisibility(View.VISIBLE);
    }

    private void setMapFragment() {
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.shuttle_map_fragment);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.shuttle_map_fragment, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        fusedLocationSource = new FusedLocationSource(this, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(fusedLocationSource);
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);

        if (who.equals("학부모"))
            loadLocationForParent();
        else
            saveLocationByDriver();
    }

    private void loadLocationForParent() {
        marker = new Marker();
        marker.setIcon(OverlayImage.fromResource(R.drawable.bus));
        marker.setWidth(80);
        marker.setHeight(80);
        mDbRef.child("shuttle").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shuttleDTO = snapshot.getValue(ShuttleDTO.class);
                getLatitude = shuttleDTO.getLatitude();
                getLongitude = shuttleDTO.getLongitude();
                getRunState = shuttleDTO.getRunState();
                marker.setMap(null);
                tvRunState.setText(getRunState);
                if (getLatitude != 0 && getLongitude != 0) {
                    marker.setPosition(new LatLng(getLatitude, getLongitude));
                    marker.setMap(naverMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void saveLocationByDriver() {
        naverMap.addOnLocationChangeListener(location -> {
            if (shareState) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                shuttleDTO = new ShuttleDTO(latitude, longitude, runState);
                mDbRef.child("shuttle").setValue(shuttleDTO);
                tvRunState.setText(runState);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (fusedLocationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!fusedLocationSource.isActivated())
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            else naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            return;
        }
    }

    private void toggleFab() {
        if (fabState) {
            ObjectAnimator fs_animation = ObjectAnimator.ofFloat(fabStart, "translationY", 0f);
            fs_animation.start();
            ObjectAnimator fe_animation = ObjectAnimator.ofFloat(fabEnd, "translationY", 0f);
            fe_animation.start();
            fabShare.setImageResource(R.drawable.shuttle);
        } else {
            ObjectAnimator fe_animation = ObjectAnimator.ofFloat(fabEnd, "translationY", -400f);
            fe_animation.start();
            ObjectAnimator fs_animation = ObjectAnimator.ofFloat(fabStart, "translationY", -200f);
            fs_animation.start();
            fabShare.setImageResource(R.drawable.ic_baseline_clear_24);
        }
        fabState = !fabState;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shuttle_fabShare:
                toggleFab();
                break;
            case R.id.shuttle_fabStart:
                toggleFab();
                Toast.makeText(ShuttleActivity.this, "운행을 시작합니다.", Toast.LENGTH_SHORT).show();
                runState = "운행 중";
                shareState = true;
                break;
            case R.id.shuttle_fabEnd:
                toggleFab();
                Toast.makeText(ShuttleActivity.this, "운행을 중지합니다.", Toast.LENGTH_SHORT).show();
                shareState = false;
                runState = "운행 중지";
                shuttleDTO = new ShuttleDTO(0, 0, runState);
                mDbRef.child("shuttle").setValue(shuttleDTO);
                tvRunState.setText(runState);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.none, R.anim.fadeout);
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
}
