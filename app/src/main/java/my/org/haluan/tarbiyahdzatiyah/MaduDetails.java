package my.org.haluan.tarbiyahdzatiyah;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import my.org.haluan.tarbiyahdzatiyah.adapters.SectionPagerAdapter;
import my.org.haluan.tarbiyahdzatiyah.fragments.MaduDetailsInfoFragment;
import my.org.haluan.tarbiyahdzatiyah.fragments.MaduDetailsStatFragment;
import my.org.haluan.tarbiyahdzatiyah.models.Madu;
import my.org.haluan.tarbiyahdzatiyah.objects.ConstVar;

/**
 * Created by Ismi on 1/20/2017.
 */

public class MaduDetails extends BaseActivity {

    /* Tab components*/
    private SectionPagerAdapter mSectionPagerAdapter;
    private ViewPager mViewPager;

    private Madu mCurrentMadu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            mCurrentMadu = (Madu) savedInstanceState.get("madu");
        }else{
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            mCurrentMadu = (Madu) bundle.get("madu");
        }

        initWidgets();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("madu", mCurrentMadu);
        super.onSaveInstanceState(outState);
    }

    private void initWidgets(){
        final Toolbar toolbar = (Toolbar) findViewById(R.id.htab_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(mCurrentMadu.madu.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager(), this);
        mSectionPagerAdapter.addFragment(MaduDetailsStatFragment.newInstance(mCurrentMadu.madu.uid));
        mSectionPagerAdapter.addFragment(MaduDetailsInfoFragment.newInstance(mCurrentMadu.madu.uid));

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionPagerAdapter);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.htab_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.htab_collapse_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.header);

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @SuppressWarnings("ResourceType")
            @Override
            public void onGenerated(Palette palette) {

                int vibrantColor = palette.getVibrantColor(R.color.primary_500);
                int vibrantDarkColor = palette.getDarkVibrantColor(R.color.primary_700);
                collapsingToolbarLayout.setContentScrimColor(vibrantColor);
                collapsingToolbarLayout.setStatusBarScrimColor(vibrantDarkColor);
            }
        });

        ImageView imgProfile = (ImageView)findViewById(R.id.madudetails_profileImage);
        imgProfile.setImageDrawable(ConstVar.resizeImage(this,R.drawable.empty_profile, 100,100));

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_madudetails;
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
