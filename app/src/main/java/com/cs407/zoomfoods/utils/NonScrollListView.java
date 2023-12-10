package com.cs407.zoomfoods.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.concurrent.atomic.AtomicReference;

public class NonScrollListView extends ListView {
    public NonScrollListView(Context context){super(context);}
    public NonScrollListView(Context context, AttributeSet attrs){
        super(context, attrs);
    }
    public NonScrollListView(Context context, AttributeSet attrs, int defStyle){ super(context, attrs, defStyle); }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        // Create a measure spec that tells that tells the ListView to be as tall as possible
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight(); // Set the height of the ListView to the measured

    }
}

