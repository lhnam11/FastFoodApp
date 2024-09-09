package com.example.doan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class MainActivity2 extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        String nameC = getIntent().getStringExtra("nameC"); // Lấy nameC từ intent

        setupToolbarAndDrawer();

        if (nameC != null) {
            Fragment_admin fragmentAdmin = Fragment_admin.newInstance(nameC);
            loadFragment(fragmentAdmin, false);
        }
    }

    private void setupToolbarAndDrawer() {
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout_2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_drawer2);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.dish) {
                loadFragment(new Fragment_dish_admin(), false);
                return true;
            } else if (id == R.id.nhanvien) {
                loadFragment(new Fragment_nv(), false);

                String nameC = getIntent().getStringExtra("nameC"); // Lấy nameC từ intent
                if (nameC != null) {
                    Fragment_admin fragmentAdmin = Fragment_admin.newInstance(nameC);
                    loadFragment(fragmentAdmin, false);
                }
                return true;
            } else if (id == R.id.khachhang) {
                loadFragment(new Fragment_user(), false);
                return true;
            }
            else if (id == R.id.Donhang) {
                loadFragment(new Fragment_Order_Admin(), false);
                return true;
            }
            else if (id == R.id.magiam) {
                loadFragment(new Fragment_discount(), false);
                return true;
            }
            else if (id == R.id.logout61) {
                finish();
            }
            return false;
        });
    }

    public void loadFragment(Fragment fragment, boolean isAppInit) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_layout2, fragment);
        ft.commit();
        drawerLayout.closeDrawer(GravityCompat.START);
    }
}
