package com.learning.dino.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by dbulj on 16/11/2014.
 */
public class BoxDrawingView extends View{
    public static final String TAG = "BoxDrawingView";
    public static final String PARCELABLE_TAG = "parcelable"; //db*
    public static final String SERIALIZABLE_TAG = "serializable"; //db*

    private Box mCurrentBox;
    private ArrayList<Box> mBoxes = new ArrayList<Box>();
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;
    private PointF centerPoint;
    private float l;
    private float r;
    private float t;
    private float b;
    private double origAngle1;
    private double origAngle2;
    private PointF curr1 = new PointF();
    private PointF orig1 = new PointF();

    //Note!  Our view could be instantiated in code or from a layout file.  Views instantiated
    //from a layout file receive an instance of AttributeSet containing XML attributes that
    //were specified in XML.

    //Used when crating the view in code
    public BoxDrawingView(Context context){
        super(context, null);
    }

    //Used when inflating the view from XML
    public BoxDrawingView(Context context, AttributeSet attrs){
        super(context, attrs);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);
        mBoxPaint.setStyle(Paint.Style.FILL);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);
    }

    //db*
    @Override
    protected Parcelable onSaveInstanceState(){
        Parcelable savedState = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARCELABLE_TAG, savedState);
        bundle.putSerializable(SERIALIZABLE_TAG, mBoxes);
        return bundle;
    }

    //db*
    @Override
    protected void onRestoreInstanceState(Parcelable state){
        Parcelable savedState = ((Bundle)state).getParcelable(PARCELABLE_TAG);
        super.onRestoreInstanceState(savedState);

        mBoxes = (ArrayList<Box>)((Bundle)state).getSerializable(SERIALIZABLE_TAG);
        invalidate();
    }

    @Override
    //Event for handling touch motion events
    public boolean onTouchEvent(MotionEvent event){
        curr1.set(event.getX(), event.getY());
        int pointerCount = event.getPointerCount();

        switch(event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                orig1.set(curr1.x, curr1.y);
                mCurrentBox = new Box();
                mBoxes.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (mCurrentBox != null && pointerCount == 2){
                    centerPoint = getCenterPoint(l, t, r, b);
                    origAngle1 = getAngle(centerPoint, event.getX(), event.getY());
                    origAngle2 = getAngle(centerPoint, event.getX(1), event.getY(1));
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mCurrentBox != null){
                    if (pointerCount > 1){
                        double currAngle1 = getAngle(centerPoint, event.getX(0), event.getY(0)) - origAngle1;
                        double currAngle2 = getAngle(centerPoint, event.getX(1), event.getY(1)) - origAngle2;
                        double currAngle = currAngle1 + currAngle2;

                        mCurrentBox.setLt(rotate(l, t, centerPoint.x, centerPoint.y, currAngle));
                        mCurrentBox.setRt(rotate(r, t, centerPoint.x, centerPoint.y, currAngle));
                        mCurrentBox.setRb(rotate(r, b, centerPoint.x, centerPoint.y, currAngle));
                        mCurrentBox.setLb(rotate(l, b, centerPoint.x, centerPoint.y, currAngle));
                    }else{
                        l = Math.min(orig1.x, curr1.x);
                        r = Math.max(orig1.x, curr1.x);
                        t = Math.min(orig1.y, curr1.y);
                        b = Math.max(orig1.y, curr1.y);

                        mCurrentBox.setLt(new PointF(l, t));
                        mCurrentBox.setRt(new PointF(r, t));
                        mCurrentBox.setRb(new PointF(r, b));
                        mCurrentBox.setLb(new PointF(l, b));
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                mCurrentBox = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                mCurrentBox = null;
                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas){
        //fill background
        canvas.drawPaint(mBackgroundPaint);

        for (Box box : mBoxes){
            Path p = new Path();
            p.moveTo(box.getLt().x, box.getLt().y);
            p.lineTo(box.getRt().x, box.getRt().y);
            p.lineTo(box.getRb().x, box.getRb().y);
            p.lineTo(box.getLb().x, box.getLb().y);
            p.close();
            canvas.drawPath(p, mBoxPaint);
        }
    }

    PointF getCenterPoint(float l, float t, float b, float r){
        return  new PointF((l+r)/2, (t+b)/2);
    }

    double getAngle(PointF c, float x, float y){
        return Math.atan2(x-c.x, c.y-y);
    }

    PointF rotate(double x, double y, double cx, double cy, double fi){
        double tx;
        x = x - cx;
        y = y - cy;
        tx = x;
        x = tx * Math.cos(fi) - y * Math.sin(fi);
        y = tx * Math.sin(fi) - y * Math.cos(fi);
        x = x + cx;
        y = y + cy;

        return new PointF((float)x, (float)y);
    }
}
