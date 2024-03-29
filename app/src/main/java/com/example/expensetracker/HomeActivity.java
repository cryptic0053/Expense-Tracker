package com.example.expensetracker;

import static com.example.expensetracker.R.id.dashboard;
import static com.example.expensetracker.R.id.income;
import static com.example.expensetracker.R.id.logout;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;

    private DashBoardFragment dashBoardFragment;
    private IncomeFragment incomeFragment;
    private ExpenseFragment expenseFragment;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Expense Tracker");
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        bottomNavigationView = findViewById(R.id.bottomNavigationbar);
        drawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.naView);
        navigationView.setNavigationItemSelectedListener(this);

        dashBoardFragment = new DashBoardFragment();
        incomeFragment = new IncomeFragment();
        expenseFragment = new ExpenseFragment();

        setFragment(dashBoardFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.dashboard) {
                setFragment(dashBoardFragment);
                bottomNavigationView.setItemBackgroundResource(R.color.dashboard_color);
            } else if (itemId == R.id.income) {
                setFragment(incomeFragment);
                bottomNavigationView.setItemBackgroundResource(R.color.income_color);
            } else if (itemId == R.id.expense) {
                setFragment(expenseFragment);
                bottomNavigationView.setItemBackgroundResource(R.color.expense_color);
            }

            return true;
        });

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void displaySelectedListener(int itemId) {
        Fragment fragment = null;

        if (itemId == R.id.dashboard) {
            itemId = dashboard;
            fragment = new DashBoardFragment();
        } else if (itemId == R.id.income) {
            itemId = income;
            fragment = new IncomeFragment();
        } else if (itemId == R.id.expense) {
            itemId = R.id.expense;
            fragment = new ExpenseFragment();
        }

        else if (itemId == R.id.logout) {
            itemId = logout;
            mAuth.signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finishAffinity();
            return;
        }

        if (fragment != null) {
            setFragment(fragment);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedListener(item.getItemId());
        return true;
    }
}
