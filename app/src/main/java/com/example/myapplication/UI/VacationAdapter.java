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
import com.example.myapplication.entities.Vacation;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {

    private List<Vacation> mVacations;
    private final Context context;
    private final LayoutInflater mInflater;
    public VacationAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        this.context=context;
    }

    public class VacationViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvHotel;
        private final TextView tvDates;
        private final TextView rowPrice;


        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvHotel = itemView.findViewById(R.id.tvHotel);
            tvDates = itemView.findViewById(R.id.tvDates);
            rowPrice = itemView.findViewById(R.id.rowPrice);

            // Click opens VacationDetails for that vacation
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) { // added null safety
                    final Vacation current = mVacations.get(position);
                    Intent intent = new Intent(context, VacationDetails.class);

                    // Pass all details through the intent
                    intent.putExtra("id", current.getVacationID());
                    intent.putExtra("name", current.getVacationName());
                    intent.putExtra("hotel", current.getHotel());
                    intent.putExtra("startDate", current.getStartDate());
                    intent.putExtra("endDate", current.getEndDate());

                    if (current.getPrice() != null) {
                        intent.putExtra("price", current.getPrice());
                    }

                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public VacationAdapter.VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate updated layout with title, hotel, and date fields
        View itemView = mInflater.inflate(R.layout.vacation_list_item, parent, false);
        return new VacationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationAdapter.VacationViewHolder holder, int position) {
        if (mVacations != null && !mVacations.isEmpty()) {
            Vacation current = mVacations.get(position);

            holder.tvTitle.setText(current.getVacationName());

            String hotelName = (current.getHotel() != null && !current.getHotel().trim().isEmpty()) ? current.getHotel(): "—"; holder.tvHotel.setText(hotelName);

            String start = current.getStartDate() != null ? current.getStartDate() : "";
            String end   = current.getEndDate()   != null ? current.getEndDate()   : "";
            holder.tvDates.setText(start + " - " + end);

            // NEW: format and show price
            Double price = current.getPrice();
            if (price != null) {
                java.text.NumberFormat money = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US);
                holder.rowPrice.setText(money.format(price));
            } else {
                holder.rowPrice.setText("");
            }
        } else {
            holder.tvTitle.setText("No vacations available");
            holder.tvHotel.setText("");
            holder.tvDates.setText("");
            holder.rowPrice.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return (mVacations != null) ? mVacations.size() : 0;
    }

    public void setVacations(List<Vacation> vacations) {
        // Update list and refresh RecyclerView
        mVacations = vacations;
        notifyDataSetChanged();
    }
}
