package com.turnpoint.ticram.tekram_driver.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.turnpoint.ticram.tekram_driver.R;
import com.turnpoint.ticram.tekram_driver.modules.SingleNotification;
import com.turnpoint.ticram.tekram_driver.modules.SingleNotification;

import java.util.List;

public class Adapter_ViewMyNotification extends RecyclerView.Adapter<Adapter_ViewMyNotification.ViewHolder> {
    public static List<SingleNotification> mItems;
    private Context mContext;
    int pos;
    public  Adapter_ViewMyNotification(){
    }

    public List<SingleNotification> getmItems(){
        return mItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_date, tv_notification;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_notification = (TextView) itemView.findViewById(R.id.tv_notification);
        }
    }

    public Adapter_ViewMyNotification(Context context, List<SingleNotification> posts) {
        mItems = posts;
        mContext = context;
    }
    
    @Override
    public Adapter_ViewMyNotification.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Toast.makeText(mContext, "nCreateViewHolder" , Toast.LENGTH_SHORT).show();
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.single_row_viewmynotification, parent, false);
     ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(Adapter_ViewMyNotification.ViewHolder holder, int position) {
        this.pos=position;
        SingleNotification item = mItems.get(position);

        TextView TVDate = holder.tv_date;
        TextView TVNotification = holder.tv_notification;

        TVDate.setText(item.getDateNotification());
        TVNotification.setText(item.getTextNotification());
    }
    
    @Override
    public int getItemCount() {
        if(mItems != null)
            return mItems.size();
        return 0;
    }
    public void updateAnswers(List<SingleNotification> items) {
        mItems = items;
        notifyDataSetChanged();
    }
    
    private SingleNotification getItem(int adapterPosition) {
        return mItems.get(adapterPosition);
    }
}