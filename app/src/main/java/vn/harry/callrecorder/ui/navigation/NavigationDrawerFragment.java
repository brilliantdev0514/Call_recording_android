package vn.harry.callrecorder.ui.navigation;


import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import vn.harry.callrecorder.R;
import vn.harry.callrecorder.entity.DrawerBean;
import vn.harry.callrecorder.listener.OnRecyclerViewItemClick;
import vn.harry.callrecorder.util.UserPreferences;
import vn.harry.callrecorder.util.ValueConstants;

public class NavigationDrawerFragment extends Fragment implements OnRecyclerViewItemClick<DrawerBean> {

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    @BindView(R.id.rvMenu)
    RecyclerView mRecyclerView;
    private DrawerLayout mDrawerLayout;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;

    private List<DrawerBean> mDrawerBeans;
    private LinearLayoutManager mLinearLayoutManager;
    private NavigationDrawerAdapter mNavigationDrawerAdapter;
    private NavigationDrawerCallbacks mCallbacks;
    private ActionBarDrawerToggle mDrawerToggle;
    private Unbinder unbinder;

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentSelectedPosition = UserPreferences.getInt(STATE_SELECTED_POSITION, -1);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initData();
        return rootView;
    }

    private void initData() {
        mDrawerBeans = new ArrayList<>();
        mDrawerBeans.add(new DrawerBean(R.drawable.ic_header, getString(R.string.menu_navigation), ValueConstants.NavigationItemType.Header));
        mDrawerBeans.add(new DrawerBean(R.drawable.ic_trashre, getString(R.string.menu_recycle), ValueConstants.NavigationItemType.Item));
        mDrawerBeans.add(new DrawerBean(R.drawable.ic_setting, getString(R.string.menu_setting), ValueConstants.NavigationItemType.Item));
        mDrawerBeans.add(new DrawerBean(R.drawable.ic_email, getString(R.string.menu_mail), ValueConstants.NavigationItemType.Item));
        mDrawerBeans.add(new DrawerBean(R.drawable.ic_support, getString(R.string.menu_help), ValueConstants.NavigationItemType.Item));
        mDrawerBeans.add(new DrawerBean(R.drawable.ic_rate_me, getString(R.string.menu_rate), ValueConstants.NavigationItemType.Item));
        mDrawerBeans.add(new DrawerBean(R.drawable.ic_about, getString(R.string.menu_about), ValueConstants.NavigationItemType.Item));
        mDrawerBeans.add(new DrawerBean(R.drawable.ic_donate, getString(R.string.menu_donate), ValueConstants.NavigationItemType.Item));
        mDrawerBeans.add(new DrawerBean(R.drawable.ic_more, getString(R.string.menu_more), ValueConstants.NavigationItemType.Item));
        mDrawerBeans.add(new DrawerBean(R.drawable.ic_paint, getString(R.string.menu_paint), ValueConstants.NavigationItemType.Item));
        mDrawerBeans.add(new DrawerBean(R.drawable.ic_exit, getString(R.string.menu_exit), ValueConstants.NavigationItemType.Item));
        mDrawerBeans.add(new DrawerBean(R.drawable.ic_power, getString(R.string.menu_power), ValueConstants.NavigationItemType.Item));

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mNavigationDrawerAdapter = new NavigationDrawerAdapter(getActivity(), mCurrentSelectedPosition);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mNavigationDrawerAdapter.loadInitialDataSet(mDrawerBeans);
        mRecyclerView.setAdapter(mNavigationDrawerAdapter);
        mNavigationDrawerAdapter.setOnRecyclerViewItemClick(this);
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    public void setUp(int fragmentId, Toolbar toolbar, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),
                mDrawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }
                getActivity().invalidateOptionsMenu();
            }
        };
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, DrawerBean item, int position) {
        selectItem(item, position);
    }

    private void selectItem(DrawerBean drawerBean, int position) {
        mCurrentSelectedPosition = position;
        if (mNavigationDrawerAdapter != null) {
            mNavigationDrawerAdapter.setCurrentSelectedPosition(mCurrentSelectedPosition);
            mNavigationDrawerAdapter.notifyDataSetChanged();
        }
        UserPreferences.setInt(STATE_SELECTED_POSITION, position);
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(drawerBean.getType(), position);
        }
    }

    public interface NavigationDrawerCallbacks {
        void onNavigationDrawerItemSelected(int type, int position);

        void onCloseNavigation();
    }
}
