package com.example.shlomit.myhwapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewCJActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView txtname,txtDate,txtDet;
    private static final String TAG = "ViewCJActivity";

    ArrayList<Assignment> arrayJobs;
    AssignmentListAdapter adapter;
    private ListView mListView;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;
    String name;
    Assignment assignment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_couse_and_jobs);

        //get user id
        userID = SignInActivity.acct.getId().toString();
        arrayJobs = new ArrayList<Assignment>();

        toolbar = (Toolbar) findViewById(R.id.toolbar_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My HW");
        toolbar.setSubtitle("welcome");
        toolbar.setLogo(R.mipmap.ic_launcher);

        txtname = findViewById(R.id.textViewCourseName);
        txtDate = findViewById(R.id.textViewCourseDate);
        txtDet = findViewById(R.id.textViewCourseDetails);
        Bundle extra = getIntent().getExtras();
        if(!extra.isEmpty()){
            name = extra.getString("tvName");
            String date = extra.getString("tvDate");
            String det = extra.getString("tvDesctription");

            txtname.setText(name);
            txtDate.setText(date);
            txtDet.setText(det);
        }

        //add Firebase Database stuff
        mListView = (ListView) findViewById(R.id.lv_Jobs);
        //declare the database reference object. This is what we use to access the database.
        //NOTE: Unless you are signed in, this will not be useable.
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child(userID).child("COURSES").child(name).child("ASSIGNMENTS");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                showData(dataSnapshot);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        adapter = new AssignmentListAdapter(this, arrayJobs);
        mListView.setAdapter(adapter);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String msg = " ";
        switch (item.getItemId()) {
            case R.id.item_all_courses:
                msg = "item all courses";
                Intent intentAll = new Intent(this, AllCoursesActivity.class);
                startActivity(intentAll);
                break;
            case R.id.item_add_course:
                msg = "item add course";
                Intent intentAddCourse = new Intent(this, NewCourseActivity.class);
                startActivity(intentAddCourse);
                break;
            case R.id.item_connect_to_folders:
                msg = "item connect to folders";
                Intent intentConnectFolders = new Intent(this, ListenChangeEventsForFilesActivity.class);
                startActivity(intentConnectFolders);
                break;
        }
        Toast.makeText(this, msg + " checked", Toast.LENGTH_LONG).show();

        return super.onOptionsItemSelected(item);
    }

    public void MoveToAddJob(View view) {
        Intent intent = new Intent(this, NewAssignmentActivity.class);
        intent.putExtra("in_course", txtname.getText().toString());
        startActivity(intent);
    }




    public void showData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            assignment = new Assignment();
            assignment = ds.getValue(Assignment.class);
            arrayJobs.add(assignment);
       }
    }
    public class AssignmentListAdapter extends BaseAdapter {
        private Context mContext;
        private List<Assignment> mListJob;

        public AssignmentListAdapter(Context mContext, List<Assignment> mListJob) {
            this.mContext = mContext;
            this.mListJob = mListJob;
        }

        @Override
        public int getCount() {
            return mListJob.size();
        }

        @Override
        public Object getItem(int i) {
            return mListJob.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            final View v = View.inflate(mContext, R.layout.item_assignment_list, null);
            final TextView tvName = (TextView) v.findViewById(R.id.tv_aname);
            final TextView tvDate = (TextView) v.findViewById(R.id.tv_adate);
            final TextView tvDesctription = (TextView) v.findViewById(R.id.tv_adescription);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Log.d("name ",tvName.getText().toString());
                    //   Intent intent  = new Intent(this,ViewCJActivity.class);
                    Intent intent = new Intent(mContext,ViewAssignmentActivity.class);

                    intent.putExtra("tvName",tvName.getText().toString());
                    intent.putExtra("tvDate",tvDate.getText().toString());
                    intent.putExtra("tvDesctription",tvDesctription.getText().toString());

                    mContext.startActivity(intent);

                }
            });

            tvName.setText(mListJob.get(i).get_aName());
            tvDate.setText(String.valueOf(mListJob.get(i).get_aDate()));
            tvDesctription.setText(mListJob.get(i).get_aDetails());

            v.setTag(mListJob.get(i).get_aName());
            return v;
        }
    }



}