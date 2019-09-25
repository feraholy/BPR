package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class WithdrawFragment extends Fragment {
    private TextInputLayout bkashNumber,amountWithdraw;
    private String bkash,amount;
    private TextView withdrawMessage;
    private float winningBalance,withdraw;
    private ProgressDialog progressDialog;
    private RadioGroup withdrawMethod;
    private RadioButton method;
    float balance;
    public WithdrawFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_withdraw, container, false);
        bkashNumber=view.findViewById(R.id.bkashNumber);
        amountWithdraw=view.findViewById(R.id.amountWithdraw);
        Button withdrawSubmit=view.findViewById(R.id.withdrawSubmit);
        withdrawMessage=view.findViewById(R.id.withdrawMessage);
        withdrawMethod=view.findViewById(R.id.withdrawMethod);
        withdrawMessage.setText("");
        withdrawSubmit=view.findViewById(R.id.withdrawSubmit);
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Sending Request!");
        progressDialog.setCancelable(false);
        withdrawSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId=withdrawMethod.getCheckedRadioButtonId();
                method=view.findViewById(selectedId);
                if(method==null)
                {
                    Toast.makeText(getActivity(),"Please Select A Method!",Toast.LENGTH_SHORT).show();
                }
                else {
                    System.out.println(method.getText().toString());
                    validate();
                }
            }
        });




        return view;
    }
    private void validate()
    {

        if(validateAmount() && validateBkash()) {
            progressDialog.show();
            String availWining=(winningBalance-withdraw)+"";
            DatabaseReference userDatabase= FirebaseDatabase.getInstance().getReference("userInfo").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
            userDatabase.child("winningBalance").setValue(availWining).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        WithdrawInfo withdrawInfo = new WithdrawInfo(DataHolder.getUserProfile().getFirstName() + DataHolder.getUserProfile().getLastName(), bkash, amount, DataHolder.getUserProfile().getCurrency(), DataHolder.getUserProfile().getEmail(), FirebaseAuth.getInstance().getUid(),method.getText().toString(),DataHolder.getVersion());
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("WithdrawRequests");
                        databaseReference.push().setValue(withdrawInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if ( task.isSuccessful() ) {
                                    progressDialog.dismiss();
                                    withdrawMessage.setText("Withdraw Successful!");
                                    Toast.makeText(getActivity(), "Withdraw Successful!", Toast.LENGTH_LONG).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                withdrawMessage.setText(e.getLocalizedMessage());
                                Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    withdrawMessage.setText(e.getLocalizedMessage());
                    Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
            try {
                DataHolder.getUserProfile().setWinningBalance(availWining);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


        }
        else
        {
            validateBkash();
            validateAmount();
            if(!validateAmount()) {
                withdrawMessage.setText("Please Fill Required Information!");
                Toast.makeText(getActivity(), "Please Fill Required Information!", Toast.LENGTH_LONG).show();
            }
        }
    }
    private boolean validateBkash()
    {
        bkash= Objects.requireNonNull(bkashNumber.getEditText()).getText().toString().trim();
        if(bkash.isEmpty() || bkash.length()<11)
        {
            bkashNumber.setErrorEnabled(true);
            bkashNumber.setError("Enter A Valid bKash Nuumber");
            return false;
        }
        else
        {
            bkashNumber.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateAmount()
    {
        amount= Objects.requireNonNull(amountWithdraw.getEditText()).getText().toString().trim();
        if(amount.isEmpty())
        {
            amountWithdraw.setErrorEnabled(true);
            amountWithdraw.setError("Enter An Amount!");
            return false;
        }
        else {
            try {
                winningBalance = Float.parseFloat(DataHolder.getUserProfile().getWinningBalance());
                withdraw = Float.parseFloat(amount);
                if ( winningBalance >= withdraw && winningBalance >= 50 ) {
                    amountWithdraw.setErrorEnabled(false);
                    return true;
                } else {
                    Toast.makeText(getContext(), "You Don't Sufficient Winning Balance!", Toast.LENGTH_LONG).show();
                    amountWithdraw.setErrorEnabled(true);
                    amountWithdraw.setError("Minimum Withdraw is 50");
                    return false;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }


}
