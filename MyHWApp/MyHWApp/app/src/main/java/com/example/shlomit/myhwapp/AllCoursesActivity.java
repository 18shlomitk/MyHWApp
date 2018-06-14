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
import android.widget.Button;
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

public class AllCoursesActivity extends AppCompatActivity
{
    //Toolbar in all the activities as a menu -part 1
    Toolbar toolbar;

    //Add Firebase Database
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;


    ArrayList<Course> arrayCourses;
    CourseListAdapter adapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_courses);



        //get user id
        userID = SignInActivity.acct.getId().toString();

        //creat array of courses
        arrayCourses = new ArrayList<Course>();

        //Toolbar in all the activities as a menu -part 2
        toolbar = (Toolbar) findViewById(R.id.toolbar_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My HW");
        toolbar.setSubtitle("welcome");
        toolbar.setLogo(R.mipmap.ic_launcher);

        mListView = (ListView) findViewById(R.id.lv_Courses);

        //connect with internet and read the data form Firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child(userID).child("COURSES");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                // whenever data at this location is updated.
                showData(dataSnapshot);
                mListView.clearAnimation();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.getCode();
                System.err.println("Listener was cancelled");
            }
        });

        //send the data to the list view by using adapter
        adapter = new CourseListAdapter(this, arrayCourses);
        mListView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mListView.clearAnimation();
        adapter.notifyDataSetChanged();
    }

    //Toolbar in all the activities as a menu -part 3
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    //Toolbar in all the activities as a menu -part 4
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

    //read the data from firebase
    private void showData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Course course = new Course();
            course = ds.getValue(Course.class);
            arrayCourses.add(course);
        }
    }


     //toast message
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    //class for the course list array - to send the data to the list view
    public class CourseListAdapter extends BaseAdapter {
        private Context mContext;
        private List<Course> mListCourse;

        public CourseListAdapter(Context mContext, List<Course> mListCourse) {
            this.mContext = mContext;
            this.mListCourse = mListCourse;
        }

        @Override
        public int getCount() {
            return mListCourse.size();
        }

        @Override
        public Object getItem(int i) {
            return mListCourse.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            final View v = View.inflate(mContext, R.layout.item_course_list, null);
            final TextView tvName = (TextView) v.findViewById(R.id.tv_name);
            final TextView tvDate = (TextView) v.findViewById(R.id.tv_date);
            final TextView tvDesctription = (TextView) v.findViewById(R.id.tv_description);
            final Button deleteCourse = (Button) v.findViewById(R.id.deleteCourse);
            deleteCourse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference();
                    myRef.child(SignInActivity.acct.getId().toString())
                            .child("COURSES")
                            .child(mListCourse.get(i).get_cName().toString())
                            .removeValue();
                    mListCourse.remove(i);
                    mListView.clearAnimation();
                    adapter.notifyDataSetChanged();
                }
            });

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext,ViewCJActivity.class);
                    intent.putExtra("tvName",tvName.getText().toString());
                    intent.putExtra("tvDate",tvDate.getText().toString());
                    intent.putExtra("tvDesctription",tvDesctription.getText().toString());
                    mContext.startActivity(intent);
                }
            });

            tvName.setText(mListCourse.get(i).get_cName());
            tvDate.setText(String.valueOf(mListCourse.get(i).get_cDate()));
            tvDesctription.setText(mListCourse.get(i).get_cDetails());
            v.setTag(mListCourse.get(i).get_cName());
            return v;
        }
    }
}
