package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class FreefireActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<MatchInfo> matchInfoList;
    private MatchAdapterFreefire matchAdapterFreefire;
    TextView no_match_freefire;
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freefire);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9931491750102157/3050905958");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mInterstitialAd.show();
            }
        });
        initialize();
    }



    void initialize()
    {

        no_match_freefire=findViewById(R.id.no_match_freefire);
        Toolbar freefireToolbar=findViewById(R.id.freefireToolbar);
        setSupportActionBar(freefireToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        freefireToolbar.setNavigationIcon(R.drawable.ic_back);
        freefireToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView=findViewById(R.id.matchCardsRecyclerfreefire);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;
        try {
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("FreefireMatch");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    matchInfoList = new ArrayList<>();
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
                            if ( !match.isPlayingState() ) {
                                matchInfoList.add(match);
                            }
                        }
                        if(matchInfoList.size()>0)
                        {
                            no_match_freefire.setVisibility(View.INVISIBLE);
                        }
                    }
                    matchAdapterFreefire = new MatchAdapterFreefire(FreefireActivity.this, matchInfoList);
                    recyclerView.setAdapter(matchAdapterFreefire);
                    System.out.println(matchInfoList);


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
}
