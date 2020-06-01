package com.example.covidregister.Utilities;

import android.content.Context;
import android.util.Log;

import com.example.covidregister.R;
import com.example.covidregister.Temperature;
import com.example.covidregister.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseMethods {
    private static final String TAG = "FirebaseMethods";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;
    private Context mContext;
    public FirebaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mContext = context;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    /**
     * @param surname
     * @param sex
     * @param names
     * @param id
     * @param age
     * @param address
     * @param PhoneNumber
     **/

    public void addNewUser(String address,String age,String id,String names,String surname,String sex,String PhoneNumber){

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_id))
                .setValue(id);
        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_phoneNumber))
                .setValue(PhoneNumber);
        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_address))
                .setValue(address);
        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_age))
                .setValue(age);
        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_names))
                .setValue(names);
        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_surname))
                .setValue(surname);
        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_sex))
                .setValue(sex);
    }

    /**
     * database: user account settings node
     * @param dataSnapshot
     * @return
     */

    public User GetRegisterAUser(DataSnapshot dataSnapshot) {
        User UserDetails = new User();

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            if (ds.getKey().equals(mContext.getString(R.string.dbname_users))) {

                try{
                    UserDetails.setUser_id(
                            ds.child(userID)
                            .getValue(Location.class)
                            .getUser_id()
                    );
                    UserDetails.setId(
                            ds.child(userID)
                            .getValue(Location.class)
                            .getId()
                    );
                    UserDetails.setPhoneNumber(
                            ds.child(userID)
                            .getValue(Location.class)
                            .getPhoneNumber()
                    );
                    UserDetails.setAddress(
                            ds.child(userID)
                                    .getValue(Location.class)
                                    .getAddress()
                    );
                    UserDetails.setAge(
                            ds.child(userID)
                                    .getValue(Location.class)
                                    .getAge()
                    );
                    UserDetails.setNames(
                            ds.child(userID)
                                    .getValue(Location.class)
                                    .getNames()
                    );
                    UserDetails.setSex(
                            ds.child(userID)
                                    .getValue(Location.class)
                                    .getSex()
                    );
                    UserDetails.setSurname(
                            ds.child(userID)
                                    .getValue(Location.class)
                                    .getSurname()
                    );
                    UserDetails.setUser_id(
                            ds.child(userID)
                            .getValue(Location.class)
                            .getUser_id()
                    );


                }catch (NullPointerException e){}
            }
        }
        Log.d(TAG, "GetRegisterAUser: retrived from Realtime Database" + UserDetails);
        return UserDetails;
    }

    /**
     * @param address
     * @param age
     * @param id
     * @param names
     * @param sex
     * @param surname
     * */
    public void updateUserDets(String names, String surname, String address, String id, String sex, String age){
        Log.d(TAG, "updateUserAccountSettings: updating information entered in other fields");
        if (names != null){
            myRef.child(mContext.getString(R.string.dbname_users))
                    .child(userID)
                    .child(mContext.getString(R.string.field_names))
                    .setValue(names);}
        if (surname != null){
            myRef.child(mContext.getString(R.string.dbname_users))
                    .child(userID)
                    .child(mContext.getString(R.string.field_surname))
                    .setValue(surname);}
        if (address != null){
            myRef.child(mContext.getString(R.string.dbname_users))
                    .child(userID)
                    .child(mContext.getString(R.string.field_address))
                    .setValue(address);}
        if (id != null){
            myRef.child(mContext.getString(R.string.dbname_users))
                    .child(userID)
                    .child(mContext.getString(R.string.field_id))
                    .setValue(id);}
        if (sex != null){
            myRef.child(mContext.getString(R.string.dbname_users))
                    .child(userID)
                    .child(mContext.getString(R.string.field_sex))
                    .setValue(sex);}
        if (age != null){
            myRef.child(mContext.getString(R.string.dbname_users))
                    .child(userID)
                    .child(mContext.getString(R.string.field_age))
                    .setValue(age);}

    }


}

