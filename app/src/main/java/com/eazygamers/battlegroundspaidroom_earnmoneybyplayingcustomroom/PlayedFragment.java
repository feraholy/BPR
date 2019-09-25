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
import java.util.Collections;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayedFragment extends Fragment {
    private ArrayList<MatchInfo> pubgMatch;
    private ArrayList<MatchInfo> freefireMatch;
    private RecyclerView pubgMatchRecyclerPlayed;
    private RecyclerView freefireMatchRecyclerPlayed;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private MatchAdapter matchAdapter;
    private MatchAdapterFreefire matchAdapterFreefire;
    private LottieAnimationView animationView;
    private TextView playedMessage1;
    private TextView playedMessage2;
    private int countPubg, countfreefire, spinnerPosition;
    private MatchInfo match;

    public PlayedFragment() {
        countPubg = 0;
        countfreefire = 0;
        match = null;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_played, container, false);
        animationView = view.findViewById(R.id.played_animation);
        pubgMatchRecyclerPlayed = view.findViewById(R.id.pubgMatchRecyclerPlayed);
        freefireMatchRecyclerPlayed = view.findViewById(R.id.freefireMatchRecyclerPlayed);

        pubgMatchRecyclerPlayed.setLayoutManager(new LinearLayoutManager(getActivity()));
        freefireMatchRecyclerPlayed.setLayoutManager(new LinearLayoutManager(getActivity()));

        Spinner spinner = view.findViewById(R.id.playedSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()),
                R.array.gamelistArray, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        playedMessage1 = view.findViewById(R.id.playedMessage1);
        playedMessage2 = view.findViewById(R.id.playedMessage2);
        firebaseDatabase = FirebaseDatabase.getInstance();
        try {
            loadPubgData();
            loadfreefireData();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Drawable spinnerDrawable = Objects.requireNonNull(spinner.getBackground().getConstantState()).newDrawable();
        spinnerDrawable.setColorFilter(ContextCompat.getColor(getActivity(), R.color.whitish), PorterDuff.Mode.SRC_ATOP);
        spinner.setBackground(spinnerDrawable);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerPosition = position;
                notFound(position);
                if ( position == 0 ) {
                    pubgMatchRecyclerPlayed.setVisibility(View.VISIBLE);
                    freefireMatchRecyclerPlayed.setVisibility(View.INVISIBLE);

                } else if ( position == 1 ) {
                    freefireMatchRecyclerPlayed.setVisibility(View.VISIBLE);
                    pubgMatchRecyclerPlayed.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    private void loadPubgData() {
        try {
            databaseReference = firebaseDatabase.getReference("PubgMatch");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    countPubg = 0;
                    pubgMatch = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        boolean joined = false;
                        try {
                            for (DataSnapshot pubgMatch : dataSnapshot1.child("joinedPlayerInfo").getChildren()) {
                                if ( Objects.requireNonNull(pubgMatch.getKey()).equals(FirebaseAuth.getInstance().getUid()) ) {
                                    joined = true;
                                }

                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            FirebaseAuth.getInstance().signOut();
                        }

                        try {
                            match = dataSnapshot1.getValue(MatchInfo.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if ( match != null ) {
                            if ( match.isPlayingState() && joined ) {
                                pubgMatch.add(match);
                                countPubg++;
                                if ( spinnerPosition == 0 ) {
                                    notFound(spinnerPosition);
                                }

                            }
                        }
                    }
                    Collections.reverse(pubgMatch);
                    matchAdapter = new MatchAdapter(getActivity(), pubgMatch);
                    pubgMatchRecyclerPlayed.setAdapter(matchAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
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
                    freefireMatch = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        boolean joined = false;
                        for (DataSnapshot freefireMatch : dataSnapshot1.child("joinedPlayerInfo").getChildren()) {
                            if ( Objects.equals(freefireMatch.getKey(), FirebaseAuth.getInstance().getUid()) ) {
                                joined = true;


                            }
                        }
                        match = null;
                        try {
                            match = dataSnapshot1.getValue(MatchInfo.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if ( match != null ) {
                            if ( match.isPlayingState() && joined ) {
                                freefireMatch.add(match);
                                countfreefire++;
                                if ( spinnerPosition == 1 ) {
                                    notFound(spinnerPosition);
                                }
                            }
                        }
                    }
                    matchAdapterFreefire = new MatchAdapterFreefire(getActivity(), freefireMatch);
                    freefireMatchRecyclerPlayed.setAdapter(matchAdapterFreefire);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
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
//                            if ( match.isPlayingState() && joined ) {
//                                rosMatch.add(match);
//                                countRos++;
//                                if ( spinnerPosition == 2 ) {
//                                    notFound(spinnerPosition);
//                                }
//
//                            }
//                        }
//                    }
//                    matchAdapterRos = new MatchAdapterRos(getActivity(), rosMatch);
//                    rosMatchRecyclerPlayed.setAdapter(matchAdapterRos);
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

    private void notFound(int position) {
        if ( position == 0 ) {
            if ( countPubg > 0 ) {
                animationView.cancelAnimation();
                animationView.setVisibility(View.INVISIBLE);
                playedMessage1.setVisibility(View.INVISIBLE);
                playedMessage2.setVisibility(View.INVISIBLE);
            } else {
                animationView.setAnimation("car.zip");
                animationView.playAnimation();
                animationView.setVisibility(View.VISIBLE);
                playedMessage2.setVisibility(View.VISIBLE);
                playedMessage1.setVisibility(View.VISIBLE);
                playedMessage1.setText(R.string.didnot_play_pubg);
            }
        } else if ( position == 1 ) {
            if ( countfreefire > 0 ) {
                animationView.cancelAnimation();
                animationView.setVisibility(View.INVISIBLE);
                playedMessage1.setVisibility(View.INVISIBLE);
                playedMessage2.setVisibility(View.INVISIBLE);
            } else {
                animationView.setAnimation("car.zip");
                animationView.playAnimation();
                animationView.setVisibility(View.VISIBLE);
                playedMessage2.setVisibility(View.VISIBLE);
                playedMessage1.setVisibility(View.VISIBLE);
                playedMessage1.setText(R.string.didnot_play_freefire);
            }
        }

    }

}
