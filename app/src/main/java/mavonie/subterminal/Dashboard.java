package mavonie.subterminal;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.pixplicity.easyprefs.library.Prefs;
import com.tomer.fadingtextview.FadingTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mavonie.subterminal.Models.Exit;
import mavonie.subterminal.Models.Jump;
import mavonie.subterminal.Models.Model;
import mavonie.subterminal.Models.Skydive.Dropzone;
import mavonie.subterminal.Models.Skydive.Skydive;
import mavonie.subterminal.Models.Synchronizable;
import mavonie.subterminal.Utils.DB.Query;
import mavonie.subterminal.Utils.Subterminal;
import mavonie.subterminal.Utils.UIHelper;
import mavonie.subterminal.Utils.UnitConverter;


/**
 * Dashboard fragment
 */
public class Dashboard extends Fragment {

    private static final int ANIMATION_SPEED = 1000;

    PieChart mChart;
    BarChart favouriteExits;
    LineChart pullLineChart;

    TextView skydiveCount;
    TextView baseCount;
    TextView dropzoneCount;
    TextView exitsCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mChart = (PieChart) view.findViewById(R.id.dashboard_base_objects_pie);
        favouriteExits = (BarChart) view.findViewById(R.id.dashboard_base_fav_exits);
        pullLineChart = (LineChart) view.findViewById(R.id.dashboard_pull_height_linechart);

        skydiveCount = (TextView) view.findViewById(R.id.dashboard_skydive_count);
        baseCount = (TextView) view.findViewById(R.id.dashboard_base_count);
        dropzoneCount = (TextView) view.findViewById(R.id.dashboard_dropzone_count);
        exitsCount = (TextView) view.findViewById(R.id.dashboard_exits_count);

        clickableSummaryStats(view);

        TextView skydiveDelay = (TextView) view.findViewById(R.id.total_freefall_skydive);
        skydiveDelay.setText("Total Freefall: " + timeConversion(new Skydive().sum(Skydive.COLUMN_NAME_DELAY)));

        TextView baseDelay = (TextView) view.findViewById(R.id.total_freefall_base);
        baseDelay.setText("Total Freefall: " + timeConversion(new Jump().sum(Jump.COLUMN_NAME_DELAY)));

