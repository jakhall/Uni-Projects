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

import java.util.ArrayList;

import aurora.model.Document;
import aurora.model.Profile;
import aurora.network.Request;
import aurora.network.Response;

import static android.view.View.VISIBLE;
import static net.aurora.main.R.id.btnViewDocuments;
import static net.aurora.main.R.id.documentRecycler;
import static net.aurora.main.R.id.fldEmail;
import static net.aurora.main.R.id.fldTitle;
import static net.aurora.main.R.id.lblDocuments;


/**
 * Detail Activity - controls the functionality behind the detail view.
 */

public class ProfileActivity extends BaseActivity {

    private Button btnRemove;
    private Button btnObserve;
    private Button btnViewUsers;
    private Button btnViewDetails;
    private TextView fldUsername;
    private TextView lblName;
    private ImageView profileIcon;
    private RecyclerView documentRecycler;
    private TextView lblDocuments;


    //onCreate - creates the toolbar and sets up the layout.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        initialize();
        setupData();
        setupListeners();
    }


    //initialize - assigns the necessary layout elements to variables.

    private void initialize(){
        btnObserve = (Button) findViewById(R.id.btnObserve);
        btnRemove = (Button) findViewById(R.id.btnRemove);
        btnViewUsers = (Button) findViewById(R.id.btnViewUsers);
        btnViewDetails = (Button) findViewById(R.id.btnViewDetails);
        fldUsername = (TextView) findViewById(R.id.fldUsername);
        lblName = (TextView) findViewById(R.id.lblName);
        profileIcon = (ImageView) findViewById(R.id.profileIcon);
        lblDocuments = (TextView) findViewById(R.id.lblDocuments);
        documentRecycler = (RecyclerView) findViewById(R.id.documentRecycler);
    }

    //setupListeners - adds the listeners to all necessary layout elements.

    private void setupListeners(){

        setupSearchbar();

        btnViewUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                viewObserve();
            }
        });


        btnObserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                observe();
                btnRemove.setVisibility(VISIBLE);
                btnObserve.setVisibility(btnObserve.INVISIBLE);
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                remove();
                btnObserve.setVisibility(VISIBLE);
                btnRemove.setVisibility(btnObserve.INVISIBLE);
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
        checkObserve();
        userDetails();
        updateDocuments();
    }


    //checkObserve - checks if the active user is observing the profile being viewed.

    private void checkObserve(){
        Response res = serverRequest(new Request(4));

        if(res.getSwitch()){
            btnRemove.setVisibility(VISIBLE);
            btnObserve.setVisibility(btnObserve.INVISIBLE);
        } else {
            btnRemove.setVisibility(btnObserve.INVISIBLE);
            btnObserve.setVisibility(btnObserve.VISIBLE);
        }
    }

    //Observe - Sets the active user as observing the viewed user.

    private void observe(){
        serverRequest(new Request(5));
    }

    //Remove - Sets the active user as not observing the viewed user.

    private void remove(){
        serverRequest(new Request(11));
    }


    //userDetails - Retrieves the profile object from the server of the specific user and updates the view.

    private void userDetails(){
        Response res = serverRequest(new Request(3));
        Profile user = (Profile)res.getObject();

        fldUsername.setText(user.getUsername());
        lblName.setText(user.getFirstName() + " " + user.getLastName());


        Bitmap bmp = BitmapFactory.decodeByteArray(user.getImage(), 0, user.getImage().length);
        profileIcon.setImageBitmap(Bitmap.createScaledBitmap(bmp, 100, 100, false));

    }



    private void updateDocuments(){
        Response res = serverRequest(new Request(9));
        ArrayList<Document> documents = (ArrayList<Document>) res.getObject();

        RecyclerView.Adapter adapter = new ItemAdapter(this, null, documents);
        documentRecycler.setAdapter(adapter);
        documentRecycler.setLayoutManager(new LinearLayoutManager(this));
        lblDocuments.setText("Documents: (" + documents.size() + ")");
    }


    //viewObserve - sends the user to the observe activity and view.

    private void viewObserve(){
        serverRequest(new Request(8));
        startActivity(new Intent(this, ObserveActivity.class));
    }

    //viewDocuments - sends the user to the DocList activity and view.

    private void viewDocuments(){
        serverRequest(new Request(9));
        startActivity(new Intent(this, DocListActivity.class));
    }


    //viewDocuments - sends the user to the Detail activity and view.

    private void viewDetails(){
        serverRequest(new Request(10));
        startActivity(new Intent(this, DetailActivity.class));
    }

}
