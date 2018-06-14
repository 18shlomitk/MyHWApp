package com.example.shlomit.myhwapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
public class ViewAssignmentActivity extends AppCompatActivity {
    TextView txtname,txtDate,txtDet;
    String name;
    private static final String TAG = "AddToDatabase";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_assignment);

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
