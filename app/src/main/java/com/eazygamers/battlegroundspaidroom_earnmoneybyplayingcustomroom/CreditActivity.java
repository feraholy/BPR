package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

import android.os.Bundle;
import android.view.View;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CreditActivity extends AppCompatActivity {
    Toolbar creditToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);
        creditToolbar=findViewById(R.id.creditToolbar);
        setSupportActionBar(creditToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        creditToolbar.setNavigationIcon(R.drawable.ic_back);
        creditToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
