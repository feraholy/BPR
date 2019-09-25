package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {
    private Context ctx;
    private ArrayList<MatchInfo> matchInfos;
    private UserProfile userProfile;
    private String pubgUserName,key;
    AlertDialog.Builder builder;
    private int execTask;
    private long people;
    private boolean checkConfirm = false;
    private float currencyConverter,userBalance,winning;
    private Query query;
    private DatabaseReference databaseReference,pubgDatabase;
    private MatchViewHolder holder;
    int limit;
    private InterstitialAd mInterstitialAd;
    MatchAdapter(Context c, ArrayList<MatchInfo> match) {
        ctx = c;
        matchInfos = match;
        execTask = 0;
        currencyConverter= (float) 1.0;
        pubgUserName=null;
        pubgDatabase=null;

    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MatchViewHolder(LayoutInflater.from(ctx).inflate(R.layout.match_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MatchViewHolder holder, final int position) {
        String textSetter;

        this.holder=holder;
        //Database Initialize

        try {

            databaseReference = FirebaseDatabase.getInstance().getReference("userInfo").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
            pubgDatabase = FirebaseDatabase.getInstance().getReference("PubgMatch").child(matchInfos.get(position).getUniqueId());
            query = pubgDatabase.child("joinedPlayerInfo").orderByKey();
            final DatabaseReference peopleData=FirebaseDatabase.getInstance().getReference("PubgMatch").child(matchInfos.get(position).getUniqueId()).child("joinedPlayerInfo");
            peopleData.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        if ( matchInfos.get(position).getPeopleLimit() == null ) {
                            matchInfos.get(position).setPeopleLimit("99");
                         }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();

                    }
                    limit=Integer.parseInt(matchInfos.get(position).getPeopleLimit());
                    people=dataSnapshot.getChildrenCount();
                    String peopleJoin = people + "/"+(limit+1)+ " People Joined";
                    holder.peopleJoined.setText(peopleJoin);
                    holder.progressBar.setProgress((int)people);
                    if ( people > Integer.parseInt(matchInfos.get(position).getPeopleLimit()) ) {
                        holder.joinButton.setText("Room Full");
                        holder.joinButton.setEnabled(false);
                        holder.joinButton.setClickable(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ctx,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                }
            });

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        //Database Initialize End
        //View Initialize
        try {
            holder.matchName.setText(matchInfos.get(position).getMatchName());
            textSetter = "Schedule: " + matchInfos.get(position).getMatchTime();
            holder.matchTime.setText(textSetter);
            holder.mapName.setText(matchInfos.get(position).getMap());
            holder.typeName.setText(matchInfos.get(position).getType());
            holder.versionType.setText(matchInfos.get(position).getVersion());


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try {
            switch (DataHolder.getUserProfile().getCurrency()) {
                case "BDT":
                    try {
                        textSetter = DataHolder.getCurrencySign() + matchInfos.get(position).getWinPrize();
                        holder.winPrizeValue.setText(textSetter);
                        textSetter = DataHolder.getCurrencySign() + matchInfos.get(position).getEntryFee();
                        holder.entryFeeValue.setText(textSetter);
                        textSetter = DataHolder.getCurrencySign() + matchInfos.get(position).getPerKill();
                        holder.perKillValue.setText(textSetter);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case "USD":
                    try {
                        float usd = DataHolder.getCurrencyInfo().getUSD();
                        currencyConverter = Float.parseFloat(matchInfos.get(position).getWinPrize());
                        currencyConverter = round(currencyConverter / usd, 2);
                        textSetter = DataHolder.getCurrencySign() + currencyConverter;
                        holder.winPrizeValue.setText(textSetter);
                        currencyConverter = Float.parseFloat(matchInfos.get(position).getEntryFee());
                        currencyConverter = round(currencyConverter / usd, 2);
                        textSetter = DataHolder.getCurrencySign() + currencyConverter;
                        holder.entryFeeValue.setText(textSetter);
                        currencyConverter = Float.parseFloat(matchInfos.get(position).getPerKill());
                        currencyConverter = round(currencyConverter / usd, 2);
                        textSetter = DataHolder.getCurrencySign() + matchInfos.get(position).getPerKill();
                        holder.perKillValue.setText(textSetter);
                        currencyConverter = usd;
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case "INR":
                    try {
                        float inr = DataHolder.getCurrencyInfo().getINR();
                        currencyConverter = Float.parseFloat(matchInfos.get(position).getWinPrize());
                        currencyConverter = round(currencyConverter / inr, 2);
                        textSetter = DataHolder.getCurrencySign() + currencyConverter;
                        holder.winPrizeValue.setText(textSetter);
                        currencyConverter = Float.parseFloat(matchInfos.get(position).getEntryFee());
                        currencyConverter = round(currencyConverter / inr, 2);
                        textSetter = DataHolder.getCurrencySign() + currencyConverter;
                        holder.entryFeeValue.setText(textSetter);
                        currencyConverter = Float.parseFloat(matchInfos.get(position).getPerKill());
                        currencyConverter = round(currencyConverter / inr, 2);
                        textSetter = DataHolder.getCurrencySign() + matchInfos.get(position).getPerKill();
                        holder.perKillValue.setText(textSetter);
                        currencyConverter = inr;
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    break;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        //View Initialize End




        //Trying To Get PUBG Username if Available
        try
        {
            pubgUserName=DataHolder.getUserProfile().getPubgUserName().trim();
            if(pubgUserName.trim().isEmpty())
            {
                pubgUserName=null;
            }
        }
        catch (Exception e)
        {
            pubgUserName=null;
            e.printStackTrace();
        }

        //Dialogue Builder
        builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Enter PUBG Username");
        final EditText input = new EditText(ctx);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setPositiveButton("Confirm!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pubgUserName=input.getText().toString().trim();
                if(pubgUserName.length()==0)
                {
                    Toast.makeText(ctx,"Please Enter Your PUBG Username",Toast.LENGTH_SHORT).show();
                    pubgUserName=null;
                }
                holder.joinButton.performClick();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(ctx);
        builder1.setTitle("Confirm?");
        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                holder.joinButton.performClick();
                checkConfirm = true;
                dialog.dismiss();
            }
        });
        builder1.setNegativeButton("No!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                execTask=0;
                dialog.cancel();
            }
        });
        builder1.setCancelable(false);
        //Dialog Builder End

        //Check Whether Player Joined Or Not
        try {
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if ( dataSnapshot.hasChildren() ) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            key = dataSnapshot1.getKey();
                            assert key != null;
                            if ( key.equals(FirebaseAuth.getInstance().getUid()) ) {
                                holder.joinButton.setClickable(false);
                                holder.joinButton.setEnabled(false);
                                holder.joinButton.setText("Joined");

                            }

                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ctx, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        //Check Whether Player Joined Or Not End


        //Join Button's Code
        holder.joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    databaseReference = FirebaseDatabase.getInstance().getReference("userInfo").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
                    pubgDatabase = FirebaseDatabase.getInstance().getReference("PubgMatch").child(matchInfos.get(position).getUniqueId());
                    query = pubgDatabase.child("joinedPlayerInfo").orderByKey();
                    try {
                        if ( matchInfos.get(position).getPeopleLimit() == null ) {
                            matchInfos.get(position).setPeopleLimit("99");
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    limit=Integer.parseInt(matchInfos.get(position).getPeopleLimit());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                try {
                    if (pubgUserName==null) {
                        ViewGroup viewGroup = (ViewGroup) input.getParent();
                        if ( viewGroup != null ) {
                            viewGroup.removeView(input);
                        }
                        builder.setView(input);
                        builder.show();
                    }

                     else if (checkConfirm || execTask == 0 ) {
                            execTask++;
                            builder1.show();
                        }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                try {
                    DatabaseReference peopleData = FirebaseDatabase.getInstance().getReference("PubgMatch").child(matchInfos.get(position).getUniqueId()).child("joinedPlayerInfo");
                    peopleData.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            try {
                                people = dataSnapshot.getChildrenCount();
                                joinOperation(position);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(ctx, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
        //Join Button's Code End

        //Card Click Listener Code
        holder.cardViewPubg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataHolder.setUrl("PubgMatch");
                DataHolder.setMatchInfo(matchInfos.get(position));
                ctx.startActivity(new Intent(ctx, MatchDetailsActivity.class));
            }
        });
        //Card Click Listener Code End

        //View Result Click Listener Code
        holder.viewResultPubg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataHolder.setMatchInfo(matchInfos.get(position));
                DataHolder.setUrl("PubgMatch");
                ctx.startActivity(new Intent(ctx, ViewResultActivity.class));
            }
        });
        //View Result Click Listener Code End

        //Button State Changing Code
        try {

            if ( matchInfos.get(position).isResultPublished() ) {
                holder.joinButton.setClickable(false);
                holder.joinButton.setEnabled(false);
                holder.joinButton.setVisibility(View.INVISIBLE);
                holder.viewResultPubg.setVisibility(View.VISIBLE);

            } else if ( matchInfos.get(position).isLive() ) {
                holder.joinButton.setText("Live");
                holder.joinButton.setEnabled(false);
                holder.joinButton.setClickable(false);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        //Button State Changing Code



    }

    @Override
    public int getItemCount() {
        return matchInfos.size();
    }

    class MatchViewHolder extends RecyclerView.ViewHolder {
        TextView matchName, matchTime, winPrizeValue, entryFeeValue, perKillValue, mapName, typeName, versionType, peopleJoined;
        ProgressBar progressBar;
        Button joinButton, viewResultPubg;
        CardView cardViewPubg;

        MatchViewHolder(@NonNull View itemView) {
            super(itemView);
            matchName = itemView.findViewById(R.id.matchName);
            matchTime = itemView.findViewById(R.id.matchTime);
            winPrizeValue = itemView.findViewById(R.id.winPrizeValue);
            entryFeeValue = itemView.findViewById(R.id.entryFeeValue);
            perKillValue = itemView.findViewById(R.id.perKillValue);
            mapName = itemView.findViewById(R.id.mapName);
            typeName = itemView.findViewById(R.id.typeName);
            versionType = itemView.findViewById(R.id.versionType);
            progressBar = itemView.findViewById(R.id.joinedBar);
            peopleJoined = itemView.findViewById(R.id.peopleJoined);
            joinButton = itemView.findViewById(R.id.joinButton);
            pubgUserName = null;
            viewResultPubg = itemView.findViewById(R.id.viewResultPubg);
            cardViewPubg = itemView.findViewById(R.id.cardViewPubg);

        }


    }

    static float round(float d, int decimalPlace) {

        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    //Method For Knowing Latest Number Of People Joined In The Match

    private void joinOperation(final int position)
    {


        try {
            final float entryFeeRequired = round(Float.parseFloat(matchInfos.get(position).getEntryFee()),2) / currencyConverter;
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        userProfile = dataSnapshot.getValue(UserProfile.class);
                        assert userProfile != null;
                        userBalance = round(Float.parseFloat(userProfile.getBalance()),2);
                        userBalance=round(userBalance,2);
                        winning=round(Float.parseFloat(userProfile.getWinningBalance()),2);



                    }
                    catch (Exception e)
                    {
                        userBalance=0;
                        e.printStackTrace();
                    }
                    try {
                        if ( people >limit ) {
                            holder.joinButton.setText("Room Full");
                            holder.joinButton.setEnabled(false);
                            holder.joinButton.setClickable(false);

                        }
                        else {
                            if ( userBalance >= entryFeeRequired && pubgUserName != null && checkConfirm) {
                                checkConfirm = false;
                                try {
                                    String confirmPeople = people+1+"";
                                    userBalance = round((userBalance - entryFeeRequired), 2);
                                    winning=winning-entryFeeRequired;

                                    pubgDatabase.child("peopleJoined").setValue(confirmPeople);
                                    databaseReference.child("balance").setValue(userBalance + "");
                                    databaseReference.child("pubgUserName").setValue(pubgUserName);
                                    pubgDatabase.child("joinedPlayerInfo").child(userProfile.getUid()).child("username").setValue(pubgUserName);
                                    mInterstitialAd = new InterstitialAd(ctx);
                                    mInterstitialAd.setAdUnitId("ca-app-pub-4260219196923952/2803154632");
                                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                                    mInterstitialAd.setAdListener(new AdListener(){

                                        @Override
                                        public void onAdLoaded() {
                                            super.onAdLoaded();
                                            mInterstitialAd.show();
                                        }
                                    });
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            } else if ( userBalance < entryFeeRequired ) {
                                DataHolder.setMatchInfo(matchInfos.get(position));
                                ctx.startActivity(new Intent(ctx, AddFundActivity.class));
                                mInterstitialAd = new InterstitialAd(ctx);
                                mInterstitialAd.setAdUnitId("ca-app-pub-4260219196923952/2803154632");
                                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                                mInterstitialAd.setAdListener(new AdListener(){

                                    @Override
                                    public void onAdLoaded() {
                                        super.onAdLoaded();
                                        mInterstitialAd.show();
                                    }
                                });
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ctx, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}

