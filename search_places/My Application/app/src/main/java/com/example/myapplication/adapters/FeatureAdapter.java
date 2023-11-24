package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.example.myapplication.R;
import com.example.myapplication.model.PlacesDescription;

import java.util.ArrayList;
import java.util.List;

public class FeatureAdapter extends BaseAdapter {

    private final List<PlacesDescription.Feature> features = new ArrayList<>();


    @Override
    public int getCount() {
        return features.size();
    }
    public void setFeatures(@Nullable List<PlacesDescription.Feature> repos) {
        if (repos == null) {
            return;
        }
        features.clear();
        features.addAll(repos);
        notifyDataSetChanged();
    }

    @Override
    public PlacesDescription.Feature getItem(int position) {
        if (position < 0 || position >= features.size()) {
            return null;
        } else {
            return features.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private View createView(ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.places_info, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = (convertView != null ? convertView : createView(parent));
        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.setFeatureViews(getItem(position));
        viewHolder.setFeatureViews(getItem(position));
        return view;
    }

    private static class ViewHolder {
        TextView textName;
        TextView textRate;
        TextView textDist;
        TextView textKinds;

        public ViewHolder(View convertView) {
            textName = convertView.findViewById(R.id.text_name);
            textRate = convertView.findViewById(R.id.text_rate);
            textDist = convertView.findViewById(R.id.text_dist);
            textKinds = convertView.findViewById(R.id.text_kinds);
        }
        public void setFeatureViews(PlacesDescription.Feature feature) {
            textName.setText(feature.getProperties().getName());
            textRate.setText("Rate: " + feature.getProperties().getRate());
            textDist.setText("Distance: " + feature.getProperties().getDist());
            textKinds.setText("Kinds: " + feature.getProperties().getKinds());
        }
    }

}
