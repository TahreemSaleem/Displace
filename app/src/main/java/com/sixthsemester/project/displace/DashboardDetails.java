package com.sixthsemester.project.displace;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hps on 16/05/2016.
 */
public class DashboardDetails {
        public ArrayList<String> Brand;
        public ArrayList<String> Outlet;
        public ArrayList<String> Product;
        public ArrayList<String> Price;
        public Bitmap[] img;

    public DashboardDetails(){ super();}

    public DashboardDetails(ArrayList<String> a,ArrayList <String> b,ArrayList<String> c,ArrayList<String> d,Bitmap[]img ) {
        super();
        this.Brand=a;
        this.Outlet=b;
        this.Product=c;
        this.Price=d;
        this.img=img;
    }
}
