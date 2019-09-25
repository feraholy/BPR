package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class AdminActivity extends AppCompatActivity {
    TextInputLayout uid,amountAdmin;
    TextView adminResult;
    int spinnerPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Button submitAdmin=findViewById(R.id.submitAdmin);
        uid=findViewById(R.id.uid);
        amountAdmin=findViewById(R.id.amountAdmin);
        adminResult=findViewById(R.id.adminResult);
        submitAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        Spinner spinner = findViewById(R.id.spinnerAdmin);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.adminBalance, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Drawable spinnerDrawable = Objects.requireNonNull(spinner.getBackground().getConstantState()).newDrawable();
        spinnerDrawable.setColorFilter(ContextCompat.getColor(this, R.color.whitish), PorterDuff.Mode.SRC_ATOP);
        spinner.setBackground(spinnerDrawable);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              spinnerPosition=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void update()
    {
        String user=uid.getEditText().getText().toString().trim();
        String amount=amountAdmin.getEditText().getText().toString().trim();
        if(!user.isEmpty() && !amount.isEmpty()) {
            final float updateBal = Float.parseFloat(amount);
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("userInfo").child(user);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final UserProfile userProfile1 = dataSnapshot.getValue(UserProfile.class);
                    if ( userProfile1 != null ) {

                        if(spinnerPosition==0) {
                            float curBal = Float.parseFloat(userProfile1.getBalance()) + updateBal;
                            databaseReference.child("balance").setValue(curBal + "").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    String placeHolder = "Balance Updated Successfully " + userProfile1.getPubgUserName();
                                    adminResult.setText(placeHolder);
                                }
                            });
                        }
                        else if(spinnerPosition==1)
                        {
                            float curBal = Float.parseFloat(userProfile1.getWinningBalance()) + updateBal;
                            databaseReference.child("winningBalance").setValue(curBal + "").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    String placeHolder = "Winning Balance Updated Successfully " + userProfile1.getPubgUserName();
                                    adminResult.setText(placeHolder);
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                        adminResult.setText(databaseError.getMessage());
                }
            });
        }
        else {
            adminResult.setText("Check Above Fields!");
        }

    }
}
