package net.lzzy.practicesonline.activities.activities.activities.constant.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.activities.activities.constant.fragments.BaseFragment;
import net.lzzy.practicesonline.activities.activities.activities.constant.models.UserCookies;
import net.lzzy.practicesonline.activities.activities.activities.constant.models.view.QuestionResult;
import net.lzzy.practicesonline.activities.activities.activities.constant.models.UserCookies;
import net.lzzy.practicesonline.activities.activities.activities.constant.models.view.QuestionResult;
import net.lzzy.practicesonline.activities.activities.activities.constant.models.view.WrongType;
import net.lzzy.practicesonline.activities.activities.activities.constant.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;

/**
 * @author lzzy_gxy
 * @date 2019/5/13
 * Description:
 */
public class ChartFragment extends BaseFragment {
    public static final String GRID_RESULTS = "results";
    private TextView tvChart;
    private GetGridFragmentListener fragmentListener;
    List<QuestionResult> results;
    private PieChart pieChart;
    private LineChart lineChart;
    private BarChart barChart;
    private Chart[] charts;
    private String[] titles = new String[]{"正确错误比例（单位%）", "题目阅读量统计", "题目错误类型统计"};
    private float rightCount = 0;
    private View[] dots;
    private float touchX1;
    private static final int MIN_DISTANCE = 100;
    private int chartIndex = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            results = getArguments().getParcelableArrayList(GRID_RESULTS);
        }

        for (QuestionResult result : results) {
            if (result.isRight()) {
                rightCount++;
            }
        }
    }

    private void displayBarChart() {
        ValueFormatter xformatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return WrongType.getInstance((int) value).toString();
            }
        };
        barChart.getXAxis().setValueFormatter(xformatter);
        int ok = 0, miss = 0, ectra = 0, wrong = 0;
        for (QuestionResult result : results) {
            switch (result.getType()) {
                case RIGHT_OPTIONS:
                    ok++;
                    break;
                case MISS_OPTIONS:
                    miss++;
                    break;
                case EXTRA_OPTIONS:
                    ectra++;
                    break;
                case WRONG_OPTIONS:
                    wrong++;
                    break;
                default:
                    break;
            }
        }
        List<BarEntry> entries=new ArrayList<>();
        entries.add(new BarEntry(0,ok));
        entries.add(new BarEntry(1,miss));
        entries.add(new BarEntry(2,ectra));
        entries.add(new BarEntry(3,wrong));
        BarDataSet dataSet=new BarDataSet(entries,"");
        dataSet.setColors(Color.GREEN,Color.BLUE,Color.GRAY,Color.RED);
        ArrayList<IBarDataSet> dataSets=new ArrayList<>();
        dataSets.add(dataSet);
        BarData data=new BarData(dataSets);
        data.setBarWidth(1f);
        barChart.setData(data);
        barChart.invalidate();
    }

    private void displayLineChart() {
        List<Entry> values = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            int val = UserCookies.getInstance().getReadCount(results.get(i).getQuestionId().toString());
            values.add(new Entry(i, val));
        }
        LineDataSet set;
        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
            set = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            set.setValues(values);
            set.notifyDataSetChanged();
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            set = new LineDataSet(values, "DataSet 1");

            set.setDrawIcons(false);

            // draw dashed line
            set.enableDashedLine(0f, 0f, 0f);

            // black lines and points
            set.setColor(Color.RED);
            set.setCircleColor(Color.BLACK);

            // line thickness and point size
            set.setLineWidth(3f);
            set.setCircleRadius(5f);
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();

            dataSets.add(set);

            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            // set data
            lineChart.setData(data);
        }
    }

    private void configBarLineChart(BarLineChartBase chart) {
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(8f);
        //间距
        xAxis.setGranularity(1f);

        YAxis yAxis = chart.getAxisLeft();
        //数量
        yAxis.setLabelCount(8, false);
        //显示在外边
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setTextSize(8f);
        //间距
        yAxis.setGranularity(1f);
        //最小值
        yAxis.setMinWidth(0);
        //图例
        chart.getLegend().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.setPinchZoom(false);

    }

    private void displayPieChart() {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(rightCount, "正确"));
        entries.add(new PieEntry(results.size() - rightCount, "错误"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        dataSet.setColors(Color.GREEN, Color.RED);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        pieChart.setData(data);
        pieChart.invalidate();
    }

    private void configPieChart() {
        //百分比显示
        pieChart.setUsePercentValues(true);
        //使饼图是否为环形
        pieChart.setDrawHoleEnabled(false);
        //图例
        //排序方向
        pieChart.getLegend().setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //上下方向
        pieChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        //左右方向
        pieChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
    }

    private void initCharts() {
        pieChart = findViewById(R.id.fragment_chart_pie);
        lineChart = findViewById(R.id.fragment_chart_line);
        barChart = findViewById(R.id.fragment_chart_bar);
        charts = new Chart[]{pieChart, lineChart, barChart};
        int i = 0;
        for (Chart chart : charts) {
            //设置触摸，false禁止触摸，true可以触摸
            chart.setTouchEnabled(false);
            chart.setVisibility(View.GONE);
            Description desc = new Description();
            desc.setText(titles[i++]);
            chart.setDescription(desc);
            chart.setNoDataText("数据获取中...");
            chart.setExtraOffsets(5, 10, 5, 25);

        }
    }

    @Override
    protected void Populate() {
        tvChart = findViewById(R.id.fragment_chart_tv);
        tvChart.setOnClickListener(v -> {
            if (fragmentListener != null) {
                fragmentListener.getGridFragment();
            }
        });
        initCharts();
        configPieChart();
        displayPieChart();
        pieChart.setVisibility(View.VISIBLE);
        View dot1 = findViewById(R.id.fragment_chart_dot1);
        View dot2 = findViewById(R.id.fragment_chart_dot2);
        View dot3 = findViewById(R.id.fragment_chart_dot3);
        dots = new View[]{dot1, dot2, dot3};
        findViewById(R.id.fragment_chart_container).setOnTouchListener(new ViewUtils.AbstractTouchListener() {
            @Override
            public boolean handleTouch(MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    touchX1 = event.getX();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float touchX2 = event.getX();
                    if (Math.abs(touchX2 - touchX1) > MIN_DISTANCE) {
                        if (touchX2 < touchX1) {
                            if (chartIndex < charts.length - 1) {
                                chartIndex++;
                            } else {
                                chartIndex = 0;
                            }
                        } else {
                            if (chartIndex > 0) {
                                chartIndex--;
                            } else {
                                chartIndex = charts.length - 1;
                            }
                        }
                        switchChart();
                    }
                }
                return true;
            }

            private void switchChart() {
                for (int i = 0; i < charts.length; i++) {
                    if (chartIndex == i) {
                        charts[i].setVisibility(View.VISIBLE);
                        dots[i].setBackgroundResource(R.drawable.dot_fill_style);
                    } else {
                        charts[i].setVisibility(View.GONE);
                        dots[i].setBackgroundResource(R.drawable.dot_style);
                    }
                }
            }
        });
        configBarLineChart(barChart);
        displayLineChart();
        displayBarChart();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_chart;
    }

    @Override
    public void sarch(String kw) {

    }

    /**
     * 静态工厂传参数
     */
    public static ChartFragment newInstance(List<QuestionResult> results) {
        ChartFragment fragment = new ChartFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(GRID_RESULTS, (ArrayList<? extends Parcelable>) results);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragmentListener = (GetGridFragmentListener) context;
        } catch (CancellationException e) {
            throw new CancellationException(context.toString() + "实现接口GetGridFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }

    public interface GetGridFragmentListener {
        /**
         * 跳转到GridFragment
         */
        void getGridFragment();
    }

}
