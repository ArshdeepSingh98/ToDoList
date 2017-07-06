package com.example.arshdeep.todolist;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class DoneFragment extends Fragment {
    ListView doneListView;
    CustomCompletedAdapter doneListAdapter;
    List< Todo > doneList;
    DatabaseHandler db;
    BottomNavigationView bm_navigation;
    ViewPagerAdapter viewPagerAdapter;

    public DoneFragment() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            doneList = new ArrayList<>();

            db = new DatabaseHandler(getContext());
            doneList = db.getAllDone();

            doneListView = (ListView) getView().findViewById(R.id.lv_done);
            doneListAdapter = new CustomCompletedAdapter(getContext(),doneList);
            doneListView.setAdapter(doneListAdapter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_done, container, false);


        bm_navigation = (BottomNavigationView) view.findViewById(R.id.bm_navigation);
        bm_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.return_main:
                        Intent I = new Intent(getContext(), MainActivity.class);
                        startActivity(I);
                        break;
                    case R.id.clear_completed:
                        db.deleteAllDone();
                        doneList.clear();
                        doneListAdapter.notifyDataSetChanged();
                        break;
                }
                return true;
            }
        });
        return view;
    }

}
