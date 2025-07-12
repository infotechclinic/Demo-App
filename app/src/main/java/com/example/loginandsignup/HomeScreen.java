package com.example.loginandsignup;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeScreen extends AppCompatActivity {

    BottomNavigationView bottomNav;
    FloatingActionButton floatingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // Set default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }

        bottomNav = findViewById(R.id.bottom_nav);
        floatingButton = findViewById(R.id.floatingButton);

        loadFragment(new HomeFragment());

        // Bottom nav click
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
                floatingButton.setVisibility(View.VISIBLE); // Show FAB on Home
            } else {
                floatingButton.setVisibility(View.GONE); // Hide FAB elsewhere
                if (itemId == R.id.nav_family) {
                    selectedFragment = new AddFamilyFragment();
                } else if (itemId == R.id.nav_profile) {
                    selectedFragment = new ProfileFragment();
                }
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }

            return false;
        });


        // FloatingActionButton click → Open AddFamilyFragment
        floatingButton.setOnClickListener(view -> {
            loadFragment(new AddFamilyFragment());
            bottomNav.setSelectedItemId(R.id.nav_family);
            floatingButton.setVisibility(View.GONE); // Hide FAB after opening Family
        });

    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (!(currentFragment instanceof HomeFragment)) {
            loadFragment(new HomeFragment());
            bottomNav.setSelectedItemId(R.id.nav_home);
            floatingButton.setVisibility(View.VISIBLE); // Show FAB again
        } else {
            super.onBackPressed();
        }
    }


    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
