package com.chlorophy.hobby.main.settings;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.chlorophy.hobby.R;
import com.chlorophy.hobby.xmpp.ProjInfo;
import com.chlorophy.hobby.xmpp.UserInfoEnhanced;

import java.util.Calendar;

public class ProjSettingsFragment extends Fragment {

    private int position;
    private TextView text;
    private Switch shutter;
    private ProgressBar loading;
    private ProjInfo info;


    public ProjSettingsFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View wrapper = inflater.inflate(
                R.layout.fragment_proj_settings, container, false);
        switch (position) {
            case 0:
                text = (TextView) wrapper.findViewById(R.id.settingText);
                text.setVisibility(View.VISIBLE);
                break;
            case 1:
                text = (TextView) wrapper.findViewById(R.id.settingText);
                text.setHint("Deadline");
                text.setVisibility(View.VISIBLE);
                text.setFocusable(false);
                text.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_DOWN) {
                            int year = Calendar.getInstance().get(Calendar.YEAR);
                            int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
                            int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                            info.setInitialDate(year + "/" + month + "/" + day);

                            DatePickerDialog dialog = new DatePickerDialog(
                                    wrapper.getContext(), new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    info.setFinalDate(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                                    text.setText(info.getFinalDate());
                                }
                            }, year, month, day);
                            dialog.show();
                            return true;
                        }
                        return true;
                    }
                });
                break;
            case 2:
                shutter = (Switch) wrapper.findViewById(R.id.settingSwitch);
                loading = (ProgressBar) wrapper.findViewById(R.id.settingProgress);
                shutter.setVisibility(View.VISIBLE);
                View fragOne = getFragmentManager().getFragments().get(0).getView();
                if(fragOne != null) {
                    TextView name = (TextView) fragOne.findViewById(R.id.settingText);
                    if(!name.getText().toString().equals("")) {
                        info.setName(name.getText().toString());
                    }
                }
                shutter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        shutter.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return true;
                            }
                        });
                        shutter.setVisibility(View.GONE);
                        loading.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(getActivity(), ExplorerSetting.class);
                        startActivityForResult(intent, 0);
                    }
                });
                break;
        }
        return wrapper;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 1) {
            info.setProjDir(data.getData());
            UserInfoEnhanced.projInfoList.add(info);
        }
        getActivity().finish();
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setInfo(ProjInfo info) {
        this.info = info;
    }

    public void initial(int position, ProjInfo info) {
        this.position = position;
        this.info = info;
    }
}
