package com.learning.dino.draganddraw;

import android.graphics.PointF;

/**
 * Created by dbulj on 16/11/2014.
 */
public class Box {

    private PointF mLt;
    private PointF mRt;
    private PointF mLb;
    private PointF mRb;

    public PointF getLt() {
        return mLt;
    }

    public void setLt(PointF mLt) {
        this.mLt = mLt;
    }

    public PointF getRt() {
        return mRt;
    }

    public void setRt(PointF mRt) {
        this.mRt = mRt;
    }

    public PointF getLb() {
        return mLb;
    }

    public void setLb(PointF mLb) {
        this.mLb = mLb;
    }

    public PointF getRb() {
        return mRb;
    }

    public void setRb(PointF mRb) {
        this.mRb = mRb;
    }
}
