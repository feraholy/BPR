package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    UserProfile userProfile;
    TextView userFullname,balanceView,winningBalPro;


    public ProfileFragment() {


        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        userFullname=view.findViewById(R.id.userFullname);
        balanceView=view.findViewById(R.id.balanceView);
        winningBalPro=view.findViewById(R.id.winningBalPro);
        try {
            final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("userInfo");
            if ( firebaseAuth.getUid() != null ) {
                databaseReference.child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userProfile = dataSnapshot.getValue(UserProfile.class);
                        assert userProfile != null;
                        try {
                            String fullName = userProfile.getFirstName() + " " + userProfile.getLastName();
                            userFullname.setText(fullName);
                            switch (userProfile.getCurrency()) {
                                case "BDT": {
                                    String balance = "Balance " + "\u09F3" + userProfile.getBalance();
                                    balanceView.setText(balance);
                                    String Winning="Winning Balance " + "\u09F3" + userProfile.getWinningBalance();
                                    winningBalPro.setText(Winning);
                                    break;
                                }
                                case "USD": {
                                    String balance = "Balance $" + userProfile.getBalance();
                                    balanceView.setText(balance);
                                    String Winning="Winning Balance $" + userProfile.getWinningBalance();
                                    winningBalPro.setText(Winning);
                                    break;
                                }
                                case "INR": {
                                    String balance = "Balance " + "\u20B9" + userProfile.getBalance();
                                    balanceView.setText(balance);
                                    String Winning="Winning Balance " + "\u20B9" + userProfile.getWinningBalance();
                                    winningBalPro.setText(Winning);
                                    break;
                                }
                            }
                        }
                        catch (NullPointerException e)
                        {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Button creditButton2=view.findViewById(R.id.creditButton2);
        creditButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),AdminActivity.class));
            }
        });
        Button logoutButton=view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Objects.requireNonNull(getActivity()).finish();
                startActivity(new Intent(getActivity(),LoginActivity.class));

            }
        });
        Button profileButton=view.findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ViewProfileActivity.class));
            }
        });
        // Inflate the layout for this fragment
        final Button walletButton=view.findViewById(R.id.walletButton);
        walletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),WalletActivity.class));
            }
        });

        Button helpButton=view.findViewById(R.id.helpButton);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.facebook.com/battlegroundspaidroom/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        Button creditButton=view.findViewById(R.id.creditButton);
        creditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),CreditActivity.class));
            }
        });
        Button referralButton=view.findViewById(R.id.referralButton);
        referralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Do You Want To Earn Money By Playing Game? Join Here: "+"https://play.google.com/store/apps/details?id=com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Welcome To Join");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
        return view;
    }


}
