package com.example.alex.largo.largo.ui.add_new_meet_screen;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.example.alex.largo.largo.R;
import com.example.alex.largo.largo.model.LargoUser;
import com.example.alex.largo.largo.model.Meet;
import com.example.alex.largo.largo.ui.meets_screen.MeetsFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AddNewMeetFragment extends Fragment implements TimePickerDialog.OnTimeSetListener,
        View.OnClickListener, OnMapReadyCallback {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseReference;
    private GoogleMap mMap;

    private String mUserName;
    private String mUserPhotoUrl;
    private int mHour;
    private int mMinute;
    private LatLng mMeetCoordinates;

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAuthListener();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_meet, container, false);
        initViews(view);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.contactFrag_F_map);
        mapFragment.getMapAsync(this);
        return view;
    }

    private void initViews(View view) {
        view.findViewById(R.id.add_new_meet_btn).setOnClickListener(this);
        view.findViewById(R.id.timer_img_btn).setOnClickListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void showTimePickerDialog() {
        new TimePickerDialog(getActivity(), this, mHour, mMinute, true).show();
    }

    private void addNewMeet() {
        Meet meet = new Meet();
        meet.setUserName(mUserName);
        meet.setUserPhotoUrl(mUserPhotoUrl);
        meet.setHour(mHour);
        meet.setMin(mMinute);
        meet.setLatitude(mMeetCoordinates.latitude);
        meet.setLongitude(mMeetCoordinates.longitude);

        mDatabaseReference.child("meets").push().setValue(meet);
        closeActivity();
    }

    private void closeActivity(){
        getActivity().finish();
    }

    private void setAuthListener() {
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user =  firebaseAuth.getCurrentUser();
                if (user != null) {
                    mUserName = user.getDisplayName();
                    mUserPhotoUrl = user.getPhotoUrl().toString();
                }
            }
        };
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        mHour = i;
        mMinute = i1;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.timer_img_btn:
                showTimePickerDialog();
                break;
            case R.id.add_new_meet_btn:
                addNewMeet();
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(latLng.latitude, latLng.longitude)).title("New Marker");

                mMeetCoordinates = latLng;
                mMap.addMarker(marker);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });
        showSetCurrentLocationButton();
    }

    private void showSetCurrentLocationButton(){
        Context context = getContext();
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {}
        mMap.setMyLocationEnabled(true);
    }
}
