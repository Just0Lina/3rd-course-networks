package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.example.myapplication.R;
import com.example.myapplication.model.LocationDescription;

import java.util.ArrayList;
import java.util.List;

public class LocationDescriptionAdapter extends BaseAdapter {

    private final List<LocationDescription> locationDescriptions = new ArrayList<>();

    @Override
    public int getCount() {
        return locationDescriptions.size();
    }

    @Override
    public LocationDescription getItem(int position) {
        if (position < 0 || position >= locationDescriptions.size()) {
            return null;
        } else {
            return locationDescriptions.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = (convertView != null ? convertView : createView(parent));
//        final View view = (convertView != null ? convertView : createView(parent, R.layout.activity_main));
        final LocationDescriptionViewHolder viewHolder = (LocationDescriptionViewHolder) view.getTag();
        viewHolder.setLocationDescription(getItem(position));
        return view;
    }

    public void setLocationDescriptions(@Nullable List<LocationDescription> repos) {
        if (repos == null) {
            return;
        }
        locationDescriptions.clear();
        locationDescriptions.addAll(repos);
        notifyDataSetChanged();
    }

    private View createView(ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.item_location, parent, false);
        final LocationDescriptionViewHolder viewHolder = new LocationDescriptionViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    private static class LocationDescriptionViewHolder {

        private final TextView textLocationName;
        private final TextView textLocationCountry;
        private final TextView textLocationState;
        private final TextView textLocationPostcode;
        private final TextView textLocationLat;
        private final TextView textLocationLng;

        public LocationDescriptionViewHolder(View view) {
            textLocationName = view.findViewById(R.id.text_location_name);
            textLocationCountry = view.findViewById(R.id.text_location_country);
            textLocationState = view.findViewById(R.id.text_location_state);
            textLocationPostcode = view.findViewById(R.id.text_location_countrycode);

            textLocationLat = view.findViewById(R.id.text_location_lat);
            textLocationLng = view.findViewById(R.id.text_location_lng);
        }

        public void setLocationDescription(LocationDescription locationDescription) {
            textLocationName.setText(locationDescription.name);
            textLocationCountry.setText(locationDescription.country);
            textLocationState.setText("State: " + locationDescription.state);
            textLocationPostcode.setText("CountryCode: " + locationDescription.countrycode);
            textLocationLat.setText("Lat: " + locationDescription.getPoints().getLat());
            textLocationLng.setText("Lng: " + locationDescription.getPoints().getLng());

        }
    }
}