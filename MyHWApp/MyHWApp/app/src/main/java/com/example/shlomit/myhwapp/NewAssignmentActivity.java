package com.example.shlomit.myhwapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class NewAssignmentActivity extends AppCompatActivity {
    public static String aName="";
    public EditText etaName;
    public EditText etaDate;
    public EditText etaDetails;
    String Cname;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference mDatabase;
    Toolbar toolbar;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private static final String TAG = "AddToDatabase";
    CreateFolderActivity createFolderActivity = new CreateFolderActivity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_assignment);

        etaName = new EditText(this);
        etaDate = new EditText(this);
        etaDetails = new EditText(this);

        Bundle extra = getIntent().getExtras();
        if(!extra.isEmpty()){
             Cname = extra.getString("in_course");
        }

        etaName = (EditText) findViewById(R.id.etJobName);
        etaDate = (EditText) findViewById(R.id.etJobDate);
        etaDetails = (EditText) findViewById(R.id.etJobDetails);

          mDatabase = FirebaseDatabase.getInstance().getReference();


        toolbar = (Toolbar) findViewById(R.id.toolbar_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My HW");
        toolbar.setSubtitle("welcome");
        toolbar.setLogo(R.mipmap.ic_launcher);


        etaDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        NewAssignmentActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                etaDate.setText(date);



            }
        };

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String msg=" ";
        switch (item.getItemId()){
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
        Toast.makeText(this, msg + " checked",Toast.LENGTH_LONG).show();

        return super.onOptionsItemSelected(item);
    }



    public void saveDetailsJob(View view) {

        DatabaseReference myRef = database.getReference();
        String myaName = etaName.getText().toString();
        String myaDate = etaDate.getText().toString();
        String myaDetails = etaDetails.getText().toString();
        Assignment mya = new Assignment(myaName, myaDate, myaDetails);
        myRef.child(SignInActivity.acct.getId().toString()).child("COURSES").child(Cname).child("ASSIGNMENTS").child(myaName).setValue(mya);

        // public void sendToCalander(View view)
        Intent calIntent = new Intent(Intent.ACTION_INSERT);
        calIntent.setData(CalendarContract.Events.CONTENT_URI);
        calIntent.putExtra(CalendarContract.Events.TITLE, myaName);
        // calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, "The W Hotel Bar on Third Street");
        calIntent.putExtra(CalendarContract.Events.DESCRIPTION, myaDetails);
        String time = "23:00";
        Calendar startTime = Calendar.getInstance();
        Log.d(TAG, "date: mm/dd/yyy: " + myaDate);
        String[] date1 = myaDate.split("/");
        String[] time1 = time.split(":");
        String[] time2 = time1[1].split(" ");  // to remove am/pm

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(Integer.parseInt(date1[2]), Integer.parseInt(date1[0]),
                Integer.parseInt(date1[1]) - 1, Integer.parseInt(time1[0]), Integer.parseInt(time2[0]));

        Calendar endTime = Calendar.getInstance();
        endTime.set(Integer.parseInt(date1[2]), Integer.parseInt(date1[0]),
                Integer.parseInt(date1[1]), Integer.parseInt(time1[0]), Integer.parseInt(time2[0]));

        calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
        startActivity(calIntent);


        Toast.makeText(this, "ASSIGNMENTS added", Toast.LENGTH_LONG).show();
        etaName.setText("");
        etaDate.setText("");
        etaDetails.setText("");
    }

    public void createFolderPerAssignment(View view) {
        Intent intent = new Intent(this, CreateFolderActivity.class);
        startActivity(intent);

    }
}