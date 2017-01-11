package com.example.dana.carmanagement.firebaseutil;

import com.example.dana.carmanagement.MainActivity;
import com.example.dana.carmanagement.model.Car;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;
import java.util.Observable;

public class FirebaseUtil extends Observable {
    // firebase
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    // save to database from firebase
    private DatabaseReference databaseReference;
    private String mUserId;

    static {
        /*
        Cached data will still be available while offline and your writes will be resent when
        network connectivity is recovered. Enabling disk persistence allows our app to also keep
        all of its state even after an app restart.
         */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public FirebaseUtil() {
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        // Initialize Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void add(Car car) {
        databaseReference.child("users").child(mUserId).child("cars").child(car.getUuid()).setValue(car);
        setChanged();
        notifyObservers();
    }

    public void update(String uuid, Map<String, Object> carMap){
        databaseReference.child("users").child(mUserId).child("cars").child(uuid).updateChildren(carMap);
        setChanged();
        notifyObservers();
    }

    public void remove(String uuid, Car car) {
        databaseReference.child("users").child(mUserId).child("cars").child(uuid).removeValue();
        MainActivity.adapter.remove(car);
        setChanged();
        notifyObservers();
    }

    public void fillData() {
        // ChildEventListener - used to receive events about changes in the child locations of a given Firebase ref
        //                    - the appropriate method will be triggered when changes occur
        databaseReference.child("users").child(mUserId).child("cars").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Car car = dataSnapshot.getValue(Car.class);
                MainActivity.adapter.add(car);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Car car = dataSnapshot.getValue(Car.class);
                MainActivity.adapter.update(car);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public FirebaseAuth getmFirebaseAuth() {
        return mFirebaseAuth;
    }

    public void setmFirebaseAuth(FirebaseAuth mFirebaseAuth) {
        this.mFirebaseAuth = mFirebaseAuth;
    }

    public FirebaseUser getmFirebaseUser() {
        return mFirebaseUser;
    }

    public void setmFirebaseUser(FirebaseUser mFirebaseUser) {
        this.mFirebaseUser = mFirebaseUser;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }
}
