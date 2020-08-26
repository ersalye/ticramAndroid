package com.turnpoint.ticram.Adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.turnpoint.ticram.R;
import com.turnpoint.ticram.modules.Department;

import java.util.List;

public class Adapter_departments extends RecyclerView.Adapter<Adapter_departments.ViewHolder> {

    public static List<Department> mItems;
    private Context mContext;
    private PostItemListener mItemListener;
    int pos;

    public  Adapter_departments(){

    }

    public List<Department> getmItems(){
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
            Department item = getItem(getAdapterPosition());
            this.mItemListener.onPostClick(item.getTitle()
                    ,item.getId()
                    ,pos);
            notifyDataSetChanged();
        }
    }




    public Adapter_departments(Context context, List<Department> posts, PostItemListener itemListener) {
        mItems = posts;
        mContext = context;
        mItemListener = itemListener;
    }

    @Override
    public Adapter_departments.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

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
        Department item = mItems.get(position);
        TextView tv_name = holder.tv_name;
        tv_name.setText(item.getTitle());

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateAnswers(List<Department> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    private Department getItem(int adapterPosition) {
        return mItems.get(adapterPosition);
    }


    public interface PostItemListener {
        void onPostClick(String title, int id, int position);
    }
}

