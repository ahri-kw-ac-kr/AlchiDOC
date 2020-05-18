package com.example.jms.settings;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jms.R;
import com.example.jms.connection.model.RestfulAPI;
import com.example.jms.connection.viewmodel.APIViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class CareListSub extends LinearLayout {
    APIViewModel apiViewModel;
    View view;
    LinearLayout linearLayout;
    TextView textView;
    ImageButton imageButton;

    public CareListSub(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        init(context, 0);
    }

    public CareListSub(Context context, int i){
        super(context);
        init(context, i);
    }

    private void init(Context context,int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.care_person, this, true);
        linearLayout = (LinearLayout) view.findViewById(R.id.careId);
        textView = (TextView) view.findViewById(R.id.careText);
        imageButton = (ImageButton) view.findViewById(R.id.careDel);
    }
}
