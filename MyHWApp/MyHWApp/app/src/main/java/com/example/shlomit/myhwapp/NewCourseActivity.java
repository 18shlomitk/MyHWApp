package com.example.shlomit.myhwapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class NewCourseActivity extends AppCompatActivity {
    private static final String TAG = "AddToDatabase";
    private Button mAddToDB;
    //add Firebase Database stuff
    private DatabaseReference myRef;
    //public static String cName = "the course name";
    public EditText eTcName;
    public EditText eTcDate;
    public EditText eTcDetails;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference mDatabase;
    Toolbar toolbar;
    Vibrator vibrator;


    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_course);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        //declare variables in oncreate
        mAddToDB = (Button) findViewById(R.id.btnCourseOK);
        eTcName = (EditText) findViewById(R.id.etCourseName);
        //declare the database reference object. This is what we use to access the database.
        //NOTE: Unless you are signed in, this will not be useable.
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        eTcName = new EditText(this);
        eTcDate = new EditText(this);
        eTcDetails = new EditText(this);
        eTcName = (EditText) findViewById(R.id.etCourseName);
        eTcDate = (EditText) findViewById(R.id.etCourseDate);
        eTcDetails = (EditText) findViewById(R.id.etCourseDetails);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        toolbar = (Toolbar) findViewById(R.id.toolbar_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My HW");
        toolbar.setSubtitle("welcome");
        toolbar.setLogo(R.mipmap.ic_launcher);

        eTcDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        NewCourseActivity.this,
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
                eTcDate.setText(date);
            }
        };

    }

    public void saveDetails(View view) {
        //cName = eTcName.getText().toString();
        DatabaseReference myRef = database.getReference();


        if(eTcName.getText().toString().isEmpty()||
        eTcDate.getText().toString().isEmpty() ||
                eTcDetails.getText().toString().isEmpty()) {
            Toast.makeText(this, "one of the field is empty", Toast.LENGTH_LONG).show();
        }
        else {
            String mycName = eTcName.getText().toString();
            String mycDate = eTcDate.getText().toString();
            String mycDetails = eTcDetails.getText().toString();
            Course myc = new Course(mycName, mycDate, mycDetails);
            //Course myc= new Course("test","test","test");
            myRef.child(SignInActivity.acct.getId().toString()).child("COURSES").child(mycName).setValue(myc);


            // myRef.child("COURSES");
            Toast.makeText(this, "course added", Toast.LENGTH_LONG).show();

            //TODO check Validation
            vibrator.vibrate(100);

            eTcName.setText("");
            eTcDate.setText("");
            eTcDetails.setText("");
        }
    }

    //add a toast to show when successfully signed in
    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

}

