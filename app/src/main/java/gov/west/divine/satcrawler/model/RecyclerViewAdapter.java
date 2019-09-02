package gov.west.divine.satcrawler.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import gov.west.divine.satcrawler.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<SatReport> reports;
    private LayoutInflater layout;
    private ItemClickListener clickListener;


    public RecyclerViewAdapter(Context context, List<SatReport> reports) {
        this.layout = LayoutInflater.from(context);
        this.reports = reports;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layout.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SatReport report = reports.get(position);
        holder.myTextView.setText(generateReport(report));
    }

    private String generateReport(SatReport report) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        StringBuilder builder = new StringBuilder(report.getMessage());
        builder.append("\n\tsource: "+report.getSource());
        builder.append(" \n\t"+format.format(report.getCreated()));
        return String.valueOf(builder);
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = (TextView) itemView.findViewById(R.id.reportRow);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    SatReport getItem(int id) {
        return reports.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
