package com.expreso.androidapp.androidapp.CustomInfoWindow;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.expreso.androidapp.androidapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by fazal on 7/24/2017.
 */

public class CustomInfo implements GoogleMap.InfoWindowAdapter {
    private final LayoutInflater inflater;


    public CustomInfo(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = inflater.inflate(R.layout.custom_info_window,null);

            TextView tv_address = (TextView) view.findViewById(R.id.address);
            TextView tv_distance = (TextView) view.findViewById(R.id.distance);

            tv_address.setText(marker.getTitle());
            tv_distance.setText(marker.getSnippet());

        return view;
    }
}
