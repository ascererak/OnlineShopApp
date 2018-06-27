package com.example.vlad.internetshop.Views;

import com.example.vlad.internetshop.Data.ShopData;
import com.example.vlad.internetshop.Data.TaskLoadMainDevices;
import com.example.vlad.internetshop.Enteties.DeviceCard;
import com.example.vlad.internetshop.Presenters.MainPresenter;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.example.vlad.internetshop.R;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, IMainActivity,
        LoaderManager.LoaderCallbacks<List<DeviceCard>>, SearchView.OnQueryTextListener,
        NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private android.support.v7.widget.Toolbar mToolBar;

    private MainPresenter presenter;
    private RecyclerViewMainAdapter deviceCardAdapter;

    List<DeviceCard> mainDevicesList = new ArrayList<>();
    List<DeviceCard> promotionalDeviceCardList = new ArrayList<>();

    private static AtomicInteger loaderToLoad = new AtomicInteger();

    private RecyclerView recyclerViewDevicesCards, recyclerViewPromotional;
    private SwipeRefreshLayout swipeRefreshLayoutMain;

    //Loaders ids
    private final int LOADER_ALL_DEVICES_ID = 1;
    private final int LOADER_ALL_PROMOTIONAL_DEVICES_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

        presenter = new MainPresenter(this);
        swipeRefreshLayoutMain = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshMain);
        swipeRefreshLayoutMain.setOnRefreshListener(this);
        swipeRefreshLayoutMain.setColorSchemeColors(getResources().getColor(android.R.color.holo_red_dark),
                getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_blue_dark));
        initRecyclerViews();

        //load all devices and promotional devices
        loadAllDeives(false, true);
        loadAllDeives(false, false);
    }

    /** Start new code*/

    private void initialize(){
        mToolBar = findViewById(R.id.navAction);
        setSupportActionBar(mToolBar);
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Handling selection menu items
     *
     * @param item selected menu item
     * @return was item been selected or wasn't
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    /**
     * Menu creation, setting search button
     *
     * @param menu menu
     * @return successfully created menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.navigation_menu_search, menu);
        // Connection with menu xml markup
        MenuItem menuItem = menu.findItem(R.id.action_search);
        // Set search view on the toolbar
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        // Set listener when text is changed
        searchView.setOnQueryTextListener(this);

        return true;
    }

    /**
     * Submit inputted text in the search field
     *
     * @param s
     * @return
     */
    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    /**
     * Change display list passwords when search field is changed
     *
     * @param s string that was inputted in search field
     * @return
     */
    @Override
    public boolean onQueryTextChange(String s) {

        s = s.toLowerCase();

        // Create new list for found devices
        ArrayList<DeviceCard> newList = new ArrayList<>();

        // Look for suitable devices and add them to new list
        for (DeviceCard deviceCard : mainDevicesList) {
            String deviceName = deviceCard.getName().toLowerCase();

            if (deviceName.contains(s))
                newList.add(deviceCard);
        }

        // Set filter to display only devices
        // that are suit to the search query
        deviceCardAdapter.setFilter(newList);

        return true;
    }

    /**
     * Handling what items from navigation view are selected
     *
     * @param item
     * @return successfully selected item
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Identify what item was selected
        switch (id) {
            case R.id.nav_Register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
        // Close drawer
        mDrawerLayout.closeDrawers();
        return false;
    }

    /** End new code*/

    /**
     *  Initialize recyclerview in the beginning
     */
    public void initRecyclerViews() {
        //Recyclerview Main
        recyclerViewDevicesCards = (RecyclerView) findViewById(R.id.recyclerViewMain);
        deviceCardAdapter = new RecyclerViewMainAdapter(mainDevicesList, promotionalDeviceCardList, getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerViewDevicesCards.setLayoutManager(layoutManager);
        recyclerViewDevicesCards.setAdapter(deviceCardAdapter);
    }

    @Override
    public void stopSwipeRefresh() {
        if (swipeRefreshLayoutMain.isRefreshing())
            swipeRefreshLayoutMain.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        loadAllDeives(true, false);
        loadAllDeives(true, true);
    }

    /**
     * Loader Callback, create loader by current id with params in bundle
     * @param id - id of the loader
     * @param args - params for init loader
     * @return list of deviceCard
     */
    @NonNull
    @Override
    public Loader<List<DeviceCard>> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == LOADER_ALL_DEVICES_ID)
            return new TaskLoadMainDevices(MainActivity.this, presenter,  false);
        else
            return new TaskLoadMainDevices(MainActivity.this, presenter,  true);
    }

    /**
     * Loader callback. Loader finishes its work here and return the result. updating all views with the new lists
     * @param loader loader object that finishes work
     * @param data data that returns loader
     */
    @Override
    public void onLoadFinished(@NonNull Loader<List<DeviceCard>> loader, List<DeviceCard> data) {
        switch (loader.getId()) {
            case LOADER_ALL_DEVICES_ID:
                deviceCardAdapter.updateMainList(data);
                break;
            case LOADER_ALL_PROMOTIONAL_DEVICES_ID:
                deviceCardAdapter.updatePromList(data);
                break;
        }

        Log.d("Tagg", "onFinished, beforedec::" + loaderToLoad);
        if (loaderToLoad.decrementAndGet() == 0) {
            Log.d("Tagg", "stop refreshing");
            swipeRefreshLayoutMain.setRefreshing(false);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<DeviceCard>> loader) {

    }

    /**
     * starts(returns existing) loader if restart is false, restarts the loader if the restart flag is true.
     * flag isPromotiomal used for make a decision what id use.
     * @param restart flag if this loader must reload data
     * @param isPromotional flag if this loader must load promotional devices
     */
    private void loadAllDeives(boolean restart, boolean isPromotional) {
        swipeRefreshLayoutMain.setRefreshing(true);
        loaderToLoad.incrementAndGet();
        Log.d("Tagg", "loadAll, afterinc:" + loaderToLoad);

        if (restart) {
            if (isPromotional)
                getSupportLoaderManager().restartLoader(LOADER_ALL_PROMOTIONAL_DEVICES_ID, Bundle.EMPTY, this);
            else
                getSupportLoaderManager().restartLoader(LOADER_ALL_DEVICES_ID, Bundle.EMPTY, this);
        } else {
            if (isPromotional)
                getSupportLoaderManager().initLoader(LOADER_ALL_PROMOTIONAL_DEVICES_ID, Bundle.EMPTY, this);
            else
                getSupportLoaderManager().initLoader(LOADER_ALL_DEVICES_ID, Bundle.EMPTY, this);
        }
    }
}
