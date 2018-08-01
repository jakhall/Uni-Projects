package aurora.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import net.aurora.main.R;

import java.util.ArrayList;

import aurora.model.Profile;
import aurora.network.Request;
import aurora.network.Response;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static net.aurora.main.R.id.btnRemove;
import static net.aurora.main.R.id.observingRecycler;


/**
 * Detail Activity - controls the functionality behind the detail view.
 */


public class ManageActivity extends BaseActivity {

    private TextView lblObserving;
    private Button btnRemove;
    private RecyclerView observingRecycler;


    //onCreate - creates the toolbar and sets up the layout.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        initialize();
        setupListeners();
        setupData();
    }

    //initialize - assigns the necessary layout elements to variables.

    private void initialize() {
        observingRecycler = (RecyclerView) findViewById(R.id.observingRecycler);
        btnRemove = (Button) findViewById(R.id.btnRemove);
        lblObserving = (TextView) findViewById(R.id.lblObserving);
    }

    //setupListeners - adds the listeners to all necessary layout elements.

    private void setupListeners() {

        setupSearchbar();

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove();
            }
        });
    }


    //setupData - fills the layout elements with data from the server.

    private void setupData() {
        updateObserving();
    }

    private void updateObserving(){
        Response res = serverRequest(new Request(3));
        ArrayList<Profile> users = (ArrayList<Profile>) res.getObject();
        ItemAdapter adapter = new ItemAdapter(this, users, null);
        observingRecycler.setAdapter(adapter);
        observingRecycler.setLayoutManager(new LinearLayoutManager(this));
        lblObserving.setText("Observing: (" + users.size() + ")");
    }

    //Remove - deletes the connection between the active user and the selected user they are observing.

    private void remove(){
       ItemAdapter adapter = (ItemAdapter)observingRecycler.getAdapter();
       Request req = new Request(4);
       req.setValue(adapter.getSelectedId(adapter.VIEW_PROFILE));
        serverRequest(req);
        updateObserving();

    }

}
