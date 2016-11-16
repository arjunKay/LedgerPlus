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
public class second extends Fragment {

    LedgerDBManager myDb;
    public second() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        myDb = new LedgerDBManager(container.getContext());
        Cursor cursor = myDb.getInCategory();

        ListView myList = (ListView) view.findViewById(R.id.listView2);

        int[] ids = new int[cursor.getCount()];
        int j = 0;
        List<String> cats = new ArrayList<>();
        while (cursor.moveToNext()) {
            cats.add(cursor.getString(1));
            ids[j] = Integer.parseInt(cursor.getString(0));
            j++;

        }
        myListAdapter adapter = new myListAdapter(getActivity(), R.layout.row_layout, ids, cats);
        myList.setAdapter(adapter);


        return view;
    }
}
