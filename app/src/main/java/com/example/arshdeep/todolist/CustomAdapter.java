package com.example.arshdeep.todolist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Arshdeep on 6/22/2017.
 */


public class CustomAdapter extends ArrayAdapter<Todo> {
    ArrayList<Todo>todos;
    Context context;
    CustomButtonListner listner;
        //MainActivity mainActivity;
    //OnListButtonClickedListener listener;
    /*
    void setOnListButtonClickedListener(OnListButtonClickedListener listener){
        this.listener = listener;
    }
    */

    public CustomAdapter(@NonNull Context context, ArrayList <Todo> todos) {
        super(context,0);
        this.context = context;
        //mainActivity = (MainActivity)context;
        this.todos = todos;
    }
    @Override
    public int getCount() {
        return todos.size();
    }

    public void setCustomButtonClickedListner(CustomButtonListner listner){
        this.listner = listner;
    }

    static class TodosHolder{
        TextView tv_task;
        TextView tv_date;
        TextView tv_time;
        Button b_done;

        public TodosHolder(TextView tv_task, TextView tv_date, TextView tv_time , Button b_done) {
            this.tv_task = tv_task;
            this.tv_date = tv_date;
            this.tv_time = tv_time;
            this.b_done = b_done;
        }
    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.todo_item_layout , null);
            TextView tv_task = (TextView) convertView.findViewById(R.id.tv_ctask);
            TextView tv_date = (TextView) convertView.findViewById(R.id.tv_cdate);
            TextView tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            Button b_done = (Button) convertView.findViewById(R.id.b_done);
            TodosHolder todosHolder = new TodosHolder(tv_task,tv_date,tv_time, b_done);
            convertView.setTag(todosHolder);
        }
        Todo t = todos.get(position);
        TodosHolder todosHolder = (TodosHolder)convertView.getTag();
        /*
            button on click listener
            position to function mainactvity.listButton(view,position)
            this adapter only applicble to mainactvity therefore interface is used

            creating a listener / interface for tranferring the click to actvity calling it
            onClickListener
                if(listener != null)
                listener.buttonClicked(view  , pos);
        */
        todosHolder.tv_time.setText(t.Time);
        todosHolder.tv_task.setText(t.Task);
        todosHolder.tv_date.setText(t.Date);
        todosHolder.b_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listner != null){
                    listner.onButtonClickListner(view,position);
                }
            }
        });
        return convertView;
    }
}

interface CustomButtonListner{
    void onButtonClickListner(View view ,int position);
}
