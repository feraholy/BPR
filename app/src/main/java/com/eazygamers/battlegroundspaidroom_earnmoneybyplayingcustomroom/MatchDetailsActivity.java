package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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

public class MatchDetailsActivity extends AppCompatActivity {
    TextView typeInfo,mapInfo,versionInfo,timeInfo,entryFeeInfo,winPrizeInfo,perKillInfo,matchName,roomid,roompassword;
    ImageView bannerImage;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<PlayerList> playerLists;
    PlayerList playerList;
    PlayerListAdapter playerListAdapter;
    RecyclerView playerListRecycler;
    MatchInfo matchInfo;
    Button joinButtonMatchDetails,liveStreamButton;
    AlertDialog.Builder builder,builder1;
    String pubgUserName,freefireUserName,roomId,roomPass,liveLink;
    boolean checkConfirm=false;
    int execTask;
    float currencyConverter,userBalance;
    EditText input;
    long people;
    UserProfile userProfile;
    private Query query;
    private DatabaseReference pubgDatabase,freefireDatabase;
    float winning;
    int limit;
    private InterstitialAd mInterstitialAd;
    @Override
    public void onBackPressed() {
        finish();
    }

    public MatchDetailsActivity() {
        execTask=0;
        currencyConverter= (float) 1.0;
        pubgUserName=null;
        freefireUserName=null;
        liveLink=null;
        currencyConverter=0;
        userBalance=0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_match_details);
            mInterstitialAd = new InterstitialAd(this);
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
        catch (OutOfMemoryError e)
        {
            Toast.makeText(this,"Please Clear Some RAM/Memory Of Your Phone",Toast.LENGTH_LONG).show();
            e.printStackTrace();
            finish();
        }

