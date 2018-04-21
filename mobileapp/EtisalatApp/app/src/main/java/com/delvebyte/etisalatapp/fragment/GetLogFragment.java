package com.delvebyte.etisalatapp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.delvebyte.etisalatapp.R;
import com.delvebyte.etisalatapp.model.LogModel;
import com.delvebyte.etisalatapp.service.INetworkService;
import com.delvebyte.etisalatapp.service.LogsService;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetLogFragment extends Fragment implements INetworkService {
    private LogsService mLogsService;
    private final int GET_LOG = 0;
    private MaterialDialog dialog;
    private BarChart barChart;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_get_log, container, false);

        barChart = view.findViewById(R.id.barChart);
        dialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .progress(true, 0)
                .show();
        mLogsService = new LogsService();
        mLogsService.addListener(this);

        mLogsService.getAllLogs(GET_LOG);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mLogsService != null ) {
            mLogsService.removeListener(this);
            mLogsService.cancelAllRequests();
        }
    }

    @Override
    public void responseSuccess(JSONObject response, int requestNumber) {
        dialog.dismiss();
    }

    @Override
    public void responseSuccess(JSONArray response, int requestNumber) {
        dialog.dismiss();
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        final ArrayList<String> BarEntryNames = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject jsonObject = response.getJSONObject(i);
                barEntries.add(new BarEntry(i, Float.parseFloat(jsonObject.getString("status"))));
                BarEntryNames.add(jsonObject.getString("name"));

            } catch (JSONException e) {
                return;
            }
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Process");
        BarData theData = new BarData(barDataSet);

        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        barChart.setData(theData);

        barChart.animateY(3000);

        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return BarEntryNames.get((int)value);
            }
        });
    }

    @Override
    public void responseFailure(JSONObject failure, int requestNumber) {
        dialog.dismiss();
    }

    @Override
    public void responseFailure(int requestNumber) {
        dialog.dismiss();
    }

    @Override
    public void responseTimeoutError(int requestNumber) {
        dialog.dismiss();
    }

    @Override
    public void responseNetworkError(int requestNumber) {
        dialog.dismiss();
    }

    @Override
    public void responseServerError(int requestNumber) {
        dialog.dismiss();
    }

    @Override
    public void responseParseError(int requestNumber) {
        dialog.dismiss();
    }

    @Override
    public void AuthFailureError(int requestNumber) {
        dialog.dismiss();
    }
}
