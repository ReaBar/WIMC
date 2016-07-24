package com.example.reabar.wimc.Model;

import android.support.annotation.NonNull;
import android.util.Log;

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

import java.util.ArrayList;
import java.util.List;

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

    public void signupUser(final FirebaseDatabase db, final User user, final String password, final Model.SignUpListener listener){
        mAuth.createUserWithEmailAndPassword(user.getEmail(), password).addOnCompleteListener(MyApplication.getAppActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                    String uid = task.getResult().getUser().getUid();
                    Log.d(TAG, "result: " + uid);
                    DatabaseReference dbRef = db.getReference(USERS_DB);
                    user.setUserId(uid);
                    dbRef.child(uid).setValue(user);
                    listener.success(true);
                }

                if (!task.isSuccessful()) {
                    listener.failed(task.getException().getMessage());
                }
            }
        });
    }

    public void signInUser(final User user, String password, final Model.LoginListener listener){
        mAuth.signInWithEmailAndPassword(user.getEmail(), password).addOnCompleteListener(MyApplication.getAppActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                    Model.getInstance().setCurrentUser(user);

                    listener.success(true);
                }

                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInWithEmail", task.getException());
                    listener.failed(task.getException().getMessage());
                }
            }
        });
    }

    public void logoutUser(){
        if(mAuth.getCurrentUser() != null){
            mAuth.signOut();
        }
    }

    public void resetPassword(String email, final Model.ResetPasswordListener listener){
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Log.d(TAG, "Email sent.");
                        listener.success(true);
                    }

                    else{
                        Log.d(TAG,"Email not sent");
                        listener.failed(task.getException().getMessage());
                    }
                }
            });
    }

    public void updatePassword(String newPassword, final Model.UpdatePasswordListener listener){
        user = mAuth.getCurrentUser();

        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "Update Password.");
                    listener.success(true);
                }
                else{
                    Log.d(TAG,"Failed to update password.");
                    listener.failed(task.getException().getMessage());
                }
            }
        });
    }
    public void getCurrentUser(){
        if(mAuth.getCurrentUser() != null) {
            Model.getInstance().setCurrentUser(new User(mAuth.getCurrentUser().getEmail()));
        }
    }

    public List<String> getUsersList(FirebaseDatabase db, final Model.SyncListener listener){
        final List<String> users = new ArrayList<String>();
        DatabaseReference dbRef = db.getReference(USERS_DB);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                while(children.iterator().hasNext()){
                    users.add(children.iterator().next().getValue(User.class).getEmail());
                }
                listener.PassData(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.failed(databaseError.toString());
            }
        });

        return users;
    }


}
