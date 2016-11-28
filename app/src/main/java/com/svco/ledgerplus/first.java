package com.svco.ledgerplus;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class first extends Fragment {

    public myListAdapter adapter;

    public List<Integer> ids=new ArrayList<>();
    public List<String> cats = new ArrayList<>();
    LedgerDBManager myDb;
    public first() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_first, container, false);

        myDb= new LedgerDBManager(container.getContext());
        Cursor cursor = myDb.getExCategory();

        ListView myList =(ListView)view.findViewById(R.id.listView1);

        while (cursor.moveToNext()){
            cats.add(cursor.getString(1));
            ids.add(Integer.parseInt(cursor.getString(0)));
        }
        adapter= new myListAdapter(getActivity(),R.layout.row_layout,ids,cats);
        myList.setAdapter(adapter);
        return view;
    }

}
