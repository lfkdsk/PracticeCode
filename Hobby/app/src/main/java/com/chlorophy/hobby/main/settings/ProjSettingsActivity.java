package com.chlorophy.hobby.main.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.chlorophy.hobby.R;
import com.chlorophy.hobby.xmpp.ProjInfo;

public class ProjSettingsActivity extends ActionBarActivity {

    private SectionsPagerAdapter adapter = null;
    public static ViewPager pager = null;
    private ProjInfo info = new ProjInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proj_settings);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.8f;
        DisplayMetrics out = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(out);
        params.height = out.heightPixels / 4;
        getWindow().setAttributes(params);

        info.setID(System.currentTimeMillis());
        adapter = new SectionsPagerAdapter(getSupportFragmentManager());

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ProjSettingsFragment fragment = new ProjSettingsFragment();
            fragment.initial(position, info);
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}
