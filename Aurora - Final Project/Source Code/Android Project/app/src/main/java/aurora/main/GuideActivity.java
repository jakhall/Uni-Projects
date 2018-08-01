package aurora.main;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.aurora.main.R;

import aurora.network.Request;

/**
 * Created by jakha on 07/05/2018.
 */

public class GuideActivity extends BaseActivity {



    private Button btnReturn;
    private EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_guide);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        initialize();
        setupData();
        setupListeners();
    }

    private void initialize(){
        searchBar = (EditText) findViewById(R.id.searchBar);
        btnReturn = (Button) findViewById(R.id.btnReturn);
    }


    private void setupData(){
        searchBar.setVisibility(searchBar.INVISIBLE);
    }

    private void setupListeners(){
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnLogin();
            }
        });
    }


    private void returnLogin(){
        serverRequest(new Request(1));
        this.finish();
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
