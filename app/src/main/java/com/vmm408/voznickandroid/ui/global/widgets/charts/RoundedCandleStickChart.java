package com.vmm408.voznickandroid.ui.global.widgets.charts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.interfaces.dataprovider.CandleDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.renderer.CandleStickChartRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.vmm408.voznickandroid.R;

public class RoundedCandleStickChart extends CandleStickChart {
    public RoundedCandleStickChart(Context context) {
        super(context);
    }

    public RoundedCandleStickChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        readRadiusAttr(context, attrs);
    }

    public RoundedCandleStickChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        readRadiusAttr(context, attrs);
    }

    private void readRadiusAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundedBarChart, 0, 0);
        try {
            setRadius(a.getDimensionPixelSize(R.styleable.RoundedBarChart_radius, 0));
        } finally {
            a.recycle();
        }
    }

    public void setRadius(int radius) {
        setRenderer(new RoundedCandleStickChartRenderer(this, getAnimator(), getViewPortHandler(), radius));
    }

    private class RoundedCandleStickChartRenderer extends CandleStickChartRenderer {

        private float[] mShadowBuffers = new float[8];
        private float[] mBodyBuffers = new float[4];
        private float[] mRangeBuffers = new float[4];
        private float[] mOpenBuffers = new float[4];
        private float[] mCloseBuffers = new float[4];

        private int mRadius;

        RoundedCandleStickChartRenderer(CandleDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler, int mRadius) {
            super(chart, animator, viewPortHandler);
            this.mRadius = mRadius;
        }

        @Override
        protected void drawDataSet(Canvas c, ICandleDataSet dataSet) {

            Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

            float phaseY = mAnimator.getPhaseY();
            float barSpace = dataSet.getBarSpace();
            boolean showCandleBar = dataSet.getShowCandleBar();

            mXBounds.set(mChart, dataSet);

            mRenderPaint.setStrokeWidth(dataSet.getShadowWidth());

            // draw the body
            for (int j = mXBounds.min; j <= mXBounds.range + mXBounds.min; j++) {

                // get the entry
                CandleEntry e = dataSet.getEntryForIndex(j);

                if (e == null)
                    continue;

                final float xPos = e.getX();

                final float open = e.getOpen();
                final float close = e.getClose();
                final float high = e.getHigh();
                final float low = e.getLow();

                if (showCandleBar) {
                    // calculate the shadow

                    mShadowBuffers[0] = xPos;
                    mShadowBuffers[2] = xPos;
                    mShadowBuffers[4] = xPos;
                    mShadowBuffers[6] = xPos;

                    if (open > close) {
                        mShadowBuffers[1] = high * phaseY;
                        mShadowBuffers[3] = open * phaseY;
                        mShadowBuffers[5] = low * phaseY;
                        mShadowBuffers[7] = close * phaseY;
                    } else if (open < close) {
                        mShadowBuffers[1] = high * phaseY;
                        mShadowBuffers[3] = close * phaseY;
                        mShadowBuffers[5] = low * phaseY;
                        mShadowBuffers[7] = open * phaseY;
                    } else {
                        mShadowBuffers[1] = high * phaseY;
                        mShadowBuffers[3] = open * phaseY;
                        mShadowBuffers[5] = low * phaseY;
                        mShadowBuffers[7] = mShadowBuffers[3];
                    }

                    trans.pointValuesToPixel(mShadowBuffers);

                    // draw the shadows

                    if (dataSet.getShadowColorSameAsCandle()) {

                        if (open > close)
                            mRenderPaint.setColor(
                                    dataSet.getDecreasingColor() == ColorTemplate.COLOR_NONE ?
                                            dataSet.getColor(j) :
                                            dataSet.getDecreasingColor()
                            );

                        else if (open < close)
                            mRenderPaint.setColor(
                                    dataSet.getIncreasingColor() == ColorTemplate.COLOR_NONE ?
                                            dataSet.getColor(j) :
                                            dataSet.getIncreasingColor()
                            );

                        else
                            mRenderPaint.setColor(
                                    dataSet.getNeutralColor() == ColorTemplate.COLOR_NONE ?
                                            dataSet.getColor(j) :
                                            dataSet.getNeutralColor()
                            );

                    } else {
                        mRenderPaint.setColor(
                                dataSet.getShadowColor() == ColorTemplate.COLOR_NONE ?
                                        dataSet.getColor(j) :
                                        dataSet.getShadowColor()
                        );
                    }

                    mRenderPaint.setStyle(Paint.Style.STROKE);

                    c.drawLines(mShadowBuffers, mRenderPaint);

                    // calculate the body

                    mBodyBuffers[0] = xPos - 0.5f + barSpace;
                    mBodyBuffers[1] = close * phaseY;
                    mBodyBuffers[2] = (xPos + 0.5f - barSpace);
                    mBodyBuffers[3] = open * phaseY;

                    trans.pointValuesToPixel(mBodyBuffers);

                    // draw body differently for increasing and decreasing entry
                    if (open > close) { // decreasing

                        if (dataSet.getDecreasingColor() == ColorTemplate.COLOR_NONE) {
                            mRenderPaint.setColor(dataSet.getColor(j));
                        } else {
                            mRenderPaint.setColor(dataSet.getDecreasingColor());
                        }

                        mRenderPaint.setStyle(dataSet.getDecreasingPaintStyle());

                        c.drawRoundRect(
                                mBodyBuffers[0], mBodyBuffers[3],
                                mBodyBuffers[2], mBodyBuffers[1],
                                mRadius, mRadius,
                                mRenderPaint);

                    } else if (open < close) {

                        if (dataSet.getIncreasingColor() == ColorTemplate.COLOR_NONE) {
                            mRenderPaint.setColor(dataSet.getColor(j));
                        } else {
                            mRenderPaint.setColor(dataSet.getIncreasingColor());
                        }

                        mRenderPaint.setStyle(dataSet.getIncreasingPaintStyle());

                        c.drawRoundRect(
                                mBodyBuffers[0], mBodyBuffers[1],
                                mBodyBuffers[2], mBodyBuffers[3],
                                mRadius, mRadius,
                                mRenderPaint);
                    } else { // equal values

                        if (dataSet.getNeutralColor() == ColorTemplate.COLOR_NONE) {
                            mRenderPaint.setColor(dataSet.getColor(j));
                        } else {
                            mRenderPaint.setColor(dataSet.getNeutralColor());
                        }

                        c.drawLine(
                                mBodyBuffers[0], mBodyBuffers[1],
                                mBodyBuffers[2], mBodyBuffers[3],
                                mRenderPaint);
                    }
                } else {

                    mRangeBuffers[0] = xPos;
                    mRangeBuffers[1] = high * phaseY;
                    mRangeBuffers[2] = xPos;
                    mRangeBuffers[3] = low * phaseY;

                    mOpenBuffers[0] = xPos - 0.5f + barSpace;
                    mOpenBuffers[1] = open * phaseY;
                    mOpenBuffers[2] = xPos;
                    mOpenBuffers[3] = open * phaseY;

                    mCloseBuffers[0] = xPos + 0.5f - barSpace;
                    mCloseBuffers[1] = close * phaseY;
                    mCloseBuffers[2] = xPos;
                    mCloseBuffers[3] = close * phaseY;

                    trans.pointValuesToPixel(mRangeBuffers);
                    trans.pointValuesToPixel(mOpenBuffers);
                    trans.pointValuesToPixel(mCloseBuffers);

                    // draw the ranges
                    int barColor;

                    if (open > close)
                        barColor = dataSet.getDecreasingColor() == ColorTemplate.COLOR_NONE
                                ? dataSet.getColor(j)
                                : dataSet.getDecreasingColor();
                    else if (open < close)
                        barColor = dataSet.getIncreasingColor() == ColorTemplate.COLOR_NONE
                                ? dataSet.getColor(j)
                                : dataSet.getIncreasingColor();
                    else
                        barColor = dataSet.getNeutralColor() == ColorTemplate.COLOR_NONE
                                ? dataSet.getColor(j)
                                : dataSet.getNeutralColor();

                    mRenderPaint.setColor(barColor);
                    c.drawLine(
                            mRangeBuffers[0], mRangeBuffers[1],
                            mRangeBuffers[2], mRangeBuffers[3],
                            mRenderPaint);
                    c.drawLine(
                            mOpenBuffers[0], mOpenBuffers[1],
                            mOpenBuffers[2], mOpenBuffers[3],
                            mRenderPaint);
                    c.drawLine(
                            mCloseBuffers[0], mCloseBuffers[1],
                            mCloseBuffers[2], mCloseBuffers[3],
                            mRenderPaint);
                }
            }
        }
    }
}
