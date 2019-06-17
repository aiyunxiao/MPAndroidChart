package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class XAxisRendererRadarChart extends XAxisRenderer {

    private RadarChart mChart;

    public XAxisRendererRadarChart(ViewPortHandler viewPortHandler, XAxis xAxis, RadarChart chart) {
        super(viewPortHandler, xAxis, null);

        mChart = chart;
    }

    @Override
    public void renderAxisLabels(Canvas c) {

        if (!mXAxis.isEnabled() || !mXAxis.isDrawLabelsEnabled())
            return;

        final float labelRotationAngleDegrees = mXAxis.getLabelRotationAngle();
        final MPPointF drawLabelAnchor = MPPointF.getInstance(0.5f, 0.25f);

        mAxisLabelPaint.setTypeface(mXAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mXAxis.getTextSize());
        mAxisLabelPaint.setColor(mXAxis.getTextColor());

        float sliceangle = mChart.getSliceAngle();

        // calculate the factor that is needed for transforming the value to
        // pixels
        float factor = mChart.getFactor();

        MPPointF center = mChart.getCenterOffsets();
        MPPointF pOut = MPPointF.getInstance(0, 0);
        for (int i = 0; i < mChart.getData().getMaxEntryCountSet().getEntryCount(); i++) {

            String label = mXAxis.getValueFormatter().getAxisLabel(i, mXAxis);

            float angle = (sliceangle * i + mChart.getRotationAngle()) % 360f;

            Utils.getPosition(center, mChart.getYRange() * factor
                    + mXAxis.mLabelRotatedWidth / 2f, angle, pOut);
            if (label.contains("\n")) {
                String[] texts = label.split("\n");
                Paint.FontMetrics mFontMetricsBuffer = new Paint.FontMetrics();
                final float lineHeight = mAxisLabelPaint.getFontMetrics(mFontMetricsBuffer);
                int lineCount = texts.length;
                for (int j = 0; j < lineCount; j++) {
                    //多行Y轴偏移计算
                    float offsetY = compuseMultiTextOffset(lineHeight, lineCount, j);
                    drawLabel(c, texts[j], pOut.x, pOut.y - mXAxis.mLabelRotatedHeight / 2.f + offsetY,
                            drawLabelAnchor, labelRotationAngleDegrees);
                }
            } else {
                drawLabel(c, label, pOut.x, pOut.y - mXAxis.mLabelRotatedHeight / 2.f,
                        drawLabelAnchor, labelRotationAngleDegrees);
            }

            drawLabel(c, label, pOut.x, pOut.y - mXAxis.mLabelRotatedHeight / 2.f,
                    drawLabelAnchor, labelRotationAngleDegrees);
        }

        MPPointF.recycleInstance(center);
        MPPointF.recycleInstance(pOut);
        MPPointF.recycleInstance(drawLabelAnchor);
    }

    private float compuseMultiTextOffset(float lineHeight, int lineCount, int lineIndex) {
        return lineHeight * (lineIndex - (lineCount - 1) / 2);
    }

    /**
     * XAxis LimitLines on RadarChart not yet supported.
     *
     * @param c
     */
    @Override
    public void renderLimitLines(Canvas c) {
        // this space intentionally left blank
    }
}
