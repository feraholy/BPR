package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout emailInputLayout;
    TextInputLayout passwordInputLayout;
    String emailInput;
    String passwordInput;
    Button signIn;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    TextView nameHeading;
    Animation fade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();

    }



    //Method For Sign Up Button Click
    public void signUpClicked(View view)
    {
        startActivity(new Intent(this,SignUpActivity.class));
        finish();
    }


    //Login Button Click Method
    public void loginButtonClicked(View view)
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait! Logging In");
        progressDialog.show();
        progressDialog.setCancelable(true);
        progressDialog.setCancelable(false);
            if (validPassword() && validateEmail()) {
                firebaseAuth.signInWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                    startActivity(new Intent(LoginActivity.this,HomePage.class));
                                    progressDialog.dismiss();
                                    finish();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(e.getLocalizedMessage().contains("disable") || e.getLocalizedMessage().contains("denied"))
                        {
                            Toast.makeText(LoginActivity.this,"Server Under Maintenance. Try Again After Some Time",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                        }

                        progressDialog.dismiss();
                    }
                });
            } else {
                progressDialog.dismiss();
                validateEmail();
                validPassword();
            }



    }

    //Initialize Views & Variables
    @SuppressLint("ClickableViewAccessibility")
    private void initialize()
    {
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        if(user!=null)
        {
            finish();
            startActivity(new Intent(this,HomePage.class));
//            if(user.isEmailVerified())
//            {
//                finish();
//                startActivity(new Intent(this,HomePage.class));
//            }
//            else
//            {
//                finish();
//                startActivity(new Intent(this,EmailVerification.class));
//            }

        }
        emailInputLayout=findViewById(R.id.emailInputLayout);
        passwordInputLayout=findViewById(R.id.passwordInputLayout);
        signIn=findViewById(R.id.signIn);
        fade= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        nameHeading=findViewById(R.id.nameHeading);
        nameHeading.startAnimation(fade);

        //On Touch Color Change Listener For Sign In Button
        signIn.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    signIn.setBackgroundColor(Color.rgb(249, 249, 249));
                    signIn.setTextColor(Color.rgb(50, 20, 147));
                    return true;
                }
                else if(event.getAction()==MotionEvent.ACTION_DOWN)
                {
                    v.performClick();
                    signIn.setBackgroundColor(Color.rgb(50, 20, 147));
                    signIn.setTextColor(Color.rgb(249, 249, 249));
                    return true;
                }

                return false;
            }

        });
        emailInput=null;
        passwordInput=null;
        Button forgotPasswordButton=findViewById(R.id.forgotPasswordButton);
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,PasswordReset.class));
            }
        });
    }

    //Email Validation Methods

    private boolean validateEmail()
    {
        emailInput= Objects.requireNonNull(emailInputLayout.getEditText()).getText().toString().trim();
        if(emailInput.isEmpty())
        {
            emailInputLayout.setErrorEnabled(true);
            emailInputLayout.setError("Enter Your Email");
            return false;
        }
        else
        {
            emailInputLayout.setErrorEnabled(false);
            emailInputLayout.setError(null);
            return true;
        }
    }

    //Password Validation Method
    private boolean validPassword()
    {
        passwordInput= Objects.requireNonNull(passwordInputLayout.getEditText()).getText().toString().trim();
        if(passwordInput.isEmpty())
        {
            passwordInputLayout.setErrorEnabled(true);
            passwordInputLayout.setError("Enter Your Password");
            return false;
        }
        else
        {
            passwordInputLayout.setErrorEnabled(false);
            passwordInputLayout.setError(null);
            return true;
        }
    }




}
