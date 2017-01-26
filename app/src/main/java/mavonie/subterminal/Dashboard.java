package mavonie.subterminal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;
import mavonie.subterminal.Models.*;
import mavonie.subterminal.Models.Exit;


/**
 * Dashboard fragment
 */
public class Dashboard extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        PieChartView chart = (PieChartView) view.findViewById(R.id.dashboard_base_objects_pie);

        PieChartData data = new PieChartData(getObjectsValues());
        data.setHasLabels(true);
        data.setHasLabelsOnlyForSelected(false);
        data.setHasLabelsOutside(true);
        data.setHasCenterCircle(true);

        data.setCenterText1("Objects");
        data.setCenterText1FontSize(20);
        chart.setPieChartData(data);
        chart.animate();

        return view;
    }

    private List<SliceValue> getObjectsValues() {
        HashMap<String, Object> whereBuilding = new HashMap<>();
        whereBuilding.put(Model.FILTER_WHERE_FIELD, Exit.COLUMN_NAME_OBJECT_TYPE);
        whereBuilding.put(Model.FILTER_WHERE_VALUE, Integer.toString(Exit.TYPE_BUILDING));

        HashMap<String, Object> whereNotDeleted = new HashMap<>();
        whereNotDeleted.put(Model.FILTER_WHERE_FIELD, Synchronizable.COLUMN_DELETED);
        whereNotDeleted.put(Model.FILTER_WHERE_VALUE, Synchronizable.DELETED_FALSE.toString());

        HashMap<String, Object> whereNotGlobal = new HashMap<>();
        whereNotDeleted.put(Model.FILTER_WHERE_FIELD, Exit.COLUMN_NAME_GLOBAL_ID);
        whereNotDeleted.put(Model.FILTER_WHERE_VALUE, null);

        HashMap<Integer, HashMap> buildingWheres = new HashMap<>();
        buildingWheres.put(buildingWheres.size(), whereNotDeleted);
        buildingWheres.put(buildingWheres.size(), whereBuilding);
        buildingWheres.put(buildingWheres.size(), whereNotGlobal);

        HashMap<String, Object> buildingParams = new HashMap<>();
        buildingParams.put(Model.FILTER_WHERE, buildingWheres);

        HashMap<String, Object> whereAntenna = new HashMap<>();
        whereAntenna.put(Model.FILTER_WHERE_FIELD, Exit.COLUMN_NAME_OBJECT_TYPE);
        whereAntenna.put(Model.FILTER_WHERE_VALUE, Integer.toString(Exit.TYPE_ANTENNA));

        HashMap<Integer, HashMap> antennaWheres = new HashMap<>();
        antennaWheres.put(antennaWheres.size(), whereNotDeleted);
        antennaWheres.put(antennaWheres.size(), whereAntenna);
        antennaWheres.put(antennaWheres.size(), whereNotGlobal);

        HashMap<String, Object> antennaParams = new HashMap<>();
        antennaParams.put(Model.FILTER_WHERE, antennaWheres);

        HashMap<String, Object> whereSpan = new HashMap<>();
        whereSpan.put(Model.FILTER_WHERE_FIELD, Exit.COLUMN_NAME_OBJECT_TYPE);
        whereSpan.put(Model.FILTER_WHERE_VALUE, Integer.toString(Exit.TYPE_SPAN));

        HashMap<Integer, HashMap> spanWheres = new HashMap<>();
        spanWheres.put(spanWheres.size(), whereNotDeleted);
        spanWheres.put(spanWheres.size(), whereSpan);
        spanWheres.put(spanWheres.size(), whereNotGlobal);

        HashMap<String, Object> spanParams = new HashMap<>();
        spanParams.put(Model.FILTER_WHERE, spanWheres);

        HashMap<String, Object> whereEarth = new HashMap<>();
        whereEarth.put(Model.FILTER_WHERE_FIELD, Exit.COLUMN_NAME_OBJECT_TYPE);
        whereEarth.put(Model.FILTER_WHERE_VALUE, Integer.toString(Exit.TYPE_EARTH));

        HashMap<Integer, HashMap> earthWheres = new HashMap<>();
        earthWheres.put(earthWheres.size(), whereNotDeleted);
        earthWheres.put(earthWheres.size(), whereEarth);
        earthWheres.put(earthWheres.size(), whereNotGlobal);

        HashMap<String, Object> earthParams = new HashMap<>();
        earthParams.put(Model.FILTER_WHERE, earthWheres);

        HashMap<String, Object> whereOther = new HashMap<>();
        whereOther.put(Model.FILTER_WHERE_FIELD, Exit.COLUMN_NAME_OBJECT_TYPE);
        whereOther.put(Model.FILTER_WHERE_VALUE, Integer.toString(Exit.TYPE_OTHER));

        HashMap<Integer, HashMap> otherWheres = new HashMap<>();
        otherWheres.put(otherWheres.size(), whereNotDeleted);
        otherWheres.put(otherWheres.size(), whereOther);
        otherWheres.put(otherWheres.size(), whereNotGlobal);

        HashMap<String, Object> otherParams = new HashMap<>();
        otherParams.put(Model.FILTER_WHERE, otherWheres);

        int buildings = new Exit().count(buildingParams);
        int antennas = new Exit().count(antennaParams);
        int spans = new Exit().count(spanParams);
        int earth = new Exit().count(earthParams);
        int other = new Exit().count(otherParams);

        List<SliceValue> values = new ArrayList<>();
        if (buildings > 0)
            values.add(new SliceValue(buildings, ChartUtils.COLOR_BLUE).setLabel("Buildings (" + buildings + ")"));
        if (antennas > 0)
            values.add(new SliceValue(antennas, ChartUtils.COLOR_GREEN).setLabel("Antennas (" + antennas + ")"));
        if (spans > 0)
            values.add(new SliceValue(spans, ChartUtils.COLOR_ORANGE).setLabel("Spans (" + spans + ")"));
        if (earth > 0)
            values.add(new SliceValue(earth, ChartUtils.COLOR_RED).setLabel("Earth (" + earth + ")"));
        if (other > 0)
            values.add(new SliceValue(other, ChartUtils.COLOR_VIOLET).setLabel("Other (" + other + ")"));
        return values;
    }

    @Override
    public void onResume() {
        super.onResume();

        String title = getString(R.string.title_dashboard);
        MainActivity.getActivity().setTitle(title);
    }

}
