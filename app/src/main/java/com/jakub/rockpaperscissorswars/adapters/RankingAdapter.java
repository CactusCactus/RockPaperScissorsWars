package com.jakub.rockpaperscissorswars.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakub.rockpaperscissorswars.R;
import com.jakub.rockpaperscissorswars.models.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Emil on 2018-01-11.
 */

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.ViewHolder> {

    private List<User> users;
    private Context context;

    public RankingAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_row_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = users.get(position);
        holder.username.setText(user.getUsername());
        holder.victories.setText(String.valueOf(user.getVictories()));
        if(position == 0) {
            holder.username.setTextColor(context.getResources().getColor(R.color.light_red));
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView username;
        TextView victories;

        ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username_label);
            victories = itemView.findViewById(R.id.victories_label);
        }
    }
}
