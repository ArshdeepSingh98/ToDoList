package com.example.arshdeep.todolist;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


public class HomeFragment extends Fragment implements CustomButtonListner {
    private ListView listView;
    static ArrayList<Todo> todos;
    static CustomAdapter listAdapter;
    ArrayList < Todo > undoList;
    static ArrayList < Todo > favList;
    DatabaseHandler db;
    BottomNavigationView bottom_navigation;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        setHasOptionsMenu(true);

        todos = new ArrayList<>();
        undoList = new ArrayList<>();
        favList = new ArrayList<>();

        listView = (ListView) view.findViewById(R.id.listView);
        listAdapter = new CustomAdapter(getContext() , todos);
        listAdapter.setCustomButtonClickedListner(this);

        listView.setAdapter(listAdapter);

        db = new DatabaseHandler(getContext());

        for(Todo p : db.getAllTodos()){
            todos.add(p);
        }
        listAdapter.notifyDataSetChanged();

        bottom_navigation = (BottomNavigationView) view.findViewById(R.id.bottom_navigation);
        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.add_item:
                        AlertDialog.Builder builder_add = new AlertDialog.Builder(getContext());

                        builder_add.setCancelable(false);
                        builder_add.setTitle("Add Item");

                        View v_add = getActivity().getLayoutInflater().inflate(R.layout.alert_add_layout, null);

                        TextView tv_task = v_add.findViewById(R.id.tv_ctask);
                        tv_task.setText("Enter Task");
                        TextView tv_date = v_add.findViewById(R.id.tv_cdate);
                        tv_date.setText("Enter Date");

                        final EditText et_task = v_add.findViewById(R.id.et_task);
                        final EditText et_date = v_add.findViewById(R.id.et_date);

