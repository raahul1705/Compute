package com.rxn.compute;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ComputeRecyclerAdapter extends RecyclerView.Adapter<ComputeRecyclerAdapter.ViewHolder> {
    private static final String TAG = "ComputeRecyclerAdapter";
    private ArrayList<String> mAddresses = new ArrayList<>();
    private Context mContext;

    public void setAddresses(ArrayList<String> addresses) {
        mAddresses.clear();
        mAddresses.addAll(addresses);
        this.notifyDataSetChanged();
    }

    public ComputeRecyclerAdapter(Context context, ArrayList<String> addresses) {
        mAddresses = addresses;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_computeentry,
                parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        holder.computeAddress.setText(mAddresses.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, mAddresses.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAddresses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView computeAddress;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            computeAddress = itemView.findViewById(R.id.compute_name);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
