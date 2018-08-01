package aurora.main;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import net.aurora.main.R;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import aurora.model.Profile;
import aurora.network.Request;
import aurora.network.Response;

/**
 * Detail Activity - controls the functionality behind the detail view.
 */

public class DetailActivity extends BaseActivity {

    private Button btnLogout;
    private RelativeLayout layout;
    private Button btnEdit;
    private Button btnSave;
    private Button btnChoose;
    private EditText fldUsername;
    private EditText fldFirstName;
    private EditText fldLastName;
    private EditText fldEmail;
    private EditText fldTitle;
    private EditText fldAge;
    private EditText fldPassword;
    private EditText fldConfirm;
    private ImageView profileIcon;
    private TextView lblPassword;
    private TextView lblConfirm;

    private Profile theUser;


    //onCreate - creates the toolbar and sets up the layout.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        initialize();
        setupData();
        setupListeners();
    }


    //initialize - assigns the necessary layout elements to variables.

    private void initialize(){
        btnSave = (Button) findViewById(R.id.btnSave);
        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnChoose = (Button) findViewById(R.id.btnChoose);
        fldUsername = (EditText) findViewById(R.id.fldUsername);
        fldFirstName = (EditText) findViewById(R.id.fldFirstName);
        fldLastName = (EditText) findViewById(R.id.fldLastName);
        fldEmail = (EditText) findViewById(R.id.fldEmail);
        fldTitle = (EditText) findViewById(R.id.fldTitle);
        fldAge = (EditText) findViewById(R.id.fldAge);
        fldPassword = (EditText) findViewById(R.id.fldPassword);
        fldConfirm = (EditText) findViewById(R.id.fldConfirm);
        profileIcon = (ImageView) findViewById(R.id.profileIcon);
        layout = (RelativeLayout) findViewById(R.id.layout);
        lblPassword = (TextView) findViewById(R.id.lblPassword);
        lblConfirm = (TextView) findViewById(R.id.lblConfirm);

    }

    //setupListeners - adds the listeners to all necessary layout elements.

    private void setupListeners(){

        setupSearchbar();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                edit();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                save();
            }
        });


        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(btnSave.getVisibility() == btnSave.VISIBLE) {
                    getImage();
                }
            }
        });

    }


    //checkEditable - check whether the user is allowed the edit the document.

    private void checkEditable(){

        if(serverRequest(new Request(7)).getSwitch()){
            btnEdit.setVisibility(btnEdit.INVISIBLE);
            fldPassword.setVisibility(fldPassword.INVISIBLE);
            fldConfirm.setVisibility(fldConfirm.INVISIBLE);
            lblPassword.setVisibility(lblPassword.INVISIBLE);
            lblConfirm.setVisibility(lblConfirm.INVISIBLE);
        }

    }


    //edit - updates the layout to allow the user to edit the details.

    private void edit(){

        if(btnSave.getVisibility() == btnSave.VISIBLE){
            btnSave.setVisibility(btnSave.INVISIBLE);
            btnChoose.setVisibility(btnSave.INVISIBLE);
            fldFirstName.setFocusableInTouchMode(false);
            fldLastName.setFocusableInTouchMode(false);
            fldEmail.setFocusableInTouchMode(false);
            fldTitle.setFocusableInTouchMode(false);
            fldAge.setFocusableInTouchMode(false);
            fldPassword.setFocusableInTouchMode(false);
            fldConfirm.setFocusableInTouchMode(false);
            layout.requestFocus();
        } else {
            btnSave.setVisibility(btnSave.VISIBLE);
            btnChoose.setVisibility(btnSave.VISIBLE);
            fldFirstName.setFocusableInTouchMode(true);
            fldLastName.setFocusableInTouchMode(true);
            fldEmail.setFocusableInTouchMode(true);
            fldTitle.setFocusableInTouchMode(true);
            fldAge.setFocusableInTouchMode(true);
            fldPassword.setFocusableInTouchMode(true);
            fldConfirm.setFocusableInTouchMode(true);
        }
    }


    //passwordCheck - makes sure the password and confirmation password are the same.

    private boolean passwordCheck(){

        if(fldPassword.getText().toString().equals(fldConfirm.getText().toString())){
            return true;
        }

        Toast.makeText(this, "Password Invalid", Toast.LENGTH_SHORT).show();

        return false;
    }

    //dateCheck - Makes sure the date follows the correct format.

    private boolean dateCheck(){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        format.setLenient(false);
        String dob = fldAge.getText().toString();

        try {
            format.parse(dob);
            if(dob.length() != 10) {
                Toast.makeText(this, "Invalid Date Format (dd/mm/yyyy)", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e) {
            Toast.makeText(this, "Invalid Date Format (dd/mm/yyyy)", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    //Save - sends the updated user object to the server to update the database.

    private void save(){
        boolean valid = passwordCheck();
        valid = dateCheck();
        if(valid) {
            Profile user = new Profile();
            user.setUsername(fldUsername.getText().toString());
            user.setFirstName(fldFirstName.getText().toString());
            user.setLastName(fldLastName.getText().toString());
            user.setEmail(fldEmail.getText().toString());
            user.setTitle(fldTitle.getText().toString());
            user.setDOB(fldAge.getText().toString());

            if (fldPassword.getText().toString().equals("")) {
                user.setPassword(theUser.getPassword());
            } else {
                user.setPassword(fldPassword.getText().toString());
            }

            Bitmap bitmap = ((BitmapDrawable) profileIcon.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            user.setImage(baos.toByteArray());

            serverRequest(new Request(6, user));

            edit();

            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show();
        }
    }

    //setupData - fills the layout elements with data from the server.

    private void setupData(){
        checkEditable();
        userDetails();
    }


    //userDetails - updates the user details with the details obtained from the server.

    private void userDetails(){
        Response res = serverRequest(new Request(3));
        Profile user = (Profile)res.getObject();
        fldUsername.setText(user.getUsername());
        fldFirstName.setText(user.getFirstName());
        fldLastName.setText(user.getLastName());
        fldEmail.setText(user.getEmail());
        fldTitle.setText(user.getTitle());
        fldAge.setText(String.valueOf(user.getDOB()));
        theUser = user;

        res = serverRequest(new Request(4));

        byte[] bytes = (byte[])res.getObject();
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        profileIcon.setImageBitmap(Bitmap.createScaledBitmap(bmp, 100, 100, false));

    }

    int GALLERY_REQUEST = 1;


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
                profileIcon.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    //viewUsers - sends the user to the observe activity and view.

    private void viewUsers(){
        serverRequest(new Request(5));
        startActivity(new Intent(this, ObserveActivity.class));
    }


    //viewDocuments - sends the user to the DocList activity and view.

    private void viewDocuments(){
        serverRequest(new Request(6));
        startActivity(new Intent(this, DocListActivity.class));
    }

}