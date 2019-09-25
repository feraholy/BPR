package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import static com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom.MatchAdapter.round;

public class ViewResultActivity extends AppCompatActivity {
    TextView matchNameView,viewMatchTime,viewEntryFee,viewPerKill,viewWinPrize;
    Toolbar resultViewToolbar;
    RecyclerView winnerView;
    RecyclerView fullResultView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ResultInfo resultInfo;
    ResultAdapter winnerAdapter;
    ResultAdapter fullAdapter;
    ArrayList<ResultInfo> winnerInfo;
    ArrayList<ResultInfo> fullInfo;
    private RewardedVideoAd mRewardedVideoAd;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRewardedVideoAd.resume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRewardedVideoAd.pause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRewardedVideoAd.destroy(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_result);
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        loadRewardedVideoAd();
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                mRewardedVideoAd.show();
            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {

            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {

            }

            @Override
            public void onRewardedVideoCompleted() {

            }
        });
        initialize();


    }
    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-4260219196923952/8371655010",
                new AdRequest.Builder().build());
    }

    void initialize()
    {
        try {
            MatchInfo matchInfo = DataHolder.getMatchInfo();
            resultViewToolbar = findViewById(R.id.resultViewToolbar);
            setSupportActionBar(resultViewToolbar);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            resultViewToolbar.setNavigationIcon(R.drawable.ic_back);
            resultViewToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            matchNameView = findViewById(R.id.viewMatchName);
            viewMatchTime = findViewById(R.id.viewMatchTime);
            viewEntryFee = findViewById(R.id.viewEntryFee);
            viewPerKill = findViewById(R.id.viewPerKill);
            viewWinPrize = findViewById(R.id.viewWinPrize);
            matchNameView.setText(matchInfo.getMatchName());
            String textSetter = "Scheduled: " + matchInfo.getMatchTime();
            viewMatchTime.setText(textSetter);
            float currencyConverter;
            switch (DataHolder.getUserProfile().getCurrency()) {
                case "BDT":
                    textSetter = DataHolder.getCurrencySign() + matchInfo.getEntryFee();
                    viewEntryFee.setText(textSetter);
                    textSetter = DataHolder.getCurrencySign() + matchInfo.getPerKill();
                    viewPerKill.setText(textSetter);
                    textSetter = DataHolder.getCurrencySign() + matchInfo.getWinPrize();
                    viewWinPrize.setText(textSetter);
                    break;
                case "USD":
                    float usd = DataHolder.getCurrencyInfo().getUSD();
                    currencyConverter = Float.parseFloat(matchInfo.getEntryFee());
                    currencyConverter = round(currencyConverter / usd, 2);
                    textSetter = DataHolder.getCurrencySign() + currencyConverter;
                    viewEntryFee.setText(textSetter);
                    currencyConverter = Float.parseFloat(matchInfo.getPerKill());
                    currencyConverter = round(currencyConverter / usd, 2);
                    textSetter = DataHolder.getCurrencySign() + currencyConverter;
                    viewPerKill.setText(textSetter);
                    currencyConverter = Float.parseFloat(matchInfo.getWinPrize());
                    currencyConverter = round(currencyConverter / usd, 2);
                    textSetter = DataHolder.getCurrencySign() + currencyConverter;
                    viewWinPrize.setText(textSetter);
                    break;
                case "INR":
                    float inr = DataHolder.getCurrencyInfo().getINR();
                    currencyConverter = Float.parseFloat(matchInfo.getEntryFee());
                    currencyConverter = round(currencyConverter / inr, 2);
                    textSetter = DataHolder.getCurrencySign() + currencyConverter;
                    viewEntryFee.setText(textSetter);
                    currencyConverter = Float.parseFloat(matchInfo.getPerKill());
                    currencyConverter = round(currencyConverter / inr, 2);
                    textSetter = DataHolder.getCurrencySign() + currencyConverter;
                    viewPerKill.setText(textSetter);
                    currencyConverter = Float.parseFloat(matchInfo.getWinPrize());
                    currencyConverter = round(currencyConverter / inr, 2);
                    textSetter = DataHolder.getCurrencySign() + currencyConverter;
                    viewWinPrize.setText(textSetter);
                    break;
            }


            winnerView = findViewById(R.id.winnerViewer);
            fullResultView = findViewById(R.id.fullResultView);
            winnerView.setLayoutManager(new LinearLayoutManager(this));
            fullResultView.setLayoutManager(new LinearLayoutManager(this));
            firebaseDatabase = FirebaseDatabase.getInstance();
            String unique = DataHolder.getMatchInfo().getUniqueId();
            databaseReference = firebaseDatabase.getReference(DataHolder.getUrl()).child(unique).child("result");
            databaseReference.child("chickenDinner").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    winnerInfo = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        try {
                            resultInfo = dataSnapshot1.getValue(ResultInfo.class);
                            winnerInfo.add(resultInfo);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    winnerAdapter = new ResultAdapter(ViewResultActivity.this, winnerInfo);
                    winnerView.setAdapter(winnerAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            databaseReference.child("fullResult").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    fullInfo = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        try {
                            resultInfo = dataSnapshot1.getValue(ResultInfo.class);
                            fullInfo.add(resultInfo);
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    fullAdapter = new ResultAdapter(ViewResultActivity.this, fullInfo);
                    fullResultView.setAdapter(fullAdapter);
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
