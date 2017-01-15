package kz.sekeww.www.kazakhgreatpeople;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = "my_log";
    private String newString;
    private ListView listView;
    private Map<String, Class> itemsBat = new HashMap<>();
    private Class Aman;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("STRING_I_NEED");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("STRING_I_NEED");
        }

        itemsBat.put("Batyr", Batyr.class);
        itemsBat.put("Akyn", Akyn.class);

        listView  = (ListView) findViewById(R.id.listView);

        Backendless.initApp(this, Konst.APP_ID,Konst.ANDROID_KEY,"v1");
        downloadBatyrs();
    }

    private void downloadBatyrs(){



        Backendless.Persistence.of(itemsBat.get(newString)).find(new AsyncCallback<BackendlessCollection<Batyr>>() {
            @Override
            public void handleResponse(BackendlessCollection<Batyr> response) {
                Log.d(TAG,response.getData().toString());

                displayBatyrs(response.getData());
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e(TAG,"some error "+ fault.getMessage());
            }
        });
    }

    private void displayBatyrs(List<Batyr> batyrs) {

        BatyrAdapter adapter = new BatyrAdapter(this, batyrs);
        listView.setAdapter(adapter);
    }
}
