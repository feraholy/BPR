package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.Objects;

public class WelcomeScreen extends AppCompatActivity {
    ShimmerTextView nameHeading;
    ConfigInfo configInfo;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    Task userTask;
    int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_welcome_screen);
            MobileAds.initialize(this, "ca-app-pub-4260219196923952~4244707746");
            FirebaseApp.initializeApp(this);
            //loadCurrency();
            // loadUserProfile();


            time = 0;
            nameHeading = findViewById(R.id.nameHeading);
            Shimmer shimmer = new Shimmer();
            shimmer.start(nameHeading);
            if ( FirebaseAuth.getInstance().getCurrentUser() != null ) {
                userTask = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).reload();
            }
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();

            Thread timer = new Thread() {
                public void run() {
                    try {

                        if ( FirebaseAuth.getInstance().getCurrentUser() != null ) {
                            while (DataHolder.getUserProfile() == null || DataHolder.getCurrencyInfo() == null || DataHolder.getConfigInfo()==null) {
                                loadUserProfile();
                                loadCurrency();
                                loadConfig();
                                time++;
                                if ( time > 30 ) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(WelcomeScreen.this,"Please Check Your Internet Connection",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    firebaseAuth.signOut();

                                    break;
                                }
                                Thread.sleep(3000);
                            }
                            load();
                        } else {
                            Thread.sleep(3000);
                            load();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            };
            timer.start();
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseAuth.getInstance().signOut();
            load();
        }

    }

    private void loadConfig() {
        DatabaseReference config = FirebaseDatabase.getInstance().getReference("config");
        config.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    configInfo = dataSnapshot.getValue(ConfigInfo.class);
                    if ( configInfo != null ) {
                        DataHolder.setConfigInfo(configInfo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(WelcomeScreen.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadCurrency() {
        try {
            DatabaseReference databaseReference;
            databaseReference = FirebaseDatabase.getInstance().getReference("currencyRate");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    CurrencyInfo currencyInfo = dataSnapshot.getValue(CurrencyInfo.class);
                    DataHolder.setCurrencyInfo(currencyInfo);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadUserProfile() {
        try {
            DatabaseReference databaseReference;
            final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            databaseReference = FirebaseDatabase.getInstance().getReference("userInfo").child(Objects.requireNonNull(firebaseAuth.getUid()));
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                    if(userProfile!=null) {
                        DataHolder.setUserProfile(userProfile);
                        try {
                            switch (userProfile.getCurrency()) {
                                case "BDT":
                                    DataHolder.setCurrencySign("\u09F3");
                                    break;
                                case "USD":
                                    DataHolder.setCurrencySign("$");
                                    break;
                                case "INR":
                                    DataHolder.setCurrencySign("\u20B9");
                                    break;
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(WelcomeScreen.this, LoginActivity.class));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void load() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final InterstitialAd mInterstitialAd = new InterstitialAd(WelcomeScreen.this);
                mInterstitialAd.setAdUnitId("ca-app-pub-4260219196923952/6165607154");
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                mInterstitialAd.setAdListener(new AdListener(){
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        mInterstitialAd.show();
                    }
                });
            }
        });


        String version = "0.3";
        DataHolder.setVersion(version);
        FirebaseUser firebaseUser12=null;
        try {
            firebaseUser12 = FirebaseAuth.getInstance().getCurrentUser();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
            if (  firebaseUser12!= null ) {

                try {
                    if ( configInfo.getStatus() == 1 || !configInfo.getVersionCheck().equals(version) ) {
                        startActivity(new Intent(WelcomeScreen.this, ConifgActivity.class));
                    } else {
                        startActivity(new Intent(WelcomeScreen.this, HomePage.class));
                    }
                } catch (Exception e) {
                    finish();
                    System.exit(0);
                    e.printStackTrace();
                }
            }
            else {
                startActivity(new Intent(WelcomeScreen.this, LoginActivity.class));
            }
            finish();


    }
}
