package com.example.jms.settings;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jms.R;

public class DeviceSet3 extends LinearLayout {

    View view;
    TextView textView;
    Button button;

    public DeviceSet3() {super(null);}

    public DeviceSet3(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        init(context);
    }

    public DeviceSet3(Context context){
        super(context);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.device3, this, true);
        textView = (TextView)view.findViewById(R.id.deviceBattery);
        button = (Button)view.findViewById(R.id.deviceFinish);
    }

}