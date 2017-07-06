package com.example.arshdeep.todolist;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;

import java.sql.Time;
import java.text.ParseException;
import java.util.Calendar;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.StringTokenizer;
import java.util.TimeZone;

public class TodoDetailActivity extends AppCompatActivity {

    String task;
    String date;
    String time;
    String description;
    String position;
    EditText et_itask;
    EditText et_idate;
    EditText et_idescription;
    EditText et_itime;
    Button b_isubmit;
    DatabaseHandler db;
    Switch switch1;
    private static PendingIntent pendingIntentAlarm;
    private static AlarmManager a;
    private static Todo tt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_detail);

        et_itask = (EditText) findViewById(R.id.et_itask);
        et_idate = (EditText) findViewById(R.id.et_idate);
        et_itime = (EditText) findViewById(R.id.et_itime);
        et_idescription = (EditText) findViewById(R.id.et_idescription);
        b_isubmit = (Button) findViewById(R.id.b_isubmit);
        switch1 = (Switch) findViewById(R.id.switch1);


        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(et_idate.getWindowToken() , 0);
        et_idate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(TodoDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR,year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        et_idate.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
            }
        });

        InputMethodManager inputMethodManager1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager1.hideSoftInputFromWindow(et_itime.getWindowToken() , 0);
        et_itime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar1 = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(TodoDetailActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        calendar1.set(Calendar.HOUR, hour);
                        calendar1.set(Calendar.MINUTE, minute);
                        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm" , Locale.getDefault());
                        et_itime.setText(simpleTimeFormat.format(calendar1.getTime()));
                    }
                },calendar1.get(Calendar.HOUR),calendar1.get(Calendar.MINUTE),true);
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
            }
        });

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Intent i = new Intent(TodoDetailActivity.this , MyReceiver.class);
                pendingIntentAlarm = PendingIntent.getBroadcast(TodoDetailActivity.this,1,i,0);
                a = (AlarmManager) TodoDetailActivity.this.getSystemService(Context.ALARM_SERVICE);
                String date = et_idate.getText().toString();
                String time = et_itime.getText().toString();
                long epoch = 0;
                try {
                    epoch = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).parse(date + " " + time + ":00").getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(b){
                    a.set(AlarmManager.RTC_WAKEUP , epoch, pendingIntentAlarm);
                }else{
                    pendingIntentAlarm.cancel();
                    a.cancel(pendingIntentAlarm);
                }
            }
        });



        db = new DatabaseHandler(this);

        Intent I = getIntent();

        final String id = I.getStringExtra(IntentConstants.ID);

        tt = db.getTodo(Integer.parseInt(id));

        task = tt.Task;
        date = tt.Date;
        description = tt.Description;
        time = tt.Time;
        position = I.getStringExtra(IntentConstants.position);



        et_itask.setText(task);
        et_idate.setText(date);
        et_itime.setText(time);
        et_idescription.setText(description);


        b_isubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newTask = et_itask.getText().toString();
                String newDate = et_idate.getText().toString();
                String newTime = et_itime.getText().toString();
                String newDescription = et_idescription.getText().toString();

                Todo todo = new Todo(Integer.parseInt(id),newTask , newDate , newTime ,newDescription);
                db.updateTodo(todo);

//                //log
//                List<Todo> asd = new ArrayList<Todo>();
//                asd = db.getAllTodos();
//                for(Todo tr : asd){
//                    Log.i("todo" , tr.getId() + " " + tr.getTask() + " " + tr.getDate() + " " + tr.getDescription());
//                }
//                //log

                Intent I = new Intent();
                I.putExtra(IntentConstants.ID ,id);
                I.putExtra(IntentConstants.position , position);
                setResult(RESULT_OK , I);
                finish();
            }
        });
    }

    public static void deleteAlarm(Todo tdel){
        if(tdel.getId() == tt.getId()){
            a.cancel(pendingIntentAlarm);
            pendingIntentAlarm.cancel();
        }
    }
}
