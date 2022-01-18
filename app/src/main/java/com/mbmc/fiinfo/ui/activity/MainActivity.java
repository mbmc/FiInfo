package com.mbmc.fiinfo.ui.activity;

import android.Manifest;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import com.mbmc.fiinfo.ui.component.RecyclerView;
import com.mbmc.fiinfo.ui.component.RefreshLayout;
import com.mbmc.fiinfo.ui.fragment.AboutFragment;
import com.mbmc.fiinfo.ui.fragment.BackupFragment;
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
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import io.reactivex.disposables.CompositeDisposable;

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

    @BindView(R.id.main_refresh) RefreshLayout refreshLayout;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.main_state) TextView state;
    @BindView(R.id.main_state_mobile) TextView mobile;
    @BindView(R.id.main_count) TextView count;
    @BindView(R.id.main_filter) TextView filter;
    @BindView(R.id.main_current_filter) TextView currentFilter;
    @BindView(R.id.main_stats) TextView stats;
    @BindView(R.id.main_clear) TextView clear;
    @BindView(R.id.main_progress) View progress;

    private RxPermissions rxPermissions;
    private CompositeDisposable disposables = new CompositeDisposable();

    private AboutFragment aboutFragment;
    private BackupFragment backupFragment;
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

        rxPermissions = new RxPermissions(this);

        NotificationManager.createChannel(this);

        setupUi();
    }

    @Override
    protected void onDestroy() {
        if (disposables != null) {
            disposables.dispose();
        }
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkPermissions();
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
            case R.id.main_action_backup:
                backupFragment.show(getFragmentManager(), "backup");
                return true;

            case R.id.main_action_notification:
                notificationSettingsFragment.show(getFragmentManager(), "notification");
                return true;

            case R.id.main_action_widget:
                widgetSettingsFragment.show(getFragmentManager(), "widget");
                return true;

            case R.id.main_action_info:
                CodeManager.send(this, Code.INFO.code);
                return true;

            case R.id.main_action_qr:
                CodeManager.send(this, Code.QR.code);
                return true;

            case R.id.main_action_carrier:
                switchCarrierFragment.show(getFragmentManager(), "codes");
                return true;

            case R.id.main_action_icons:
                iconsFragment.show(getFragmentManager(), "icons");
                return true;

            case R.id.main_action_about:
                aboutFragment.show(getFragmentManager(), "about");
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

        aboutFragment = new AboutFragment();
        backupFragment = new BackupFragment();
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

    private void checkPermissions() {
        disposables.add(rxPermissions.request(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (!granted) {
                        Toast.makeText(this, R.string.error_permissions,
                                Toast.LENGTH_LONG).show();
                    }
                }));
    }
}
