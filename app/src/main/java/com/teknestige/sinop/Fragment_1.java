package com.teknestige.sinop;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Fragment_1 extends Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        view = inflater.inflate(R.layout.fragment_fragment_1, container, false);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setListView(ArrayList<String> answers) {
        ArrayAdapter<String> adapterComment;
        adapterComment = new ArrayAdapter<String>(getContext(), R.layout.answer_layout_item, R.id.textView111, answers);


        ListView listName = (ListView) view.findViewById(R.id.answersList);

        listName.setAdapter(adapterComment);
    }

}
