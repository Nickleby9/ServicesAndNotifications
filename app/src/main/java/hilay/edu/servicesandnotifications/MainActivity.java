package hilay.edu.servicesandnotifications;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        //AlarmManager -> stop using it
        //API 21 and up -> Job Scheduler


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        SharedPreferences prefs = getSharedPreferences("id", MODE_PRIVATE);

        String token = prefs.getString("token", "");
//        Toast.makeText(this, token, Toast.LENGTH_SHORT).show();
        Log.d("Ness", token);

//        recurringJob();
        Intent intent = new Intent(this, ChooserActivity.class);
        startActivity(intent);
    }

    private void oneTimeJobNow() {
        //dispatcher dispatch and cancel jobs:
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this/*context*/));


        Job job = dispatcher.newJobBuilder().
                setService(MyJobService.class).
                setTag("oneTimeJobNowTag").
                setTrigger(Trigger.NOW).
                build();

        dispatcher.mustSchedule(job);
        //startService(new Intent(this, myJobService.class);
    }

    private void oneTimeJobAfterOneHour() {
        //dispatcher dispatch and cancel jobs:
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this/*context*/));

        //explicit cast
        int secondsToStart = (int) TimeUnit.HOURS.toSeconds(1);

        Job job = dispatcher.newJobBuilder().
                setService(MyJobService.class).
                setTag("myOneTimeJobHourTag").
                //One Time Job.
                        setRecurring(false).
                        setLifetime(Lifetime.FOREVER). //Boot Complete
                //the tolerance is but a suggestion to the system
                //windowStart -> don't start me before this time.
                //time in seconds:
                        setTrigger(Trigger.executionWindow(secondsToStart, secondsToStart + 60)).
                        build();

        dispatcher.mustSchedule(job);
    }

    private void recurringJob() {
        //dispatcher dispatch and cancel jobs:
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this/*context*/));

        //explicit cast
        int secondsToStart = (int) TimeUnit.HOURS.toSeconds(1);

        Job job = dispatcher.newJobBuilder().
                setService(MyJobService.class).
                setTag("myRecurringUniqueTag").
                //Recurring job
                        setRecurring(true).
                        setLifetime(Lifetime.FOREVER). //Boot Complete
                        setReplaceCurrent(true).
                        //setConstraints(Constraint.ON_UNMETERED_NETWORK, Constraint.DEVICE_CHARGING, Constraint.DEVICE_IDLE).
                        setConstraints(Constraint.ON_ANY_NETWORK).
                        //time in seconds:
                        //once per hour.
                        //setTrigger(Trigger.executionWindow(secondsToStart, secondsToStart + 60)).
                        //mere suggestion to the os.
                        setTrigger(Trigger.executionWindow(0, 19)).
                        build();

        dispatcher.mustSchedule(job);
        //dispatcher.cancel("myRecurringUniqueTag");
    }


}
