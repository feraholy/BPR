package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddFundActivity extends AppCompatActivity {
    TextView match_name_addFund,entryfeeAddFund,availBalance,winningBalance,slots_remain;
    UserProfile userProfile;
    MatchInfo matchInfo;
    String currencySign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fund);
        try {
            userProfile = DataHolder.getUserProfile();
            matchInfo = DataHolder.getMatchInfo();
            match_name_addFund = findViewById(R.id.match_name_addFund);
            entryfeeAddFund = findViewById(R.id.entryfeeAddFund);
            availBalance = findViewById(R.id.availBalance);
            winningBalance = findViewById(R.id.winningBalance);
            slots_remain = findViewById(R.id.slots_remain);
            match_name_addFund.setText(matchInfo.getMatchName());
            Toolbar addFundToolbar=findViewById(R.id.addFundToolbar);
            setSupportActionBar(addFundToolbar);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            addFundToolbar.setNavigationIcon(R.drawable.ic_back);
            addFundToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try {
            switch (userProfile.getCurrency()) {
                case "BDT":
                    currencySign = "\u09F3";
                    break;
                case "USD":
                    currencySign = "$";
                    break;
                case "INR":
                    currencySign = "\u20B9";
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            currencySign = "\u09F3";
        }
        try {
            DataHolder.setCurrencySign(currencySign);
            String entryFee = "Entry Fee: " + currencySign + matchInfo.getEntryFee();
            entryfeeAddFund.setText(entryFee);
            String availableBal = "Available: " + currencySign + userProfile.getBalance();
            availBalance.setText(availableBal);
            String winBalance = "Winning Balance " + currencySign + userProfile.getWinningBalance();
            winningBalance.setText(winBalance);
            String people = (100 - Integer.parseInt(matchInfo.getPeopleJoined())) + " Slots Remaining";
            slots_remain.setText(people);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void addFundClicked(View view)
    {
        startActivity(new Intent(AddFundActivity.this,WalletActivity.class));
        finish();
    }
}
