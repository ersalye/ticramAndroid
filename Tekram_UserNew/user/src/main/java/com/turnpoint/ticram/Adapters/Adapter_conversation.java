package com.turnpoint.ticram.Adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.turnpoint.ticram.GetCurrentLanguagePhone;
import com.turnpoint.ticram.R;
import com.turnpoint.ticram.modules.Conversation_ticket;

import java.util.List;

public class Adapter_conversation  extends RecyclerView.Adapter<Adapter_conversation.ViewHolder> {

    public static List<Conversation_ticket> mItems;
    private Context mContext;
    private PostItemListener mItemListener;
    int pos;
    View postView;
    String language;
    public  Adapter_conversation(){

    }

    public List<Conversation_ticket> getmItems(){
        return mItems;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tv_msg, tv_sender, tv_date;

       PostItemListener mItemListener;

        public ViewHolder(View itemView, PostItemListener postItemListener) {
            super(itemView);
            //Toast.makeText(mContext, "ViewHolder" , Toast.LENGTH_SHORT).show();

            tv_msg = (TextView) itemView.findViewById(R.id.textView_content);
            tv_sender= (TextView) itemView.findViewById(R.id.textView_sender);
            tv_date = (TextView) itemView.findViewById(R.id.textView_date);

            this.mItemListener = postItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            /*Conversation_ticket item = getItem(getAdapterPosition());
            this.mItemListener.onPostClick(String.valueOf(item.getId()));
            notifyDataSetChanged();*/
        }
    }




    public Adapter_conversation(Context context, List<Conversation_ticket> posts,
                           PostItemListener itemListener) {
        mItems = posts;
        mContext = context;
        mItemListener = itemListener;
    }

    @Override
    public Adapter_conversation.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Toast.makeText(mContext, "nCreateViewHolder" , Toast.LENGTH_SHORT).show();
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        language= new GetCurrentLanguagePhone().getLang();
        if(language.equals("eng"))
            postView = inflater.inflate(R.layout.single_row_conversation, parent, false);
        else if(language.equals("ara"))
            postView = inflater.inflate(R.layout.single_row_converation_ara, parent, false);

        ViewHolder viewHolder = new ViewHolder(postView, this.mItemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Toast.makeText(mContext, "onBindViewHolder" , Toast.LENGTH_SHORT).show();
        this.pos=position;
        Conversation_ticket item = mItems.get(position);
        TextView tv_msg = holder.tv_msg;
        TextView tv_sender = holder.tv_sender;
        TextView tv_date = holder.tv_date;

        tv_msg.setText(item.getMsg());
        tv_sender.setText(item.getUserType());
        tv_date.setText(item.getDate());
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateAnswers(List<Conversation_ticket> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    private Conversation_ticket getItem(int adapterPosition) {
        return mItems.get(adapterPosition);
    }


    public interface PostItemListener {
        void onPostClick(String id);
    }

    public void addItem(Conversation_ticket item) {
        mItems.add(item);
        notifyDataSetChanged();
    }
}
