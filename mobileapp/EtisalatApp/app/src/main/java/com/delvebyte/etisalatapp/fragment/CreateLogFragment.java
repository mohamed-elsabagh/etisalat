package com.delvebyte.etisalatapp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.afollestad.materialdialogs.MaterialDialog;
import com.delvebyte.etisalatapp.R;
import com.delvebyte.etisalatapp.model.LogModel;
import com.delvebyte.etisalatapp.service.INetworkService;
import com.delvebyte.etisalatapp.service.LogsService;

import org.json.JSONArray;
import org.json.JSONObject;

public class CreateLogFragment extends Fragment implements INetworkService, View.OnClickListener {
    private LogsService mLogsService;
    private final int CREATE_LOG = 0;
    private MaterialDialog dialog;
    private EditText editTextName;
    private Switch switchStatus;
    private Button buttonSend;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_log, container, false);

        editTextName = view.findViewById(R.id.editTextProcess);
        switchStatus = view.findViewById(R.id.switchStatus);
        buttonSend = view.findViewById(R.id.buttonSend);

        buttonSend.setOnClickListener(this);

        mLogsService = new LogsService();
        mLogsService.addListener(this);

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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonSend) {
            String process_name = editTextName.getText().toString();
            int status = 0;
            if (switchStatus.isChecked()) {
                status = 1;
            }

            Log.d("DEBUG","Process Name = " + process_name);
            Log.d("DEBUG","Status = " + status);

            LogModel newLog = new LogModel(process_name, status);

            dialog = new MaterialDialog.Builder(getActivity())
                    .title(R.string.progress_dialog)
                    .content(R.string.please_wait)
                    .progress(true, 0)
                    .show();

            mLogsService.pushLog(CREATE_LOG, newLog);
        }
    }
}
