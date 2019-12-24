package com.hoangpro.dotplaysapp.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hoangpro.dotplaysapp.R;
import com.hoangpro.dotplaysapp.base.BaseActivity;
import com.hoangpro.dotplaysapp.fragment.CatFragment;
import com.hoangpro.dotplaysapp.fragment.HomeFragment;
import com.hoangpro.dotplaysapp.fragment.ViewCatFragment;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public BottomNavigationView bottomNavigationView;
    private Toolbar toolBar;
    private String TAG="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        getSupportFragmentManager().beginTransaction().replace(R.id.fragView, new HomeFragment()).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void initView() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        toolBar = findViewById(R.id.toolBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.seach_menu_toolbar, menu);
//        MenuItem itemSearch = menu.findItem(R.id.item_search);
//        SearchView searchView = (SearchView) itemSearch.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Log.e(TAG, query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });

        return true;
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof ViewCatFragment){

        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_search:
                openActivity(SearchActivity.class, false);
                overridePendingTransition(0, 0);
                break;
            case android.R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragView,new CatFragment()).commit();
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.item_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragView, new HomeFragment()).commit();
                break;
            case R.id.item_categories:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragView, new CatFragment()).commit();
                break;
        }
        return true;
    }
}
