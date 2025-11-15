package com.example.myapplication.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.entities.Excursion;

import org.w3c.dom.Text;

import java.util.List;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {
    private List<Excursion> mExcursion;
    private final Context context;
    private final LayoutInflater mInflater;

    class ExcursionViewHolder extends RecyclerView.ViewHolder {
        TextView titleView;
        TextView dateView;

        private ExcursionViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.textExcursionTitle);
            dateView  = itemView.findViewById(R.id.textExcursionDate);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                final Excursion current = mExcursion.get(position);
                Intent intent = new Intent(context, ExcursionDetails.class);
                intent.putExtra("id", current.getExcursionID());
                intent.putExtra("name", current.getExcursionName());
                intent.putExtra("vacationID", current.getVacationID());
                intent.putExtra("date", current.getExcursionDate());
                context.startActivity(intent);
            });
        }
    }

        public ExcursionAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
            this.context = context;
        }

        @Override
        public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = mInflater.inflate(R.layout.excursion_list_item, parent, false);
            return new ExcursionViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position) {
            if (mExcursion != null && !mExcursion.isEmpty()) {
                Excursion ex = mExcursion.get(position);
                holder.titleView.setText(ex.getExcursionName());
                String d = ex.getExcursionDate();
                holder.dateView.setText((d == null || d.trim().isEmpty()) ? "—" : d);
            } else {
                holder.titleView.setText("No excursions found");
                holder.dateView.setText("");
            }
        }
        public void setExcursion(List<Excursion> excursion){
            mExcursion = excursion;
            notifyDataSetChanged();
        }
        public int getItemCount() {
            if (mExcursion != null) return mExcursion.size();
            else return 0;
        }
}
