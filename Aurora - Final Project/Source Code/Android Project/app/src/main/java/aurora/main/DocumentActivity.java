package aurora.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import net.aurora.main.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import aurora.model.Document;
import aurora.network.Request;
import aurora.network.Response;

import static android.R.attr.button;
import static android.R.attr.format;
import static net.aurora.main.R.id.btnChoose;
import static net.aurora.main.R.id.profileIcon;


/**
 * Detail Activity - controls the functionality behind the detail view.
 */

public class DocumentActivity extends BaseActivity {

    private Button btnSave;
    private Button btnEdit;
    private Button btnChoose;
    private Button btnProfile;
    private TextView lblTitle;
    private TextView lblDate;
    private TextView lblAuthor;
    private EditText documentText;
    private ImageView documentIcon;
    private ScrollView documentScroll;

    int GALLERY_REQUEST = 1;
    int authorId;

    //onCreate - creates the toolbar and sets up the layout.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.document_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        initialize();
        setupData();
        setupListeners();
    }

    //initialize - assigns the necessary layout elements to variables.

    private void initialize(){
        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnProfile = (Button) findViewById(R.id.btnProfile);
        btnChoose = (Button) findViewById(R.id.btnChoose);
        btnSave = (Button) findViewById(R.id.btnSave);
        lblTitle = (TextView) findViewById(R.id.fldTitle);
        lblAuthor = (TextView) findViewById(R.id.lblAuthor);
        lblDate = (TextView) findViewById(R.id.lblDate);
        documentText = (EditText) findViewById(R.id.documentText);
        documentIcon = (ImageView) findViewById(R.id.documentIcon);
        documentScroll = (ScrollView) findViewById(R.id.documentScroll);

    }

    //setupData - fills the layout elements with data from the server.

    private void setupData(){
        checkEditable();
        updateDocument();
    }


    //setupListeners - adds the listeners to all necessary layout elements.

    private void setupListeners(){

        setupSearchbar();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
                btnSave.setVisibility(btnSave.INVISIBLE);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewProfile();
            }
        });

        documentIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(btnSave.getVisibility() == btnSave.VISIBLE) {
                    getImage();
                }
            }
        });
    }


    //checkEditable - checks whether the user is allowed to edit this document.

    private void checkEditable(){

        if(serverRequest(new Request(5)).getSwitch()){
            btnEdit.setVisibility(btnEdit.INVISIBLE);
        }

    }

    //getImage - Creates a new popup which allows the user to search their gallery for an image to insert.

    private void getImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    //onActivityResult - updates the layout view with the new image the user has selected.

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                documentIcon.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    //updateDocument - retrieves the selected document from the database and updates the view.

    private void updateDocument() {

        Response res = serverRequest(new Request(3));
        Document doc = (Document) res.getObject();

        lblTitle.setText(doc.getTitle());
        lblDate.setText("last Updated: " + doc.getLastUpdated());
        documentText.setText(doc.getText());
        lblAuthor.setText("Published by: " + doc.getAuthor().getUsername());
        authorId = doc.getAuthor().getUserID();
        Bitmap bmp = BitmapFactory.decodeByteArray(doc.getImage(), 0, doc.getImage().length);
        documentIcon.setImageBitmap(Bitmap.createScaledBitmap(bmp, 100, 100, false));
    }


    //edit - updates the layout to allow the user to edit the document.

    private void edit(){

        if(btnSave.getVisibility() == btnSave.VISIBLE){
            btnSave.setVisibility(btnSave.INVISIBLE);
            btnChoose.setVisibility(btnChoose.INVISIBLE);
            documentText.setFocusable(false);
            documentText.setFocusableInTouchMode(false);
            lblTitle.setFocusable(false);
            lblTitle.setFocusableInTouchMode(false);
        } else {
            btnSave.setVisibility(btnSave.VISIBLE);
            btnChoose.setVisibility(btnChoose.VISIBLE);
            documentText.setFocusable(true);
            documentText.setFocusableInTouchMode(true);
            lblTitle.setFocusable(true);
            lblTitle.setFocusableInTouchMode(true);
        }
    }

    //Save - sends the updated document object to the server to update the database.

    private void save(){
        Document doc = new Document(documentText.getText().toString());
        doc.setTitle(lblTitle.getText().toString());

        Bitmap bitmap = ((BitmapDrawable) documentIcon.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] image = baos.toByteArray();

        doc.setImage(image);
        Request req = new Request(4, doc);
        serverRequest(req);
        edit();
        updateDocument();
        Toast.makeText(this, "Document Saved", Toast.LENGTH_SHORT).show();

    }

    //viewProfile - sends the user to the profile activity and view of the selected author.

    private void viewProfile(){
        Response res = serverRequest(new Request(7));
        Request req = new Request(6);
        req.setValue(authorId);
        serverRequest(req);
        if(authorId == res.getValue()){
            startActivity(new Intent(this, HomeActivity.class));
        } else {
            startActivity(new Intent(this, ProfileActivity.class));
        }

    }



}
