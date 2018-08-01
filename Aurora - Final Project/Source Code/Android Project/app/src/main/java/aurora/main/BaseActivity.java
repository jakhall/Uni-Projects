package aurora.main;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.aurora.main.R;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

import aurora.network.AuroraClient;
import aurora.network.Request;
import aurora.network.Response;
import aurora.network.SocketController;

import static android.R.attr.configure;
import static android.R.attr.id;
import static net.aurora.main.R.id.item;
import static net.aurora.main.R.id.searchBar;

/**
 * BaseActivity - Defines the basic functionality of all activities.
 */

public class BaseActivity extends AppCompatActivity {

    protected Socket clientSocket;
    protected AuroraClient client;
    private EditText searchBar;

    public BaseActivity(){

    }

    //onCreate - Will make the tool bar menu when the activity is created.

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        serverRequest(new Request(0));
        startActivity(new Intent(this, LoginActivity.class));
        finish();
        return true;
    }

    //serverRequest - Sends a request object to the server and returns the servers response.

    protected Response serverRequest(Request request){
        ServerRequest sr = new ServerRequest();
        Response response = null;

        try {
            response = sr.execute(request).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return response;
    }


    //setupSearchbar - adds a listener to allow the user to interact with the searchbar.

    protected void setupSearchbar(){
        searchBar = (EditText) findViewById(R.id.searchBar);

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
    }

    //newSearch - Sends the user to the search view with the inputed query.

    private void newSearch(){
        String query = searchBar.getText().toString();
        serverRequest(new Request(2, query));
        startActivity(new Intent(this, SearchActivity.class));
    }

    //ServerRequest - Starts a seperate task to obtain the server response.

    protected class ServerRequest extends AsyncTask<Request, Void, Response> {

        Socket clientSocket = SocketHandler.getSocket();

        @Override
        protected Response doInBackground(Request... params) {
            ObjectOutputStream objectOut = null;
            ObjectInputStream objectIn = null;
            Request request = params[0];
            Response response = null;

            try {
                objectOut = new ObjectOutputStream(clientSocket.getOutputStream());
                objectIn = new ObjectInputStream(clientSocket.getInputStream());

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                objectOut.writeObject(request);
                response = (Response)objectIn.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            return response;
        }
    }


    //Logout - Sends the user to the login activity and view.

    protected void logout() {
        serverRequest(new Request(0));
        finish();
    }

    //onBackPressed - sends the user to the previous activity and view.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        serverRequest(new Request(1));
    }

    //search - sends the query as a request object to the server.

    protected void search(String query){
        serverRequest(new Request(query));
    }

}
