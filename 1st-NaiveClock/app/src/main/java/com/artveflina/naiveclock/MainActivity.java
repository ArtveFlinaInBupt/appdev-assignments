package com.artveflina.naiveclock;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.artveflina.naiveclock.util.FragmentBuilder;
import com.artveflina.naiveclock.widget.WorldClockFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public Fragment getItem(int i) {
                return new Fragment[]{
                        new FragmentBuilder(R.layout.dial_layout),
                        new FragmentBuilder(R.layout.digital_layout),
                        new WorldClockFragment(),
                }[i];
            }

            @Override
            public CharSequence getPageTitle(int i) {
                return new String[]{"Dial", "Digital", "World Time"}[i];
            }
        });

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }
}
