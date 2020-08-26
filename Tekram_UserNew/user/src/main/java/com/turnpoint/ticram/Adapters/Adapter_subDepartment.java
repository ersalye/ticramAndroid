package com.turnpoint.ticram.Adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.turnpoint.ticram.R;
import com.turnpoint.ticram.modules.SubDepartment;

import java.util.List;

public class Adapter_subDepartment extends RecyclerView.Adapter<Adapter_subDepartment.ViewHolder> {

    public static List<SubDepartment> mItems;
    private Context mContext;
    private PostItemListener mItemListener;
    int pos;

    public  Adapter_subDepartment(){

    }

    public List<SubDepartment> getmItems(){
        return mItems;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tv_name;
        PostItemListener mItemListener;

        public ViewHolder(View itemView, PostItemListener postItemListener) {
            super(itemView);
            //Toast.makeText(mContext, "ViewHolder" , Toast.LENGTH_SHORT).show();

            tv_name = (TextView) itemView.findViewById(R.id.textView5);
            this.mItemListener = postItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            SubDepartment item = getItem(getAdapterPosition());
            this.mItemListener.onPostClick(item.getName()
                    ,String.valueOf(item.getId())
                    ,pos);
            notifyDataSetChanged();
        }
    }




    public Adapter_subDepartment(Context context, List<SubDepartment> posts, PostItemListener itemListener) {
        mItems = posts;
        mContext = context;
        mItemListener = itemListener;
    }

    @Override
    public Adapter_subDepartment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Toast.makeText(mContext, "nCreateViewHolder" , Toast.LENGTH_SHORT).show();
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.single_row_department, parent, false);

       ViewHolder viewHolder = new ViewHolder(postView, this.mItemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Toast.makeText(mContext, "onBindViewHolder" , Toast.LENGTH_SHORT).show();
        this.pos=position;
        SubDepartment item = mItems.get(position);
        TextView tv_name = holder.tv_name;
        tv_name.setText(item.getName());

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateAnswers(List<SubDepartment> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    private SubDepartment getItem(int adapterPosition) {
        return mItems.get(adapterPosition);
    }


    public interface PostItemListener {
        void onPostClick(String title, String id, int position);
    }
}

