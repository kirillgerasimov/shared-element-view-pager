package ki.pagetransformer.sharedelement.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import ki.pagetransformer.sharedelement.SharedElementPageTransformer;
import ki.pagetransformer.sharedelement.demo.fragment.BigPictureFragment;
import ki.pagetransformer.sharedelement.demo.fragment.SmallPictureFragment;
import ki.pagetransformer.sharedelement.demo.fragment.HelloFragment;


public class MainActivity extends AppCompatActivity {

    public static final int PAGE_HELLO = 0;
    public static final int PAGE_SMALL_CAT = 1;
    public static final int PAGE_BIG_CAT = 2;

    public ViewPager viewPager;
    public TabLayout tabLayout;

    public final SmallPictureFragment small_picture_fragment = new SmallPictureFragment();
    public final BigPictureFragment big_picture_fragment = new BigPictureFragment();
    public final HelloFragment hello_fragment = new HelloFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.viewPager = findViewById(R.id.main_viewPager);
        this.tabLayout = findViewById(R.id.main_tabLayout);

        setupViewPager();
        setupTabLayout();

        getSupportActionBar().hide();
    }

    private void setupTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(PAGE_HELLO).setText("First");
        tabLayout.getTabAt(PAGE_SMALL_CAT).setText("Second");
        tabLayout.getTabAt(PAGE_BIG_CAT).setText("Third");

    }

    private void setupViewPager() {
        TestViewPager adapter = new TestViewPager(getSupportFragmentManager());

        adapter.addFragment(PAGE_HELLO, hello_fragment);
        adapter.addFragment(PAGE_SMALL_CAT, small_picture_fragment);
        adapter.addFragment(PAGE_BIG_CAT, big_picture_fragment);


        this.viewPager.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(hello_fragment);
        fragments.add(small_picture_fragment);
        fragments.add(big_picture_fragment);


        SharedElementPageTransformer transformer =
                new SharedElementPageTransformer(this,  fragments);

        transformer.addSharedTransition(R.id.smallPic_image_cat2, R.id.bigPic_image_cat);
        transformer.addSharedTransition(R.id.bigPic_image_cat, R.id.smallPic_image_cat2);

        transformer.addSharedTransition(R.id.smallPic_text_label3, R.id.bigPic_text_label);
        transformer.addSharedTransition(R.id.bigPic_text_label, R.id.smallPic_text_label3);

        transformer.addSharedTransition(R.id.hello_text, R.id.smallPic_text_label3);
        transformer.addSharedTransition(R.id.smallPic_text_label3, R.id.hello_text);

        viewPager.setPageTransformer(false, transformer);
        viewPager.addOnPageChangeListener(transformer);
    }

    public class TestViewPager extends FragmentPagerAdapter {
        private final List<Fragment> fragments = new ArrayList<>();
        private final Map itemList = new HashMap<>();
        private int size;

        public TestViewPager(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment) {
            fragments.add(fragment);
        }

        public void addFragment(int index, Fragment fragment) {
            fragments.add(index, fragment);
        }

    }
}
