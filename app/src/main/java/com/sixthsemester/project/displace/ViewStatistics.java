package com.sixthsemester.project.displace;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

public class ViewStatistics extends Activity {

    private static int[] COLORS = new int[] {Color.rgb(255,0,66),Color.rgb(230,223,68),Color.rgb(11, 183, 185),Color.rgb(227,130,15) };

    private static int[] VALUES = new int[] { 20, 2, 50, 10 };

    private static String[] NAME_LIST = new String[] { "Roman Tikka", "Shahlik", "Chiken karai", "Greem karai" };

    private CategorySeries mSeries = new CategorySeries("");

    private DefaultRenderer mRenderer = new DefaultRenderer();

    private GraphicalView mChartView;
    private TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewstatistics);

        mRenderer.setChartTitleTextSize(20);
        mRenderer.setLabelsTextSize(40);
        mRenderer.setLegendTextSize(0);
        mRenderer.setMargins(new int[]{20, 30, 15, 0});
        //mRenderer.setZoomButtonsVisible(true);
       //mRenderer.setStartAngle(60);

        t=new TextView(this);

            t = (TextView) findViewById(R.id.textView1);
                t.setText(NAME_LIST[ 0] + "\n" + VALUES[0]+"\n");
                t.setTextColor(COLORS[0]);
            t = (TextView) findViewById(R.id.textView2);
                t.setText(NAME_LIST[ 1] + "\n" + VALUES[1]+"\n");
                t.setTextColor(COLORS[1]);
            t = (TextView) findViewById(R.id.textView4);
                t.setText(NAME_LIST[ 2] + "\n" + VALUES[2]+"\n");
                t.setTextColor(COLORS[2]);
            t = (TextView) findViewById(R.id.textView5);
                t.setText(NAME_LIST[ 3] + "\n" + VALUES[3]+"\n");
                t.setTextColor(COLORS[3]);


        for (int i = 0; i < VALUES.length; i++) {
            mSeries.add( VALUES[i]);
            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
            renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
            mRenderer.addSeriesRenderer(renderer);
        }

        if (mChartView != null) {
           mChartView.repaint();
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mChartView == null) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
            mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);
            mRenderer.setPanEnabled(false);
            mRenderer.setZoomEnabled(false);


            mChartView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();

                    if (seriesSelection == null) {
                       // Toast.makeText(ViewStatistics.this, "No chart element was clicked", Toast.LENGTH_SHORT).show();
                    } else {
                       // Toast.makeText(ViewStatistics.this, "Chart element data point index " + (seriesSelection.getPointIndex() + 1) + " was clicked" + " point value=" + seriesSelection.getValue(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            mChartView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
                    if (seriesSelection == null) {
                       // Toast.makeText(ViewStatistics.this, "No chart element was long pressed", Toast.LENGTH_SHORT).show();

                        return false;
                    } else {
                       // Toast.makeText(ViewStatistics.this, "Chart element data point index " + seriesSelection.getPointIndex() + " was long pressed", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
            });
            layout.addView(mChartView, new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
        }
        else {
            mChartView.repaint();
        }
    }

}
