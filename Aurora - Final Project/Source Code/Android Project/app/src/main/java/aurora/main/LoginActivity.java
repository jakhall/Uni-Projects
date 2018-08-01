package aurora.main;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import net.aurora.main.R;
import java.util.ArrayList;
import aurora.network.AuroraClient;
import aurora.network.Request;

import static android.R.string.ok;
import static aurora.main.SocketHandler.getSocket;
import static net.aurora.main.R.drawable.black_border;
import static net.aurora.main.R.id.btnChoose;
import static net.aurora.main.R.id.btnConnect;
import static net.aurora.main.R.id.btnRegister;
import static net.aurora.main.R.id.btnSave;
import static net.aurora.main.R.id.documentText;
import static net.aurora.main.R.id.lblTitle;


/**
 * Detail Activity - controls the functionality behind the detail view.
 */


public class LoginActivity extends BaseActivity {

    private Button btnLogin;
    private Button btnConnect;
    private Button btnDisconnect;
    private Button btnRegister;
    private Button btnGuide;
    private EditText fldUsername;
    private EditText fldPassword;
    private EditText searchBar;

    //onCreate - creates the toolbar and sets up the layout.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        initialize();
        setupListeners();
        setupData();
    }


    //initialize - assigns the necessary layout elements to variables.

    private void initialize(){
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnGuide = (Button) findViewById(R.id.btnGuide);
        btnDisconnect = (Button) findViewById(R.id.btnDisconnect);
        fldUsername = (EditText) findViewById(R.id.fldUsername);
        fldPassword = (EditText) findViewById(R.id.fldPassword);
        searchBar  = (EditText) findViewById(R.id.searchBar);

    }

    //setupListeners - adds the listeners to all necessary layout elements.

    public void setupListeners() {


        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                login(fldUsername.getText().toString(), fldPassword.getText().toString());
            }
        });



        btnRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
               register();
            }
        });

        btnGuide.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                guide();
            }
        });

        btnConnect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                View ipView = LayoutInflater.from(LoginActivity.this).inflate(R.layout.user_input, null);
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(LoginActivity.this);
                alertBuilder.setView(ipView);
                final EditText ip = (EditText)ipView.findViewById(R.id.userinput);
                ip.setText("192.168.0.11");
                alertBuilder.setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int which){
                                    connect(ip.getText().toString());
                                }
                        });
                Dialog dialog = alertBuilder.create();
                dialog.show();
            }

        });


        btnDisconnect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                disconnect();
            }
        });
    }


    //Connect - creates a new connection to the server and stores it.

    private void connect(String ip){

        client = new AuroraClient(ip);

        if (client.getState() == Thread.State.NEW)
        {
            client.start();
        }
        while(getSocket() == null){
            SocketHandler.setSocket(client.getSocket());
        }

        btnConnect.setVisibility(btnConnect.INVISIBLE);
        btnDisconnect.setVisibility(btnDisconnect.VISIBLE);

        Toast.makeText(LoginActivity.this, "Connected", Toast.LENGTH_SHORT).show();
    }

    //Disconnect - closes the connection to the server and throws the connection away.

    private void disconnect(){
        client.shutdown();
        SocketHandler.setSocket(null);

        btnConnect.setVisibility(btnConnect.VISIBLE);
        btnDisconnect.setVisibility(btnDisconnect.INVISIBLE);

        Toast.makeText(LoginActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
    }


    //setupData - fills the layout elements with data from the server.

    private void setupData(){

        searchBar.setVisibility(searchBar.INVISIBLE);

    }

    //validateUser - finds the user with the inputed username and password if exists.

    public boolean validateUser(String username, String password){
        ArrayList<String> login = new ArrayList<String>();
        login.add(username);
        login.add(password);
        Request req = new Request(3, login);
        return  serverRequest(req).getSwitch();
    }

    //login - if the user exists then it will send the user to the home activity and view for that user.

    public void login(String username, String password){
        if(checkLogin()) {
            if (validateUser(username, password)) {
                Request req = new Request(4);
                serverRequest(req);
                startActivity(new Intent(this, HomeActivity.class));
                return;
            }
        }
        Toast.makeText(this, "Username or password incorrect", Toast.LENGTH_SHORT).show();
    }


    public boolean checkLogin(){

        String u = fldUsername.getText().toString();
        String p = fldPassword.getText().toString();

        if(u.equals("") || p.equals("")){
            return false;
        }

        return true;
    }

    //Register - Sends the user to the register activity and view.

    private void register(){
        serverRequest(new Request(5));
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void guide(){
        serverRequest(new Request(6));
        startActivity(new Intent (this, GuideActivity.class));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.blank_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ActivityCompat.finishAffinity(this);
        System.exit(0);
        return true;
    }

}
