package aurora.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import net.aurora.main.R;

import java.util.ArrayList;

import aurora.model.Document;
import aurora.model.Profile;
import aurora.network.Request;
import aurora.network.Response;

import static net.aurora.main.R.id.lblObserving;
import static net.aurora.main.R.id.lblSearch;
import static net.aurora.main.R.id.observingRecycler;


/**
 * Detail Activity - controls the functionality behind the detail view.
 */

public class SearchActivity extends BaseActivity {


    private Spinner spinner;
    private TextView lblSearch;
    private RecyclerView searchRecycler;
    private EditText searchBar;


    //onCreate - creates the toolbar and sets up the layout.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        initialize();
        setupSpinner();
        setupListeners();
        setupData();

        }


    //initialize - assigns the necessary layout elements to variables.

    private void initialize(){
        spinner = (Spinner) findViewById(R.id.spinner);
        lblSearch = (TextView) findViewById(R.id.lblSearch);
        searchRecycler = (RecyclerView) findViewById(R.id.searchRecycler);
        searchBar = (EditText) findViewById(R.id.searchBar);
    }

    //setupListeners - adds the listeners to all necessary layout elements.


    private void setupListeners() {

        searchBar.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    newSearch();
                    return true;
                }
                return false;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setResults(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setResults(0);
            }
        });
    }


    //setupData - fills the layout elements with data from the server.

    private void setupData(){
        String query = (String)serverRequest(new Request(7)).getObject();
        searchBar.setText(query);
    }


    //setResults - Retrieve the results from the database from the input query add it to the recycler view.

    private void setResults(int pos){

        ItemAdapter adapter = null;

        if(pos > 0){
            Request req = new Request(5, searchBar.getText().toString());
            req.setValue(0);
            Response res = serverRequest(req);
            ArrayList<Document> results = (ArrayList<Document>)res.getObject();
            adapter = new ItemAdapter(this, null, results);
            lblSearch.setText("Search Results: (" + results.size() + ")");
        } else {
            ArrayList<Profile> results = (ArrayList<Profile>)serverRequest(new Request(4)).getObject();
            adapter = new ItemAdapter(this, results, null);
            lblSearch.setText("Search Results: (" + results.size() + ")");
        }

        searchRecycler.setAdapter(adapter);
        searchRecycler.setLayoutManager(new LinearLayoutManager(this));

    }


    //setupSpinner - set the spinner options from a string resource.

    private void setupSpinner(){

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.search_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    //newSearch updates the search view with a new query.

    private void newSearch(){
        serverRequest(new Request(3,searchBar.getText().toString()));
        setResults(spinner.getSelectedItemPosition());
    }

}
