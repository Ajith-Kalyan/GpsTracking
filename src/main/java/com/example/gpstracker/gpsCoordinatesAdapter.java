package com.example.gpstracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hanks.htextview.typer.TyperTextView;

import java.util.List;

public class gpsCoordinatesAdapter extends RecyclerView.Adapter<gpsCoordinatesAdapter.ViewHolder> {

    private List<gpsCoordinates> mData;
    private LayoutInflater mlayoutInflater;

    public gpsCoordinatesAdapter(Context context, List<gpsCoordinates> data) {

        this.mlayoutInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvCoordinate, tvTimestamp;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCoordinate = itemView.findViewById(R.id.tvcoordinate);
            tvTimestamp = itemView.findViewById(R.id.tvtimestamp);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mlayoutInflater.inflate(R.layout.list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        gpsCoordinates mgpscoordinates = mData.get(i);
        viewHolder.tvCoordinate.setText(mgpscoordinates.getCoordinates());
        viewHolder.tvTimestamp.setText(mgpscoordinates.getTimestamp());



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


}