        initialize();
        liveStreamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse(liveLink);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    void initialize()
    {


        try {

            joinButtonMatchDetails = findViewById(R.id.joinButtonMatchDetails);
            liveStreamButton = findViewById(R.id.liveStreamButton);
            matchInfo = DataHolder.getMatchInfo();


            bannerImage = findViewById(R.id.bannerImage);
            if ( DataHolder.getUrl().equals("FreefireMatch") ) {
                bannerImage.setImageResource(R.drawable.freefire_banner);
                bannerImage.setScaleType(ImageView.ScaleType.FIT_XY);
            }
            matchName = findViewById(R.id.matchName);
            playerListRecycler = findViewById(R.id.playerListRecycler);
            playerListRecycler.setLayoutManager(new LinearLayoutManager(this));
            typeInfo = findViewById(R.id.typeInfo);
            mapInfo = findViewById(R.id.mapInfo);
            versionInfo = findViewById(R.id.versionInfo);
            timeInfo = findViewById(R.id.timeInfo);
            entryFeeInfo = findViewById(R.id.entryFeeInfo);
            winPrizeInfo = findViewById(R.id.winPrizeInfo);
            perKillInfo = findViewById(R.id.perKillInfo);


            typeInfo.setText(matchInfo.getType());
            mapInfo.setText(matchInfo.getMap());
            versionInfo.setText(matchInfo.getVersion());
            timeInfo.setText(matchInfo.getMatchTime());

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        String textSetter;

        try {
            switch (DataHolder.getUserProfile().getCurrency()) {
                case "BDT":
                    try {
                        textSetter = DataHolder.getCurrencySign() + matchInfo.getEntryFee();
                        entryFeeInfo.setText(textSetter);
                        textSetter = DataHolder.getCurrencySign() + matchInfo.getWinPrize();
                        winPrizeInfo.setText(textSetter);
                        textSetter = DataHolder.getCurrencySign() + matchInfo.getPerKill();
                        perKillInfo.setText(textSetter);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case "USD":
                    try {
                        float usd = DataHolder.getCurrencyInfo().getUSD();
                        currencyConverter = Float.parseFloat(matchInfo.getEntryFee());
                        currencyConverter = round(currencyConverter / usd, 2);
                        textSetter = DataHolder.getCurrencySign() + currencyConverter;
                        entryFeeInfo.setText(textSetter);
                        currencyConverter = Float.parseFloat(matchInfo.getWinPrize());
                        currencyConverter = round(currencyConverter / usd, 2);
                        textSetter = DataHolder.getCurrencySign() + currencyConverter;
                        winPrizeInfo.setText(textSetter);
                        currencyConverter = Float.parseFloat(matchInfo.getPerKill());
                        currencyConverter = round(currencyConverter / usd, 2);
                        textSetter = DataHolder.getCurrencySign() + currencyConverter;
                        perKillInfo.setText(textSetter);
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
                        currencyConverter = Float.parseFloat(matchInfo.getEntryFee());
                        currencyConverter = round(currencyConverter / inr, 2);
                        textSetter = DataHolder.getCurrencySign() + currencyConverter;
                        entryFeeInfo.setText(textSetter);
                        currencyConverter = Float.parseFloat(matchInfo.getWinPrize());
                        currencyConverter = round(currencyConverter / inr, 2);
                        textSetter = DataHolder.getCurrencySign() + currencyConverter;
                        winPrizeInfo.setText(textSetter);
                        currencyConverter = Float.parseFloat(matchInfo.getPerKill());
                        currencyConverter = round(currencyConverter / inr, 2);
                        textSetter = DataHolder.getCurrencySign() + currencyConverter;
                        perKillInfo.setText(textSetter);
                        currencyConverter = inr;
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    break;
            }
        }
        catch (NullPointerException e)
        {
            Toast.makeText(MatchDetailsActivity.this,"Something Wrong! Check your Internet.\nIf problem remains please restart your apps",Toast.LENGTH_LONG).show();
        }
        try {
            pubgUserName = DataHolder.getUserProfile().getPubgUserName();
            freefireUserName = DataHolder.getUserProfile().getFreefireUserName();
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        try {
            matchName.setText(matchInfo.getMatchName());
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference(DataHolder.getUrl()).child(matchInfo.getUniqueId());
            databaseReference.child("joinedPlayerInfo").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    playerLists = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if ( people>limit ) {
                            joinButtonMatchDetails.setClickable(false);
                            joinButtonMatchDetails.setEnabled(false);
                            joinButtonMatchDetails.setText("Room Full");
                        }
                        if ( Objects.equals(dataSnapshot1.getKey(), FirebaseAuth.getInstance().getUid()) ) {
                            joinButtonMatchDetails.setClickable(false);
                            joinButtonMatchDetails.setEnabled(false);
                            joinButtonMatchDetails.setText("Already Joined");
                            try {
                                roomId = matchInfo.getRoomId().trim();
                                roomPass = matchInfo.getRoomPass().trim();
                                roomid = findViewById(R.id.roomid);
                                roompassword = findViewById(R.id.roompassword);
                                if ( !(roomId.isEmpty()) ) {
                                    roomId = "Room ID: " + roomId;
                                    roomid.setText(roomId);
                                }
                                if ( !(roomPass.isEmpty()) ) {
                                    roomPass = "Room Password: " + roomPass;
                                    roompassword.setText(roomPass);
                                }
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }
                        playerList = dataSnapshot1.getValue(PlayerList.class);
                        playerLists.add(playerList);
                    }
                    playerListAdapter = new PlayerListAdapter(MatchDetailsActivity.this, playerLists);
                    playerListRecycler.setAdapter(playerListAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            joinButtonMatchDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    joinOperation(DataHolder.getUrl());
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Username");
        input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setPositiveButton("Confirm!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    switch (DataHolder.getUrl()) {
                        case "PubgMatch":
                            pubgUserName = input.getText().toString().trim();
                            if ( pubgUserName.isEmpty() ) {
                                Toast.makeText(MatchDetailsActivity.this, "Please Enter Your PUBG Username", Toast.LENGTH_SHORT).show();
                                pubgUserName = null;
                            }
                            break;
                        case "FreefireMatch":
                            freefireUserName = input.getText().toString();
                            if ( freefireUserName.isEmpty() ) {
                                Toast.makeText(MatchDetailsActivity.this, "Please Enter Your Freefire Username", Toast.LENGTH_SHORT).show();
                                freefireUserName = null;
                            }
                            break;
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                joinButtonMatchDetails.performClick();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Confirm?");
        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                joinButtonMatchDetails.performClick();
                checkConfirm = true;
                dialog.dismiss();
            }
        });
        builder1.setNegativeButton("No!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        try {
            liveLink=matchInfo.getLiveStram();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if(liveLink!=null)
        {
            joinButtonMatchDetails.setClickable(false);
            joinButtonMatchDetails.setEnabled(false);
            joinButtonMatchDetails.setVisibility(View.INVISIBLE);
            liveStreamButton.setVisibility(View.VISIBLE);
            liveStreamButton.setClickable(true);
            liveStreamButton.setEnabled(true);
        }



    }

    public void joinOperation(String url) {
        try {

            final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("userInfo").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
            if ( url.equals("PubgMatch") ) {
                try {
                    databaseReference = FirebaseDatabase.getInstance().getReference("userInfo").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
                    pubgDatabase = FirebaseDatabase.getInstance().getReference("PubgMatch").child(matchInfo.getUniqueId());
                    query = pubgDatabase.child("joinedPlayerInfo").orderByKey();
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
                    DatabaseReference peopleData = FirebaseDatabase.getInstance().getReference("PubgMatch").child(matchInfo.getUniqueId()).child("joinedPlayerInfo");
                    peopleData.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            try {
                                try {
                                    if ( matchInfo.getPeopleLimit() == null ) {
                                        matchInfo.setPeopleLimit("99");
                                    }
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                                limit=Integer.parseInt(matchInfo.getPeopleLimit());
                                people = dataSnapshot.getChildrenCount();
                                joinPubg();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(MatchDetailsActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


            }

            else if ( url.equals("FreefireMatch") ) {

                try {
                    databaseReference = FirebaseDatabase.getInstance().getReference("userInfo").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
                    freefireDatabase = FirebaseDatabase.getInstance().getReference("FreefireMatch").child(matchInfo.getUniqueId());
                    query = freefireDatabase.child("joinedPlayerInfo").orderByKey();
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
                DatabaseReference peopleData=FirebaseDatabase.getInstance().getReference("FreefireMatch").child(matchInfo.getUniqueId()).child("joinedPlayerInfo");
                peopleData.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            people = dataSnapshot.getChildrenCount();
                            try {
                                if ( matchInfo.getPeopleLimit() == null ) {
                                    matchInfo.setPeopleLimit("49");
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            limit=Integer.parseInt(matchInfo.getPeopleLimit());
                            joinFreefire();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(MatchDetailsActivity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });



            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void joinPubg()
    {
        try {
            final float entryFeeRequired = round(Float.parseFloat(matchInfo.getEntryFee()),2) / currencyConverter;
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
                            joinButtonMatchDetails.setText("Room Full");
                            joinButtonMatchDetails.setEnabled(false);
                            joinButtonMatchDetails.setClickable(false);

                        }
                        else {
                            if ( userBalance >= entryFeeRequired && pubgUserName != null && checkConfirm) {
                                checkConfirm = false;

                                    String confirmPeople = people+1+"";
                                    userBalance = round((userBalance - entryFeeRequired), 2);
                                    winning=winning-entryFeeRequired;
                                    System.out.println("Jim" +userBalance+" "+entryFeeRequired);
                                    pubgDatabase.child("peopleJoined").setValue(confirmPeople);
                                    databaseReference.child("balance").setValue(userBalance + "");
                                    databaseReference.child("pubgUsername").setValue(pubgUserName);
                                    pubgDatabase.child("joinedPlayerInfo").child(userProfile.getUid()).child("username").setValue(pubgUserName);
                                    System.out.println("Jim" +userBalance+" "+entryFeeRequired);

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
                    Toast.makeText(MatchDetailsActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void joinFreefire()
    {
        try {
            final float entryFeeRequired = Float.parseFloat(matchInfo.getEntryFee()) / currencyConverter;
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
                            joinButtonMatchDetails.setText("Room Full");
                            joinButtonMatchDetails.setEnabled(false);
                            joinButtonMatchDetails.setClickable(false);
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
                                    System.out.println("Jim" +userBalance+" "+entryFeeRequired);
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            } else if ( userBalance < entryFeeRequired ) {
                                DataHolder.setMatchInfo(matchInfo);
                                startActivity(new Intent(MatchDetailsActivity.this, AddFundActivity.class));

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
                    Toast.makeText(MatchDetailsActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
