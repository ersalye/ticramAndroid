package com.turnpoint.ticram.tekram_driver.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.bumptech.glide.Glide;
import com.turnpoint.ticram.tekram_driver.Activites.ViewDetailsOrder;
import com.turnpoint.ticram.tekram_driver.CustomRunnable;
import com.turnpoint.ticram.tekram_driver.DBHelper2;
import com.turnpoint.ticram.tekram_driver.MySharedPreference;
import com.turnpoint.ticram.tekram_driver.R;
import com.turnpoint.ticram.tekram_driver.modules.Current;
import io.paperdb.Paper;

public class Adapter_MyOrders extends RecyclerView.Adapter<Adapter_MyOrders.ViewHolder> {

        public static List<Current> mItems;
        private Context mContext;
        private PostItemListener mItemListener;
        int pos;
        TextView TVTimer;
        int currenttime=0;
        LinearLayout LinearAll;

        private Handler handler = new Handler();


    public void clearAll() {
        handler.removeCallbacksAndMessages(null);
    }





    public Adapter_MyOrders(Context context, List<Current> posts, PostItemListener itemListener) {
        mItems = posts;
        mContext = context;
        mItemListener = itemListener;

    }





    public List<Current> getmItems(){
            return mItems;
        }





        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            public TextView tv_driver_name , tv_rate , tv_time, tv_distance , tv_payment_method ,
                    tv_timeTOuser , tv_timer;
            LinearLayout linear_cash, linear_dist_time , linear_all;
            public ImageView icon_user;
            Button btn_tawklna;
            RatingBar rb;
            PostItemListener mItemListener;
            CustomRunnable customRunnable;

            public void bind() {
                Current item = mItems.get(pos);
                handler.removeCallbacks(customRunnable);
                customRunnable.holder = tv_timer;
                customRunnable.millisUntilFinished = item.getTime_to_hide();
                handler.postDelayed(customRunnable, 100);

            }

