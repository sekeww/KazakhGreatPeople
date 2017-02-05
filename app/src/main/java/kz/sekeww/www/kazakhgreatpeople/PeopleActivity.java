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
import com.backendless.persistence.BackendlessDataQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeopleActivity extends AppCompatActivity {

    private String categoryId;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        listView = (ListView) findViewById(R.id.listView);

        categoryId = getIntent().getExtras().getString("category_id");

        Log.d("myLog",categoryId+"");
        downloadPeople(categoryId);
    }

    private void downloadPeople(String categoryId) {
        Log.d("myLog","going To Download People");

        String whereClause = "category.objectId = " + "'" + categoryId + "'";

        BackendlessDataQuery query = new BackendlessDataQuery();
        query.setWhereClause(whereClause);

        Backendless.Persistence.of(People.class).find(query, new AsyncCallback<BackendlessCollection<People>>() {
            @Override
            public void handleResponse(BackendlessCollection<People> response) {
                Log.d("myLog",response.getData()+"");
                displayPeople(response.getData());
            }



            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }

    private void displayPeople(List<People> peoples) {
        PeopleAdapter adapter = new PeopleAdapter(this, peoples);
        listView.setAdapter(adapter);
    }
}
