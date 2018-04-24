package com.markupartist.sthlmtraveling;

/**
 * Blenda och Filip
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.markupartist.sthlmtraveling.data.models.Leg;
import com.markupartist.sthlmtraveling.data.models.Route;

public class AlarmPreferencesActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner mTimeSpinnerDeparture;
    private Spinner mTimeSpinnerDestination;
    long mTimeDeparture;
    long mTimeDestination;
    boolean mTimeSelectedDeparture = true;
    boolean mTimeSelectedDestination = true;
    CheckBox mAlarmEveryStopCheckBox;
    Date mStartDate;
    long mStartTime;
    Date mEndDate;
    long mEndTime;
    List<Long> endTime;
    int mRequestCode = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_preferences);

        /**Get legs**/
        Intent intent = getIntent();
        Route route = intent.getParcelableExtra("ParceableTest");
        List<Leg> legList = route.getLegs();

        /** Get every destination time **/
        endTime = new ArrayList<>();
        for(Leg leg : legList ){
            if(!leg.getTravelMode().equals("foot")){
                Log.v("asznee", leg.getTravelMode());
                endTime.add(leg.getEndTime().getTime());
            }
        }

        /** Get final destination time**/
        mEndDate = legList.get(legList.size()-1).getEndTime();
        mEndTime = mEndDate.getTime();

        /** Get departure time**/
        mStartDate = legList.get(0).getStartTime();
        mStartTime = mStartDate.getTime();

        /** From ChangeRouteTimeActivity **/
        //Set up spinner departure & destination
        mTimeSpinnerDeparture = (Spinner) findViewById(R.id.time_spinner_departure);
        mTimeSpinnerDestination = (Spinner) findViewById(R.id.time_spinner_destination);
        mTimeSpinnerDeparture.setEnabled(false);
        mTimeSpinnerDestination.setEnabled(false);

        int selectedPosDeparture = 0;
        ArrayAdapter<CharSequence> whenChoiceAdapter = ArrayAdapter.createFromResource(
                this, R.array.time_interval_departure, android.R.layout.simple_spinner_item);
        whenChoiceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTimeSpinnerDeparture.setAdapter(whenChoiceAdapter);
        mTimeSpinnerDeparture.setSelection(selectedPosDeparture);

        int selectedPosDestination = 0;
        ArrayAdapter<CharSequence> whenChoiceAdapterDestination = ArrayAdapter.createFromResource(
                this, R.array.time_interval_destination, android.R.layout.simple_spinner_item);
        whenChoiceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTimeSpinnerDestination.setAdapter(whenChoiceAdapterDestination);
        mTimeSpinnerDestination.setSelection(selectedPosDestination);
        /***/


        /** From ChangeRouteTimeActivity **/
        // Inflate a "Done/Discard" custom action bar view.
        LayoutInflater inflater = (LayoutInflater) getSupportActionBar().getThemedContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(
                R.layout.actionbar_custom_view_done_discard, null);
        customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(this);
        customActionBarView.findViewById(R.id.actionbar_discard).setOnClickListener(this);

        // Show the custom action bar view and hide the normal Home icon and title.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(
                ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView, new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        /***/

        /** Set up checkbox **/
        final CheckBox mAlarmDepartureCheckBox = (CheckBox) findViewById(R.id.select_alarm_departure);
        final boolean checkedAlarmDeparture = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("departureBox", false);
        mAlarmDepartureCheckBox.setChecked(checkedAlarmDeparture);
        final CheckBox mAlarmDestinationCheckBox = (CheckBox) findViewById(R.id.select_alarm_destination);
        mAlarmEveryStopCheckBox = (CheckBox) findViewById(R.id.select_alarm_everyStop);
        mAlarmEveryStopCheckBox.setEnabled(false);

        /** OnClick checkbox departure**/
        mAlarmDepartureCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAlarmDepartureCheckBox.isChecked()){
                    mTimeSpinnerDeparture.setEnabled(true);
                } else {
                    mTimeSpinnerDeparture.setEnabled(false);
                }
            }
        });

        /** OnClick checkbox destination **/
        mAlarmDestinationCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAlarmDestinationCheckBox.isChecked()){
                    mTimeSpinnerDestination.setEnabled(true);
                    mAlarmEveryStopCheckBox.setEnabled(true);
                } else {
                    mTimeSpinnerDestination.setEnabled(false);
                    mAlarmEveryStopCheckBox.setEnabled(false);
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_done:
                int mSelectedTimeDeparture = (int) mTimeSpinnerDeparture.getSelectedItemId();
                mTimeDeparture = getAlarmTimeDep(mSelectedTimeDeparture, mStartTime);

                int mSelectedTimeDestination = (int) mTimeSpinnerDestination.getSelectedItemId();
                mTimeDestination = getAlarmTimeDest(mSelectedTimeDestination, mEndTime);

                if (mTimeSelectedDeparture) {
                    setAlarm(mTimeDeparture);
                }
                if (mTimeSelectedDestination) {
                    setAlarm(mTimeDestination);
                }

                /** Set alarm for every stop **/
                if(mAlarmEveryStopCheckBox.isChecked()){
                    for(long time : endTime) {
                      setAlarm(getAlarmTimeDest(mSelectedTimeDestination, time));
                    }
                }

                finish();
                break;

            case R.id.actionbar_discard:
                finish();
                break;
        }
    }

    public void setAlarm(long mTime){
        Intent intent = new Intent(AlarmPreferencesActivity.this, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), mRequestCode, intent, 0);
        mRequestCode++;
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, mTime, pendingIntent);
    }

    /** Get alarm time departure **/
    public long getAlarmTimeDep(int selectedTimeDeparture, long startTime) {
       long alarmDep = 0;
        switch (selectedTimeDeparture) {
            case 0:
                mTimeSelectedDeparture = false;
            case 1:
                alarmDep = startTime - 120000;
                break;
            case 2:
                alarmDep = startTime - 300000;
                break;
            case 3:
                alarmDep = startTime - 600000;
                break;
            case 4:
                alarmDep = startTime - 900000;
                break;
            case 5:
                alarmDep = startTime - 1800000;
                break;
        }
        return alarmDep;
    }

    /** Get alarm time destination **/
    public long getAlarmTimeDest(int selectedTimeDestination, long endTime){
        long alarmDest = 0;
        switch (selectedTimeDestination){
            case 0:
                mTimeSelectedDestination = false;
                break;
            case 1:
                alarmDest = endTime - 120000;
                break;
            case 2:
                alarmDest = endTime- 180000;
                break;
            case 3:
                alarmDest = endTime - 300000;
                break;
        }
        return alarmDest;
    }
}
