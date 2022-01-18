package com.mbmc.fiinfo.ui.fragment;

import android.app.Dialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.event.RefreshListener;
import com.mbmc.fiinfo.provider.EventProvider;
import com.mbmc.fiinfo.ui.adapter.StatAdapter;
import com.mbmc.fiinfo.ui.component.RecyclerView;
import com.mbmc.fiinfo.ui.component.RefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StatsFragment extends DialogFragment
        implements LoaderManager.LoaderCallbacks<Cursor>, RefreshListener {

    private static final int URL_LOADER = 0;

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.stats_refresh) RefreshLayout refreshLayout;
    @BindView(R.id.stats_progress) View progress;

    private Unbinder unbinder;
    private StatAdapter eventAdapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_stats, null);
        unbinder = ButterKnife.bind(this, view);

        recyclerView.setAdapter(eventAdapter = new StatAdapter());
        refreshLayout.setRefreshListener(this);
        getLoaderManager().initLoader(URL_LOADER, null, this);

        return new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme)
                .setView(view)
                .setTitle(R.string.stat_occurrences)
                .create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        switch (i) {
            case URL_LOADER:
                return new CursorLoader(
                        getActivity(),
                        EventProvider.URI_COUNT,
                        null,
                        null,
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
        eventAdapter.swapCursor(getActivity(), cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        eventAdapter.swapCursor(getActivity(), null);
    }

    @Override
    public void onRefresh() {
        getLoaderManager().restartLoader(URL_LOADER, null, this);
    }
}
