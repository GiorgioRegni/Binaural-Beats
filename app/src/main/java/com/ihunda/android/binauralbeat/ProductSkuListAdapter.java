package com.ihunda.android.binauralbeat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ProductSkuListAdapter extends ArrayAdapter {
    private String mTitles[];
    private String mDescriptions[];
    private String mPrices[];
    private Context mContext;

    public ProductSkuListAdapter(Context context, String titles[], String descriptions[], String prices[]) {

        super(context, R.layout.dialog_show_sku_row , titles);

        mContext = context;
        mTitles = titles;
        mDescriptions = descriptions;
        mPrices = prices;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.dialog_show_sku_row, parent,false);

        //this code gets references to objects in the listview_row.xml file
        TextView tvTitle = (TextView) rowView.findViewById(R.id.title);
        TextView tvDescription = (TextView) rowView.findViewById(R.id.description);
        TextView tvPrice = (TextView) rowView.findViewById(R.id.price);
        //this code sets the values of the objects to values from the arrays
        tvTitle.setText(mTitles[position]);
        tvDescription.setText(mDescriptions[position]);
        tvPrice.setText(mPrices[position].replace("(Binaural Beats Therapy)", ""));

        return rowView;
    };
}
