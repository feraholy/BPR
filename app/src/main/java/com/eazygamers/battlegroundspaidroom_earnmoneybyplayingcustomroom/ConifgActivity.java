package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ConifgActivity extends AppCompatActivity {
    ConfigInfo configInfo;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        System.exit(1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conifg);
        Button configUpdate=findViewById(R.id.configUpdate);
        TextView configNotice=findViewById(R.id.configNotice);
        LottieAnimationView animationView = findViewById(R.id.cloud);
        loadConfig();
        int status=0;
        try
        {
            status=DataHolder.getConfigInfo().getStatus();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if(status==1) {
            configNotice.setText("Server Is Under Maintenance\nWe Will Live Very Soon");
            animationView.setAnimation("cloud.json");
            animationView.playAnimation();
        }
        else
        {
            animationView.cancelAnimation();
            configNotice.setText("Please Update Your Apps To Latest Version ");
            animationView.setAnimation("playstore.json");
            animationView.playAnimation();
            configUpdate.setVisibility(View.VISIBLE);
            Button alreadyUpdate=findViewById(R.id.alreadyUpdate);
            alreadyUpdate.setVisibility(View.VISIBLE);
            alreadyUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ConifgActivity.this,HomePage.class));
                    finish();
                }
            });
        }
        configUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


    }
    private void loadConfig() {
        DatabaseReference config = FirebaseDatabase.getInstance().getReference("config");
        config.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    configInfo = dataSnapshot.getValue(ConfigInfo.class);
                    if ( configInfo != null && configInfo.getVersionCheck()!=null ) {
                        try {
                            DataHolder.setConfigInfo(configInfo);
                            String version = DataHolder.getVersion();
                            if ( configInfo.getStatus() == 1 || !configInfo.getVersionCheck().equals(version) ) {
                                System.out.println("Not Updated");
                            }
                            else
                            {
                                finish();
                                startActivity(new Intent(ConifgActivity.this,HomePage.class));
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ConifgActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
