package com.example.loginandsignup;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.ViewHolder> {

    private Context context;
    private List<FamilyMemberModel> list;
    private FamilyDBHelper dbHelper;
    private Runnable refreshCallback;

    public interface OnItemClickListener {
        void onItemClick(FamilyMemberModel member);
    }

    private OnItemClickListener clickListener;

    public FamilyAdapter(Context context, List<FamilyMemberModel> list, FamilyDBHelper dbHelper, Runnable refreshCallback) {
        this.context = context;
        this.list = list;
        this.dbHelper = dbHelper;
        this.refreshCallback = refreshCallback;
        this.clickListener = null;
    }

    public FamilyAdapter(Context context, List<FamilyMemberModel> list, FamilyDBHelper dbHelper, Runnable refreshCallback, OnItemClickListener clickListener) {
        this.context = context;
        this.list = list;
        this.dbHelper = dbHelper;
        this.refreshCallback = refreshCallback;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_family_member, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FamilyMemberModel member = list.get(position);
        holder.tvName.setText(member.getName());
        holder.tvAge.setText("Age: " + member.getAge());
        holder.tvRelation.setText("Relation: " + member.getRelation());

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onItemClick(member);
            } else {
                Intent intent = new Intent(context, EditFamilyMemberActivity.class);
                intent.putExtra("id", member.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAge, tvRelation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvAge = itemView.findViewById(R.id.tvAge);
            tvRelation = itemView.findViewById(R.id.tvRelation);
        }
    }
}
