package com.example.vlad.internetshop.Views;

import com.example.vlad.internetshop.Data.TaskLoadMainDevices;
import com.example.vlad.internetshop.Enteties.DeviceCard;
import com.example.vlad.internetshop.Presenters.MainPresenter;

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

import com.example.vlad.internetshop.Data.IShopData;
import com.example.vlad.internetshop.Data.ShopData;
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

        }
        // Close drawer
        mDrawerLayout.closeDrawers();
        return false;
    }

    /** End new code*/

    public void initRecyclerViews() {
        //Recyclerview Main
        mainDevicesList.add(new DeviceCard());
        promotionalDeviceCardList.add(new DeviceCard());
        recyclerViewDevicesCards = (RecyclerView) findViewById(R.id.recyclerViewMain);
        deviceCardAdapter = new RecyclerViewMainAdapter(mainDevicesList, promotionalDeviceCardList, getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerViewDevicesCards.setLayoutManager(layoutManager);
        recyclerViewDevicesCards.setAdapter(deviceCardAdapter);

        //Recyclerview with promotional devices
        //recyclerViewPromotional = (RecyclerView)findViewById(R.id.recyclerViewPromotional);
        //promotionalDeviceCardAdapter = new RecyclerViewPromAdapter(promotionalDeviceCardList, getApplicationContext());
        //LinearLayoutManager horizontalManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        //recyclerViewPromotional.setLayoutManager(horizontalManager);
        //recyclerViewPromotional.setAdapter(promotionalDeviceCardAdapter);
    }

    public void recyclerViewsDataSetChange(List<DeviceCard> deviceCardList, List<DeviceCard> promotionalDeviceCardList) {
        mainDevicesList.clear();
        mainDevicesList.addAll(deviceCardList);
        //update data to the list
        deviceCardAdapter.notifyDataSetChanged();

        this.promotionalDeviceCardList.clear();
        this.promotionalDeviceCardList.addAll(promotionalDeviceCardList);
        //Update data to the list
        deviceCardAdapter.getPromAdapter().notifyDataSetChanged();
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

    @NonNull
    @Override
    public Loader<List<DeviceCard>> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == LOADER_ALL_DEVICES_ID)
            return new TaskLoadMainDevices(MainActivity.this, presenter, presenter.getShopData(), false);
        else
            return new TaskLoadMainDevices(MainActivity.this, presenter, presenter.getShopData(), true);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<DeviceCard>> loader, List<DeviceCard> data) {
        switch (loader.getId()) {
            case LOADER_ALL_DEVICES_ID:
                mainDevicesList.clear();
                mainDevicesList.addAll(data);
                //update data to the list
                deviceCardAdapter.notifyDataSetChanged();
                break;
            case LOADER_ALL_PROMOTIONAL_DEVICES_ID:
                this.promotionalDeviceCardList.clear();
                this.promotionalDeviceCardList.addAll(data);
                //Update data to the list
                deviceCardAdapter.getPromAdapter().notifyDataSetChanged();
                break;
        }

        Log.d("Tagg", "onFinished, beforedec::" + loaderToLoad);
        if (loaderToLoad.decrementAndGet() == 0) {
            Log.d("Tagg", "stop refreshing");
            swipeRefreshLayoutMain.setRefreshing(false);
            //recyclerViewsDataSetChange(mainDevicesList, promotionalDeviceCardList);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<DeviceCard>> loader) {

    }

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
