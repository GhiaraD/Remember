package com.ghiarad.dragos.myapplication.Galeria;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyCoolScreen extends LinearLayout {

    public MyCoolScreen(Context context) {
        super(context);
    }

    public MyCoolScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCoolScreen(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public EditText init(int coordX, int coordY) {
        EditText mEditTexts=createScreen(coordX,coordY);
        return mEditTexts;
    }

    private EditText createScreen(int coordX, int coordY) {
        ViewGroup newScreen=this;
        EditText mEditTexts=new EditText(getContext());
        mEditTexts.setHint("Scrie numele");
        mEditTexts.setTypeface(null, Typeface.BOLD);
        mEditTexts.setX(coordX);
        mEditTexts.setY(coordY);
        mEditTexts.setTextSize(18);
        mEditTexts.setTextColor(Color.BLACK);
        newScreen.addView(mEditTexts, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return mEditTexts;
    }

    public TextView initTextView(int coordX, int coordY,String nume) {
        TextView mTextView = createScreenTextView(coordX,coordY,nume);
        return mTextView;
    }

    private TextView createScreenTextView(int coordX, int coordY, String nume) {
        ViewGroup newScreen=this;
        TextView mTextViews=new TextView(getContext());
        mTextViews.setText(nume);
        mTextViews.setTypeface(null, Typeface.BOLD);
        mTextViews.setX(coordX);
        mTextViews.setY(coordY);
        mTextViews.setTextSize(24);
        mTextViews.setTextColor(Color.BLACK);
        mTextViews.setPadding(18,12,18,12);
        mTextViews.setBackgroundColor(Color.WHITE);
        newScreen.addView(mTextViews, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return mTextViews;
    }

}