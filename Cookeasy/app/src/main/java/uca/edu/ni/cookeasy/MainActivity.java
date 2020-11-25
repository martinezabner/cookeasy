package uca.edu.ni.cookeasy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import Adapters.CategoryAdapter;
import Fragments.HomeFragment;
import Fragments.LogoFragment;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 1000;

    // Variables
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        showLogo();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                HomeFragment homeFragment =  new HomeFragment();
                fragmentTransaction.replace(R.id.frg_main, homeFragment);
                fragmentTransaction.commit();
            }
        }, SPLASH_SCREEN);

        recyclerView = (RecyclerView) findViewById(R.id.rv_Category);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new CategoryAdapter();
        recyclerView.setAdapter(mAdapter);

    }

    private void showLogo() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        LogoFragment logoFragment =  new LogoFragment();
        fragmentTransaction.replace(R.id.frg_main, logoFragment);
        fragmentTransaction.commit();
    }
}