package com.example.taeik.android_socketclient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;

import java.util.ArrayList;

public class dayTimeDataAnalysis extends Activity {
    private ArrayList<DayTimeDataType> timedataList = new ArrayList<DayTimeDataType>();
    CombinedChart mChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_day_time_data_analysis);


        Intent intent = getIntent();
        timedataList = (ArrayList<DayTimeDataType>)intent.getSerializableExtra("data");

        for(DayTimeDataType n:timedataList){
            Log.d("checkData",n.getDate()+"아침 :"+n.getMornigTimeCount()+"점심 :"+n.getLunchTimeCount());
        }

        mChart = (CombinedChart) findViewById(R.id.chart2S);
        //mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);

        // draw bars behind lines
/*
        Legend l = mChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
*/
  /*
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        //  rightAxis.setAxisMinimum(0f);f // this replaces setStartAtZero(true)

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        // leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)*/
        CombinedData data = new CombinedData();

        data.setData(generateBarData());

        mChart.setData(data);
        mChart.invalidate();
        mChart.setX(0.0f);
    }
    private BarData generateBarData() {

        ArrayList<BarEntry> entries1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> entries2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> entries3 = new ArrayList<BarEntry>();
        int cnt=0;
        for (DayTimeDataType n:timedataList) {
            entries1.add(new BarEntry(cnt, n.getMornigTimeCount(),"1"));
            entries2.add(new BarEntry(cnt, n.getLunchTimeCount(),"1"));
            entries3.add(new BarEntry(cnt, n.getDinnerTimeCount(),"1"));
            cnt++;
        }

        BarDataSet set1 = new BarDataSet(entries1, "Bar 1");

        set1.setValueTextColor(Color.rgb(60, 220, 78));
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        BarDataSet set2 = new BarDataSet(entries2, "");
        set2.setStackLabels(new String[]{"Stack 1", "Stack 2"});
        set2.setColor(Color.rgb(051, 153, 204));
        set2.setValueTextColor(Color.rgb(61, 165, 255));
        set2.setValueTextSize(10f);
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);

        BarDataSet set3 = new BarDataSet(entries3, "");
        set3.setStackLabels(new String[]{"Stack 1", "Stack 2"});
        set3.setColor(Color.rgb(102, 204, 255));
        set3.setValueTextColor(Color.rgb(200, 165, 255));
        set3.setValueTextSize(10f);
        set3.setAxisDependency(YAxis.AxisDependency.LEFT);

        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset
        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"

        BarData d = new BarData(set1, set2, set3);
        d.setBarWidth(barWidth);

        // make this BarData object grouped
        d.groupBars(0, groupSpace, barSpace); // start at x = 0

        return d;
    }

    @Override
    protected void onDestroy() {
        Log.d("ㅎㅎㅎㅎ", "onDestroy호출");
        super.onDestroy();
        //finish();
    }
}
