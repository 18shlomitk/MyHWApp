package com.example.shlomit.myhwapp;

import android.content.Context;
import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by shlomit on 07/02/2018.
 */

public class DataAccess {
    private static DataAccess instance = null;
    private DataAccess(Context c) {}
    /**
     * Gets the singleton instance of the Sailors Data Access class
     * @param c The context in which the database works
     * @return The instance
     */
    public static DataAccess getInstance(Context c) {
        if (instance == null)
        {
            instance = new DataAccess(c);
        }
        return instance;
    }

    ////////////////////////////////////////////////////////////////////////

    static final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    public static boolean _ViewCourse(String name){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Course post = dataSnapshot.getValue(Course.class);
                Log.d("post",post.get_cName()+" "+post.get_cDate().toString())     ;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("_ViewCourse", "loadPost:onCancelled", databaseError.toException());
                return;
            }
        };
        mDatabase.child(SignInActivity.acct.getId().toString())
                .child("COURSES")
                .child(name)
                .addValueEventListener(postListener);
        return  true;
    }


}