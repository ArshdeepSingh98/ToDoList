package com.example.arshdeep.todolist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class FavoriteListActivity extends AppCompatActivity {
    ListView favListView;
    ArrayList <Todo> favList;
    CustomAdapter favListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        favList = new ArrayList<>();

        for(Todo t : HomeFragment.favList){
            favList.add(t);
        }

        favListView = (ListView) findViewById(R.id.favListView);
        favListAdapter = new CustomAdapter(FavoriteListActivity.this , favList);
        favListView.setAdapter(favListAdapter);
    }
}
