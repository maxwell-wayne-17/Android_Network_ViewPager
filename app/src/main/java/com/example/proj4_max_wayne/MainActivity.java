package com.example.proj4_max_wayne;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.widget.ViewPager2;

import java.util.HashMap;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainAct";
    // Persists across config changes
    private DataVM myVM;

    private ConnectivityCheck myCheck;

    // Set reference to viewpager
    private ViewPager2 vp;
    private RecyclerViewAdapter rva;

    // Preference variables
    private SharedPreferences myPreference;
    private SharedPreferences.OnSharedPreferenceChangeListener listener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "On create test");

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
            handleResults(result);
        };
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        myVM.getResult().observe(this,resultObserver);
        myVM.getJSON();

        // Try to replace this first with async task
        // Create observer to update UI image
        final Observer<HashMap<Bitmap, RecyclerViewAdapter.ImgViewHolder>> bmpObserver = map -> {
            // Update UI Image
            Bitmap pic = map.keySet().stream().collect(Collectors.toList()).get(0);
            RecyclerViewAdapter.ImgViewHolder tempVh = map.get(pic);
            tempVh.setIv(pic);
        };
        // Observe the LiveData
        myVM.getbmp().observe(this, bmpObserver);

    }

    private void handleResults(String result){
        HashMap<String,String> petsAndImgs = myVM.setImgLinks(result);
        // Get reference to viewpager
        vp = findViewById(R.id.viewpager2);
        // Create an instance of swipe adapter
        rva = new RecyclerViewAdapter(this, myVM, myCheck, petsAndImgs);
        // Set viewpager to the adapter
        vp.setAdapter(rva);
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