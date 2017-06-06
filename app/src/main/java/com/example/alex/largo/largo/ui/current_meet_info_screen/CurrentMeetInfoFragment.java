package com.example.alex.largo.largo.ui.current_meet_info_screen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.alex.largo.largo.R;
import com.example.alex.largo.largo.api_service.ForecastService;
import com.example.alex.largo.largo.model.ChatMessage;
import com.example.alex.largo.largo.model.LargoUser;
import com.example.alex.largo.largo.model.Meet;
import com.example.alex.largo.largo.model.forecast_response.ForecastResponse;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.alex.largo.largo.api_service.ForecastService.retrofit;


public class CurrentMeetInfoFragment extends Fragment implements OnMapReadyCallback, ChildEventListener {

    public static final String EXTRA_MEET_INFO = "information_about_meet_from_meets_fragment";
    private static final String FORECAST_API_KEY = "34a3aff33b65e53f0775500c3d294f9b";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mSimpleFirechatDatabaseReference;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    private Meet mCurrentMeet;

    private TextView mTimeTextView;
    private EditText mMessageEditText;


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentMeet = getArguments().getParcelable(EXTRA_MEET_INFO);
        mSimpleFirechatDatabaseReference = FirebaseDatabase.getInstance().getReference();
        setAuthListener();
        getWeatherInfo();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meet_info, container, false);
        initViews(view);
        setDataToViews();
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void initViews(View view) {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.contactFrag_F_map);
        mapFragment.getMapAsync(this);

        mTimeTextView = (TextView) view.findViewById(R.id.meet_time_tv);
        mMessageEditText = (EditText) view.findViewById(R.id.message_ed);

        view.findViewById(R.id.send_message_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mMessageRecyclerView = (RecyclerView) view.findViewById(R.id.messages_list_rv);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

    }

    private void setDataToViews() {
        int hours = mCurrentMeet.getHour();
        int minutes = mCurrentMeet.getMin();
        mTimeTextView.setText(hours + ":" + minutes);
    }

    private void getWeatherInfo() {
        ForecastService forecastApi = retrofit.create(ForecastService.class);
        Call<ForecastResponse> call = forecastApi.getCurrentWeatherInfo(FORECAST_API_KEY, 37.8267, -122.4233);
        call.enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                int a = 5;
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {

            }
        });
    }

    private void setAuthListener(){
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mAuth = FirebaseAuth.getInstance();
                mCurrentUser = firebaseAuth.getCurrentUser();
            }
        };
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        double latitude = mCurrentMeet.getLatitude();
        double longitude = mCurrentMeet.getLongitude();

        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title("Meet");
        googleMap.addMarker(marker);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
