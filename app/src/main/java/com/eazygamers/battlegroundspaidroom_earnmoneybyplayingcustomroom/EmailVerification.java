package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class EmailVerification extends AppCompatActivity {
    TextView messageView;
    TextView message2;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    Task userTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);
        initialize();
        user=firebaseAuth.getCurrentUser();
        System.out.println(Objects.requireNonNull(user).isEmailVerified());
        if(user!=null) {
            if(user.isEmailVerified())
            {
                finish();
                startActivity(new Intent(this,HomePage.class));
            }
            else {
                messageView.setText(getString(R.string.mail_sent));
                message2.setText(getString(R.string.please_verify));
            }
        }
        else
        {
            messageView.setText(getString(R.string.password_reset));
            message2.setText(getString(R.string.check_email));

        }
        Animation blink= AnimationUtils.loadAnimation(this,R.anim.blink);
        messageView.startAnimation(blink);
        message2.startAnimation(blink);

    }

    public void initialize()
    {
        try {
            messageView = findViewById(R.id.messageView);
            message2 = findViewById(R.id.message2);
            firebaseAuth = FirebaseAuth.getInstance();
            userTask = Objects.requireNonNull(firebaseAuth.getCurrentUser()).reload().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EmailVerification.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void backToLoginClicked(View view)
    {
        firebaseAuth.signOut();
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
    @Override
    public void onBackPressed() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    public void sendAgainClicked(View view)
    {
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(EmailVerification.this,"Verification Email Snet",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EmailVerification.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void refreshClicked(View view)
    {

        startActivity(new Intent(EmailVerification.this,EmailVerification.class));
        finish();
    }
}

