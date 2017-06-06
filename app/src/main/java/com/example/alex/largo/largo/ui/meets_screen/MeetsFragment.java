package com.example.alex.largo.largo.ui.meets_screen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.alex.largo.largo.R;
import com.example.alex.largo.largo.model.LargoUser;
import com.example.alex.largo.largo.model.Meet;
import com.example.alex.largo.largo.ui.add_new_meet_screen.AddNewMeetFragment;
import com.example.alex.largo.largo.ui.current_meet_info_screen.CurrentMeetInfoFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;


public class MeetsFragment extends Fragment implements View.OnClickListener {

    private TextView mNoMeets;
    private RecyclerView mMeetsRecycler;
    private LinearLayoutManager mLinearLayoutManager;
    private FloatingActionButton mFab;

    private DatabaseReference mReference;
    private FirebaseRecyclerAdapter<Meet, FireMeetsViewHolder> mFirebaseAdapter;

    private Button mGoBtn;
    LargoUser user;

    private static class FireMeetsViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout root;
        TextView userNameTextView;
        CircleImageView userPicture;
        TextView meetTimeTextView;
        Button goToTheMeet;

        public FireMeetsViewHolder(View v) {
            super(v);
            userNameTextView = (TextView) itemView.findViewById(R.id.user_name_tv);
            userPicture = (CircleImageView) itemView.findViewById(R.id.pet_owner_image);
            meetTimeTextView = (TextView) itemView.findViewById(R.id.start_meet_time_tv);
            goToTheMeet = (Button) itemView.findViewById(R.id.go_btn);
            root = (RelativeLayout) itemView.findViewById(R.id.root);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm realm = Realm.getDefaultInstance();
        user = realm.where(LargoUser.class).findFirst();
        mReference = FirebaseDatabase.getInstance().getReference();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meets, container, false);
        initViews(view);
        return view;
    }


    private void initViews(View view){
        mNoMeets = (TextView) view.findViewById(R.id.no_meets_yet_tv);
        mMeetsRecycler = (RecyclerView) view.findViewById(R.id.meets_list_rv);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setStackFromEnd(true);
        mMeetsRecycler.setLayoutManager(mLinearLayoutManager);

        mGoBtn = (Button) view.findViewById(R.id.go_btn);
        mFab = (FloatingActionButton) view.findViewById(R.id.add_new_meet_fab_btn);
        mFab.setOnClickListener(this);

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Meet,
                FireMeetsViewHolder>(Meet.class,
                R.layout.item_meet,
                FireMeetsViewHolder.class, mReference.child("meets")) {

            @Override
            protected void populateViewHolder(FireMeetsViewHolder viewHolder, final Meet meet, int position) {
                viewHolder.userNameTextView.setText(meet.getUserName());
                viewHolder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CurrentMeetInfoFragment currentMeetInfoFragment = new CurrentMeetInfoFragment();
                        Bundle args = new Bundle();
                        args.putParcelable(CurrentMeetInfoFragment.EXTRA_MEET_INFO, meet);
                        currentMeetInfoFragment.setArguments(args);
                        replaceFragment(currentMeetInfoFragment);
                    }
                });
                Glide.with(getActivity())
                        .load(meet.getUserPhotoUrl())
                        .into(viewHolder.userPicture);
            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int meetsCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();

                if (lastVisiblePosition == -1 ||
                        (positionStart >= (meetsCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mMeetsRecycler.scrollToPosition(positionStart);
                }
            }
        });
        mMeetsRecycler.setLayoutManager(mLinearLayoutManager);
        mMeetsRecycler.setAdapter(mFirebaseAdapter);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.add_new_meet_fab_btn : replaceFragment(new AddNewMeetFragment()); break;
            case R.id.root : replaceFragment(new CurrentMeetInfoFragment()); break;
            case R.id.go_btn :
        }
    }

    private void replaceFragment(Fragment fragment){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
    }
}
