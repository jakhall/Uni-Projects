package aurora.main;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.aurora.main.R;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import aurora.model.Profile;
import aurora.network.Request;
import aurora.network.Response;

import static net.aurora.main.R.id.btnEdit;
import static net.aurora.main.R.id.btnRegister;
import static net.aurora.main.R.id.btnSave;
import static net.aurora.main.R.id.lblConfirm;
import static net.aurora.main.R.id.lblPassword;


/**
 * Detail Activity - controls the functionality behind the detail view.
 */


public class RegisterActivity extends BaseActivity {

    private Button btnRegister;
    private RelativeLayout layout;
    private Button btnChoose;
    private EditText fldUsername;
    private EditText fldFirstName;
    private EditText fldLastName;
    private EditText fldEmail;
    private EditText fldTitle;
    private EditText fldAge;
    private EditText fldPassword;
    private EditText fldConfirm;
    private EditText searchBar;
    private ImageView profileIcon;


    //onCreate - creates the toolbar and sets up the layout.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        initialize();
        setupData();
        setupListeners();
    }

    //initialize - assigns the necessary layout elements to variables.


    private void initialize(){
        btnRegister = (Button) findViewById(R.id.btnRegister);
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
        searchBar = (EditText) findViewById(R.id.searchBar);

    }

    //setupListeners - adds the listeners to all necessary layout elements.

    private void setupListeners(){


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                registerUser();
            }
        });

        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                    getImage();
            }
        });

    }

    //passwordCheck - makes sure the password and confirmation password are the same and not null.

    private boolean passwordCheck(){
        String password = fldPassword.getText().toString();
        if(password.equals(fldConfirm.getText().toString()) && (!password.equals(""))){
            return true;
        }

        Toast.makeText(this, "Password Invalid", Toast.LENGTH_SHORT).show();

        return false;
    }


    //registerUser - creates a new Profile object and sends it to the server to be added.

    private void registerUser(){

        boolean valid = passwordCheck();
        valid = dateCheck();
        valid = usernameCheck();

        if(valid) {
            Profile user = new Profile();
            user.setUsername(fldUsername.getText().toString());
            user.setFirstName(fldFirstName.getText().toString());
            user.setLastName(fldLastName.getText().toString());
            user.setEmail(fldEmail.getText().toString());
            user.setTitle(fldTitle.getText().toString());
            user.setDOB(fldAge.getText().toString());
            user.setPassword(fldPassword.getText().toString());

            serverRequest(new Request(3, user));

            Bitmap bitmap = ((BitmapDrawable) profileIcon.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] image = baos.toByteArray();
            serverRequest(new Request(5, image));
            Toast.makeText(this, "Registration Complete", Toast.LENGTH_SHORT).show();

            login();
         }
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

    //usernameCheck - checks if the username is taken or invalid.

    private boolean usernameCheck(){
        String username = fldUsername.getText().toString();
        Response res = serverRequest(new Request(6, username));
        if(res.getSwitch()){
            Toast.makeText(this, "Username is already taken", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    //sends the user to the Home activity and view.

    private void login(){
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }


    //setupData - fills the layout elements with data from the server.

    private void setupData(){
        searchBar.setVisibility(searchBar.INVISIBLE);
        userDetails();
    }


    //userDetails - gets the default user details from the server.

    private void userDetails(){

        Response res = serverRequest(new Request(4));

        byte[] bytes = (byte[])res.getObject();
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        profileIcon.setImageBitmap(Bitmap.createScaledBitmap(bmp, 100, 100, false));

    }

    //getImage - Creates a new popup which allows the user to search their gallery for an image to insert.

    private void getImage() {
        int GALLERY_REQUEST = 1;
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.blank_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ActivityCompat.finishAffinity(this);
        ActivityCompat.finishAffinity(this);
        System.exit(0);
        return true;
    }


}