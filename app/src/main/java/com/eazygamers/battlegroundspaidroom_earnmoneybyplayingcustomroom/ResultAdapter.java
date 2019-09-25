package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom.MatchAdapter.round;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {
    private Context ctx;
    private ArrayList<ResultInfo> resultInfos;

    ResultAdapter(Context ctx, ArrayList<ResultInfo> resultInfos) {
        this.ctx = ctx;
        this.resultInfos = resultInfos;
    }

    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.result_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.rank.setText(resultInfos.get(position).getRank());
            holder.player_name.setText(resultInfos.get(position).getPlayerName());
            holder.kills.setText(resultInfos.get(position).getKill());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        float currencyConverter;
        String textSetter;
        try {
            switch (DataHolder.getUserProfile().getCurrency()) {
                case "BDT":
                    textSetter = DataHolder.getCurrencySign() + resultInfos.get(position).getWinning();
                    holder.winningAmmount.setText(textSetter);
                    break;
                case "USD":
                    try {
                        float usd = DataHolder.getCurrencyInfo().getUSD();
                        currencyConverter = Float.parseFloat(resultInfos.get(position).getWinning());
                        currencyConverter = round(currencyConverter / usd, 2);
                        textSetter = DataHolder.getCurrencySign() + currencyConverter;
                        holder.winningAmmount.setText(textSetter);
                        break;
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                case "INR":
                    try {
                        float inr = DataHolder.getCurrencyInfo().getINR();
                        currencyConverter = Float.parseFloat(resultInfos.get(position).getWinning());
                        currencyConverter = round(currencyConverter / inr, 2);
                        textSetter = DataHolder.getCurrencySign() + currencyConverter;
                        holder.winningAmmount.setText(textSetter);
                        break;
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
            }
        }
        catch (NullPointerException e)
        {
            Toast.makeText(ctx,"Something Wrong! Check your Internet.\nIf problem remains please restart your apps",Toast.LENGTH_LONG).show();

        }


    }

    @Override
    public int getItemCount() {
        return resultInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView rank,player_name,kills,winningAmmount;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            rank=itemView.findViewById(R.id.rank);
            player_name=itemView.findViewById(R.id.player_name);
            kills=itemView.findViewById(R.id.kills);
            winningAmmount=itemView.findViewById(R.id.winningAmmount);
        }
    }
}
