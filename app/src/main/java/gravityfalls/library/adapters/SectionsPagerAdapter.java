package gravityfalls.library.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import gravityfalls.library.fragments.ChatsFragment;
import gravityfalls.library.fragments.EventFragment;
import gravityfalls.library.fragments.OrganizationFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a EventFragment (defined as a static inner class below).
        Fragment f;
        switch (position) {
            case 1:
                f = ChatsFragment.newInstance(1);
                break;
            case 2:
                f = OrganizationFragment.newInstance(2);
                break;
            default:
                f = EventFragment.newInstance(position);
        }
        return f;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }
}





