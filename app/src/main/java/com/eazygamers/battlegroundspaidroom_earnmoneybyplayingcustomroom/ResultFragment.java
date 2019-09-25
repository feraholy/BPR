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
import java.util.Collections;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment {
    private ArrayList<MatchInfo> pubgMatch,freefireMatch;
    private RecyclerView pubgMatchRecyclerResult,freefireMatchRecyclerResult;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private MatchAdapter matchAdapter;
    private MatchAdapterFreefire matchAdapterFreefire;
    private int countPubg,spinnerPosition,countfreefire;
    private TextView result_message;
    private LottieAnimationView animationView;

    public ResultFragment() {
        countPubg = 0;
        countfreefire=0;

        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_result, container, false);
        pubgMatchRecyclerResult=view.findViewById(R.id.pubgRecycleViewResult);
        freefireMatchRecyclerResult=view.findViewById(R.id.freefireRecycleViewResult);
        pubgMatchRecyclerResult.setLayoutManager(new LinearLayoutManager(getActivity()));
        freefireMatchRecyclerResult.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseDatabase= FirebaseDatabase.getInstance();
        animationView = view.findViewById(R.id.resultNotFound);
        result_message=view.findViewById(R.id.result_messag);
        Spinner spinner = view.findViewById(R.id.resultSpinner);
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
                    freefireMatchRecyclerResult.setVisibility(View.INVISIBLE);
                    pubgMatchRecyclerResult.setVisibility(View.VISIBLE);
                }
                else if(position==1)
                {
                    freefireMatchRecyclerResult.setVisibility(View.VISIBLE);
                    pubgMatchRecyclerResult.setVisibility(View.INVISIBLE);
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
                            System.out.println(match.isResultPublished());
                            if ( match.isResultPublished() ) {
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
                    pubgMatchRecyclerResult.setAdapter(matchAdapter);
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
                            if ( match.isResultPublished() ) {
                                freefireMatch.add(match);
                                countfreefire++;
                                if ( spinnerPosition == 1 ) {
                                    notFound(spinnerPosition);
                                }
                            }
                        }
                    }
                    matchAdapterFreefire = new MatchAdapterFreefire(getActivity(), freefireMatch);
                    freefireMatchRecyclerResult.setAdapter(matchAdapterFreefire);

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
//                            if ( match.isResultPublished() ) {
//                                rosMatch.add(match);
//                                countRos++;
//                                if ( spinnerPosition == 1 ) {
//                                    notFound(spinnerPosition);
//                                }
//                            }
//                        }
//                    }
//                    matchAdapterRos = new MatchAdapterRos(getActivity(), rosMatch);
//                    rosMatchRecyclerResult.setAdapter(matchAdapterRos);
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
                    result_message.setVisibility(View.INVISIBLE);
                } else {
                    animationView.setAnimation("glasses-dance.json");
                    animationView.playAnimation();
                    animationView.setVisibility(View.VISIBLE);
                    result_message.setVisibility(View.VISIBLE);
                    result_message.setText("No Result Published For PUBG Match");
                }
            } else if ( position == 1 ) {
                if ( countfreefire > 0 ) {
                    animationView.cancelAnimation();
                    animationView.setVisibility(View.INVISIBLE);
                    result_message.setVisibility(View.INVISIBLE);
                } else {
                    animationView.setAnimation("glasses-dance.json");
                    animationView.playAnimation();
                    animationView.setVisibility(View.VISIBLE);
                    result_message.setVisibility(View.VISIBLE);
                    result_message.setText("No Result Published For Freefire Match");
                }
            }
        }


}
