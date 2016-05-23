package com.jgcheca.kiuwandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.jgcheca.kiuwandroid.fragments.AppsListFragment;
import com.jgcheca.kiuwandroid.fragments.piecharting;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity{

    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
     /*   ((TextView) findViewById(R.id.nav_header_txt_organization)).setText(intent.getStringExtra("organization"));
        ((TextView) findViewById(R.id.nav_header_txt_username)).setText(intent.getStringExtra("username"));*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,  mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        Fragment fragment = new AppsListFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        navigationView.getMenu().getItem(0).setChecked(true);
        // Highlight the selected item, update the title, and close the drawer
        setTitle(getResources().getString(R.string.nav_title_appList));


        /*ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }*/

        //Using FAB from android design app compat library
        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


   /*     TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {

       Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new AppsListFragment(), "List of Apps");
        adapter.addFragment(new piecharting(), "Chart");

        viewPager.setAdapter(adapter);

    }

    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(
                        new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        selectItem(menuItem);
                        return true;
                    }
                });

    }
    public void selectItem(MenuItem menuItem) {

        Fragment fragment = null;




        switch (menuItem.getItemId()){
            case R.id.navigation_item_1:
                fragment = new AppsListFragment();
                Toast.makeText(this, "Selected 0", Toast.LENGTH_SHORT).show();
                break;
            case R.id.navigation_item_2:
                fragment = new piecharting();
                Toast.makeText(this, "Selected 1", Toast.LENGTH_SHORT).show();
                break;

            case R.id.navigation_item_3:
                Toast.makeText(this, "Slected 2", Toast.LENGTH_SHORT).show();
                break;

            case 3:
                Toast.makeText(this, "Selected 3", Toast.LENGTH_SHORT).show();
                break;
            // Insert the fragment by replacing any existing fragment

        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        // Highlight the selected item, update the title, and close the drawer
        setTitle(menuItem.getTitle());
    }
    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

}
