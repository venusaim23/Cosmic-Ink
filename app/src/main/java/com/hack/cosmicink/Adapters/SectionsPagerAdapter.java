package com.hack.cosmicink.Adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hack.cosmicink.Fragments.ActionsFragment;
import com.hack.cosmicink.Fragments.FollowFragment;
import com.hack.cosmicink.Fragments.MessagesFragment;
import com.hack.cosmicink.Fragments.QuestionsFragment;
import com.hack.cosmicink.Fragments.TopicsFragment;
import com.hack.cosmicink.R;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2,
            R.string.tab_text_3, R.string.tab_text_4, R.string.tab_text_5};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return new MessagesFragment();
        if (position == 1)
            return new TopicsFragment();
        if (position == 2)
            return new QuestionsFragment();
        if (position == 3)
            return new ActionsFragment();
        if (position == 4)
            return new FollowFragment();
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return TAB_TITLES.length;
    }
}