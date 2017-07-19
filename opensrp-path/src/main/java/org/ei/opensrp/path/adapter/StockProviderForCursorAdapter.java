package org.opensrp.path.adapter;

import android.view.LayoutInflater;
import android.view.View;

import org.opensrp.path.domain.Stock;
import org.opensrp.view.contract.SmartRegisterClient;
import org.opensrp.view.contract.SmartRegisterClients;
import org.opensrp.view.dialog.FilterOption;
import org.opensrp.view.dialog.ServiceModeOption;
import org.opensrp.view.dialog.SortOption;
import org.opensrp.view.viewHolder.OnClickFormLauncher;

/**
 * Created by raihan on 3/9/16.
 */
public interface StockProviderForCursorAdapter {
    public void getView(Stock stock, View view);
    SmartRegisterClients updateClients(FilterOption villageFilter, ServiceModeOption serviceModeOption,
                                       FilterOption searchFilter, SortOption sortOption);
    void onServiceModeSelected(ServiceModeOption serviceModeOption);
    public OnClickFormLauncher newFormLauncher(String formName, String entityId, String metaData);
    public LayoutInflater inflater();
    public View inflatelayoutForCursorAdapter();
}
