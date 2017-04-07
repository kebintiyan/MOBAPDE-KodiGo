package ph.edu.dlsu.mobapde.chan_david_roque.kodigo;

/**
 * Created by user on 3/19/2017.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Page> pages;

    public SectionsPagerAdapter(FragmentManager fm, ArrayList<Page> pages) {
        super(fm);
        this.pages = pages;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Log.i("getItemf", position+"");
        return PlaceholderFragment.newInstance(pages.get(0).getNotebookID(), pages.get(position).getPageID());
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return pages.size();
    }

}
