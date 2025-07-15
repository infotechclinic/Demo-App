package com.example.loginandsignup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.InputType;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private Context context;
    private List<ReportModel> reportList;
    private FamilyDBHelper dbHelper;

    public ReportAdapter(Context context, List<ReportModel> reportList) {
        this.context = context;
        this.reportList = reportList;
        this.dbHelper = new FamilyDBHelper(context);
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_report, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        ReportModel report = reportList.get(position);
        holder.tvReportTitle.setText(report.getTitle());

        Glide.with(context)
                .load(Uri.parse(report.getFilePath()))
                .placeholder(R.drawable.ic_profile)
                .into(holder.imgReport);

        holder.ivEdit.setOnClickListener(v -> confirmRename(report, position));

        holder.itemView.setOnLongClickListener(v -> {
            showLongPressOptions(report, position);
            return true;
        });
    }

    private void confirmRename(ReportModel report, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Are you sure you want to rename?")
                .setPositiveButton("Yes", (dialog, which) -> showRenameDialog(report, position))
                .setNegativeButton("No", null)
                .show();
    }

    private void showRenameDialog(ReportModel report, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update Report Name");

        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(report.getTitle());
        builder.setView(input);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String newTitle = input.getText().toString().trim();
            if (!newTitle.isEmpty()) {
                report.setTitle(newTitle);
                dbHelper.updateReportTitle(report.getId(), newTitle);
                notifyItemChanged(position);
                Toast.makeText(context, "Report name updated", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void showLongPressOptions(ReportModel report, int position) {
        String[] options = {"Delete Report", "Change Image"};

        new AlertDialog.Builder(context)
                .setTitle("Options")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        dbHelper.deleteReport(report.getId());
                        reportList.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Report deleted", Toast.LENGTH_SHORT).show();
                    } else if (which == 1) {
                        Toast.makeText(context, "Feature not implemented", Toast.LENGTH_SHORT).show();
                        // You can start file picker or camera intent here
                    }
                })
                .show();
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView tvReportTitle;
        ImageView imgReport, ivEdit;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReportTitle = itemView.findViewById(R.id.tvReportTitle);
            imgReport = itemView.findViewById(R.id.imgReport);
            ivEdit = new ImageView(itemView.getContext());
            ivEdit.setImageResource(R.drawable.ic_edit);

            ((LinearLayout) itemView.findViewById(R.id.reportContainer)).addView(ivEdit);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ivEdit.getLayoutParams();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.setMargins(16, 0, 0, 0);
            ivEdit.setLayoutParams(params);
        }
    }
}