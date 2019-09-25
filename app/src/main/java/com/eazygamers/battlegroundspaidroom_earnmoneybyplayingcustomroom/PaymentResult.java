package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class PaymentResult extends AppCompatActivity {
    TextView pay_status, pay_time, pay_amount, epw_txnid;
    float balance,amountF,backup;
    DepositeStatus depositeStatus;;
    TransactionInfo transactionInfo;
    UserProfile userProfile;
    ProgressDialog progressDialog;
    Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_result);

        doneButton=findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentResult.this,HomePage.class));
            }
        });
        new backgroundTask().execute();

    }

    private class backgroundTask extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    userProfile=DataHolder.getUserProfile();
                    transactionInfo = DataHolder.getTransactionInfo();
                    doneButton.setVisibility(View.INVISIBLE);
                    progressDialog=new ProgressDialog(PaymentResult.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Please Wait! We Are Updating Balance");
                    progressDialog.show();
                }
            });

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pay_status = findViewById(R.id.pay_status);
                    pay_time = findViewById(R.id.pay_time);
                    pay_amount = findViewById(R.id.pay_amount);
                    epw_txnid = findViewById(R.id.epw_txnid);
                    String placeHolder="Time: "+transactionInfo.getPay_time();
                    pay_time.setText(placeHolder);
                    placeHolder="Amount:"+ transactionInfo.getAmount();
                    pay_amount.setText(placeHolder);
                    placeHolder="EPW TXNID: "+transactionInfo.getEpw_txnid();
                    epw_txnid.setText(placeHolder);
                    progressDialog.dismiss();
                    doneButton.setVisibility(View.VISIBLE);
                }
            });

        }

        @Override
        protected Void doInBackground(Void... voids) {
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("userInfo").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userProfile=dataSnapshot.getValue(UserProfile.class);
                    if(userProfile!=null)
                    {
                        balance=Float.parseFloat(userProfile.getBalance());
                        DataHolder.setUserProfile(userProfile);
                        try {
                            if ( transactionInfo.getPay_status().contains("Successful") || transactionInfo.getPay_status().contains("ssful") ) {
                                try {
                                    amountF = Float.parseFloat(transactionInfo.getAmount());
                                    String updateBalance = (balance+amountF)+"";
                                    userProfile.setBalance(updateBalance);
                                    DataHolder.setUserProfile(userProfile);
                                    System.out.println("Check Bal"+ updateBalance);
                                    databaseReference.child("balance").setValue(updateBalance + "").addOnCompleteListener(new OnCompleteListener<Void>() {

                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        pay_status.setText(transactionInfo.getPay_status());
                                                    }
                                                });
                                            }
                                            else {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        pay_status.setText("Failed For Network Issue!\nTake a Screenshot And Send it to support");

                                                    }
                                                });
                                            }
                                        }
                                    }).addOnCanceledListener(new OnCanceledListener() {
                                        @Override
                                        public void onCanceled() {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    pay_status.setText("Failed For Network Issue!\nTake a Screenshot And Send it to support");
                                                }
                                            });
                                        }
                                    });

                                    final DatabaseReference database = FirebaseDatabase.getInstance().getReference("DepositeStatus");
                                    database.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            depositeStatus = dataSnapshot.getValue(DepositeStatus.class);
                                            if ( depositeStatus != null ) {
                                                int dep = depositeStatus.getAmount();
                                                int pep = depositeStatus.getPeople();
                                                pep++;
                                                dep = dep + Integer.parseInt(transactionInfo.getAmount());
                                                database.child("people").setValue(pep);
                                                database.child("amount").setValue(dep);
                                            } else {
                                                int dep = Integer.parseInt(transactionInfo.getAmount());
                                                int pep = 1;
                                                database.child("people").setValue(pep);
                                                database.child("amount").setValue(dep);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            pay_status.setText(transactionInfo.getPay_status());
                                        }
                                        catch (Exception e)
                                        {
                                            e.printStackTrace();
                                            pay_status.setText("Failed");
                                        }
                                    }
                                });

                            }

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            return null;
        }
    }

}
