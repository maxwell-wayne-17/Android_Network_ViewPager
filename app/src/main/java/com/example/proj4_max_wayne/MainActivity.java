package com.example.proj4_max_wayne;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.proj4_max_wayne.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainAct";
    // Persists across config changes
    private DataVM myVM;

    private ConnectivityCheck myCheck;
    private boolean setUpRecycler = false;

    // Set reference to viewpager
    ViewPager2 vp;
    RecyclerViewAdapter rva;

    // Preference variables
    private SharedPreferences myPreference;
    private SharedPreferences.OnSharedPreferenceChangeListener listener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Don't display title
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Check connectivity
        myCheck = new ConnectivityCheck(this);

        // Create ViewModel
        myVM = new ViewModelProvider(this).get(DataVM.class);

        // Set up preferences
        if (myPreference == null){
            myPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        }
        if (listener == null){
            listener = (sharedPreferences, key) -> {
                Log.d(TAG, "on create preference");
                myVM.getPrefValues(myPreference);
                myVM.getJSON();
            };
        }
        myPreference.registerOnSharedPreferenceChangeListener(listener);
        myVM.getPrefValues(myPreference);

        // Create the observer for JSON
        final Observer<String> resultObserver = result -> {
            // Update the UI
            Log.d(TAG, "onChanged listener = " + result);
//            myVM.setImgLinks(result);
            handleResults(result);
        };
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        myVM.getResult().observe(this,resultObserver);
        myVM.getJSON();

        // *** DOWNLOAD JSON FIRST, AND THEN DECIDE IF WE CAN CREATE ADAPTER ***
//        // Get reference to viewpager
//        vp = findViewById(R.id.recycler_view);
//        // Create an instance of swipe adapter
//        rva = new RecyclerViewAdapter(this, myVM);
//        // Set viewpager to the adapter
//        vp.setAdapter(rva);

        // Try to replace this first with async task
        // Create observer to update UI image
        final Observer<Bitmap> bmpObserver = bitmap -> {
            // Update UI Image
            //imageViewAnimatedChange(getApplicationContext(), iv, bitmap);
        };
        // Observe the LiveData
        myVM.getbmp().observe(this, bmpObserver);

    }

    private void handleResults(String result){
        // Test is json is valid through setImg links
        // if invalid, clear spinner, set scared cat background, set text
        // if valid, set up spinner
        HashMap<String,String> petAndImgs = myVM.setImgLinks(result);
        if (petAndImgs.isEmpty()){
            Log.d(TAG, "Handle results empty array");
            // Reset background
            //setErrorConnectionGUI(result);
        }
        else{
            // Get reference to viewpager
            vp = findViewById(R.id.recycler_view);
            // Create an instance of swipe adapter
            rva = new RecyclerViewAdapter(this, myVM, petAndImgs);
            // Set viewpager to the adapter
            vp.setAdapter(rva);


            Log.d(TAG, "Handle results not empty array");
        }
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
            Intent myIntent = new Intent(this, SettingsActivity.class);
            startActivity(myIntent);
        }

        return super.onOptionsItemSelected(item);
    }

}