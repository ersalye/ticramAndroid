package com.turnpoint.ticram.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.BaseAdapter;
import com.turnpoint.ticram.R;
import com.turnpoint.ticram.modules.Cancels;
import java.util.ArrayList;


public class Adapter_cancel2 extends BaseAdapter {

    private ArrayList<Cancels> mProductList;
    Cancels curProduct;
    Context context;
    private int position;

    public Adapter_cancel2(ArrayList<Cancels> list,Context ctx) {
        mProductList = list;
        this.context=ctx;
    }

    @Override
    public int getCount() {
        return mProductList.size();
    }

    @Override
    public Object getItem(int position) {
        return mProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

        public Cancels getSelected(int position){
        return mProductList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewItem item;

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.single_row_cancel, null);
            item = new ViewItem();

            item.tx_name = (TextView) convertView.findViewById(R.id.textView);

            convertView.setTag(item);
        } else {
            item = (ViewItem) convertView.getTag();
        }

        curProduct = mProductList.get(position);

        item.tx_name.setText(curProduct.getTitle());
        return convertView;
    }


    public static class ViewItem {

        TextView tx_name;

    }




}



