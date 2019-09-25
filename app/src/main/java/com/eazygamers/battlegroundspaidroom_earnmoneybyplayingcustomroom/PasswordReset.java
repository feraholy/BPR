package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class PasswordReset extends AppCompatActivity {
    TextInputLayout emailInput;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        emailInput=findViewById(R.id.emailInput);
        firebaseAuth=FirebaseAuth.getInstance();
    }
    public void backToLoginClicked(View view)
    {
        finish();
    }
    public void confirmClicked(View view)
    {
        try {
            String email = Objects.requireNonNull(emailInput.getEditText()).getText().toString().trim();
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if ( task.isSuccessful() ) {
                        Toast.makeText(PasswordReset.this, "Please Check Inbox/Spam Of Your Mail", Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PasswordReset.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this,"Please Enter Your Email Correctly",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}
