package com.sixthsemester.project.displace;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hps on 16/05/2016.
 */

public class ProviderDasboardAdapter extends ArrayAdapter<String>{

    private final Activity context;
    private final ArrayList<String> brand;
    private final ArrayList<String> outlet;
    private final ArrayList<String> product;
    private final ArrayList<String> price;
    private final ArrayList<String> view;
    private final Bitmap[] img;



    public ProviderDasboardAdapter(Activity context, ArrayList<String> brand, ArrayList<String> outlet, ArrayList<String> product, ArrayList<String> price,Bitmap[]img,ArrayList<String> view) {
        super(context,  R.layout.trial, brand);
        this.context = context;
        this.brand = brand;
        this.outlet = outlet;
        this.product = product;
        this.price = price;
        this.img=img;
        this.view= view;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.trial, null, true);

        System.out.println(position);
        TextView txt1 = (TextView) rowView.findViewById(R.id.Brand);
        txt1.setText(brand.get(position));
        TextView txt2 = (TextView) rowView.findViewById(R.id.Outlet);
        txt2.setText(outlet.get(position));
        TextView txt3 = (TextView) rowView.findViewById(R.id.Product);
        txt3.setText(product.get(position));
        TextView txt4 = (TextView) rowView.findViewById(R.id.Price);
        txt4.setText(price.get(position));
        ImageView img1 = (ImageView)rowView.findViewById(R.id.imageView);
        img1.setImageBitmap(img[position]);



        return rowView;
    }
}