                        et_date.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onClick(View view) {
                                final Calendar calendar = Calendar.getInstance();
                                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                    @Override
                                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                        calendar.set(Calendar.YEAR,year);
                                        calendar.set(Calendar.MONTH, monthOfYear);
                                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy" , Locale.getDefault());
                                        et_date.setText(simpleDateFormat.format(calendar.getTime()));
                                    }
                                },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                                datePickerDialog.setTitle("Select Date");
                                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                                datePickerDialog.show();
                            }
                        });

                        builder_add.setView(v_add);
                        builder_add.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if(et_task.getText().toString().trim().length() > 0) {
                                    Todo temp = new Todo(et_task.getText().toString().trim(),et_date.getText().toString(),"","");

                                    long id = db.addTodo(temp);

                                    temp.setId((int)id);

                                    todos.add(temp);
                                    //log
                                    List<Todo> asd = new ArrayList<Todo>();
                                    asd = db.getAllTodos();
                                    for(Todo tr : asd){
                                        Log.i("todo" , tr.getId() + " " +  tr.getTask() + " " + tr.getDate() + " " + tr.getDescription());
                                    }
                                    //log
                                    listAdapter.notifyDataSetChanged();
                                }else{
                                    Toast.makeText(getContext(), "Invalid Input", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder_add.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog dialog_add = builder_add.create();
                        dialog_add.show();
                        break;
                    case R.id.remove_item:
                        AlertDialog.Builder builder_remove = new AlertDialog.Builder(getContext());

                        builder_remove.setCancelable(false);

                        View v_remove = getActivity().getLayoutInflater().inflate(R.layout.alert_remove_layout, null);

                        final EditText et_position = v_remove.findViewById(R.id.et_position);

                        builder_remove.setView(v_remove);
                        builder_remove.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if(et_position.getText().length() > 0 && Integer.parseInt(et_position.getText().toString()) <= todos.size()) {
                                    int position = Integer.parseInt(et_position.getText().toString());

                                    Todo temp = todos.get(position-1);
                                    long id = db.deleteTodo(temp);

                                    undoList.add(temp);

                                    todos.remove(position-1);

//                          for(Todo t : todos){
//
//                              Log.i("Todo: ", ""+t.getId());
//                          }

                                    listAdapter.notifyDataSetChanged();
                                }else{
                                    Toast.makeText(getContext(), "Invalid Input", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder_remove.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog dialog_remove = builder_remove.create();
                        dialog_remove.show();
                        break;
                    case R.id.undo_delete:
                        if(undoList.size() != 0) {
                            Todo ty = undoList.get(undoList.size() - 1);
                            undoList.remove(undoList.size() - 1);
                            todos.add(ty);
                            db.addTodo(ty);
                            listAdapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(getContext(), "No Recent Deletes", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                return true;
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, final View view, final int i, long l) {
                AlertDialog.Builder builder_rem = new AlertDialog.Builder(getContext());
                builder_rem.setCancelable(false);
                View v_rem = getActivity().getLayoutInflater().inflate(R.layout.alert_remove_long_layout, null);
                TextView tv_remove = v_rem.findViewById(R.id.tv_remove);
                tv_remove.setText("Confirm Delete");
                builder_rem.setView(v_rem);
                builder_rem.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {
                        final Todo temp = todos.get(i);
                        TodoDetailActivity.deleteAlarm(temp);
                        undoList.add(temp);
                        db.deleteTodo(temp);
                        todos.remove(i);
                        listAdapter.notifyDataSetChanged();
                        Snackbar.make(listView, "Todo Deleted" , Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                undoList.remove(undoList.size()-1);
                                todos.add(temp);
                                db.addUndoTodo(temp);
                                listAdapter.notifyDataSetChanged();
                            }
                        }).show();
                    }
                });
                builder_rem.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog_remove = builder_rem.create();
                dialog_remove.show();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent I = new Intent(getContext() , TodoDetailActivity.class);
                I.putExtra(IntentConstants.position,String.valueOf(i));
                I.putExtra(IntentConstants.ID,String.valueOf(todos.get(i).getId()));
                startActivityForResult(I , 1);
            }
        });

        return view;
    }

    @Override
    public void onButtonClickListner(View v , final int position) {
        int id = v.getId();
        switch (id){
            case R.id.b_done:
                Todo temp = todos.get(position);
                todos.remove(position);
                db.deleteTodo(temp);
                listAdapter.notifyDataSetChanged();
                db.addDoneTodo(temp);
                break;
//            case R.id.b_fav:
//                final ImageButton b = (ImageButton) v;
//                if(b.getTag() == "ON"){
//                    int id2 = todos.get(position).getId();
//                    int posDel = 0;
//                    int g=0;
//                    for(Todo t : favList){
//                        if(t.getId() == id2){
//                            posDel = g;
//                            break;
//                        }
//                        g++;
//                    }
//                    favList.remove(g);
//                    b.setImageResource(R.drawable.white_fav);
//                    Snackbar.make(v, "Removed from Favorites" , Snackbar.LENGTH_LONG).show();
//                    b.setTag("OFF");
//                }else {
//                    favList.add(todos.get(position));
//                    b.setImageResource(R.drawable.black_fav);
//                    Snackbar.make(v, "Added to Favorites", Snackbar.LENGTH_LONG).show();
//                    b.setTag("ON");
//                }
//                break;
        }
    }

    private void updateTodoList(Intent data){
        String id = data.getStringExtra(IntentConstants.ID);
        String position = data.getStringExtra(IntentConstants.position);

        Todo tt = db.getTodo(Integer.parseInt(id));
        todos.set(Integer.parseInt(position) , tt);

//        //log
//        List<Todo> asd = new ArrayList<Todo>();
//        asd = db.getAllTodos();
//        for(Todo tr : asd){
//            Log.i("todo" , tr.getId() + " " + tr.getTask() + " " + tr.getDate() + " " + tr.getDescription());
//        }

        listAdapter.notifyDataSetChanged();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK){
            updateTodoList(data);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_2_layout , menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
