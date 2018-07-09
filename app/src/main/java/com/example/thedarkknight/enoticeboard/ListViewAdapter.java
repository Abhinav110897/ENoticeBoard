package com.example.thedarkknight.enoticeboard;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by The Dark Knight on 20-05-2018.
 */

public class ListViewAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    List<Model> modellist;
    ArrayList<Model> arrayList;

    public ListViewAdapter(Context context, List<Model> modellist) {
        mContext = context;
        this.modellist = modellist;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Model>();
        this.arrayList.addAll(modellist);
    }

    public class ViewHolder {
        TextView mNameTv, mCityTv;
        ImageView mImageIv;
    }

    @Override
    public int getCount() {
        return modellist.size();
    }

    @Override
    public Object getItem(int i) {
        return modellist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list, null);
            holder.mNameTv = view.findViewById(R.id.Name);
            holder.mCityTv = view.findViewById(R.id.City);
            holder.mImageIv = view.findViewById(R.id.mainImage);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.mCityTv.setText(modellist.get(position).getCity());
        holder.mNameTv.setText(modellist.get(position).getName());
        holder.mImageIv.setImageResource(modellist.get(position).getImages());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (modellist.get(position).getName().equals("Dit")) {
                    Intent intent = new Intent(mContext, Login.class);
                    intent.putExtra("actionBarTitle", "DIT");
                    mContext.startActivity(intent);
                } else if (modellist.get(position).getName().equals("Graphic Era")) {
                    Intent intent = new Intent(mContext, Login.class);
                    intent.putExtra("actionBarTitle", "Graphic Era");
                    mContext.startActivity(intent);
                } else if (modellist.get(position).getName().equals("Upes")) {
                    Intent intent = new Intent(mContext, Login.class);
                    intent.putExtra("actionBarTitle", "Upes");
                    mContext.startActivity(intent);
                }

            }
        });
        return view;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        modellist.clear();
        if (charText.length() == 0) {
            modellist.addAll(arrayList);
        } else {
            for (Model model : arrayList) {
                if (model.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    modellist.add(model);
                }
            }
        }
        notifyDataSetChanged();
    }
}
