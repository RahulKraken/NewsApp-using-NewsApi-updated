package com.example.rahuldroid.project_news;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.rahuldroid.project_news.ContentRecievers.Constants;
import com.example.rahuldroid.project_news.ViewPagerFragments.Business;
import com.example.rahuldroid.project_news.ViewPagerFragments.Entertainment;
import com.example.rahuldroid.project_news.ViewPagerFragments.General;
import com.example.rahuldroid.project_news.ViewPagerFragments.Health;
import com.example.rahuldroid.project_news.ViewPagerFragments.Science;
import com.example.rahuldroid.project_news.ViewPagerFragments.Sports;
import com.example.rahuldroid.project_news.ViewPagerFragments.Technology;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ForYouActivity extends AppCompatActivity {

    private static final String TAG = "ForYouActivity";

    private TabLayout tabLayout;
    private Constants constants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_you);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Toast.makeText(this, FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No user signed in!", Toast.LENGTH_LONG).show();
        }

        // Initializing Constants object.
        constants = new Constants();

        // Adding the custom toolbar
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.tabLayoutToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.for_you_page_title));

        // Refer to the viewpager and tab layout
        ViewPager viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);

        // Setting up the view pager with the tab layout
        tabLayout.setupWithViewPager(viewPager);
        setUpTabTitles();

        // Adding tabs to the view Pager.
        addTabs(viewPager);
    }

    //---------------------------------------------------Menu Creation------------------------------------------------------

    /**
     * This method inflates the @param menu with a menu layout.
     * @param menu
     * @return menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.for_you_activity_overflow_menu, menu);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            menu.add(0, 1, 0, getResources().getString(R.string.log_out_menu_item_label));
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This method handles the responses to the menu item selections made.
     * @param item
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                break;
            case 1:
                logout();
                break;
        }
        return true;
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, AuthorisationActivity.class));
        finish();
        Log.d(TAG, "logout: User logged out");
    }

    //-----------------------------------------------------View Pager-------------------------------------------------------

    // This method adds the title on the top of every view pager
    @SuppressWarnings("ConstantConditions")
    private void setUpTabTitles() {
        try {
            tabLayout.getTabAt(0).setText(returnCategory(0));
            tabLayout.getTabAt(1).setText(returnCategory(1));
            tabLayout.getTabAt(2).setText(returnCategory(2));
            tabLayout.getTabAt(3).setText(returnCategory(3));
            tabLayout.getTabAt(4).setText(returnCategory(4));
            tabLayout.getTabAt(5).setText(returnCategory(5));
            tabLayout.getTabAt(6).setText(returnCategory(6));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    // This method adds the tabs to the viewPager tab strip on top.
    private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new General(), constants.categoryList.get(0));
        viewPagerAdapter.addFragments(new Business(), constants.categoryList.get(1));
        viewPagerAdapter.addFragments(new Entertainment(), constants.categoryList.get(2));
        viewPagerAdapter.addFragments(new Sports(), constants.categoryList.get(3));
        viewPagerAdapter.addFragments(new Technology(), constants.categoryList.get(4));
        viewPagerAdapter.addFragments(new Science(), constants.categoryList.get(5));
        viewPagerAdapter.addFragments(new Health(), constants.categoryList.get(6));
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(viewPagerAdapter);
    }

    // This method returns the category that is to be added as the title of the tabs in viewPager
    private String returnCategory(int index) {
        return constants.categoryList.get(index);
    }
}

// ---------------------------------------------PagerAdapterClass----------------------------------------------------------

// This class extends the FragmentPagerAdapter for the ViewPager.
class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    public void addFragments(Fragment fragment, String title) {
        mFragmentTitleList.add(title);
        mFragments.add(fragment);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
