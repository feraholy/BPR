package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class WalletActivity extends AppCompatActivity {
    Toolbar walletToolbar;
    TabLayout walletTablayout;
    ViewPager walletViewPager;
    WalletPageAdapter walletPageAdapter;
    TextView balanceInfoWallet,balancewWallet,winningBalanceWallet,currencyInfoWallet;
    String textSetter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);


        initialize();
        walletTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                walletViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        walletViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(walletTablayout));


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        initialize();
        super.onResume();
    }

    private void initialize()
    {
        try {
            walletToolbar = findViewById(R.id.walletToolbar);
            setSupportActionBar(walletToolbar);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            walletToolbar.setNavigationIcon(R.drawable.ic_back);
            walletToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            walletTablayout = findViewById(R.id.walletTablayout);
            walletViewPager = findViewById(R.id.walletViewPager);
            walletPageAdapter = new WalletPageAdapter(getSupportFragmentManager(), walletTablayout.getTabCount());
            walletViewPager.setAdapter(walletPageAdapter);
            balanceInfoWallet = findViewById(R.id.balanceInfoWallet);
            balancewWallet = findViewById(R.id.balancewWallet);
            winningBalanceWallet = findViewById(R.id.winningBalanceWallet);
            currencyInfoWallet = findViewById(R.id.currencyInfoWallet);

            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("userInfo").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserProfile userProfile=dataSnapshot.getValue(UserProfile.class);
                    if(userProfile!=null)
                    {
                        DataHolder.setUserProfile(userProfile);
                        textSetter = DataHolder.getCurrencySign() +userProfile.getBalance();
                        balanceInfoWallet.setText(textSetter);
                        textSetter = "Balance: " + textSetter;
                        balancewWallet.setText(textSetter);
                        textSetter = "Winning Balance: " + DataHolder.getCurrencySign() + userProfile.getWinningBalance();
                        winningBalanceWallet.setText(textSetter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            textSetter = "Currency: " + DataHolder.getUserProfile().getCurrency();
            currencyInfoWallet.setText(textSetter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
