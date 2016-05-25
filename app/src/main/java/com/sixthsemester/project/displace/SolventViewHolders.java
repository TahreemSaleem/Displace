package com.sixthsemester.project.displace;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class SolventViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView CardName;
    public ImageView CardImage;
    private Context context;



    public SolventViewHolders(Context context,View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        CardName = (TextView) itemView.findViewById(R.id.card_name);
        CardImage = (ImageView) itemView.findViewById(R.id.card_photo);
        this.context = context;
    }

    @Override
    public void onClick(View view) {

            Intent i1 = new Intent(context, OpenAd.class);
            i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i1);
        Toast.makeText(view.getContext(), "Clicked Position = " + getPosition(), Toast.LENGTH_SHORT).show();

    }




}
