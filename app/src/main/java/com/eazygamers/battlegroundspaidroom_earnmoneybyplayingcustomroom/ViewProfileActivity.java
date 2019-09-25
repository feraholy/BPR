package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ViewProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextInputLayout firstNameInputProfile,pubgUsernameInput,freefireUsernameInput;
    TextInputLayout lastNameInputProfile;
    TextInputLayout emailNotEditable;
    TextInputLayout mobileProfile;
    TextInputLayout usernameProfile;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    UserProfile userProfile;
    public String firstName,lastName,email,username,mobile,pubgUsername,freefireUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        initialize();
        retrieveDataFromFirebase();
    }



    void initialize()
    {
        toolbar=findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        firstNameInputProfile=findViewById(R.id.firstNameInputProfile);
        lastNameInputProfile=findViewById(R.id.lastNameInputProfile);
        emailNotEditable=findViewById(R.id.emailNotEditable);
        mobileProfile=findViewById(R.id.mobileProfile);
        usernameProfile=findViewById(R.id.usernameProfile);
        pubgUsernameInput=findViewById(R.id.pubgUsernameInput);
        freefireUsernameInput=findViewById(R.id.freefireUsernameInput);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("userInfo");
    }

    void retrieveDataFromFirebase()
    {
        if(firebaseAuth.getUid()!=null) {
            databaseReference.child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        userProfile = dataSnapshot.getValue(UserProfile.class);
                        assert userProfile != null;
                        DataHolder.setUserProfile(userProfile);
                        firstName = userProfile.getFirstName();
                        lastName = userProfile.getLastName();
                        username = userProfile.getUserName();
                        email = userProfile.getEmail();
                        mobile = userProfile.getMobileNumber();
                        Objects.requireNonNull(emailNotEditable.getEditText()).setEnabled(false);
                        Objects.requireNonNull(usernameProfile.getEditText()).setEnabled(false);
                        Objects.requireNonNull(firstNameInputProfile.getEditText()).setText(firstName);
                        Objects.requireNonNull(lastNameInputProfile.getEditText()).setText(lastName);


                        if ( userProfile.getPubgUserName() != null ) {
                            pubgUsername = userProfile.getPubgUserName();
                            Objects.requireNonNull(pubgUsernameInput.getEditText()).setText(pubgUsername);

                        }

                        if ( userProfile.getFreefireUserName() != null ) {
                            freefireUsername = userProfile.getFreefireUserName().trim();
                            Objects.requireNonNull(freefireUsernameInput.getEditText()).setText(freefireUsername);
                        }


                        emailNotEditable.getEditText().setText(email);
                        Objects.requireNonNull(mobileProfile.getEditText()).setText(mobile);
                        usernameProfile.getEditText().setText(username);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ViewProfileActivity.this,"Error!",Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void submitClicked(View view)
    {
        firstName= Objects.requireNonNull(firstNameInputProfile.getEditText()).getText().toString();
        lastName= Objects.requireNonNull(lastNameInputProfile.getEditText()).getText().toString();
        pubgUsername= Objects.requireNonNull(pubgUsernameInput.getEditText()).getText().toString().trim();
        freefireUsername= Objects.requireNonNull(freefireUsernameInput.getEditText()).getText().toString().trim();
        try {
            if(firebaseAuth.getUid()!=null) {
                databaseReference.child(firebaseAuth.getUid()).child("firstName").setValue(firstName);
                databaseReference.child(firebaseAuth.getUid()).child("lastName").setValue(lastName);
                databaseReference.child(firebaseAuth.getUid()).child("mobileNumber").setValue(mobile);
                if(!(pubgUsername.trim().isEmpty())) {
                    databaseReference.child(firebaseAuth.getUid()).child("pubgUserName").setValue(pubgUsername);
                }
                if(!(freefireUsername.trim().isEmpty())) {
                    databaseReference.child(firebaseAuth.getUid()).child("freefireUserName").setValue(freefireUsername);
                }



            }
            Toast.makeText(this,"Completed!",Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


}
