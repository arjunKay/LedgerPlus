package com.svco.ledgerplus;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.support.v4.app.Fragment;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import static com.svco.ledgerplus.R.drawable.f;

/**
 * Created by user on 11/26/2016.
 */

public class ListviewAdapter extends FragmentActivity implements ListAdapter {
    //Declare variables
    Context con;
    String[] amount;
    String[] date;
    String[] type;
    String[] source;
    String[] desc;
    List idList =new ArrayList<String>();
    LedgerDBManager database;
    LayoutInflater jrnlLayoutInflater;
    View diagView;
    private HashSet<Integer> unfoldedIndexes=new HashSet<>();

    public ListviewAdapter(Context context) {
        this.con=context;
        database=new LedgerDBManager(con);
        Cursor c=database.executeQuery("Select * from TRANSACTIONS ORDER BY ((YEAR*10000)+(MONTH*100)+DAY)");
        setJournalValues(c);

    }
    public ListviewAdapter(Context con, Cursor cur) {
        database = new LedgerDBManager(con);
        setJournalValues(cur);
    }

    public void setJournalValues(Cursor c)
    {

        //Cursor c=database.getAllData("Transactions");

         idList.clear();
        amount=new String[c.getCount()];
        date=new String[c.getCount()];
        type=new String[c.getCount()];
        source=new String[c.getCount()];
        desc=new String[c.getCount()];
        int i=0;
        if(c.moveToFirst())
        {
            do {


                    amount[i]=c.getString(1);
               // if(amount[i].equals("-"))
                  //  database.deleteTxn(c.getString(0));
                    idList.add(c.getString(0));
                    String xx=c.getString(0);
                    date[i]=c.getString(5)+"/"+c.getString(6)+"/"+c.getString(7);
                    source[i]=c.getString(2);
                    type[i]=c.getString(3);
                    desc[i]=c.getString(4);
                    i++;



            }while(c.moveToNext());
        }

    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return amount.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        jrnlLayoutInflater  = (LayoutInflater) (parent.getContext()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        diagView=jrnlLayoutInflater.inflate(R.layout.dialog_layout_journal_edit,null);

        RelativeLayout rljrnl = null, jrnlDiagEditDelete;
        FoldingCell cell=null ;//(FoldingCell) convertView;
        final  ViewHolder holder;

        if (cell == null) {
            holder = new ViewHolder();
            LayoutInflater view = LayoutInflater.from(parent.getContext());
            rljrnl=(RelativeLayout)view.inflate(R.layout.fcell,parent,false);
           cell=(FoldingCell)rljrnl.findViewById(R.id.foldingCell);
            holder.jrnlEditDelete=(Button)cell.findViewById(R.id.jrnlEditDelete);
            holder.jrnlRelLayout=(RelativeLayout)cell.findViewById(R.id.jrnlRelLayout);
            holder.activityMain=(RelativeLayout)parent.findViewById(R.id.activity_main);
            holder.fc=(FoldingCell) cell.findViewById(R.id.foldingCell);
            holder.amtTx2=(TextView)cell.findViewById(R.id.amtTx2);
            holder.jrnlInDateTx=(TextView)cell.findViewById(R.id.jrnlInDateTx);
            holder.amtOut=(TextView)cell.findViewById(R.id.amtOut1);
            holder.dateOutFront=(TextView)cell.findViewById(R.id.dateOut1);
            holder.typeOut=(TextView)cell.findViewById(R.id.typeTx);
            holder.sourceOut=(TextView)cell.findViewById(R.id.sourceTx);
            holder.descOut=(TextView)cell.findViewById(R.id.descTx);
            holder.jrnlCatTx=(TextView)cell.findViewById(R.id.jrnlCatTx);
            holder.jrnlSrcTx=(TextView)cell.findViewById(R.id.jrnlSrcTx);
            holder.jrnlSrcIndTx=(TextView)cell.findViewById(R.id.jrnlSrcIndTx);
            holder.jrnlCatIndTx=(TextView)cell.findViewById(R.id.jrnlCatIndTx);
            cell.setTag(holder);
        } else {


            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            holder = (ViewHolder) cell.getTag();
        }

        holder.jrnlEditDelete.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                final Spinner catIn=(Spinner)diagView.findViewById(R.id.diagCatSpinner);
                final RadioGroup inExRG, sourceRG;
                final EditText amountIn, descIn;
                final TextView dateIn;
                dateIn=(TextView)diagView.findViewById(R.id.diagDateIn);
                amountIn=(EditText)diagView.findViewById(R.id.diagAmtIn);
                descIn=(EditText)diagView.findViewById(R.id.diagDescIn);
                inExRG=(RadioGroup)diagView.findViewById(R.id.jrnlDiagInExRG);
                sourceRG=(RadioGroup)diagView.findViewById(R.id.jrnlDiagSourceRG);
                final  RadioButton inRB,exRB,cashRb,bankRB;
                inRB=(RadioButton)diagView.findViewById(R.id.jrnlInRB);
                exRB=(RadioButton)diagView.findViewById(R.id.jrnlExpRB);
                cashRb=(RadioButton)diagView.findViewById(R.id.jrnlCashRB);
                bankRB=(RadioButton)diagView.findViewById(R.id.jrnlBankRB);
                final List<String> catList=new ArrayList<String>();
                int tempint = 0;
                if(Integer.parseInt(amount[position])>=0)
                {
                    inRB.setChecked(true);
                    amountIn.setText(""+Math.abs(Integer.parseInt(amount[position])));
                    catList.clear();
                    catList.add("Select a Category");
                    Cursor c=database.getInCategory();
                    if(c.moveToFirst())
                    {
                        do
                        {
                            catList.add(c.getString(1));
                            if(c.getString(1).equals(type[position]))
                                tempint=catList.size()-1;
                        }while(c.moveToNext());
                    }
                    ArrayAdapter<String> dataAdapterCat= new ArrayAdapter<String>(parent.getContext(), android.R.layout.simple_spinner_item, catList);
                    dataAdapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    catIn.setAdapter(dataAdapterCat);
                    catIn.setSelection(tempint);
                }
                else
                {
                    exRB.setChecked(true);
                    amountIn.setText(""+Math.abs(Integer.parseInt(amount[position])));
                    catList.clear();
                    catList.add("Select a Category");
                    Cursor c=database.getExCategory();
                    if(c.moveToFirst())
                    {
                        do
                        {
                            catList.add(c.getString(1));
                            if(c.getString(1).equals(type[position]))
                                tempint=catList.size()-1;
                        }while(c.moveToNext());
                    }
                    ArrayAdapter<String> dataAdapterCat= new ArrayAdapter<String>(parent.getContext(), android.R.layout.simple_spinner_item, catList);
                    dataAdapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    catIn.setAdapter(dataAdapterCat);
                    catIn.setSelection(tempint);
                }
                if(source[position].equalsIgnoreCase("cash"))
                    cashRb.setChecked(true);
                else
                    bankRB.setChecked(true);
                descIn.setText(desc[position]);
                dateIn.setHint(""+date[position]);
                inRB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        catList.clear();
                        catList.add("Select a Category");
                        Cursor c=database.getInCategory();
                        if(c.moveToFirst())
                        {
                            do
                            {
                                catList.add(c.getString(1));
                            }while(c.moveToNext());
                        }
                        ArrayAdapter<String> dataAdapterCat= new ArrayAdapter<String>(parent.getContext(), android.R.layout.simple_spinner_item, catList);
                        dataAdapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        catIn.setAdapter(dataAdapterCat);

                    }

                });
                exRB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        catList.clear();
                        catList.add("Select a Category");
                        Cursor c=database.getExCategory();
                        if(c.moveToFirst())
                        {
                            do
                            {
                                catList.add(c.getString(1));
                            }while(c.moveToNext());
                        }
                        ArrayAdapter<String> dataAdapterCat= new ArrayAdapter<String>(parent.getContext(), android.R.layout.simple_spinner_item, catList);
                        dataAdapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        catIn.setAdapter(dataAdapterCat);
                    }
                });
                //DatePicker
                final Calendar cal=Calendar.getInstance();
                dateIn.setHint(date[position]);

                final DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String dateToSet = "" + dayOfMonth + "/" + (month + 1) + "/" + year;
                        dateIn.setHint(dateToSet);

                    }


                };

                    dateIn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            new DatePickerDialog(parent.getContext(),listener,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show();

                        }
                    });

                final MaterialDialog dialogEditDelete=new MaterialDialog.Builder(parent.getContext())
                        .title("Edit/Delete")
                        //.customView(((RelativeLayout) jrnlLayoutInflater.inflate(R.layout.dialog_layout_journal_edit,null)),true)
                        .customView((RelativeLayout)diagView,true)
                        .positiveText("Update")
                        .negativeText("Cancel")
                        .neutralText("Delete")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                String[] tempDate;
                                String source, inEx;
                                if(inExRG.getCheckedRadioButtonId()==R.id.jrnlInRB)
                                {
                                    inEx="i";
                                }
                                else
                                    inEx="e";
                                if(sourceRG.getCheckedRadioButtonId()==R.id.jrnlBankRB)
                                {
                                    source="Bank";
                                }
                                else
                                {
                                    source="Cash";
                                }
                                tempDate=dateIn.getHint().toString().split("/");
                                String updatedAmount=amountIn.getText().toString();
                                if(inEx.equals("e") )
                                    updatedAmount="-"+updatedAmount;
                                String updatedCategory=catIn.getSelectedItem().toString();

                                String updatedDescription=descIn.getText().toString().trim();

                               database.updateTxn((""+idList.get(position)),updatedAmount,source,updatedCategory,updatedDescription,tempDate[0],tempDate[1],tempDate[2]);
                               Toast.makeText(parent.getContext(),"Successfully Updated",Toast.LENGTH_SHORT).show();
                                Cursor c=database.executeQuery("Select * from TRANSACTIONS ORDER BY ((YEAR*10000)+(MONTH*100)+DAY)");
                               ListView lvParent=(ListView)parent.findViewById(R.id.recyclerVi);
                                lvParent.setAdapter(new ListviewAdapter(parent.getContext(),c));

                            }
                        })


                        .onNeutral(new MaterialDialog.SingleButtonCallback(){
                            //Delete entry.
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                final AlertDialog.Builder alertDialog=new AlertDialog.Builder(parent.getContext());
                                alertDialog.setTitle("Delete");
                                alertDialog.setMessage("Are you sure you want to delete this entry?");
                                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                       database.deleteTxn(idList.get(position).toString());
                                        Cursor c=database.executeQuery("Select * from TRANSACTIONS ORDER BY ((YEAR*10000)+(MONTH*100)+DAY)");
                                        setJournalValues(c);
                                        ListView lvParent=(ListView)parent.findViewById(R.id.recyclerVi);
                                        lvParent.setAdapter(new ListviewAdapter(parent.getContext(),c));
                                    }
                                });
                                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                alertDialog.show();
                            }
                        })
                        .build();
                dialogEditDelete.show();


            }
        });

        // bind data from selected element to view through view holder
        holder.amtOut.setText("₹"+amount[position]);
        holder.amtTx2.setText("₹"+amount[position]);
        holder.jrnlCatIndTx.setText("Recieved as");
        holder.jrnlSrcIndTx.setText("Credited to");

            if(Integer.parseInt(amount[position])<0)
            {

                holder.amtOut.setText("₹"+(-1*(Integer.parseInt(amount[position]))));
                holder.amtTx2.setText(holder.amtOut.getText().toString());
                holder.jrnlSrcTx.setBackgroundColor(Color.parseColor("#F9966B"));
                holder.jrnlCatTx.setBackgroundColor(Color.parseColor("#F9966B"));
                holder.jrnlRelLayout.setBackgroundColor(Color.parseColor("#2CF08E"));
                holder.jrnlCatIndTx.setText("Spent on");
                holder.jrnlSrcIndTx.setText("Debited from");


            }



      //--DO NOT DELETE-----------------------------------------------------------------------
        //
                //final List<String> inexList=new ArrayList<String>();
               // inexList.add("Expenditure/Income");
              /*  EditText amountIn=(EditText)diagView.findViewById(R.id.diagAmtIn);
                EditText descriptionIn=(EditText)diagView.findViewById(R.id.diagDescIn) ;
                String source="cash", inEx="e";
                Spinner catIn=(Spinner)diagView.findViewById(R.id.diagCatSpinner);
                RadioGroup inExRG, sourceRG;
                inExRG=(RadioGroup)diagView.findViewById(R.id.jrnlDiagInExRG);
                sourceRG=(RadioGroup)diagView.findViewById(R.id.jrnlDiagSourceRG);
                if(inExRG.getCheckedRadioButtonId()==R.id.jrnlInRB)
                {
                    inEx="i";
                }
                if(sourceRG.getCheckedRadioButtonId()==R.id.jrnlBankRB)
                {
                    source="bank";
                }

                String updatedAmount=amountIn.getText().toString();
                String updatedCategory=catIn.getSelectedItem().toString();
                String updatedDescription=descriptionIn.getText().toString();*/
        //---------------------------------------------------------------------------------------


        holder.fc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.fc.toggle(false);

            }
        });
        holder.jrnlInDateTx.setText(date[position]);
        holder.dateOutFront.setText(date[position]);
        holder.typeOut.setText(type[position]);
        holder.jrnlCatTx.setText(type[position]);
        holder.sourceOut.setText(source[position]);
        holder.jrnlSrcTx.setText(source[position]);
        if(!desc[position].isEmpty())
        {
            holder.descOut.setText(desc[position]);
            holder.descOut.setTypeface(holder.descOut.getTypeface(), Typeface.NORMAL);
            holder.descOut.setText(desc[position]);

        }
        else
        {
            holder.descOut.setTypeface(holder.descOut.getTypeface(), Typeface.ITALIC);
            holder.descOut.setTypeface(holder.descOut.getTypeface(), Typeface.NORMAL);
            holder.descOut.setHint("No description");
        }

        return rljrnl;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public int getItemCount() {
        return amount.length;
    }


    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }



    private static class ViewHolder {
        TextView amtOut, dateOut, typeOut, sourceOut,descOut,amtOut2, dateOutFront, jrnlSrcTx,jrnlCatTx,amtTx2,jrnlInDateTx,jrnlSrcIndTx,jrnlCatIndTx;
        RelativeLayout jrnlRelLayout,activityMain;
        FoldingCell fc;
        Button jrnlEditDelete;

    }

}
