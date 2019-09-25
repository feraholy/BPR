package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

public class PubgActivity extends AppCompatActivity {
    Toolbar pubgToolbar;
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<MatchInfo> matchInfoList;
    MatchAdapter matchAdapter;
    TextView no_match_pubg;
    LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pubg);

        initialize();


    }


    void initialize()
    {



        no_match_pubg=findViewById(R.id.no_match_pubg);
        pubgToolbar=findViewById(R.id.pubgToolbar);
        setSupportActionBar(pubgToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        pubgToolbar.setNavigationIcon(R.drawable.ic_back);
        pubgToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView=findViewById(R.id.matchCardsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        animationView=findViewById(R.id.pubg_loading);
        new loadData().execute();



    }

    @Override
    protected void onStart() {
        super.onStart();


    }
    private class loadData extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    animationView.setAnimation("glow-loading.json");
                    animationView.playAnimation();
                }
            });
        }

        @Override
        protected Void doInBackground(Void... voids) {
            firebaseDatabase=FirebaseDatabase.getInstance();
            try {
                databaseReference = firebaseDatabase.getReference("PubgMatch");
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

                        }
                        matchAdapter = new MatchAdapter(PubgActivity.this, matchInfoList);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(matchInfoList.size()==0)
                                {

                                    no_match_pubg.setVisibility(View.VISIBLE);
                                }
                                recyclerView.setAdapter(matchAdapter);
                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(PubgActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    animationView.cancelAnimation();
                    animationView.setVisibility(View.INVISIBLE);
                }
            });
        }
    }
}
