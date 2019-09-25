package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayFragment extends Fragment {
    private TextView pubg_match,freefire_match;
    View view;
    public PlayFragment() {

        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            String countMatch = DataHolder.getRunningPubg() + " Match Available!";
            pubg_match.setText(countMatch);
            countMatch = DataHolder.getRunningFreefire() + " Match Available!";
            freefire_match.setText(countMatch);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            String countMatch = DataHolder.getRunningPubg() + " Match Available!";
            pubg_match.setText(countMatch);
            countMatch = DataHolder.getRunningFreefire() + " Match Available!";
            freefire_match.setText(countMatch);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater,container,savedInstanceState);
        view =inflater.inflate(R.layout.fragment_play, container, false);

        ConstraintLayout pubgm_button,freefire_button;
        pubgm_button=view.findViewById(R.id.pubgm_button);
        freefire_button=view.findViewById(R.id.freefire_button);
        pubg_match=view.findViewById(R.id.pubg_match);
        freefire_match=view.findViewById(R.id.freefire_match);

        Animation blink2= AnimationUtils.loadAnimation(getContext(),R.anim.blink2);
        pubg_match.startAnimation(blink2);

        freefire_match.startAnimation(blink2);

        pubgm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),PubgActivity.class));
            }
        });
        freefire_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),FreefireActivity.class));
            }
        });
        Button fbPage=view.findViewById(R.id.fbPage);
        Button fbGroup=view.findViewById(R.id.fbGroup);

        fbPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.facebook.com/battlegroundspaidroom/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        fbGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://m.me/join/AbYA4B2N8HNetNK-");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        // Inflate the layout for this fragment

        return view;

    }


}
