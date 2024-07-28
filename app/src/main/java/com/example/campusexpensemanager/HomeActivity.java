package com.example.campusexpensemanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {
    ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;
    BottomNavigationView bottomNavigationView;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Kiểm tra session đăng nhập
        SharedPreferences sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE);
        String userId = sharedPreferences.getString("USER_ID", null);
        String userName = sharedPreferences.getString("USER_NAME", null);
        String userMail = sharedPreferences.getString("USER_MAIL", null);

        if (userId != null && userMail != null && userName != null) {


            setContentView(R.layout.activity_home);
            bottomNavigationView = findViewById(R.id.bottomNav);
            viewPager2 = findViewById(R.id.view_pager_layout_menu);
            viewPagerAdapter = new ViewPagerAdapter(this);
            viewPager2.setAdapter(viewPagerAdapter);
            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.bottom_home) {
                        viewPager2.setCurrentItem(0);
                    } else if (id == R.id.bottom_expense_tracker) {
                        viewPager2.setCurrentItem(1);
                    } else if (id == R.id.bottom_statistic) {
                        viewPager2.setCurrentItem(2);
                    } else if (id == R.id.bottom_profile) {
                        viewPager2.setCurrentItem(3);
                    } else {
                        return false;
                    }
                    return true;
                }


            });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    bottomNavigationView.getMenu().findItem(R.id.bottom_home).setChecked(true);
                } else if (position == 1) {
                    bottomNavigationView.getMenu().findItem(R.id.bottom_expense_tracker).setChecked(true);
                } else if (position == 2) {
                    bottomNavigationView.getMenu().findItem(R.id.bottom_statistic).setChecked(true);
                } else if (position == 3) {
                    bottomNavigationView.getMenu().findItem(R.id.bottom_profile).setChecked(true);
                }
            }
        });

        } else {
            // Hiển thị giao diện đăng nhập
            setContentView(R.layout.activity_login);
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Xóa session đăng nhập
        SharedPreferences sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


}
