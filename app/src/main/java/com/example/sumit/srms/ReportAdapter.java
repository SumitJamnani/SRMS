package com.example.sumit.srms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder>
{
    Context context;
    ArrayList<ReportHelper> list;

    public ReportAdapter(Context context, ArrayList<ReportHelper> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new ReportViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAdapter.ReportViewHolder holder, int position)
    {
        ReportHelper result = list.get(position);
        holder.enrollment_number.setText(result.getEnrollment_number());
        holder.name.setText(result.getName());
        holder.marks_obtained.setText(result.getMarks_obtained());
        holder.status.setText(result.getStatus());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder
    {
        TextView enrollment_number, name, marks_obtained, status;
        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            enrollment_number = itemView.findViewById(R.id.txt_enrollment);
            name = itemView.findViewById(R.id.txt_name);
            marks_obtained = itemView.findViewById(R.id.txt_marks);
            status = itemView.findViewById(R.id.txt_status);
        }
    }
}
