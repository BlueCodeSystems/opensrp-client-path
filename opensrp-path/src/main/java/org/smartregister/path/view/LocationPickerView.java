package org.smartregister.path.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import org.smartregister.path.R;
import org.smartregister.path.adapter.ServiceLocationsAdapter;
import org.smartregister.path.application.VaccinatorApplication;
import org.smartregister.view.customcontrols.CustomFontTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import util.LocationUtils;
import util.Utils;

/**
 * @author Jason Rogena - jrogena@ona.io
 * @since 03/03/2017
 */
public class LocationPickerView extends CustomFontTextView implements View.OnClickListener {

    private final Context context;
    private Dialog locationPickerDialog;
    private ServiceLocationsAdapter serviceLocationsAdapter;
    private OnLocationChangeListener onLocationChangeListener;

    public static final String PREF_TEAM_LOCATIONS = "PREF_TEAM_LOCATIONS";
    public static final ArrayList<String> ALLOWED_LEVELS;
    public static final String DEFAULT_LOCATION_LEVEL = "Health Facility";

    static {
        ALLOWED_LEVELS = new ArrayList<>();
        ALLOWED_LEVELS.add("Health Facility");
        ALLOWED_LEVELS.add("Zone");
    }

    public LocationPickerView(Context context) {
        super(context);
        this.context = context;
    }

    public LocationPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public LocationPickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public void init() {
        locationPickerDialog = new Dialog(context);
        locationPickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        locationPickerDialog.setContentView(R.layout.dialog_location_picker);

        ListView locationsLV = (ListView) locationPickerDialog.findViewById(R.id.locations_lv);
        serviceLocationsAdapter = new ServiceLocationsAdapter(context, getLocations(), getSelectedItem());
        locationsLV.setAdapter(serviceLocationsAdapter);
        locationsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VaccinatorApplication.getInstance().context().allSharedPreferences().saveCurrentLocality(serviceLocationsAdapter
                        .getLocationAt(position));
                LocationPickerView.this.setText(LocationUtils.getOpenMrsReadableName(
                        serviceLocationsAdapter.getLocationAt(position)));
                if (onLocationChangeListener != null) {
                    onLocationChangeListener.onLocationChange(serviceLocationsAdapter
                            .getLocationAt(position));
                }
                locationPickerDialog.dismiss();
            }
        });
        this.setText(LocationUtils.getOpenMrsReadableName(getSelectedItem()));

        setClickable(true);
        setOnClickListener(this);
    }

    public String getSelectedItem() {
        String selectedLocation = VaccinatorApplication.getInstance().context().allSharedPreferences().fetchCurrentLocality();
        if (TextUtils.isEmpty(selectedLocation) || !getLocations().contains(selectedLocation)) {
            selectedLocation = getDefaultLocation();
        }
        return selectedLocation;
    }

    public void setOnLocationChangeListener(final OnLocationChangeListener onLocationChangeListener) {
        this.onLocationChangeListener = onLocationChangeListener;
    }

    private ArrayList<String> getLocations() {
        String defaultLocation = getDefaultLocation();
        ArrayList<String> locations = LocationUtils.locationsFromHierarchy(defaultLocation);

        if (locations.contains(defaultLocation)) {
            locations.remove(defaultLocation);
        }
        Collections.sort(locations);
        locations.add(0, defaultLocation);

        return locations;
    }

    private String getDefaultLocation() {
        List<String> rawDefaultLocation = LocationUtils.generateDefaultLocationHierarchy(ALLOWED_LEVELS);

        if (rawDefaultLocation != null && !rawDefaultLocation.isEmpty()) {
            return rawDefaultLocation.get(rawDefaultLocation.size() - 1);
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        showDialog();
    }

    private void showDialog() {
        serviceLocationsAdapter.setSelectedLocation(getSelectedItem());


        Window window = locationPickerDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP;
        int[] coords = new int[2];
        LocationPickerView.this.getLocationInWindow(coords);
        wlp.x = coords[0]
                + (int) (LocationPickerView.this.getWidth() * 0.5)
                - (int) (Utils.convertDpToPx(context, 780) * 0.5);

        locationPickerDialog.show();
    }

    public interface OnLocationChangeListener {
        void onLocationChange(String newLocation);
    }

}
