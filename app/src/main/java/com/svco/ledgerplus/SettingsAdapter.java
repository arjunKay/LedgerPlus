/*
 * Created by Nidhin on 28-11-2016.
 */
package com.svco.ledgerplus;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;


class SettingsAdapter extends ArrayAdapter<String> {
    private Activity activity;
    private List<String> title;
    private List<String> detail;
    private Integer[] image_id;

    SettingsAdapter(Activity activity, int resource, List<String> title, List<String> detail, Integer[] image_id) {
        super(activity, resource, title);
        this.activity = activity;
        this.title = title;
        this.detail = detail;
        this.image_id=image_id;
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View rowView= activity.getLayoutInflater().inflate(R.layout.settings_list_item, null);

        Button settingsIcon = (Button) rowView.findViewById(R.id.settings_icon);
        TextView settingsTitle = (TextView) rowView.findViewById(R.id.settings_title);
        TextView settingsDetail = (TextView) rowView.findViewById(R.id.settings_detail);

        settingsIcon.setBackground(ContextCompat.getDrawable(activity,image_id[position]));
        settingsTitle.setText(title.get(position).trim());
        settingsDetail.setText(detail.get(position).trim());

        if(settingsDetail.length()==0)
            settingsDetail.setVisibility(View.GONE);
        return rowView;
    }

}

