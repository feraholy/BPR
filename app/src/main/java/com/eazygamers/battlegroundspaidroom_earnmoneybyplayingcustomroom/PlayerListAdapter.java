package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.ViewHolder> {
    private Context ctx;
    private ArrayList<PlayerList> playerList;
    PlayerListAdapter(Context ctx, ArrayList<PlayerList> playerList) {
        this.ctx = ctx;
        this.playerList = playerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.player_list_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item;
        item=(position+1)+"";
        holder.itemNumber.setText(item);
        holder.playerName.setText(playerList.get(position).getUsername());

    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView itemNumber,playerName;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNumber=itemView.findViewById(R.id.itemNumber);
            playerName=itemView.findViewById(R.id.playerName);
        }
    }
}
