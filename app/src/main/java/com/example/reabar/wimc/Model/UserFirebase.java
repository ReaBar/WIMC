package com.example.reabar.wimc.Model;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.reabar.wimc.MyApplication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by reabar on 29/06/2016.
 */
public class UserFirebase {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private String TAG = "UserFirebase";
    private String USERS_DB = "users";

    public UserFirebase(){
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.d(TAG,"onAuthStateChanged:signed_in:" + user.getUid());
                }
                else{
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void registerNewUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(MyApplication.getAppActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                if (!task.isSuccessful()) {
                    Toast.makeText(MyApplication.getAppActivity(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                }
            }
        });
    }

    public void signInUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(MyApplication.getAppActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInWithEmail", task.getException());
                    Toast.makeText(MyApplication.getAppActivity(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void logoutUser(){
        if(mAuth.getCurrentUser() != null){
            mAuth.signOut();
        }
    }

    public User getCurrentUser(FirebaseDatabase db){
        if(mAuth.getCurrentUser() != null){
            DatabaseReference dbRef = db.getReference(USERS_DB);
            final String userId = mAuth.getCurrentUser().getUid();
            dbRef.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User tempUser = dataSnapshot.getValue(User.class);
                    // TODO: return this user somehow
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                }
            });
        }
        return null;
    }
}
