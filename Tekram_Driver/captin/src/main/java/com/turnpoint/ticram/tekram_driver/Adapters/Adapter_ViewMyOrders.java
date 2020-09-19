package com.turnpoint.ticram.tekram_driver.Adapters;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import com.turnpoint.ticram.tekram_driver.R;
import com.turnpoint.ticram.tekram_driver.modules.singleOrder;
public class Adapter_ViewMyOrders extends RecyclerView.Adapter<Adapter_ViewMyOrders.ViewHolder> {
    public static List<singleOrder> mItems;
    private Context mContext;
    private PostItemListener mItemListener;
    int pos;
    public  Adapter_ViewMyOrders(){
    }
    public List<singleOrder> getmItems(){
        return mItems;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tv_start_loc , tv_end_loc , tv_date_,
                tv_cash_credit_ , tv_price, tv_cancel ,tv_duration;
        PostItemListener mItemListener;
        public ViewHolder(View itemView, PostItemListener postItemListener) {
            super(itemView);
            //    Toast.makeText(mContext, "ViewHolder" , Toast.LENGTH_SHORT).show();
            tv_start_loc = (TextView) itemView.findViewById(R.id.tv_from);
            tv_end_loc = (TextView) itemView.findViewById(R.id.tv_to);
            tv_date_ = (TextView) itemView.findViewById(R.id.tv_date);
            tv_cash_credit_ = (TextView) itemView.findViewById(R.id.tv_cash_credit);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price_trip);
            tv_cancel = (TextView) itemView.findViewById(R.id.tv_canceled);
            tv_duration = (TextView) itemView.findViewById(R.id.tv_duration);
            this.mItemListener = postItemListener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            singleOrder item = getItem(getAdapterPosition());
            this.mItemListener.onPostClick(item.getOrderId(),pos, item.getFee());
            notifyDataSetChanged();
        }
    }
    public Adapter_ViewMyOrders(Context context, List<singleOrder> posts, PostItemListener itemListener) {
        mItems = posts;
        mContext = context;
        mItemListener = itemListener;
    }
    @Override
    public Adapter_ViewMyOrders.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Toast.makeText(mContext, "nCreateViewHolder" , Toast.LENGTH_SHORT).show();
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.single_row_viewmyorders, parent, false);
        ViewHolder viewHolder = new ViewHolder(postView, this.mItemListener);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(Adapter_ViewMyOrders.ViewHolder holder, int position) {
        // Toast.makeText(mContext, "onBindViewHolder" , Toast.LENGTH_SHORT).show();
        this.pos=position;
        singleOrder item = mItems.get(position);
        TextView TV_from = holder.tv_start_loc;
        TextView TV_to = holder.tv_end_loc;
        TextView TVDate = holder.tv_date_;
        TextView TVCASH = holder.tv_cash_credit_;
        TextView TVPrice= holder.tv_price ;
        TextView TVCancel= holder.tv_cancel ;
        TextView TVDuration = holder.tv_duration;
        TV_from.setText(item.getLocationTxt());
        TV_to.setText(item.getToLocationTxt());
        TVDate.setText(item.getDateTrip());
        TVPrice.setText(item.getFee()+"د.أ");
        TVCASH.setText(mContext.getResources().getString(R.string.netPrice));
        TVDuration.setVisibility(View.GONE);
        if(!item.getStatus().equals("C")){
            TVCancel.setVisibility(View.INVISIBLE);
        }
        if(item.getStatus().equals("C")){
            TVCancel.setText("ملفي");
        }
    }
    @Override
    public int getItemCount() {
        return mItems.size();
    }
    public void updateAnswers(List<singleOrder> items) {
        mItems = items;
        notifyDataSetChanged();
    }
    private singleOrder getItem(int adapterPosition) {
        return mItems.get(adapterPosition);
    }
    public interface PostItemListener {
        void onPostClick(int order_id, int position, String fee);
    }
}