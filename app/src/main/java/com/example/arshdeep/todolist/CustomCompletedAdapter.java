package com.example.arshdeep.todolist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Arshdeep on 6/30/2017.
 */

public class CustomCompletedAdapter extends ArrayAdapter<Todo>{

    List<Todo> completed_todos;
    Context context;

    public CustomCompletedAdapter(@NonNull Context context , List< Todo > todos) {
        super(context,0);
        this.context = context;
        this.completed_todos = todos;
    }

    @Override
    public int getCount() {
        return completed_todos.size();
    }

    static class TodosHolder{
        TextView tv_ctask;
        TextView tv_cdate;
        TextView tv_ctime;

        public TodosHolder(TextView tv_ctask, TextView tv_cdate, TextView tv_ctime) {
            this.tv_ctask = tv_ctask;
            this.tv_cdate = tv_cdate;
            this.tv_ctime = tv_ctime;
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.todo_completed_item_layout , null);
            TextView tv_ctask = (TextView) convertView.findViewById(R.id.tv_ctask);
            TextView tv_cdate = (TextView) convertView.findViewById(R.id.tv_cdate);
            TextView tv_ctime = (TextView) convertView.findViewById(R.id.tv_ctime);
            CustomCompletedAdapter.TodosHolder todosHolder = new CustomCompletedAdapter.TodosHolder(tv_ctask,tv_cdate,tv_ctime);
            convertView.setTag(todosHolder);
        }
        Todo t = completed_todos.get(position);
        CustomCompletedAdapter.TodosHolder todosHolder = (CustomCompletedAdapter.TodosHolder)convertView.getTag();
        todosHolder.tv_ctask.setText(t.Task);
        todosHolder.tv_cdate.setText(t.Date);
        todosHolder.tv_ctime.setText(t.Time);
        return convertView;
    }
}
