package com.turnpoint.ticram.Adapters;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.turnpoint.ticram.MySharedPreference;
import com.turnpoint.ticram.R;
import com.turnpoint.ticram.modules.count_cars;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marina on 15/10/2018.
 */

public class Adapter_subCatogary extends RecyclerView.Adapter<Adapter_subCatogary.ViewHolder> {

    public static List<count_cars> mItems;
    private Context mContext;
    private PostItemListener mItemListener;
    int pos;
    List<LinearLayout> itemViewList = new ArrayList<>();
    List<ImageView> itemImages = new ArrayList<>();


    public  Adapter_subCatogary(){
    }


    public List<count_cars> getmItems(){
        return mItems;
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tv_name , tv_num ;
        LinearLayout layout_all;
        ImageView img_icon;
        PostItemListener mItemListener;

        public ViewHolder(View itemView, PostItemListener postItemListener) {
            super(itemView);
            //    Toast.makeText(mContext, "ViewHolder" , Toast.LENGTH_SHORT).show();

            tv_num = (TextView) itemView.findViewById(R.id.tv_count_yellow);
            tv_name = (TextView) itemView.findViewById(R.id.tv_yellow_taxi);
            img_icon=(ImageView) itemView.findViewById(R.id.icon_yellow_taxi);
            layout_all=(LinearLayout)itemView.findViewById(R.id.lin_tax_yellow);
            this.mItemListener = postItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            count_cars item = getItem(getAdapterPosition());
            this.mItemListener.onPostClick(item.getSubtype(),item.getCount(),pos);
            notifyDataSetChanged();
        }
    }




    public Adapter_subCatogary(Context context, List<count_cars> posts, PostItemListener itemListener) {
        mItems = posts;
        mContext = context;
        mItemListener = itemListener;
    }

    @Override
    public Adapter_subCatogary.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Toast.makeText(mContext, "nCreateViewHolder" , Toast.LENGTH_SHORT).show();
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.single_row_subcatogry, parent, false);

        ViewHolder viewHolder = new ViewHolder(postView, this.mItemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Adapter_subCatogary.ViewHolder holder, int position) {
        // Toast.makeText(mContext, "onBindViewHolder" , Toast.LENGTH_SHORT).show();
        this.pos=position;
        final count_cars item = mItems.get(position);
        TextView TV_name = holder.tv_name;
        TextView TV_num = holder.tv_num;
        ImageView img=holder.img_icon;
        final LinearLayout linearLayout=holder.layout_all;
        itemViewList.add(linearLayout);
        itemImages.add(img);

        TV_name.setText(item.getSubtypeTxt());
        TV_num.setText(item.getCount());
        Glide.with(mContext).load(item.getIcon()).into(img);






       linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //linearLayout.setBackgroundResource(R.drawable.circle_sellected);
               // notifyDataSetChanged();

                Toast.makeText(mContext, "hoooo",Toast.LENGTH_LONG).show();

                for(LinearLayout lin : itemViewList){
                    lin.setBackgroundResource(R.drawable.circle_blankhdpi);
                }
                linearLayout.setBackgroundResource(R.drawable.circle_sellected);

                new MySharedPreference(mContext).setStringShared("selected_car_txt", item.getSubtype());
                new MySharedPreference(mContext).setStringShared("selected_car_count", item.getCount());


            }
        });

        if (item.getCount().equals("0")) {
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            img.setColorFilter(filter);
            TV_num.setVisibility(View.INVISIBLE);
            linearLayout.setClickable(false);

            //Toast.makeText(MapActivity.this, "zero", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateAnswers(List<count_cars> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    private count_cars getItem(int adapterPosition) {
        return mItems.get(adapterPosition);
    }


    public interface PostItemListener {
        void onPostClick(String selected_car, String count,int position);
    }
}
