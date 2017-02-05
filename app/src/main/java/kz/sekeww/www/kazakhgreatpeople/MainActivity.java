package kz.sekeww.www.kazakhgreatpeople;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "my_log";
    private String newString;
    private ListView listView;
    private List<Category> batyrs;
    private Map<String, Class> itemsBat = new HashMap<>();
    private Class Aman;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

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

        itemsBat.put("Category", Category.class);


        listView  = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openDescription(position);

            }
        });

        Backendless.initApp(this, Konst.APP_ID,Konst.ANDROID_KEY,"v1");
        downloadBatyrs();
    }

    private void openDescription(int position) {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("batyr_id",batyrs.get(position).getObjectId());
        startActivity(intent);
    }

    private void downloadBatyrs(){

        Backendless.Persistence.of(itemsBat.get(newString)).find(new AsyncCallback<BackendlessCollection<Category>>() {
            @Override
            public void handleResponse(BackendlessCollection<Category> response) {
                Log.d(TAG,response.getData()+"");

                displayBatyrs(response.getData());
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e(TAG,"some error "+ fault.getMessage());
            }
        });
    }

    private void displayBatyrs(List<Category> batyrs) {
        this.batyrs=batyrs;
        CategoryAdapter adapter = new CategoryAdapter(this, batyrs);
        listView.setAdapter(adapter);
    }
}
