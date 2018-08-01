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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.aurora.main.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import aurora.model.Document;
import aurora.model.Profile;
import aurora.network.Request;
import aurora.network.Response;

/**
 * Detail Activity - controls the functionality behind the detail view.
 */

public class HomeActivity extends BaseActivity {


    private Button btnViewUsers;
    private Button btnViewDocuments;
    private Button btnViewDetails;
    private TextView fldUsername;
    private TextView lblName;
    private ImageView profileIcon;
    private ItemAdapter adapter;
    private TextView lblDocuments;
    private RecyclerView suggestionRecycler;
    private RecyclerView documentRecycler;


    //onCreate - creates the toolbar and sets up the layout.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        initialize();
        setupData();
        setupListeners();
    }

    //initialize - assigns the necessary layout elements to variables.

    private void initialize(){
        btnViewDetails =  (Button) findViewById(R.id.btnViewDetails);
        btnViewUsers = (Button) findViewById(R.id.btnViewUsers);
        btnViewDocuments = (Button) findViewById(R.id.btnViewDocuments);
        fldUsername = (TextView) findViewById(R.id.fldUsername);
        lblName = (TextView) findViewById(R.id.lblName);
        profileIcon = (ImageView) findViewById(R.id.profileIcon);
        lblDocuments = (TextView) findViewById(R.id.lblDocuments);
        profileIcon = (ImageView) findViewById(R.id.profileIcon);
        suggestionRecycler = (RecyclerView) findViewById(R.id.suggestionRecycler);
        documentRecycler = (RecyclerView) findViewById(R.id.documentRecycler);
    }

    //setupListeners - adds the listeners to all necessary layout elements.

    private void setupListeners(){

        setupSearchbar();

        btnViewUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                viewUsers();
            }
        });

        btnViewDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                viewDocuments();
            }
        });

        btnViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                viewDetails();
            }
        });

    }

    //setupData - fills the layout elements with data from the server.

    private void setupData(){
        userDetails();
    }


    private void userDetails(){
        Response res = serverRequest(new Request(3));
        Profile user = (Profile)res.getObject();

        fldUsername.setText(user.getUsername());
        lblName.setText(user.getFirstName() + " " + user.getLastName());
        res = serverRequest(new Request(4));

        Bitmap bmp = BitmapFactory.decodeByteArray(user.getImage(), 0, user.getImage().length);
        profileIcon.setImageBitmap(Bitmap.createScaledBitmap(bmp, 100, 100, false));

        updateDocuments();
        updateSuggestions();

    }


    //onResume - refresh data when the user returns to the activity.

    @Override
    protected void onResume() {
        super.onResume();
        setupData();
    }


    //updateDocuments - Retrieve the list of documents created by the user.

    private void updateDocuments(){
        Response res = serverRequest(new Request(9));
        ArrayList<Document> documents = (ArrayList<Document>) res.getObject();

        adapter = new ItemAdapter(this, null, documents);
        documentRecycler.setAdapter(adapter);
        documentRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    //updateSuggestions - Retrieve the list of suggestions created for the user.

    private void updateSuggestions(){
        Response res = serverRequest(new Request(10));
        ArrayList<Document> documents = (ArrayList<Document>) res.getObject();

        adapter = new ItemAdapter(this, null, documents);
        suggestionRecycler.setAdapter(adapter);
        suggestionRecycler.setLayoutManager(new LinearLayoutManager(this));
    }


    //viewUsers - sends the user to the observe activity and view.
    private void viewUsers(){
        serverRequest(new Request(5));
        startActivity(new Intent(this, ObserveActivity.class));
    }

    //viewDocuments - sends the user to the DocList activity and view.

    private void viewDocuments(){
        serverRequest(new Request(7));
        startActivity(new Intent(this, DocListActivity.class));
    }

    //viewDocuments - sends the user to the Detail activity and view.

    private void viewDetails(){
        serverRequest(new Request(8));
        startActivity(new Intent(this, DetailActivity.class));
    }

}
