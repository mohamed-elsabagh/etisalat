package com.delvebyte.etisalatapp.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.delvebyte.etisalatapp.R;
import com.delvebyte.etisalatapp.fragment.AboutFragment;
import com.delvebyte.etisalatapp.fragment.CreateLogFragment;
import com.delvebyte.etisalatapp.fragment.GetLogFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String FRAGMENT_CREATE_LOG_TAG = "CreateLogFragment";
        final String FRAGMENT_GET_LOG_TAG = "GetLogFragment";
        final String FRAGMENT_ABOUT_TAG = "AboutFragment";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getFragmentManager();

        BottomBar bottomBar = findViewById(R.id.bottomBar);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_create_log) {
                    // The tab with id R.id.tab_create_log was selected,
                    // change your content accordingly.
                    CreateLogFragment createFragment = (CreateLogFragment) fragmentManager.findFragmentByTag(FRAGMENT_CREATE_LOG_TAG);
                    if (createFragment == null) {
                        // Never clicked before
                        createFragment = new CreateLogFragment();
                    }
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, createFragment, FRAGMENT_CREATE_LOG_TAG);
                    fragmentTransaction.commit();
                } else if (tabId == R.id.tab_get_log) {
                    // The tab with id R.id.tab_get_log was selected,
                    // change your content accordingly.
                    GetLogFragment getLogFragment = (GetLogFragment) fragmentManager.findFragmentByTag(FRAGMENT_GET_LOG_TAG);
                    if (getLogFragment == null) {
                        // Never clicked before
                        getLogFragment = new GetLogFragment();
                    }
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, getLogFragment, FRAGMENT_GET_LOG_TAG);
                    fragmentTransaction.commit();
                } else if (tabId == R.id.tab_about) {
                    // The tab with id R.id.tab_about was selected,
                    // change your content accordingly.
                    AboutFragment aboutFragment = (AboutFragment) fragmentManager.findFragmentByTag(FRAGMENT_ABOUT_TAG);
                    if (aboutFragment == null) {
                        // Never clicked before
                        aboutFragment = new AboutFragment();
                    }
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, aboutFragment, FRAGMENT_ABOUT_TAG);
                    fragmentTransaction.commit();
                }
            }
        });

        // Add the news fragment as the first default fragment
//        CreateLogFragment createLogFragment = new CreateLogFragment();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.fragment_container, createLogFragment, "InitialDummyTag");
//        fragmentTransaction.commit();
    }
}
