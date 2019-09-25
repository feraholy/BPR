package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;


import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingFragment extends Fragment {
    private ArrayList<MatchInfo> pubgMatch;
    private ArrayList<MatchInfo> freefireMatch;
    private RecyclerView recyclerViewPubg;
    private RecyclerView recyclerViewFreefire;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference databaseReference;
    private MatchAdapter matchAdapter;
    private MatchAdapterFreefire matchAdapterFreefire;
    private int countPubg,spinnerPosition,countfreefire,runningPubg,runningFreefire;
    private TextView no_match;
    private LottieAnimationView animationView;
    View view;
    public UpcomingFragment() {
        countPubg = 0;
        countfreefire=0;
        runningPubg=0;
        runningFreefire=0;

        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            super.onCreateView(inflater, container, savedInstanceState);
            view = inflater.inflate(R.layout.fragment_upcoming, container, false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        recyclerViewPubg = view.findViewById(R.id.pubgMatchRecycler);
        recyclerViewFreefire = view.findViewById(R.id.freefireMatchRecycler);
        recyclerViewPubg.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewFreefire.setLayoutManager(new LinearLayoutManager(getActivity()));
        animationView = view.findViewById(R.id.upcoming_not_found);
        Spinner spinner = view.findViewById(R.id.upcomingSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()),
                R.array.gamelistArray, R.layout.spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        no_match = view.findViewById(R.id.no_match);
        Drawable spinnerDrawable = Objects.requireNonNull(spinner.getBackground().getConstantState()).newDrawable();
        spinnerDrawable.setColorFilter(ContextCompat.getColor(getActivity(), R.color.whitish), PorterDuff.Mode.SRC_ATOP);
        spinner.setBackground(spinnerDrawable);
        try {
            loadPubgData();
            loadfreefireData();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerPosition=position;
                notFound(position);
                if(position==0)
                {
                    recyclerViewFreefire.setVisibility(View.INVISIBLE);
                    recyclerViewPubg.setVisibility(View.VISIBLE);
                }
                else if(position==1)
                {
                    recyclerViewFreefire.setVisibility(View.VISIBLE);
                    recyclerViewPubg.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return view;
    }

    private void notFound(int position)
    {

            if ( position == 0 ) {
                if ( countPubg > 0 ) {
                    animationView.cancelAnimation();
                    animationView.setVisibility(View.INVISIBLE);
                    no_match.setVisibility(View.INVISIBLE);
                } else {
                    animationView.setAnimation("not-found.json");
                    animationView.playAnimation();
                    animationView.setVisibility(View.VISIBLE);
                    no_match.setVisibility(View.VISIBLE);
                    no_match.setText("Please Join A PUBG Match From Home->Play");
                }
            } else if ( position == 1 ) {
                if ( countfreefire > 0 ) {
                    animationView.cancelAnimation();
                    animationView.setVisibility(View.INVISIBLE);
                    no_match.setVisibility(View.INVISIBLE);
                } else {
                    animationView.setAnimation("not-found.json");
                    animationView.playAnimation();
                    animationView.setVisibility(View.VISIBLE);
                    no_match.setVisibility(View.VISIBLE);
                    no_match.setText("Please Join A Freefire Match From Home->Play");
                }
            }

    }



    private void loadPubgData() {
        try {
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("PubgMatch");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    countPubg = 0;
                    runningPubg = 0;
                    pubgMatch = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        boolean joined = false;
                        for (DataSnapshot pubgMatch : dataSnapshot1.child("joinedPlayerInfo").getChildren()) {
                            if ( Objects.equals(pubgMatch.getKey(), FirebaseAuth.getInstance().getUid()) ) {
                                joined = true;
                            }
                        }
                        MatchInfo match=null;
                        try {
                            match = dataSnapshot1.getValue(MatchInfo.class);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        if(match!=null) {
                            if ( !match.isPlayingState() && joined ) {
                                pubgMatch.add(match);
                                countPubg++;
                                if ( spinnerPosition == 0 ) {
                                    notFound(spinnerPosition);
                                }

                            }
                            if ( !match.isPlayingState() ) {
                                runningPubg++;
                            }
                        }
                    }
                    matchAdapter = new MatchAdapter(getActivity(), pubgMatch);
                    recyclerViewPubg.setAdapter(matchAdapter);
                    DataHolder.setRunningPubg(runningPubg);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void loadfreefireData() {
        try {
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("FreefireMatch");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    countfreefire = 0;
                    runningFreefire = 0;
                    freefireMatch = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        boolean joined = false;
                        for (DataSnapshot freefireMatch : dataSnapshot1.child("joinedPlayerInfo").getChildren()) {
                            if ( Objects.equals(freefireMatch.getKey(), FirebaseAuth.getInstance().getUid()) ) {
                                joined = true;

                            }
                        }
                        MatchInfo match=null;
                        try {
                            match = dataSnapshot1.getValue(MatchInfo.class);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        if(match!=null) {
                            if ( !match.isPlayingState() && joined ) {
                                freefireMatch.add(match);
                                countfreefire++;
                                if ( spinnerPosition == 1 ) {
                                    notFound(spinnerPosition);
                                }

                            }
                            if ( !match.isPlayingState() ) {
                                runningFreefire++;
                            }
                        }
                    }
                    matchAdapterFreefire = new MatchAdapterFreefire(getActivity(), freefireMatch);
                    recyclerViewFreefire.setAdapter(matchAdapterFreefire);
                    DataHolder.setRunningFreefire(runningFreefire);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

//    private void loadRosData() {
//        try {
//            firebaseDatabase = FirebaseDatabase.getInstance();
//            databaseReference = firebaseDatabase.getReference("RosMatch");
//            databaseReference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    countRos = 0;
//                    runningRos = 0;
//                    rosMatch = new ArrayList<MatchInfo>();
//                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                        boolean joined = false;
//                        for (DataSnapshot rosMatch : dataSnapshot1.child("joinedPlayerInfo").getChildren()) {
//                            if ( Objects.equals(rosMatch.getKey(), FirebaseAuth.getInstance().getUid()) ) {
//                                joined = true;
//                            }
//                        }
//                        MatchInfo match=null;
//                        try {
//                            match = dataSnapshot1.getValue(MatchInfo.class);
//                        }
//                        catch (Exception e)
//                        {
//                            e.printStackTrace();
//                        }
//                        if(match!=null) {
//                            if ( !match.isPlayingState() && joined ) {
//                                rosMatch.add(match);
//                                countRos++;
//                                if ( spinnerPosition == 2 ) {
//                                    notFound(spinnerPosition);
//                                }
//
//
//                            }
//                            if ( !match.isPlayingState() ) {
//                                runningRos++;
//                            }
//                        }
//
//
//                    }
//                    matchAdapterRos = new MatchAdapterRos(getActivity(), rosMatch);
//                    recyclerViewRos.setAdapter(matchAdapterRos);
//                    DataHolder.setRunningRos(runningRos);
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }

}
