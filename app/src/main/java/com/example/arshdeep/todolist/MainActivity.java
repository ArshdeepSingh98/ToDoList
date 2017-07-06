package com.example.arshdeep.todolist;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DrawableUtils;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.annotation.Annotation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Stack;
import static android.R.attr.activatedBackgroundIndicator;
import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.checked;
import static android.R.attr.data;
import static android.R.attr.dropDownWidth;
import static android.R.attr.start;
import static android.R.attr.switchMinWidth;
import static android.R.attr.top;

import android.support.design.widget.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewpager;
    ViewPagerAdapter viewPagerAdapter;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                int id = item.getItemId();
                switch(id) {
                    case R.id.remove_all:

                        AlertDialog.Builder builder_removeAll = new AlertDialog.Builder(MainActivity.this);

                        builder_removeAll.setCancelable(false);
                        builder_removeAll.setTitle("Confirm to Remove All");

                        View v_removeAll = getLayoutInflater().inflate(R.layout.alert_remove_all_layout, null);

                        builder_removeAll.setView(v_removeAll);

                        builder_removeAll.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseHandler db = new DatabaseHandler(MainActivity.this);
                                db.deleteAllTodo();
                                HomeFragment.todos.clear();
                                HomeFragment.listAdapter.notifyDataSetChanged();
                            }
                        });
                        builder_removeAll.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog dialog_removeAll = builder_removeAll.create();
                        dialog_removeAll.show();
                        break;
                    case R.id.favorites:
                        Toast.makeText(MainActivity.this, "Favorites", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.about_us:
                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_VIEW);
                        Uri uri = Uri.parse("https://www.google.com");
                        i.setData(uri);
                        startActivity(i);
                        break;
                    case R.id.contact_us:
                        Intent i1 = new Intent();
                        i1.setAction(Intent.ACTION_DIAL);
                        Uri uri1 = Uri.parse("tel:7009399456");
                        i1.setData(uri1);
                        startActivity(i1);
                        break;
                    case R.id.feedback:
                        Intent i2 = new Intent();
                        i2.setAction(Intent.ACTION_SENDTO);
                        Uri uri2 = Uri.parse("mailto:arshdeep.mailme@gmail.com");
                        i2.putExtra(Intent.EXTRA_SUBJECT, "feedback");
                        i2.setData(uri2);
                        if (i2.resolveActivity(getPackageManager()) != null) {
                            startActivity(i2);
                        }
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new HomeFragment() , "Home");
        viewPagerAdapter.addFragments(new DoneFragment() , "Done");
        viewpager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewpager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
