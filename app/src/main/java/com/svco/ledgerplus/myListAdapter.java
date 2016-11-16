package com.svco.ledgerplus;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

/**
 * Created by Nidhin on 16-11-2016.
 */

public class myListAdapter extends ArrayAdapter<String> {
    Activity activity;
    int[] ids;
    List<String> cats;
    public myListAdapter(Activity activity, int resource, int[] ids, List<String> cats) {
        super(activity, resource,cats);
        this.activity=activity;
        this.ids=ids;
        this.cats=cats;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final LinearLayout dialogLayout= (LinearLayout) activity.getLayoutInflater().inflate(R.layout.row_layout,null);
        TextView v= (TextView) dialogLayout.findViewById(R.id.textViewLayoutCategory);
        v.setText(cats.get(position));
        Button edit= (Button) dialogLayout.findViewById(R.id.buttonRowDelete);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout layout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.dialog_layout_category,null);
                final EditText editText = (EditText) layout.findViewById(R.id.cat_edit);
                editText.setText(cats.get(position));
                MaterialDialog dialog = new MaterialDialog.Builder(activity)
                        .customView(layout,true)
                        .positiveText("Edit")
                        .negativeText("Cancel")
                        .neutralText("Delete")


                        .onPositive(new MaterialDialog.SingleButtonCallback() {

                            @Override
                            public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                LedgerDBManager myDb = new LedgerDBManager(activity);
                                String id=String.valueOf(ids[position]);

                                String text = editText.getText().toString();
                                if(text.isEmpty()){
                                    Toast.makeText(activity.getApplicationContext(),"ERROR!!! Enter a valid Category name",Toast.LENGTH_LONG).show();

                                }
                                else{
                                    myDb.updateCat(id,text);
                                    cats.set(position,text);
                                    myListAdapter.this.notifyDataSetChanged();
                                    Toast.makeText(activity.getApplicationContext(),"Edit Successful",Toast.LENGTH_SHORT).show();
                                }

                                }

                        })
                        .onNeutral(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                LedgerDBManager myDb = new LedgerDBManager(activity);
                                String id=String.valueOf(ids[position]);
                                if(myDb.deleteCat(id)>0){
                                    cats.remove(position);
                                    myListAdapter.this.notifyDataSetChanged();
                                    Toast.makeText(activity.getApplicationContext(),"Category Deleted",Toast.LENGTH_SHORT).show();

                                }
                                else{
                                    Toast.makeText(activity.getApplicationContext(),"ERROR!!! Could not Delete Category",Toast.LENGTH_SHORT).show();

                                }

                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .build();

                dialog.show();

            }
        });

        return dialogLayout;
    }
}