            public ViewHolder(View itemView, PostItemListener postItemListener) {
                super(itemView);
                //    Toast.makeText(mContext, "ViewHolder" , Toast.LENGTH_SHORT).show();

                linear_cash= itemView.findViewById(R.id.lin_cash);
                linear_dist_time= itemView.findViewById(R.id.lin_dis_time);
                tv_driver_name = (TextView) itemView.findViewById(R.id.txtNameDriver);
                tv_rate = (TextView) itemView.findViewById(R.id.txtRate);
                tv_time = (TextView) itemView.findViewById(R.id.txtTime);
                tv_distance = (TextView) itemView.findViewById(R.id.txtKm);
                tv_payment_method = (TextView) itemView.findViewById(R.id.txtCash);
                tv_timeTOuser = (TextView) itemView.findViewById(R.id.txtBaqya);
                tv_timer = (TextView) itemView.findViewById(R.id.txt_timer);
                btn_tawklna=itemView.findViewById(R.id.button_tawklnaa);
                rb =  itemView.findViewById(R.id.ratingBar);
                icon_user=itemView.findViewById(R.id.imgDriver);
                linear_all=itemView.findViewById(R.id.rtMain);


                Current item = mItems.get(pos);
//                Toast.makeText(mContext,item.getTime_to_hide(), Toast.LENGTH_SHORT).show();
                customRunnable = new CustomRunnable(handler,tv_timer, linear_all,item.getTime_to_hide());



                btn_tawklna.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(mContext,"clicked", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                        alertDialogBuilder.setMessage("هل انت متأكد؟");
                        alertDialogBuilder.setPositiveButton("نعم",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {

                                        try {
                                            if (mItems.size() > 0) {

                                                new MySharedPreference(mContext).setFloatShared("total_dis_before", (float) 0.0);
                                                new MySharedPreference(mContext).setIntShared("total_time_normal_before", 0);
                                                new MySharedPreference(mContext).setIntShared("total_time_slow_before", 0);

                                                Paper.book().write("totalDistance",Double.parseDouble("0"));
                                                Paper.book().write("totalTimeNormal",Double.parseDouble("0"));
                                                Paper.book().write("totalTimeSlow", Double.parseDouble("0"));

                                           /* new MySharedPreference(mContext).setStringShared("coords_c","");
                                            new MySharedPreference(mContext).setStringShared("array_time", "");
                                            new MySharedPreference(mContext).setStringShared("array_sec","");*/
                                                DBHelper2 db = new DBHelper2(mContext);
                                                db.deleteCoordsTable();
                                                db.deleteTimesTable();
                                                db.deleteSecTable();

                                                Current item = getItem(getAdapterPosition());
                                                Intent i = new Intent(mContext, ViewDetailsOrder.class);
                                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                new MySharedPreference(mContext).setStringShared("cur_order_id", String.valueOf(item.getOrderId()));

                                                i.putExtra("order_id", item.getOrderId());
                                                i.putExtra("pos", pos);
                                                i.putExtra("from", "adapter");
                                                mContext.startActivity(i);
                                            } else
                                                Toast.makeText(mContext, "Index out of Bound Exception.", Toast.LENGTH_SHORT).show();
                                        }
                                        catch (Exception ex){}

                                    }
                                });
                        alertDialogBuilder.setNegativeButton("لا",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    }
                });

                this.mItemListener = postItemListener;
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                try {
                    Current item = getItem(getAdapterPosition());
                    this.mItemListener.onPostClick(item.getOrderId(), pos);
                    notifyDataSetChanged();
                }
                catch (Exception ex){}

            }






        }






        @Override
        public Adapter_MyOrders.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            // Toast.makeText(mContext, "nCreateViewHolder" , Toast.LENGTH_SHORT).show();
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View postView = inflater.inflate(R.layout.single_row_myordes, parent, false);

            ViewHolder viewHolder = new ViewHolder(postView, this.mItemListener);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(Adapter_MyOrders.ViewHolder holder, int position) {
            // Toast.makeText(mContext, "onBindViewHolder" , Toast.LENGTH_SHORT).show();

            this.pos=position;
            Current item = mItems.get(position);
            TextView TVdriver_name = holder.tv_driver_name;
            TextView TVrate = holder.tv_rate;
            TextView TVtime = holder.tv_time;
            TextView TVdistance = holder.tv_distance;
            TextView TVcash= holder.tv_payment_method;
            ImageView img_user= holder.icon_user;
            TextView TVTimeToUser= holder.tv_timeTOuser;
            TVTimer= holder.tv_timer;
            RatingBar rb_user=holder.rb;
             LinearLayout LinCASH=holder.linear_cash;
             LinearAll=holder.linear_all;
            LinearLayout linTimeDistance= holder.linear_dist_time;


            Glide.with(mContext).load(item.getUserPhoto()).into(img_user);
           if(item.getTime_val()== 0){
                linTimeDistance.setVisibility(View.INVISIBLE);
               // LinCASH.setVisibility(View.INVISIBLE);
            }

            TVdriver_name.setText(item.getUserName());
            TVtime.setText(item.getTime());
            TVdistance.setText(item.getDistance());
            TVcash.setText(item.getPaymentMethod());
            TVrate.setText(item.getUserRate());
            rb_user.setRating(Float.parseFloat(item.getUserRate()));
            TVTimeToUser.setText(" يبعد عنك "+item.getTimeToUser());

            holder.bind();

        }


        public void countdownItems(int time_) {
             currenttime=time_;
             new CountDownTimer(time_*1000,
                     1000) { // adjust the milli seconds here
                public void onTick(long millisUntilFinished) {
                    //Toast.makeText(getApplicationContext(),"tick!", Toast.LENGTH_SHORT).show();
                    currenttime=currenttime-1;
                    TVTimer.setText(String.valueOf(currenttime));

                }
                public void onFinish() {
                    TVTimer.setText("0");
                    if(currenttime ==0 || currenttime==1){
                        LinearAll.setVisibility(View.INVISIBLE);

                    }
                }
            }.start();
        }



        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public void updateAnswers(List<Current> items) {
            mItems = items;
            notifyDataSetChanged();
        }

        private Current getItem(int adapterPosition) {
            return mItems.get(adapterPosition);
        }


        public interface PostItemListener {
            void onPostClick(int order_id, int position);
        }
    }
