package com.example.thedarkknight.enoticeboard;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by The Dark Knight on 23-05-2018.
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    private List<ListItem> listItems;
    private Context context;
    public int i = 0;

    public NoticeAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ListItem listItem = listItems.get(position);
        holder.textHead.setText(listItem.getHead());
        holder.textDesc.setText(listItem.getDesc());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i == 0) {
                    i++;
                    holder.imageView.setImageResource(R.drawable.baseline_star_black_18dp);
                    Toast.makeText(context, "Marked as important", Toast.LENGTH_SHORT).show();
                } else {
                    i = 0;
                    holder.imageView.setImageResource(R.drawable.baseline_star_border_black_18dp);
                }
            }
        });
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NoticeDisplay.class);
                intent.putExtra("Type", listItem.getType());
                intent.putExtra("Name", listItem.getHead());
                intent.putExtra("Description", listItem.getDesc());
                intent.putExtra("CollegeName",listItem.getCollege());
                context.startActivity(intent);
            }
        });
        holder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context wrapper = new ContextThemeWrapper(context, R.style.PopUpMenuStyle);
                PopupMenu popupMenu = new PopupMenu(wrapper, holder.option);
                popupMenu.inflate(R.menu.option_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_item_read:
                                Toast.makeText(context, "Notice marked read", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.menu_item_delete:
                                listItems.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(context, "Notice deleted", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }

                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textHead;
        private ImageView imageView;
        private TextView textDesc;
        private ImageView option;
        private LinearLayout parentLayout;

        private ViewHolder(View itemView) {
            super(itemView);
            textHead = itemView.findViewById(R.id.textViewHead);
            textDesc = itemView.findViewById(R.id.textViewDesc);
            imageView = itemView.findViewById(R.id.imp);
            option = itemView.findViewById(R.id.option);
            parentLayout = itemView.findViewById(R.id.card_layout);
        }
    }
}
