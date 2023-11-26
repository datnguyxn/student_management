package com.com.student_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.blob.BlobHandle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.com.student_management.constants.App;
import com.com.student_management.constants.Roles;
import com.com.student_management.entities.User;
import com.com.student_management.fragments.AccountFragment;
import com.com.student_management.fragments.HomeFragment;
import com.com.student_management.fragments.ManagerFragment;
import com.com.student_management.fragments.NewStudentFragment;
import com.com.student_management.models.UserModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String CREATE_FRAGMENT_TAG = "CreateFragment";
    private DrawerLayout drawerLayout;
    private TextView tvRole;
    private BottomNavigationView bottomNavigationView;
    private UserModel userModel;
    private String uuid;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        replaceFragment(new HomeFragment());

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d(TAG, "onNavigationItemSelected: " + item.getItemId());
                switch (item.getItemId()) {
                    case App.BOTTOM_HOME:
                        replaceFragment(new HomeFragment());
//                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        return true;
                    case App.BOTTOM_MANAGER:
                        if (role.equals(Roles.ADMIN.name())) {
                            replaceFragment(new ManagerFragment());
                        } else {
                            Toast.makeText(MainActivity.this, "You don't have permission to access this page", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    case App.BOTTOM_ACCOUNT:
                        replaceFragment(new AccountFragment());
                        return true;
                    default:
                        return false;
                }
            }
        });
    }


    private void init() {
        drawerLayout = findViewById(R.id.drawer_layout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        userModel = new UserModel();
        SharedPreferences sharedPreferences = getSharedPreferences(App.SHARED_PREFERENCES_USER, MODE_PRIVATE);
        uuid = sharedPreferences.getString(App.SHARED_PREFERENCES_UUID, null);
        role = sharedPreferences.getString("role", "");
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
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
}