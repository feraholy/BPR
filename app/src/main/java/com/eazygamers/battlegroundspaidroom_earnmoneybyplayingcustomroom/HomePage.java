package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;



public class HomePage extends AppCompatActivity {
    Toolbar topToolbar;
    MyViewPager myviewPager;
    TabLayout bottomTabLayout;
    BottomPageAdapter bottomPageAdapter;
    Button wallet;
    UserProfile userProfile;
    int time;

    private int[] tabIcons = {
            R.drawable.ic_career,
            R.drawable.ic_home,
            R.drawable.ic_profile_tab
    };

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Confirm Exit?");
        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
                dialog.dismiss();
            }
        });
        builder1.setNegativeButton("No!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder1.setCancelable(false);
        builder1.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);





        time=0;
        final ProgressDialog progressDialog=new ProgressDialog(HomePage.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait! Loading Latest Data");
        progressDialog.show();
        Thread timer=new Thread(){
            public void run()
            {
                try {
                    while(DataHolder.getUserProfile()==null || DataHolder.getCurrencyInfo()==null)
                    {
                            loadUserProfile();
                            loadCurrency();
                            time++;
                            if(time>30)
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(HomePage.this,"Your Internet Connection Is too slow\nTry Again Later",Toast.LENGTH_LONG).show();
                                    }
                                });
                                break;
                            }
                            Thread.sleep(1000);

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                initialize();
                                progressDialog.dismiss();
                            }
                            catch (IllegalArgumentException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });


                } catch (InterruptedException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(HomePage.this,"Something Going Wrong. Contact Support",Toast.LENGTH_LONG).show();
                        }
                    });
                    e.printStackTrace();
                }
            }

        };
        timer.start();

        try {
            DatabaseReference user = FirebaseDatabase.getInstance().getReference("userInfo").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
            user.child("appVersion").setValue(DataHolder.getVersion());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.d("FirebaseInstanceId","Success");
                        }


                    }
                });

}
//    private void loadConfig() {
//        DatabaseReference config = FirebaseDatabase.getInstance().getReference("config");
//        config.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                try {
//                    configInfo = dataSnapshot.getValue(ConfigInfo.class);
//                    if ( configInfo != null && configInfo.getVersionCheck()!=null ) {
//                        try {
//                            DataHolder.setConfigInfo(configInfo);
//                            String version = DataHolder.getVersion();
//                            if ( configInfo.getStatus() == 1 || !configInfo.getVersionCheck().equals(version) ) {
//                                startActivity(new Intent(HomePage.this, ConifgActivity.class));
//                                finish();
//                            }
//                        }
//                        catch (Exception e)
//                        {
//                            e.printStackTrace();
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(HomePage.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
//    }
    private void loadCurrency()
    {
        try {
            DatabaseReference databaseReference;
            databaseReference = FirebaseDatabase.getInstance().getReference("currencyRate");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        CurrencyInfo currencyInfo = dataSnapshot.getValue(CurrencyInfo.class);
                        DataHolder.setCurrencyInfo(currencyInfo);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
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
    private void loadUserProfile()
    {
        try {
            userProfile=null;
            final DatabaseReference databaseReference;
            final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            databaseReference = FirebaseDatabase.getInstance().getReference("userInfo").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        float balance=0;
                        userProfile = dataSnapshot.getValue(UserProfile.class);
                        if(userProfile!=null) {
                            DataHolder.setUserProfile(userProfile);
                            try {
                                balance = Float.parseFloat(userProfile.getBalance());
                            }
                            catch (Exception e)
                            {
                                databaseReference.child("balance").setValue("0");
                            }
                            if(balance<1)
                            {
                                databaseReference.child("balance").setValue("0");
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    assert userProfile != null;
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
                    }
                    catch (NullPointerException e)
                    {
                        e.printStackTrace();
                        DataHolder.setCurrencySign("\u09F3");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(HomePage.this, LoginActivity.class));
                }
            });


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    private void initialize()
    {

        if ( android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O ) {
            NotificationChannel channel=new NotificationChannel("BattleGround's Paid Room","Notification", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager=getSystemService(NotificationManager.class);

            manager.createNotificationChannel(channel);
        }
        FirebaseMessaging.getInstance().subscribeToTopic("all").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!(task.isSuccessful()))
                {
                    Log.d("FirebaseMessaging","Not Success");
                }
            }
        });

        wallet=findViewById(R.id.walletButton);
        topToolbar=findViewById(R.id.topToolbar);
        setSupportActionBar(topToolbar);
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        topToolbar.setNavigationIcon(R.mipmap.ic_launcher_round);
//        topToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(HomePage.this,"Be Ready With Your Battle",Toast.LENGTH_SHORT).show();
//            }
//        });
        myviewPager=findViewById(R.id.viewPager);
        setSupportActionBar(topToolbar);
        bottomTabLayout=findViewById(R.id.bottomTabLayout);
        bottomPageAdapter=new BottomPageAdapter(getSupportFragmentManager(),bottomTabLayout.getTabCount());
        myviewPager.setAdapter(bottomPageAdapter);
        myviewPager.setPagingEnabled(false);
        myviewPager.setOffscreenPageLimit(3);

        myviewPager.setCurrentItem(1,false);
        TabLayout.Tab tab = bottomTabLayout.getTabAt(1);
        assert tab != null;
        tab.select();

        bottomTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                myviewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        setUpTabIcon();
        //wallet.setText(DataHolder.getCurrencySign());





    }

    public void walletClicked(View view)
    {
        startActivity(new Intent(HomePage.this,WalletActivity.class));
    }



    private void setUpTabIcon()
    {
        Objects.requireNonNull(bottomTabLayout.getTabAt(0)).setIcon(tabIcons[0]);
        Objects.requireNonNull(bottomTabLayout.getTabAt(1)).setIcon(tabIcons[1]);
        Objects.requireNonNull(bottomTabLayout.getTabAt(2)).setIcon(tabIcons[2]);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

}
