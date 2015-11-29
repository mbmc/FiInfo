package com.mbmc.fiinfo.ui.activity;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mbmc.fiinfo.data.Code;
import com.mbmc.fiinfo.data.ConnectivityEvent;
import com.mbmc.fiinfo.data.Event;
import com.mbmc.fiinfo.data.Filter;
import com.mbmc.fiinfo.event.Listener;
import com.mbmc.fiinfo.event.RefreshListener;
import com.mbmc.fiinfo.helper.CodeManager;
import com.mbmc.fiinfo.helper.Database;
import com.mbmc.fiinfo.helper.NotificationManager;
import com.mbmc.fiinfo.provider.EventProvider;
import com.mbmc.fiinfo.ui.adapter.EventAdapter;
import com.mbmc.fiinfo.ui.component.RefreshLayout;
import com.mbmc.fiinfo.ui.fragment.ClearEventsFragment;
import com.mbmc.fiinfo.ui.fragment.CodeInstructionsFragment;
import com.mbmc.fiinfo.ui.fragment.FiltersFragment;
import com.mbmc.fiinfo.ui.fragment.IconsFragment;
import com.mbmc.fiinfo.ui.fragment.NotificationSettingsFragment;
import com.mbmc.fiinfo.ui.fragment.StatsFragment;
import com.mbmc.fiinfo.ui.fragment.SwitchCarrierFragment;
import com.mbmc.fiinfo.ui.fragment.WidgetSettingsFragment;
import com.mbmc.fiinfo.util.ConnectivityUtil;
import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.util.StringUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;


public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, RefreshListener {
    
    private static final int URL_LOADER = 0;
    private static final String[] projection = {
            Database.COLUMN_ID,
            Database.COLUMN_TYPE,
            Database.COLUMN_DATE,
            Database.COLUMN_TIME_ZONE,
            Database.COLUMN_COUNTRY,
            Database.COLUMN_NAME,
            Database.COLUMN_MOBILE,
            Database.COLUMN_SPEED
    };

    @Bind(R.id.main_refresh) RefreshLayout refreshLayout;
    @Bind(R.id.recycler_view) RecyclerView recyclerView;
    @Bind(R.id.main_state) TextView state;
    @Bind(R.id.main_state_mobile) TextView mobile;
    @Bind(R.id.main_count) TextView count;
    @Bind(R.id.main_filter) TextView filter;
    @Bind(R.id.main_current_filter) TextView currentFilter;
    @Bind(R.id.main_stats) TextView stats;
    @Bind(R.id.main_clear) TextView clear;
    @Bind(R.id.main_progress) View progress;

    private ClearEventsFragment clearEventsFragment;
    private CodeInstructionsFragment codeInstructionsFragment;
    private FiltersFragment filtersFragment;
    private IconsFragment iconsFragment;
    private NotificationSettingsFragment notificationSettingsFragment;
    private StatsFragment statsFragment;
    private SwitchCarrierFragment switchCarrierFragment;
    private WidgetSettingsFragment widgetSettingsFragment;

    private String selection = "";
    private EventAdapter eventAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setupUi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        NotificationManager.cancel(this);
        EventBus.getDefault().register(this);
        setCurrentState();
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.main_action_notification:
                notificationSettingsFragment.show(getFragmentManager(), "notification");
                return true;

            case R.id.main_action_widget:
                widgetSettingsFragment.show(getFragmentManager(), "widget");
                return true;

            case R.id.main_action_info:
                CodeManager.send(this, Code.INFO.code);
                return true;

            case R.id.main_action_carrier:
                switchCarrierFragment.show(getFragmentManager(), "codes");
                return true;

            case R.id.main_action_icons:
                iconsFragment.show(getFragmentManager(), "icons");
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        switch (i) {
            case URL_LOADER:
                return new CursorLoader(
                        MainActivity.this,
                        EventProvider.URI,
                        projection,
                        selection,
                        null,
                        null
                );

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        refreshLayout.stopAnimation();
        progress.setVisibility(View.GONE);
        count.setText(getString(R.string.event_count, cursor.getCount()));
        eventAdapter.swapCursor(MainActivity.this, cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        eventAdapter.swapCursor(MainActivity.this, null);
    }

    @Override
    public void onRefresh() {
        getLoaderManager().restartLoader(URL_LOADER, null, this);
    }

    @OnClick(R.id.main_stats)
    void stats() {
        statsFragment.show(getFragmentManager(), "stats");
    }

    @OnClick(R.id.main_filter)
    void filter() {
        filtersFragment.show(getFragmentManager(), "filters");
    }

    @OnClick(R.id.main_clear)
    void clear() {
        clearEventsFragment.show(getFragmentManager(), "clear");
    }

    public void onEvent(Listener.Connectivity connectivity) {
        setCurrentState();
    }

    public void showCodeInstructions(final int code) {
        codeInstructionsFragment.setCode(code);
        codeInstructionsFragment.show(getFragmentManager(), "code_instructions");
    }

    public void applyFilter(Filter filter) {
        selection = filter.selection;
        setCurrentFilter(filter.stringId);
        onRefresh();
    }


    private void setupUi() {
        setCurrentFilter(R.string.filter_all);

        Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        stats.setTypeface(font);
        filter.setTypeface(font);
        clear.setTypeface(font);

        clearEventsFragment = new ClearEventsFragment();
        codeInstructionsFragment = new CodeInstructionsFragment();
        filtersFragment = new FiltersFragment();
        iconsFragment = new IconsFragment();
        notificationSettingsFragment = new NotificationSettingsFragment();
        statsFragment = new StatsFragment();
        switchCarrierFragment = new SwitchCarrierFragment();
        widgetSettingsFragment = new WidgetSettingsFragment();

        refreshLayout.setRefreshListener(this);

        recyclerView.setAdapter(eventAdapter = new EventAdapter());
        getLoaderManager().initLoader(URL_LOADER, null, this);
    }

    private void setCurrentState() {
        mobile.setVisibility(View.GONE);
        ConnectivityEvent connectivityEvent = ConnectivityUtil.getConnectivityEvent(this);
        if (connectivityEvent.event == Event.WIFI_MOBILE) {
            mobile.setVisibility(View.VISIBLE);
            mobile.setText(getString(R.string.state_mobile, connectivityEvent.mobile, connectivityEvent.speed));
        }
        state.setText(StringUtil.getConnectionName(this, connectivityEvent));
    }

    private void setCurrentFilter(int resId) {
        currentFilter.setText(getString(R.string.filter_current, getString(resId)));
    }

}
