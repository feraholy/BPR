package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class ConvertActivity extends AppCompatActivity {
    String amount;
    TextInputLayout amountWantToConvert;
    Toolbar convertToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);

        convertToolbar = findViewById(R.id.convertToolbar);
        setSupportActionBar(convertToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        convertToolbar.setNavigationIcon(R.drawable.ic_back);
        convertToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        try {
            amountWantToConvert = findViewById(R.id.amountWantToConvert);
            String winning = DataHolder.getUserProfile().getWinningBalance();
            final float win = Float.parseFloat(winning);
            final float balance = Float.parseFloat(DataHolder.getUserProfile().getBalance());
            Button convert = findViewById(R.id.confirmConvert);
            convert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( Objects.requireNonNull(amountWantToConvert.getEditText()).getText().toString().isEmpty() ) {

                        amountWantToConvert.setErrorEnabled(true);
                        amountWantToConvert.setError("Please Enter Amount");

                    } else {
                        amountWantToConvert.setErrorEnabled(false);
                        amount = Objects.requireNonNull(amountWantToConvert.getEditText()).getText().toString().trim();
                        System.out.println(amount);
                        float amountF = Float.parseFloat(amount);
                        if ( amountF > win ) {
                            amountWantToConvert.setErrorEnabled(true);
                            amountWantToConvert.setError("You Don't Have Enough Winning Balance");
                        } else {
                            final ProgressDialog progressDialog=new ProgressDialog(ConvertActivity.this);
                            progressDialog.setCancelable(false);
                            progressDialog.setMessage("Please Wait!");
                            progressDialog.show();
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("userInfo");
                            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("userInfo");
                            float remain = win - amountF;
                            float remainBal = balance + amountF;
                            databaseReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("balance").setValue(remainBal + "").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        progressDialog.dismiss();
                                    }
                                    else
                                    {
                                        amountWantToConvert.setErrorEnabled(true);
                                        amountWantToConvert.setError("Please Try Again Later!");
                                    }
                                }
                            });
                            progressDialog.show();
                            DataHolder.getUserProfile().setBalance(remainBal + "");
                            databaseReference1.child(FirebaseAuth.getInstance().getUid()).child("winningBalance").setValue(remain + "").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(ConvertActivity.this,"Converted Successfully!",Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                    else
                                    {
                                        amountWantToConvert.setErrorEnabled(true);
                                        amountWantToConvert.setError("Please Try Again Later!");
                                    }
                                }
                            });;
                            DataHolder.getUserProfile().setWinningBalance(remain + "");
                        }
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
