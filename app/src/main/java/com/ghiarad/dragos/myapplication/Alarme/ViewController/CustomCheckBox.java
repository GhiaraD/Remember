package com.ghiarad.dragos.myapplication.Alarme.ViewController;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.ghiarad.dragos.myapplication.R;

public class CustomCheckBox extends androidx.appcompat.widget.AppCompatCheckBox {

    public CustomCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setChecked(boolean t){
        if(t) {
            this.setBackgroundResource(R.drawable.checkbox_background);
            this.setTextColor(Color.WHITE);
        } else {
            this.setBackgroundColor(Color.TRANSPARENT);
            this.setTextColor(Color.BLACK);
        }
        super.setChecked(t);
    }
}