package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    TextInputLayout firstNameInput;
    TextInputLayout lastNameInput;
    TextInputLayout usernameInput;
    TextInputLayout emailInputLayout;
    TextInputLayout mobileNumberInput;
    TextInputLayout passwordInputLayout;
    TextView currencyError;
    Button signUp;
    ImageButton closeButton;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    public String firstName, lastName, username, email, mobileNumber, password,currency;
    ProgressDialog progressDialog;
    RadioGroup currencyGroup;
    RadioButton currentButton;
    //Initialize
    public void initialize() {
        firebaseAuth = FirebaseAuth.getInstance();
        firstNameInput = findViewById(R.id.firstNameInput);
        lastNameInput = findViewById(R.id.lastNameInput);
        usernameInput = findViewById(R.id.usernameInputLayout);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        mobileNumberInput = findViewById(R.id.mobileNumberInput);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        signUp = findViewById(R.id.signUpButton);
        closeButton = findViewById(R.id.closeButton);
        currencyGroup=findViewById(R.id.currencyGroup);
        currencyError=findViewById(R.id.currencyError);
        firstName = null;
        lastName = null;
        username = null;
        email = null;
        mobileNumber = null;
        password = null;
        currency=null;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initialize();


    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    //Method For Close Button & Sign In Button
    public void backToSignIn(View view) {
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }

    public void signUpClicked(View view) {
        int selectedId=currencyGroup.getCheckedRadioButtonId();
        currentButton=findViewById(selectedId);
        if(currentButton==null)
        {
            currencyError.setText("Please Select Your Currency");
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait! Logging In");
        progressDialog.show();
        progressDialog.setCancelable(false);
        if (firstNameValidate() && lastNameValidate() && userNameValidate() && mobileNumberValidate() && emailValidate() && passwordValidate() && currentButton!=null) {
            currencyError.setText("");
            currency=currentButton.getText().toString();
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        sendUserDataToFirebase();
                        Toast.makeText(SignUpActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();
                    }
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            });

        } else {
            progressDialog.dismiss();
            firstNameValidate();
            lastNameValidate();
            userNameValidate();
            mobileNumberValidate();
            emailValidate();
            passwordValidate();
            Toast.makeText(this, "Check Errors!", Toast.LENGTH_SHORT).show();
        }

    }

    //Method For Sending Data To Firebase
    private void sendUserDataToFirebase() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        UserProfile userProfile = new UserProfile(firstName, lastName, username, email, mobileNumber,password,FirebaseAuth.getInstance().getUid(),currency);
        databaseReference.child("userInfo").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).setValue(userProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    progressDialog.dismiss();
                    startActivity(new Intent(SignUpActivity.this,HomePage.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(SignUpActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }

    //Initialize Views & Variables

    //Verification Email
//    private void emailVerification() {
//        user = firebaseAuth.getCurrentUser();
//        if(user!=null) {
//            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()) {
//                        progressDialog.dismiss();
//
//                    }
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    progressDialog.dismiss();
//                    Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//
//    }


    //All Fields Validation

    private boolean firstNameValidate() {
        firstName = Objects.requireNonNull(firstNameInput.getEditText()).getText().toString().trim();
        if (firstName.isEmpty()) {
            firstNameInput.setErrorEnabled(true);
            firstNameInput.setError("Can't Be Empty!");
            return false;
        } else {
            firstNameInput.setErrorEnabled(false);
            firstNameInput.setError(null);
            return true;
        }
    }

    private boolean lastNameValidate() {
        lastName = Objects.requireNonNull(lastNameInput.getEditText()).getText().toString().trim();
        if (lastName.isEmpty()) {
            lastNameInput.setErrorEnabled(true);
            lastNameInput.setError("Can't Be Empty!");
            return false;
        } else {
            lastNameInput.setErrorEnabled(false);
            lastNameInput.setError(null);
            return true;
        }
    }

    private boolean userNameValidate() {
        username = Objects.requireNonNull(usernameInput.getEditText()).getText().toString().trim();
        if (username.isEmpty()) {
            usernameInput.setErrorEnabled(true);
            usernameInput.setError("Can't Be Empty!");
            return false;
        } else if (username.length() > 15) {
            usernameInput.setErrorEnabled(true);
            usernameInput.setError("Username Shouldn't Exceed 15 Character");
            return false;
        } else {
            usernameInput.setErrorEnabled(false);
            usernameInput.setError(null);
            return true;
        }
    }

    private boolean emailValidate() {
        email = Objects.requireNonNull(emailInputLayout.getEditText()).getText().toString().trim();
        if (email.isEmpty()) {
            emailInputLayout.setErrorEnabled(true);
            emailInputLayout.setError("Can't Be Empty!");
            return false;
        } else {
            emailInputLayout.setErrorEnabled(false);
            emailInputLayout.setError(null);
            return true;
        }
    }

    private boolean mobileNumberValidate() {
        mobileNumber = Objects.requireNonNull(mobileNumberInput.getEditText()).getText().toString().trim();
        if (mobileNumber.isEmpty()) {
            mobileNumberInput.setErrorEnabled(true);
            mobileNumberInput.setError("Can't Be Empty!");
            return false;
        } else {
            mobileNumberInput.setErrorEnabled(false);
            mobileNumberInput.setError(null);
            return true;
        }
    }

    private boolean passwordValidate() {

        password = Objects.requireNonNull(passwordInputLayout.getEditText()).getText().toString().trim();
        if (password.isEmpty()) {
            passwordInputLayout.setErrorEnabled(true);
            passwordInputLayout.setError("Can't Be Empty!");
            return false;
        } else {
            passwordInputLayout.setErrorEnabled(false);
            passwordInputLayout.setError(null);
            return true;
        }
    }
    //Validation All Fields Ends Here

}
