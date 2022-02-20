package com.views.redsocial.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.views.redsocial.R;
import com.views.redsocial.fragments.ChatsFragment;
import com.views.redsocial.fragments.FiltersFragment;
import com.views.redsocial.fragments.HomeFragment;
import com.views.redsocial.fragments.ProfileFragment;
import com.views.redsocial.providers.AuthProvider;
import com.views.redsocial.providers.TokenProvider;
import com.views.redsocial.providers.UsersProvider;
import com.views.redsocial.utils.ViewedMessageHelper;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    TokenProvider mTokenProvider;
    AuthProvider mAuthProvider;
    UsersProvider mUserProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        mTokenProvider = new TokenProvider();
        mAuthProvider = new AuthProvider();
        mUserProvider = new UsersProvider();
        openFragment(new HomeFragment());

        createToken();

        Toast.makeText(this, mAuthProvider.getUid(), Toast.LENGTH_LONG).show();

    }// fin del oncreate

    @Override
    protected void onStart() {
        super.onStart();
        //updateOnline(true);
        ViewedMessageHelper.updateOnline(true, HomeActivity.this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        ViewedMessageHelper.updateOnline(false, HomeActivity.this);
    }

    /*
        @Override
        protected void onStop() {
            super.onStop();
            updateOnline(false);
        }

        private void updateOnline(boolean status) {
            mUserProvider.updateOnline(mAuthProvider.getUid(), status);
        }

         */
    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.itemHome) {

                        openFragment(new HomeFragment());
                    } else if (item.getItemId() == R.id.itemFilters) {

                        openFragment(new FiltersFragment());
                    } else if (item.getItemId() == R.id.itemChats) {

                        openFragment(new ChatsFragment());
                    } else if (item.getItemId() == R.id.itemProfile) {

                        openFragment(new ProfileFragment());
                    }
                    return true;
                }
            };

    private void createToken() {
        mTokenProvider.create(mAuthProvider.getUid());
    }
}