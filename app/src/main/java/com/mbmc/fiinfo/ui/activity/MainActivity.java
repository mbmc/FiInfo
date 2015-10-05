package com.mbmc.fiinfo.ui.activity;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mbmc.fiinfo.constant.Constants;
import com.mbmc.fiinfo.data.Code;
import com.mbmc.fiinfo.data.ConnectivityEvent;
import com.mbmc.fiinfo.data.Event;
import com.mbmc.fiinfo.data.Filter;
import com.mbmc.fiinfo.event.Listener;
import com.mbmc.fiinfo.event.RefreshListener;
import com.mbmc.fiinfo.helper.Database;
import com.mbmc.fiinfo.helper.PreferencesManager;
import com.mbmc.fiinfo.provider.EventProvider;
import com.mbmc.fiinfo.ui.adapter.EventAdapter;
import com.mbmc.fiinfo.ui.component.RefreshLayout;
import com.mbmc.fiinfo.util.ConnectivityUtil;
import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.util.StringUtil;

import java.util.Arrays;
import java.util.List;

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
            Database.COLUMN_SPEED
    };

    @Bind(R.id.main_refresh) RefreshLayout refreshLayout;
    @Bind(R.id.main_recycler_view) RecyclerView recyclerView;
    @Bind(R.id.main_state) TextView state;
    @Bind(R.id.main_state_mobile) TextView mobile;
    @Bind(R.id.main_count) TextView count;

    private AlertDialog filter, carrier;
    private String selection = "";
    private EventAdapter eventAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setupUi(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        setNotificationMenu(menu.findItem(R.id.main_action_notifications));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.main_action_notifications:
                toggleNotifications();
                setNotificationMenu(menuItem);
                return true;

            case R.id.main_action_filter:
                filter.show();
                return true;

            case R.id.main_action_info:
                sendCode(Code.INFO.code);
                return true;

            case R.id.main_action_carrier:
                carrier.show();
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
        count.setText(getString(R.string.event_count, cursor.getCount(),
                getResources().getQuantityString(R.plurals.event, cursor.getCount())));
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

    @OnClick(R.id.main_clear)
    void clear() {
        getContentResolver().delete(EventProvider.URI, null, null);
    }

    public void onEvent(Listener.Connectivity connectivity) {
        setCurrentState();
    }


    private void setupUi(Bundle bundle) {
        setupFilterPicked();
        setupCodePicker();

        refreshLayout.setRefreshListener(this);

        recyclerView.setAdapter(eventAdapter = new EventAdapter());

        getLoaderManager().initLoader(URL_LOADER, bundle, this);
    }

    private void setupCodePicker() {
        List<Code> codes = Arrays.asList(Code.AUTO, Code.REPAIR, Code.NEXT, Code.SPRINT, Code.T_MOBILE);
        int size = codes.size();
        CharSequence[] titles = new CharSequence[size];
        for (int i = 0 ; i < size; ++i) {
            titles[i] = getString(codes.get(i).labelId);
        }
        carrier = new AlertDialog.Builder(this)
                .setItems(titles, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sendCode(Code.values()[which].code);
                    }
                })
                .create();
    }

    private void setupFilterPicked() {
        int size = Filter.values().length;
        CharSequence[] titles = new CharSequence[size];
        for (int i = 0 ; i < size; ++i) {
            titles[i] = getString(Filter.values()[i].stringId);
        }
        filter = new AlertDialog.Builder(this)
                .setItems(titles, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        selection = Filter.values()[which].selection;
                        onRefresh();
                    }
                })
                .create();
    }

    private void sendCode(int code) {
        if (!PreferencesManager.getInstance(this).getBoolean(Constants.HIDE_CODE_INSTRUCTIONS)) {
            showCodeInstructions(code);
        } else {
            openDialer(code);
        }
    }

    private void toggleNotifications() {
        PreferencesManager.getInstance(this).setBoolean(Constants.NOTIFICATIONS,
                !PreferencesManager.getInstance(this).getBoolean(Constants.NOTIFICATIONS));
    }

    private void setNotificationMenu(MenuItem menuItem) {
        menuItem.setTitle(PreferencesManager.getInstance(this).getBoolean(Constants.NOTIFICATIONS)
                ? R.string.menu_disable_notifications
                : R.string.menu_enable_notifications);
    }

    private void setCurrentState() {
        mobile.setVisibility(View.GONE);
        ConnectivityEvent connectivityEvent = ConnectivityUtil.getConnectivityEvent(this);
        state.setText(StringUtil.getConnectionName(this, connectivityEvent));
        if (connectivityEvent.event == Event.WIFI) {
            setMobileName();
        }
    }

    private void setMobileName() {
        ConnectivityEvent connectivityEvent = ConnectivityUtil.getMobileConnectivityIfAny(this);
        String name = connectivityEvent.name;
        String speed = connectivityEvent.speed;
        if (!name.isEmpty() && !speed.equals("Unknown")) {
            mobile.setVisibility(View.VISIBLE);
            mobile.setText(getString(R.string.state_mobile, name, connectivityEvent.speed));
        }
    }

    private void showCodeInstructions(final int code) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.view_code_instructions, null);
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.code_instructions_check_box);
        builder.setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (checkBox.isChecked()) {
                            PreferencesManager.getInstance(MainActivity.this)
                                    .setBoolean(Constants.HIDE_CODE_INSTRUCTIONS, true);
                        }
                        openDialer(code);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void copyCode(int code) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(getString(R.string.code), getString(R.string.code_dialer, code));
        clipboardManager.setPrimaryClip(clipData);
    }

    private void openDialer(int code) {
        copyCode(code);
        startActivity(new Intent(Intent.ACTION_DIAL));
    }

}
