package com.sixthsemester.project.displace;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;

/**
 * Created by hps on 03/05/2016.
 */
public class category extends DialogFragment {

    private Button category;
    public static String selection;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final CharSequence[] array = {"Food", "Electronics", "Books","Pharmacy","Cosmetics","Clothing","Jewelery","Sports","Shoes","Home Appliances"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select Category")
                .setSingleChoiceItems(array,-1 ,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                selection= (String)array[which];
                                break;
                            case 1:
                                selection= (String)array[which];
                                break;
                            case 2:
                                selection= (String)array[which];
                                break;
                            case 3:
                                selection= (String)array[which];
                                break;
                            case 4:
                                selection= (String)array[which];
                                break;
                            case 5:
                                selection= (String)array[which];
                                break;
                            case 6:
                                selection= (String)array[which];
                                break;
                            case 7:
                                selection= (String)array[which];
                                break;
                            case 8:
                                selection= (String)array[which];
                                break;
                            case 9:
                                selection= (String)array[which];
                                break;
                            case 10:
                                selection= (String)array[which];
                                break;


                        }

                        // The 'which' argument contains the index position
                        // of the selected item
                    }
                }).setPositiveButton("OK",new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which) {
                //    Toast.makeText(getActivity(), "your high:" + selection, Toast.LENGTH_SHORT).show();
            }
        });
        return builder.create();
        //return super.onCreateDialog(savedInstanceState);

    }


}
