package com.turnpoint.ticram.Adapters;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.turnpoint.ticram.GetCurrentLanguagePhone;
import com.turnpoint.ticram.R;
import com.turnpoint.ticram.modules.Ticket;

import java.util.List;

public class Adapter_tickets extends RecyclerView.Adapter<Adapter_tickets.ViewHolder> {

    public static List<Ticket> mItems;
    private Context mContext;
    private PostItemListener mItemListener;
    int pos;
    View postView;
    String language;

    public  Adapter_tickets(){

    }

    public List<Ticket> getmItems(){
        return mItems;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tv_title, tv_ref_num, tv_date , tv_status;

        PostItemListener mItemListener;

        public ViewHolder(View itemView, PostItemListener postItemListener) {
            super(itemView);
            //Toast.makeText(mContext, "ViewHolder" , Toast.LENGTH_SHORT).show();

            tv_title = (TextView) itemView.findViewById(R.id.textView15);
            tv_ref_num= (TextView) itemView.findViewById(R.id.textView17);
            tv_date = (TextView) itemView.findViewById(R.id.textView18);
            tv_status = (TextView) itemView.findViewById(R.id.texttvieww);

            this.mItemListener = postItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Ticket item = getItem(getAdapterPosition());
            this.mItemListener.onPostClick(String.valueOf(item.getId()));
            notifyDataSetChanged();
        }
    }




    public Adapter_tickets(Context context, List<Ticket> posts,
                           PostItemListener itemListener) {
        mItems = posts;
        mContext = context;
        mItemListener = itemListener;
    }

    @Override
    public Adapter_tickets.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Toast.makeText(mContext, "nCreateViewHolder" , Toast.LENGTH_SHORT).show();
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
         language= new GetCurrentLanguagePhone().getLang();
        if(language.equals("eng"))
         postView = inflater.inflate(R.layout.single_row_ticket, parent, false);
        else if(language.equals("ara"))
            postView = inflater.inflate(R.layout.single_row_ticket_ara, parent, false);
        ViewHolder viewHolder = new ViewHolder(postView, this.mItemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Toast.makeText(mContext, "onBindViewHolder" , Toast.LENGTH_SHORT).show();
        this.pos=position;
        Ticket item = mItems.get(position);
        TextView tv_title = holder.tv_title;
        TextView tv_ref_num = holder.tv_ref_num;
        TextView tv_date = holder.tv_date;
        TextView tv_status = holder.tv_status;

        tv_title.setText(item.getDept());
        tv_ref_num.setText(item.getRefNo());
        tv_date.setText(item.getDate());
        tv_status.setText(item.getStatus_text());

        if(item.getStatus().equals("NEW") || item.getStatus().equals("OPEN")){
            tv_status.setBackground(ContextCompat.getDrawable(mContext, R.drawable.round_orange));

        }else if(item.getStatus().equals("REPLY")){
            tv_status.setBackground(ContextCompat.getDrawable(mContext, R.drawable.round_blue));

        }else if(item.getStatus().equals("CLOSED")){
            tv_status.setBackground(ContextCompat.getDrawable(mContext, R.drawable.round_green));
        }

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateAnswers(List<Ticket> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    private Ticket getItem(int adapterPosition) {
        return mItems.get(adapterPosition);
    }


    public interface PostItemListener {
        void onPostClick(String id);
    }
}
