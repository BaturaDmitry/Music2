package by.bsuir.mobilki2laba;

import static by.bsuir.mobilki2laba.MainActivity.GLOBAL_CURRENCY_BYN;
import static by.bsuir.mobilki2laba.MainActivity.GLOBAL_CURRENCY_USD;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

import by.bsuir.mobilki2laba.R;

public class ProfileFragment extends Fragment {

    public static SupportMapFragment supportMapFragment;
    public static FusedLocationProviderClient client;
    private Menu menu;
    private TextView userNameTextView;
    private TextView userEmailTextView;
    private ImageView userProfileImageView;
    private Button favouriteButton;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu, menu);
        setUsdOrByn();
        setNightOrDay();
    }
    private void setNightOrDay(){
        if(handleDayNightTheme()){
            menu.findItem(R.id.item_change_theme).setIcon(R.drawable.sunny);
        }else{
            menu.findItem(R.id.item_change_theme).setIcon(R.drawable.ic_moon);
        }
    }
    private void setUsdOrByn(){
        if(handleCurrencySwitch()){
            menu.findItem(R.id.item_convert_money).setTitle("USD");
        }else{
            menu.findItem(R.id.item_convert_money).setTitle("BYN");
        }
    }
    private void findMapFragment() { supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map); }


    public static void getCurrentLocation() {
        @SuppressLint("MissingPermission")
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {

                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            MarkerOptions options = new MarkerOptions().position(latLng);
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                            Objects.requireNonNull(googleMap.addMarker(options));
                        }
                    });
                }
            }
        });
    }



    private boolean handleDayNightTheme() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.mainActivityContext);
        boolean dayTheme = sharedPrefs.getBoolean("IS_DAY_THEME", true);

        return dayTheme;
    }

    private boolean handleCurrencySwitch() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.mainActivityContext);
        boolean isUsd = sharedPrefs.getBoolean("IS_USD", true);

        return isUsd;
    }

    private void showCourseOfDollar(){
        Toast.makeText(MainActivity.mainActivityContext.getApplicationContext(),
                "1 USD = "+ MainActivity.USD_IN_BYN + " BYN",Toast.LENGTH_SHORT).show();
    }

    private void handleProfileInfoTextViews(){
        userNameTextView.setText(MainActivity.userProfileName);
        userEmailTextView.setText(MainActivity.userEmail);
        userProfileImageView.setImageBitmap(MainActivity.userProfileBitmap);
    }

    private void findSpotifyUserViews(View v){
        userNameTextView = v.findViewById(R.id.userNameTextView);
        userEmailTextView = v.findViewById(R.id.emailTextView);
        userProfileImageView = v.findViewById(R.id.profileImageView);
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_change_theme:
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.mainActivityContext);
                if (handleDayNightTheme()){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("IS_DAY_THEME", false);
                    editor.apply();
                } else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    item.setIcon(R.drawable.ic_moon);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("IS_DAY_THEME", true);
                    editor.apply();
                }
                setNightOrDay();

                return true;
            case R.id.item_convert_money:
                SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(MainActivity.mainActivityContext);
                if (handleCurrencySwitch()){

                    SharedPreferences.Editor editor = shared.edit();
                    editor.putBoolean("IS_USD", false);
                    MainActivity.IS_USD = false;

                    MainActivity.GLOBAL_CURRENCY_COEFF = GLOBAL_CURRENCY_BYN;

                    editor.apply();
                } else{
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putBoolean("IS_USD", true);
                    MainActivity.IS_USD = true;

                    MainActivity.GLOBAL_CURRENCY_COEFF = GLOBAL_CURRENCY_USD;

                    editor.apply();
                }
                setUsdOrByn();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);

        findMapFragment();
        findSpotifyUserViews(view);

        if (MainActivity.isNetworkAvailable()) {
            client = LocationServices.getFusedLocationProviderClient(MainActivity.mainActivityContext);
            if (ActivityCompat.checkSelfPermission(MainActivity.mainActivityContext, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                ActivityCompat.requestPermissions((Activity) MainActivity.mainActivityContext,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }
        }

        showCourseOfDollar();
        handleDayNightTheme();
        handleCurrencySwitch();
        handleProfileInfoTextViews();




        favouriteButton = view.findViewById(R.id.getFavouritesActivity);
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toFavouriteActivity = new Intent(MainActivity.mainActivityContext, FavouriteActivity.class);
                startActivity(toFavouriteActivity);
            }
        });

        return view;
    }
}