        FadingTextView premiumAdd = (FadingTextView) view.findViewById(R.id.premium_ad);
        if (Subterminal.getUser().isPremium()) {
            premiumAdd.setVisibility(View.GONE);
        } else {
            premiumAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.goToFragment(R.id.nav_premium);
                }
            });
        }

        HashMap<String, Object> whereNotGlobal = new HashMap<>();
        whereNotGlobal.put(Model.FILTER_WHERE_FIELD, Exit.COLUMN_NAME_GLOBAL_ID);
        whereNotGlobal.put(Model.FILTER_WHERE_VALUE, null);

        skydiveCount.setText(Integer.toString(Prefs.getInt(Preference.PREFS_SKYDIVE_START_COUNT, 0) + new Skydive().count(Synchronizable.getActiveParams())));
        baseCount.setText(Integer.toString(Prefs.getInt(Preference.PREFS_JUMP_START_COUNT, 0) + new Jump().count(Synchronizable.getActiveParams())));
        dropzoneCount.setText(Integer.toString(Dropzone.getDropzonesVisitedCount()));
        exitsCount.setText(Integer.toString(new Exit().count(whereNotGlobal)));

        setPieChartData();
        setBarChartData();
        setLineChartData();

        return view;
    }

    private void clickableSummaryStats(View view) {
        Map<CardView, Integer> clickSummary = new HashMap<CardView, Integer>();

        clickSummary.put((CardView) view.findViewById(R.id.dashboard_skydive_summary), R.id.skydiving_nav_jumps);
        clickSummary.put((CardView) view.findViewById(R.id.dashboard_base_summary), R.id.nav_jumps);
        clickSummary.put((CardView) view.findViewById(R.id.dashboard_dropzone_summary), R.id.skydiving_nav_dropzones);
        clickSummary.put((CardView) view.findViewById(R.id.dashboard_exits_summary), R.id.nav_exits);

        for (final Map.Entry<CardView, Integer> entry : clickSummary.entrySet()) {
            entry.getKey().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.goToFragment(entry.getValue());
                }
            });
        }
    }

    private void setLineChartData() {

        ArrayList<Entry> values = new ArrayList<Entry>();

        Query query = new Query();
        query.getWheres().put(query.getWheres().size(), Skydive.getActiveParams());
        query.orderDir(Skydive.COLUMN_NAME_DATE, Model.FILTER_ORDER_DIR_DESC);
        query.getParams().put(Model.FILTER_LIMIT, 10);

        List<Skydive> skydives = new Skydive().getItems(query.getParams());
        Collections.reverse(skydives);

        int i = 1;

        for (Skydive skydive : skydives) {
            if (skydive.getDeployAltitude() != null) {
                values.add(new Entry(i++, UnitConverter.getConvertedInteger(skydive.getDeployAltitude(), skydive.getHeightUnit())));
            }
        }

        if (values.size() > 0) {
            LineDataSet set1;

            // create a dataset and give it a type
            set1 = new LineDataSet(values, "Pull height (Last 10 Skydives)");

            // set the line to be drawn like this "- - - - - -"
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.BLUE);
            set1.setCircleColor(Color.BLUE);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            pullLineChart.getXAxis().setDrawLabels(false);
            pullLineChart.getAxisRight().setDrawLabels(false);
            pullLineChart.getXAxis().setDrawGridLines(false);

            // set data
            pullLineChart.setData(data);
            pullLineChart.getDescription().setEnabled(false);
            pullLineChart.getLegend().setTextSize(11f);
            pullLineChart.animateX(ANIMATION_SPEED, Easing.EasingOption.Linear);
        }
    }

    private void setBarChartData() {

        LinkedHashMap<Exit, Integer> top3 = Exit.getTop3Exits();

        if (top3.size() > 0) {
            favouriteExits.getDescription().setEnabled(false);
            favouriteExits.getXAxis().setEnabled(false);
            favouriteExits.getAxisLeft().setEnabled(true);
            favouriteExits.getAxisRight().setEnabled(false);

            Legend l = favouriteExits.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setForm(Legend.LegendForm.NONE);
            l.setFormSize(9f);
            l.setTextSize(11f);
            l.setXEntrySpace(4f);

            ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

            int i = 0;
            for (Map.Entry<Exit, Integer> entry : top3.entrySet()) {
                yVals1.add(new BarEntry(i++, entry.getValue(), entry.getKey().getName()));
            }

            BarDataSet set;

            set = new BarDataSet(yVals1, "Favourite Exits");
            set.setColors(ColorTemplate.MATERIAL_COLORS);
            set.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return entry.getData().toString();
                }
            });
            set.setDrawValues(true);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(14f);
            data.setBarWidth(0.9f);

            favouriteExits.setData(data);
            favouriteExits.setVisibleXRange(1, 3);
            favouriteExits.animateXY(ANIMATION_SPEED, ANIMATION_SPEED, Easing.EasingOption.Linear, Easing.EasingOption.Linear);
        }
    }

    private void setPieChartData() {

        mChart.setUsePercentValues(false);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setCenterText("Objects");

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);

        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);


        Query buildingQuery = new Query(Exit.COLUMN_NAME_OBJECT_TYPE, Exit.TYPE_BUILDING);
        buildingQuery.getWheres().put(buildingQuery.getWheres().size(), Synchronizable.getActiveParams());
        buildingQuery.addWhere(Exit.COLUMN_NAME_GLOBAL_ID, null);

        Query antennaQuery = new Query(Exit.COLUMN_NAME_OBJECT_TYPE, Exit.TYPE_ANTENNA);
        antennaQuery.getWheres().put(antennaQuery.getWheres().size(), Synchronizable.getActiveParams());
        antennaQuery.addWhere(Exit.COLUMN_NAME_GLOBAL_ID, null);

        Query spanQuery = new Query(Exit.COLUMN_NAME_OBJECT_TYPE, Exit.TYPE_SPAN);
        spanQuery.getWheres().put(spanQuery.getWheres().size(), Synchronizable.getActiveParams());
        spanQuery.addWhere(Exit.COLUMN_NAME_GLOBAL_ID, null);

        Query earthQuery = new Query(Exit.COLUMN_NAME_OBJECT_TYPE, Exit.TYPE_EARTH);
        earthQuery.getWheres().put(earthQuery.getWheres().size(), Synchronizable.getActiveParams());
        earthQuery.addWhere(Exit.COLUMN_NAME_GLOBAL_ID, null);

        Query otherQuery = new Query(Exit.COLUMN_NAME_OBJECT_TYPE, Exit.TYPE_OTHER);
        otherQuery.getWheres().put(otherQuery.getWheres().size(), Synchronizable.getActiveParams());
        otherQuery.addWhere(Exit.COLUMN_NAME_GLOBAL_ID, null);

        int buildings = new Exit().count(buildingQuery.getParams());
        int antennas = new Exit().count(antennaQuery.getParams());
        int spans = new Exit().count(spanQuery.getParams());
        int earth = new Exit().count(earthQuery.getParams());
        int other = new Exit().count(otherQuery.getParams());

        ArrayList<PieEntry> values = new ArrayList<>();
        if (buildings > 0)
            values.add(new PieEntry(buildings, "Buildings"));
        if (antennas > 0)
            values.add(new PieEntry(antennas, "Antennas"));
        if (spans > 0)
            values.add(new PieEntry(spans, "Spans"));
        if (earth > 0)
            values.add(new PieEntry(earth, "Earth"));
        if (other > 0)
            values.add(new PieEntry(other, "Other"));

        PieDataSet dataSet = new PieDataSet(values, "Objects");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        dataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return Integer.toString(Math.round(value));
            }
        });
        PieData data = new PieData(dataSet);
        mChart.setData(data);
        mChart.animateXY(ANIMATION_SPEED, ANIMATION_SPEED, Easing.EasingOption.Linear, Easing.EasingOption.Linear);
        mChart.getLegend().setEnabled(false);
        mChart.setEntryLabelColor(Color.BLACK);
    }

    @Override
    public void onResume() {
        super.onResume();

        String title = getString(R.string.title_dashboard);
        MainActivity.getActivity().setTitle(title);
    }

    @Override
    public void onPause() {
        super.onPause();

        this.getView().findViewById(R.id.premium_ad).setVisibility(View.GONE);
    }

    private static String timeConversion(int seconds) {

        final int MINUTES_IN_AN_HOUR = 60;
        final int SECONDS_IN_A_MINUTE = 60;

        int minutes = seconds / SECONDS_IN_A_MINUTE;
        seconds -= minutes * SECONDS_IN_A_MINUTE;

        int hours = minutes / MINUTES_IN_AN_HOUR;
        minutes -= hours * MINUTES_IN_AN_HOUR;

        return hours + "h " + minutes + "m " + seconds + "s";
    }
}
