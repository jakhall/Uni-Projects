package aurora.main;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import net.aurora.main.R;

import java.util.ArrayList;

import aurora.model.Profile;
import aurora.network.Request;
import aurora.network.Response;

/**
 * Detail Activity - controls the functionality behind the detail view.
 */

public class ObserveActivity extends BaseActivity {

    private Button btnManage;

    private RecyclerView observingRecycler;

    private RecyclerView observerRecycler;

    private ItemAdapter adapter;

    private TextView lblObserving;

    private TextView lblObservers;


    //onCreate - creates the toolbar and sets up the layout.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        initialize();
        setupData();
        setupListeners();
    }

    //onResume - refresh data when the user returns to the activity.

    @Override
    protected void onResume() {
        super.onResume();
        setupData();
    }

    //initialize - assigns the necessary layout elements to variables.


    private void initialize() {
        btnManage = (Button) findViewById(R.id.btnManage);
        observingRecycler = (RecyclerView) findViewById(R.id.observingRecycler);
        observerRecycler = (RecyclerView) findViewById(R.id.observerRecycler);
        lblObserving = (TextView) findViewById(R.id.lblObserving);
        lblObservers = (TextView) findViewById(R.id.lblObservers);

    }

    //setupListeners - adds the listeners to all necessary layout elements.

    private void setupListeners() {

        setupSearchbar();

        btnManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                manage();
            }
        });

    }

    //setupData - fills the layout elements with data from the server.

    private void setupData() {
        setManage();
        updateObserving();
        updateObserver();
    }


    //setManage - checks if the user can edit the list of observers.

    private void setManage(){
        Response res = serverRequest(new Request(7));
        if(res.getSwitch()){
            btnManage.setVisibility(btnManage.INVISIBLE);
        }
    }

    //updateObserving - gets all of the profiles that the active user is observing.

    private void updateObserving(){
        ArrayList<Profile> observingList = (ArrayList<Profile>)serverRequest(new Request(4)).getObject();

                adapter = new ItemAdapter(this, observingList, null);
                observingRecycler.setAdapter(adapter);
                observingRecycler.setLayoutManager(new LinearLayoutManager(this));
                lblObserving.setText("Observing: (" + observingList.size() + ")");
    }

    //updateObserving - gets all of the profiles that are observing the active user.

    private void updateObserver(){
        ArrayList<Profile> observerList = (ArrayList<Profile>)serverRequest(new Request(3)).getObject();

        adapter = new ItemAdapter(this, observerList, null);
        observerRecycler.setAdapter(adapter);
        observerRecycler.setLayoutManager(new LinearLayoutManager(this));
        lblObservers.setText("Observers: (" + observerList.size() + ")");
    }

    //Manage - sends the user to the manage activity and view.

    private void manage(){
        serverRequest(new Request(5));
        startActivity(new Intent(this, ManageActivity.class));
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
    }



}
