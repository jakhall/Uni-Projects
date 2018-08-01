package aurora.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.aurora.main.R;

import java.util.ArrayList;

import aurora.model.Document;
import aurora.model.Profile;
import aurora.network.Request;
import aurora.network.Response;

import static net.aurora.main.R.id.btnAddNew;
import static net.aurora.main.R.id.btnRemove;
import static net.aurora.main.R.id.observingRecycler;


/**
 * Detail Activity - controls the functionality behind the detail view.
 */

public class DocListActivity extends BaseActivity {



    private Button btnRemove;
    private Button btnAddNew;
    private TextView lblDocuments;
    private RecyclerView documentRecycler;

    //onCreate - creates the toolbar and sets up the layout.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doclist_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        initialize();
        setupData();
        setupListeners();
    }

    //initialize - assigns the necessary layout elements to variables.

    private void initialize(){
        btnRemove = (Button) findViewById(R.id.btnRemove);
        btnAddNew = (Button) findViewById(R.id.btnAddNew);
        lblDocuments = (TextView) findViewById(R.id.lblDocuments);
        documentRecycler = (RecyclerView) findViewById(R.id.documentRecycler);

    }

    //setupData - fills the layout elements with data from the server.

    private void setupData(){
        updateDocuments();

        if(serverRequest(new Request(7)).getSwitch()){
            btnAddNew.setVisibility(btnAddNew.INVISIBLE);
            btnRemove.setVisibility(btnRemove.INVISIBLE);
        }

    }

    //setupListeners - adds the listeners to all necessary layout elements.

    private void setupListeners(){

        setupSearchbar();

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeDocument();
            }
        });

        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDocument();
            }
        });
    }

    //updateDocuments - retrieve all documents from the server created by the active user.

    private void updateDocuments() {
        Response res = serverRequest(new Request(3));
        ArrayList<Document> documents = (ArrayList<Document>) res.getObject();

        ItemAdapter adapter = new ItemAdapter(this, null, documents);
        documentRecycler.setAdapter(adapter);
        documentRecycler.setLayoutManager(new LinearLayoutManager(this));
        lblDocuments.setText("Documents: (" + documents.size() + ")");
    }

    //removeDocument - remove the selected document from the server.

    private void removeDocument(){
        ItemAdapter adapter = (ItemAdapter)documentRecycler.getAdapter();
        int id = adapter.getSelectedId(adapter.VIEW_DOCUMENT);
        Request req = new Request(8);
        req.setValue(id);
        serverRequest(req);
        updateDocuments();
    }

    //onResume - refresh data when the user returns to the activity.

    @Override
    protected void onResume() {
        super.onResume();
        setupData();
    }

    //addDocument - adds a blank document to the list of user documents.

    private void addDocument(){
        serverRequest(new Request(4));
        updateDocuments();
    }


}
