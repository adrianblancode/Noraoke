package co.adrianblan.noraoke;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;

import java.util.ArrayList;



public class MainActivity extends ActionBarActivity {

    private final String[] TITLES = {"Now playing", "Songs", "Groups"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {

            /*
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment())
                    .commit();
            */
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize the ViewPager and set an adapter
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager(), getBaseContext()));

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setShouldExpand(true);
        tabs.setViewPager(pager);

        //Whenever the user changes tab, we want the title to change too
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageSelected(int position) {
                setTitle(TITLES[position]);
            }

        });

        //We want to have the library as default view
        pager.setCurrentItem(1);
        setTitle(TITLES[1]);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class MainFragment extends Fragment {

        public MainFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_song_library, container, false);

            /*
            final ObservableScrollView scrollView = (ObservableScrollView) view.findViewById(R.id.scroll);
            Fragment parentFragment = getParentFragment();
            ViewGroup viewGroup = (ViewGroup) parentFragment.getView();
            if (viewGroup != null) {
                scrollView.setTouchInterceptionViewGroup((ViewGroup) viewGroup.findViewById(R.id.container));
                if (parentFragment instanceof ObservableScrollViewCallbacks) {
                    scrollView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentFragment);
                }
            }*/

            return view;
        }
    }

    public class PagerAdapter extends FragmentPagerAdapter
            implements PagerSlidingTabStrip.CustomTabProvider {

        private ArrayList<Integer> tab_icon = new ArrayList<Integer>();

        Context myContext;

        public PagerAdapter(FragmentManager fm, Context context) {
            super(fm);

            myContext = context;
            tab_icon.add(context.getResources().getIdentifier("ic_play_arrow_white_36dp", "drawable", context.getPackageName()));
            tab_icon.add(context.getResources().getIdentifier("ic_list_white_36dp", "drawable", context.getPackageName()));
            tab_icon.add(context.getResources().getIdentifier("ic_group_white_36dp", "drawable", context.getPackageName()));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {

            if(position == 0){
                return SongNowPlayingFragment.newInstance();
            } else if (position == 1){
                return SongLibraryFragment.newInstance();
            } else if(position == 2){
                return GroupFragment.newInstance();
            }

            System.err.println("Invalid tab fragment!");
            return new Fragment();
        }

        @Override
        public View getCustomTabView(ViewGroup viewGroup, int position) {

            LinearLayout imageView = (LinearLayout) LayoutInflater.from(myContext)
                    .inflate(R.layout.tab_layout, null, false);

            ImageView tabImage = (ImageView) imageView.findViewById(R.id.tabImage);
            tabImage.setImageResource(tab_icon.get(position));

            /*
            Picasso.with(mContext)
                    .load(ICONS[position])
                    .fit()
                    .centerInside()
                    .into(tabImage);
            */

            return imageView;
        }
    }
}
