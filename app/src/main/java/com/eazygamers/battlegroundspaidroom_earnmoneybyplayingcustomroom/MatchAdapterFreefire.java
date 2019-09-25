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

import java.util.ArrayList;
import java.util.Objects;

import static com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom.MatchAdapter.round;

public class MatchAdapterFreefire extends RecyclerView.Adapter<MatchAdapterFreefire.MatchViewHolderFreefire> {
    private Context ctx;
    private ArrayList<MatchInfo> matchInfos;
    private UserProfile userProfile;
    private String freefireUserName, key;
    AlertDialog.Builder builder;
    private int execTask;
    private long people;
    private boolean checkConfirm = false;
    private float currencyConverter,userBalance;
    private Query query;
    private DatabaseReference databaseReference, freefireDatabase;
    private MatchViewHolderFreefire holder;
    int limit;
    private InterstitialAd mInterstitialAd;
    MatchAdapterFreefire(Context ctx, ArrayList<MatchInfo> matchInfos) {
        this.ctx = ctx;
        this.matchInfos = matchInfos;
        execTask = 0;
        currencyConverter = (float) 1.0;
        freefireUserName = null;
        freefireDatabase=null;
    }

    @NonNull
    @Override
    public MatchViewHolderFreefire onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MatchViewHolderFreefire(LayoutInflater.from(ctx).inflate(R.layout.freefire_match_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MatchViewHolderFreefire holder, final int position) {
        String textSetter;
        this.holder=holder;
        //Database Initialize
        try {
            databaseReference = FirebaseDatabase.getInstance().getReference("userInfo").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
            freefireDatabase = FirebaseDatabase.getInstance().getReference("FreefireMatch").child(matchInfos.get(position).getUniqueId());
            query = freefireDatabase.child("joinedPlayerInfo").orderByKey();
            final DatabaseReference peopleData=FirebaseDatabase.getInstance().getReference("FreefireMatch").child(matchInfos.get(position).getUniqueId()).child("joinedPlayerInfo");
            peopleData.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    try {
                        if ( matchInfos.get(position).getPeopleLimit() == null ) {
                            matchInfos.get(position).setPeopleLimit("49");
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();

                    }
                    limit=Integer.parseInt(matchInfos.get(position).getPeopleLimit());
                    people=dataSnapshot.getChildrenCount();
                    String peopleJoin = people + "/"+(limit+1)+ " People Joined";
                    holder.peopleJoinedfreefire.setText(peopleJoin);
                    holder.progressBarfreefire.setProgress((int)people);
                    if ( people > limit ) {
                        holder.joinButtonfreefire.setText("Room Full");
                        holder.joinButtonfreefire.setEnabled(false);
                        holder.joinButtonfreefire.setClickable(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ctx,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Database Initialize End
        //View Initialize
        try {
            holder.matchNamefreefire.setText(matchInfos.get(position).getMatchName());
            textSetter = "Schedule: " + matchInfos.get(position).getMatchTime();
            holder.matchTimefreefire.setText(textSetter);
            holder.mapNamefreefire.setText(matchInfos.get(position).getMap());
            holder.typeNamefreefire.setText(matchInfos.get(position).getType());
            holder.versionTypefreefire.setText(matchInfos.get(position).getVersion());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            float usd = DataHolder.getCurrencyInfo().getUSD();
            float inr = DataHolder.getCurrencyInfo().getINR();
            switch (DataHolder.getUserProfile().getCurrency()) {
                case "BDT":
                    try {
                        textSetter = DataHolder.getCurrencySign() + matchInfos.get(position).getWinPrize();
                        holder.winPrizeValuefreefire.setText(textSetter);
                        textSetter = DataHolder.getCurrencySign() + matchInfos.get(position).getEntryFee();
                        holder.entryFeeValuefreefire.setText(textSetter);
                        textSetter = DataHolder.getCurrencySign() + matchInfos.get(position).getPerKill();
                        holder.perKillValuefreefire.setText(textSetter);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case "USD":
                    try {
                        currencyConverter = Float.parseFloat(matchInfos.get(position).getWinPrize());
                        currencyConverter = round(currencyConverter / usd, 2);
                        textSetter = DataHolder.getCurrencySign() + currencyConverter;
                        holder.winPrizeValuefreefire.setText(textSetter);
                        currencyConverter = Float.parseFloat(matchInfos.get(position).getEntryFee());
                        currencyConverter = round(currencyConverter / usd, 2);
                        textSetter = DataHolder.getCurrencySign() + currencyConverter;
                        holder.entryFeeValuefreefire.setText(textSetter);
                        currencyConverter = Float.parseFloat(matchInfos.get(position).getPerKill());
                        currencyConverter = round(currencyConverter / usd, 2);
                        textSetter = DataHolder.getCurrencySign() + matchInfos.get(position).getPerKill();
                        holder.perKillValuefreefire.setText(textSetter);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case "INR":
                    try {
                        currencyConverter = Float.parseFloat(matchInfos.get(position).getWinPrize());
                        currencyConverter = round(currencyConverter / inr, 2);
                        textSetter = DataHolder.getCurrencySign() + currencyConverter;
                        holder.winPrizeValuefreefire.setText(textSetter);
                        currencyConverter = Float.parseFloat(matchInfos.get(position).getEntryFee());
                        currencyConverter = round(currencyConverter / inr, 2);
                        textSetter = DataHolder.getCurrencySign() + currencyConverter;
                        holder.entryFeeValuefreefire.setText(textSetter);
                        currencyConverter = Float.parseFloat(matchInfos.get(position).getPerKill());
                        currencyConverter = round(currencyConverter / inr, 2);
                        textSetter = DataHolder.getCurrencySign() + matchInfos.get(position).getPerKill();
                        holder.perKillValuefreefire.setText(textSetter);
                        break;
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
            }
        } catch (NullPointerException e) {
            Toast.makeText(ctx, "Something Wrong!", Toast.LENGTH_LONG).show();
        }
        //View Initialize End




        //Trying To Get Freefire Username if Available
        try {
            freefireUserName = DataHolder.getUserProfile().getFreefireUserName().trim();
            if ( freefireUserName.trim().isEmpty() ) {
                freefireUserName = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Dialogue Builder
        final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Enter Freefire Username");
        final EditText input = new EditText(ctx);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setPositiveButton("Confirm!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                freefireUserName = input.getText().toString().trim();
                if ( freefireUserName.length() == 0 ) {
                    Toast.makeText(ctx, "Please Enter Your Freefire Username", Toast.LENGTH_SHORT).show();
                    freefireUserName = null;
                }
                holder.joinButtonfreefire.performClick();
                dialog.dismiss();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(ctx);
        builder1.setTitle("Confirm?");
        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                holder.joinButtonfreefire.performClick();
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
        //Dialogue Builder End

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
                                holder.joinButtonfreefire.setClickable(false);
                                holder.joinButtonfreefire.setEnabled(false);
                                holder.joinButtonfreefire.setText("Joined");
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ctx, databaseError.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Check Whether Player Joined Or Not End

        //Join Button's Code
        holder.joinButtonfreefire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    databaseReference = FirebaseDatabase.getInstance().getReference("userInfo").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
                    freefireDatabase = FirebaseDatabase.getInstance().getReference("FreefireMatch").child(matchInfos.get(position).getUniqueId());
                    query = freefireDatabase.child("joinedPlayerInfo").orderByKey();
                    try {
                        if ( matchInfos.get(position).getPeopleLimit() == null ) {
                            matchInfos.get(position).setPeopleLimit("49");
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
                    if ( freefireUserName == null ) {
                        ViewGroup viewGroup = (ViewGroup) input.getParent();
                        if ( viewGroup != null ) {
                            viewGroup.removeView(input);
                        }
                        builder.setView(input);
                        builder.show();
                    } else if ( checkConfirm || execTask == 0 ) {
                        execTask++;
                        builder1.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DatabaseReference peopleData=FirebaseDatabase.getInstance().getReference("FreefireMatch").child(matchInfos.get(position).getUniqueId()).child("joinedPlayerInfo");
                peopleData.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            people = dataSnapshot.getChildrenCount();
                            joinOperation(position);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ctx,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });




            }
        });
        //Join Button's Code End

        //View Result Click Listener Code
        holder.viewResultfreefire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataHolder.setMatchInfo(matchInfos.get(position));
                DataHolder.setUrl("FreefireMatch");

                ctx.startActivity(new Intent(ctx, ViewResultActivity.class));
            }
        });
        //View Result Click Listener Code End

        //Card Click Listener Code
        holder.cardViewFreefire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataHolder.setUrl("FreefireMatch");
                DataHolder.setMatchInfo(matchInfos.get(position));
                ctx.startActivity(new Intent(ctx, MatchDetailsActivity.class));
            }
        });
        //Card Click Listener Code

        //Button State Changing Code
        if ( matchInfos.get(position).isResultPublished() ) {
            holder.joinButtonfreefire.setClickable(false);
            holder.joinButtonfreefire.setEnabled(false);
            holder.joinButtonfreefire.setVisibility(View.INVISIBLE);
            holder.viewResultfreefire.setVisibility(View.VISIBLE);

        } else if ( matchInfos.get(position).isLive() ) {
            holder.joinButtonfreefire.setText("Live");
            holder.joinButtonfreefire.setEnabled(false);
            holder.joinButtonfreefire.setClickable(false);
        }
        //Button State Changing Code End




    }

    @Override
    public int getItemCount() {
        return matchInfos.size();
    }

    class MatchViewHolderFreefire extends RecyclerView.ViewHolder {
        TextView matchNamefreefire, matchTimefreefire, winPrizeValuefreefire, entryFeeValuefreefire, perKillValuefreefire, mapNamefreefire, typeNamefreefire, versionTypefreefire, peopleJoinedfreefire;
        ProgressBar progressBarfreefire;
        Button joinButtonfreefire, viewResultfreefire;
        CardView cardViewFreefire;

        MatchViewHolderFreefire(@NonNull View itemView) {
            super(itemView);
            matchNamefreefire = itemView.findViewById(R.id.matchNamefreefire);
            matchTimefreefire = itemView.findViewById(R.id.matchTimefreefire);
            winPrizeValuefreefire = itemView.findViewById(R.id.winPrizeValuefreefire);
            entryFeeValuefreefire = itemView.findViewById(R.id.entryFeeValuefreefire);
            perKillValuefreefire = itemView.findViewById(R.id.perKillValuefreefire);
            mapNamefreefire = itemView.findViewById(R.id.mapNamefreefire);
            typeNamefreefire = itemView.findViewById(R.id.typeNamefreefire);
            versionTypefreefire = itemView.findViewById(R.id.versionTypefreefire);
            progressBarfreefire = itemView.findViewById(R.id.joinedBarfreefire);
            peopleJoinedfreefire = itemView.findViewById(R.id.peopleJoinedfreefire);
            joinButtonfreefire = itemView.findViewById(R.id.joinButtonfreefire);
            viewResultfreefire = itemView.findViewById(R.id.viewResultfreefire);
            cardViewFreefire = itemView.findViewById(R.id.cardViewFreefire);

        }
    }
    private void joinOperation(final int position)
    {
        try {
            final float entryFeeRequired = Float.parseFloat(matchInfos.get(position).getEntryFee()) / currencyConverter;
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        userProfile = dataSnapshot.getValue(UserProfile.class);
                        assert userProfile != null;
                        String userBalanceString = userProfile.getBalance();
                        userBalance = Float.valueOf(userBalanceString);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    try {
                        if ( people > limit ) {
                            holder.joinButtonfreefire.setText("Room Full");
                            holder.joinButtonfreefire.setEnabled(false);
                            holder.joinButtonfreefire.setClickable(false);
                        } else {
                            if ( userBalance >= entryFeeRequired && freefireUserName != null && checkConfirm ) {
                                checkConfirm = false;
                                try {
                                    String confirmPeople = people+1+"";
                                    userBalance = round(userBalance - entryFeeRequired, 2);
                                    String balance = userBalance + "";
                                    freefireDatabase.child("peopleJoined").setValue(confirmPeople + "");
                                    databaseReference.child("balance").setValue(balance);
                                    databaseReference.child("freefireUserName").setValue(freefireUserName);
                                    freefireDatabase.child("joinedPlayerInfo").child(userProfile.getUid()).child("username").setValue(freefireUserName);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

