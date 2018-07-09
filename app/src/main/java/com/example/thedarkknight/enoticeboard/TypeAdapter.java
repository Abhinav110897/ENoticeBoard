package com.example.thedarkknight.enoticeboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by The Dark Knight on 20-06-2018.
 */

public class TypeAdapter extends ArrayAdapter<TypeItem> {

    public TypeAdapter(Context context, ArrayList<TypeItem> typeList) {
        super(context, 0, typeList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.custom_spinner_row, parent, false
            );
        }
        TextView textView = convertView.findViewById(R.id.spinnertv);
        TypeItem currentItem = getItem(position);
        if (currentItem != null) {
            textView.setText(currentItem.getmType());
        }
        return convertView;
    }

}
