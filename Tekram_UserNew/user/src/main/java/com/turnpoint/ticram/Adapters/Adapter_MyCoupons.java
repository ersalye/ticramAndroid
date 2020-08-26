package com.turnpoint.ticram.Adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.turnpoint.ticram.GetCurrentLanguagePhone;
import com.turnpoint.ticram.R;
import com.turnpoint.ticram.modules.current_coupoun;

import java.util.List;

/**
 * Created by marina on 10/05/2018.
 */


public class Adapter_MyCoupons extends RecyclerView.Adapter<Adapter_MyCoupons.ViewHolder> {

    public static List<current_coupoun> mItems;
    private Context mContext;
    private PostItemListener mItemListener;
    int pos;

    public  Adapter_MyCoupons(){

    }

    public List<current_coupoun> getmItems(){
        return mItems;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tv_endDate, tv_content ;
        public ImageView icon_;

        PostItemListener mItemListener;

        public ViewHolder(View itemView, PostItemListener postItemListener) {
            super(itemView);
            //    Toast.makeText(mContext, "ViewHolder" , Toast.LENGTH_SHORT).show();

            tv_endDate = (TextView) itemView.findViewById(R.id.tv_date_end);
            tv_content = (TextView) itemView.findViewById(R.id.tv_text);
            icon_=itemView.findViewById(R.id.imgDriver);

            this.mItemListener = postItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            /*current_coupoun item = getItem(getAdapterPosition());
            this.mItemListener.onPostClick(item.getId(),pos);
            notifyDataSetChanged();*/
        }
    }




    public Adapter_MyCoupons(Context context, List<current_coupoun> posts, PostItemListener itemListener) {
        mItems = posts;
        mContext = context;
        mItemListener = itemListener;
    }

    @Override
    public Adapter_MyCoupons.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Toast.makeText(mContext, "nCreateViewHolder" , Toast.LENGTH_SHORT).show();
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.single_row_mycoupns, parent, false);

        ViewHolder viewHolder = new ViewHolder(postView, this.mItemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Adapter_MyCoupons.ViewHolder holder, int position) {
        // Toast.makeText(mContext, "onBindViewHolder" , Toast.LENGTH_SHORT).show();
        this.pos=position;
        current_coupoun item = mItems.get(position);
        TextView TV_endDate = holder.tv_endDate;
        TextView TV_Content = holder.tv_content;
        ImageView img_= holder.icon_;

        if( new GetCurrentLanguagePhone().getLang().equals("ara")){
            TV_endDate.setText(" ينتهي "+item.getExpiredDate());

        }
        else if( !new GetCurrentLanguagePhone().getLang().equals("ara")){
            TV_endDate.setText(" Ends "+item.getExpiredDate());
        }

        TV_Content.setText(item.getText());

        if(item.getEnd().equals("1")){ // still not end
            img_.setBackgroundResource(R.drawable.green_circle);
            img_.setImageResource(R.drawable.green_circle);        }
       else if(item.getEnd().equals("0")){ // ended
            img_.setBackgroundResource(R.drawable.blue_circle);
            img_.setImageResource(R.drawable.blue_circle);        }


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateAnswers(List<current_coupoun> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    private current_coupoun getItem(int adapterPosition) {
        return mItems.get(adapterPosition);
    }


    public interface PostItemListener {
        void onPostClick(int order_id, int position);
    }
}

