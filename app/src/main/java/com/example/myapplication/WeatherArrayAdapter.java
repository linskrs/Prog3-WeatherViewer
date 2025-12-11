package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class WeatherArrayAdapter extends ArrayAdapter<Weather> {

    private static class ViewHolder {
        TextView iconView, dateView, descView, minView, maxView, humidityView;
    }

    public WeatherArrayAdapter(Context context, List<Weather> items) {
        super(context, -1, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Weather weather = getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item, parent, false);

            holder = new ViewHolder();
            holder.iconView = convertView.findViewById(R.id.conditionTextView);
            holder.dateView = convertView.findViewById(R.id.dayTextView);
            holder.descView = convertView.findViewById(R.id.descriptionTextView);
            holder.minView = convertView.findViewById(R.id.lowTextView);
            holder.maxView = convertView.findViewById(R.id.hiTextView);
            holder.humidityView = convertView.findViewById(R.id.humidityTextView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.iconView.setText(weather.icon);
        holder.dateView.setText(weather.date);
        holder.descView.setText(weather.description);
        holder.minView.setText(weather.minTemp);
        holder.maxView.setText(weather.maxTemp);
        holder.humidityView.setText(weather.humidity);

        return convertView;
    }
}
