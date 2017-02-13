package kz.sekeww.www.kazakhgreatpeople;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;

import java.util.List;

public class PeopleActivity extends AppCompatActivity {

    private String categoryId;
    private List<People> peoples;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onPeopleClick(position);
            }
        });

        categoryId = getIntent().getExtras().getString("category_id");

        //Log.d("myLog",categoryId+"");
        downloadPeople(categoryId);
    }

    private void onPeopleClick(int position) {
        Intent intent = new Intent(this, AboutActivity.class);
        //Log.d("myLog1",peoples.get(position).getAbout()+"");
        intent.putExtra("people_txt_url",peoples.get(position).getAbout());
        intent.putExtra("image_url",peoples.get(position).getImage());
        intent.putExtra("people_title",peoples.get(position).getTitle());
        startActivity(intent);
    }

    private void downloadPeople(String categoryId) {
       // Log.d("myLog","going To Download People");

        String whereClause = "category.objectId = " + "'" + categoryId + "'";

        BackendlessDataQuery query = new BackendlessDataQuery();
        query.setWhereClause(whereClause);

        Backendless.Persistence.of(People.class).find(query, new AsyncCallback<BackendlessCollection<People>>() {
            @Override
            public void handleResponse(BackendlessCollection<People> response) {
               // Log.d("myLog",response.getData()+"");
                displayPeople(response.getData());
            }



            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }

    private void displayPeople(List<People> peoples) {
        this.peoples=peoples;
        PeopleAdapter adapter = new PeopleAdapter(this, peoples);
        listView.setAdapter(adapter);
    }
}
