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

import com.example.proj4_max_wayne.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainAct";
    // Persists across config changes
    private DataVM myVM;

    private ConnectivityCheck myCheck;

    private ImageView iv;
    private TextView tv;

    // Preference variables
    private SharedPreferences myPreference;
    private SharedPreferences.OnSharedPreferenceChangeListener listener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set reference to widgets
//        iv = findViewById(R.id.image);
//        tv = findViewById(R.id.imgName);
//        tv.setText("On create");

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

        // Create observer to update UI image
        final Observer<Bitmap> bmpObserver = bitmap -> {
            // Update UI Image
            //imageViewAnimatedChange(getApplicationContext(), iv, bitmap);
        };
        // Observe the LiveData
        myVM.getbmp().observe(this, bmpObserver);

        // Create the observer which updates the UI
        final Observer<String> resultObserver = result -> {
            // Update the UI
            Log.d(TAG, "onChanged listener = " + result);
            handleResults(result);
        };
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        myVM.getResult().observe(this,resultObserver);
        myVM.getJSON();
    }

    private void handleResults(String result){
        // Test is json is valid through setImg links
        // if invalid, clear spinner, set scared cat background, set text
        // if valid, set up spinner
        List<String> petNames = myVM.setImgLinks(result);
        if (petNames.isEmpty()){
            Log.d(TAG, "Handle results empty array");
            // Reset background
            //setErrorConnectionGUI(result);
        }
        else{
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