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
public class LiveFragment extends Fragment {
    private ArrayList<MatchInfo> pubgMatch,freefireMatch;
    private RecyclerView pubgMatchRecyclerLive,freefireMatchRecyclerLive;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private MatchAdapter matchAdapter;
    private MatchAdapterFreefire matchAdapterFreefire;
    private int countPubg,spinnerPosition,countfreefire,runningPubg,runningfreefire;

    private TextView live_message;
    private LottieAnimationView animationView;


    public LiveFragment() {
        countPubg = 0;
        countfreefire=0;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_live, container, false);
        pubgMatchRecyclerLive=view.findViewById(R.id.pubgMatchRecyclerLive);
        freefireMatchRecyclerLive=view.findViewById(R.id.freefireMatchRecyclerLive);
        pubgMatchRecyclerLive.setLayoutManager(new LinearLayoutManager(getActivity()));
        freefireMatchRecyclerLive.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseDatabase= FirebaseDatabase.getInstance();
        animationView = view.findViewById(R.id.live_animation);
        live_message=view.findViewById(R.id.live_message);
        Spinner spinner = view.findViewById(R.id.liveSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()),
                R.array.gamelistArray, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Drawable spinnerDrawable = Objects.requireNonNull(spinner.getBackground().getConstantState()).newDrawable();
        spinnerDrawable.setColorFilter(ContextCompat.getColor(getActivity(), R.color.whitish), PorterDuff.Mode.SRC_ATOP);
        spinner.setBackground(spinnerDrawable);
        try {
            loadPubgData();
            loadFreefireData();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerPosition=position;
                notFound(spinnerPosition);
                if(position==0)
                {
                    freefireMatchRecyclerLive.setVisibility(View.INVISIBLE);
                    pubgMatchRecyclerLive.setVisibility(View.VISIBLE);
                }
                else if(position==1)
                {
                    freefireMatchRecyclerLive.setVisibility(View.VISIBLE);
                    pubgMatchRecyclerLive.setVisibility(View.INVISIBLE);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        return view;
    }

    private void loadPubgData()
    {
        try {
            databaseReference = firebaseDatabase.getReference("PubgMatch");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    runningPubg = 0;
                    countPubg = 0;
                    pubgMatch = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        MatchInfo match=null;
                        try {
                            match = dataSnapshot1.getValue(MatchInfo.class);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        if(match!=null) {
                            if ( match.isLive() ) {
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
                    DataHolder.setRunningPubg(runningPubg);
                    matchAdapter = new MatchAdapter(getActivity(), pubgMatch);
                    pubgMatchRecyclerLive.setAdapter(matchAdapter);

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

    private void loadFreefireData()
    {
        try {
            databaseReference = firebaseDatabase.getReference("FreefireMatch");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    runningfreefire = 0;
                    countfreefire = 0;
                    freefireMatch = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        MatchInfo match=null;
                        try {
                            match = dataSnapshot1.getValue(MatchInfo.class);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        if(match!=null) {
                            if ( match.isLive() ) {
                                freefireMatch.add(match);
                                countfreefire++;
                                if ( spinnerPosition == 1 ) {
                                    notFound(spinnerPosition);
                                }
                            }
                            if ( !match.isPlayingState() ) {
                                runningfreefire++;
                            }
                        }
                    }
                    DataHolder.setRunningFreefire(runningfreefire);
                    matchAdapterFreefire = new MatchAdapterFreefire(getActivity(), freefireMatch);
                    freefireMatchRecyclerLive.setAdapter(matchAdapterFreefire);

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
//    private void loadRosData()
//    {
//        try {
//            databaseReference = firebaseDatabase.getReference("RosMatch");
//            databaseReference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    runingRos = 0;
//                    countRos = 0;
//                    rosMatch = new ArrayList<MatchInfo>();
//                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                        MatchInfo match=null;
//                        try {
//                            match = dataSnapshot1.getValue(MatchInfo.class);
//                        }
//                        catch (Exception e)
//                        {
//                            e.printStackTrace();
//                        }
//                        if(match!=null) {
//                            if ( match.isLive() ) {
//                                rosMatch.add(match);
//                                countRos++;
//                                if ( spinnerPosition == 1 ) {
//                                    notFound(spinnerPosition);
//                                }
//                            }
//                            if ( !match.isPlayingState() ) {
//                                runingRos++;
//                            }
//                        }
//                    }
//                    DataHolder.setRunningRos(runingRos);
//                    matchAdapterRos = new MatchAdapterRos(getActivity(), rosMatch);
//                    rosMatchRecyclerLive.setAdapter(matchAdapterRos);
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

    private void notFound(int position)
    {
            if ( position == 0 ) {
                if ( countPubg > 0 ) {
                    animationView.cancelAnimation();
                    animationView.setVisibility(View.INVISIBLE);
                    live_message.setVisibility(View.INVISIBLE);
                } else {
                    animationView.setAnimation("not-found.json");
                    animationView.playAnimation();
                    animationView.setVisibility(View.VISIBLE);
                    live_message.setVisibility(View.VISIBLE);
                    live_message.setText("No Live PUBG Match");
                }
            } else if ( position == 1 ) {
                if ( countfreefire > 0 ) {
                    animationView.cancelAnimation();
                    animationView.setVisibility(View.INVISIBLE);
                    live_message.setVisibility(View.INVISIBLE);
                } else {
                    animationView.setAnimation("not-found.json");
                    animationView.playAnimation();
                    animationView.setVisibility(View.VISIBLE);
                    live_message.setVisibility(View.VISIBLE);
                    live_message.setText("No Live Freefire Match");
                }
            }

    }

}
