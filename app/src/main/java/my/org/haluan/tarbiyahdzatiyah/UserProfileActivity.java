package my.org.haluan.tarbiyahdzatiyah;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import my.org.haluan.tarbiyahdzatiyah.adapters.SectionPagerAdapter;
import my.org.haluan.tarbiyahdzatiyah.fragments.MaduFragment;
import my.org.haluan.tarbiyahdzatiyah.fragments.MasulFragment;
import my.org.haluan.tarbiyahdzatiyah.fragments.StatFragment;

/**
 * Created by Ismi on 1/20/2017.
 */

public class UserProfileActivity extends BaseActivity {

    /* Tab components*/
    private SectionPagerAdapter mSectionPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWidgets();
    }

    private void initWidgets(){

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfileActivity.this, AddMaduActivity.class));
            }
        });
        fab.setVisibility(View.GONE);

        mSectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager(), this);
        mSectionPagerAdapter.addFragment(StatFragment.newInstance());
        mSectionPagerAdapter.addFragment(MasulFragment.newInstance());
        mSectionPagerAdapter.addFragment(MaduFragment.newInstance());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 2:
                        fab.setVisibility(View.VISIBLE);
                        fab.setImageResource(R.drawable.ic_add_white_48dp);
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(UserProfileActivity.this, AddMaduActivity.class));
                            }
                        });

                        break;

                    default:
                        fab.setVisibility(View.GONE);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_userprofile;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
