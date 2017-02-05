package kz.sekeww.www.kazakhgreatpeople;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class CategoryActivity extends AppCompatActivity {

    private ListView listView;
    private List<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Backendless.initApp(this, Konst.APP_ID,Konst.ANDROID_KEY,"v1");
        downloadCategories();

        listView  = (ListView) findViewById(R.id.listView);

        Log.d("myLog11",categories+"");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openPeople(position);
            }
        });
    }

    private void openPeople(int position) {
        Intent intent = new Intent(this, PeopleActivity.class);
        Log.d("myLog1",categories.get(position).getObjectId()+"");
        intent.putExtra("category_id",categories.get(position).getObjectId());
        startActivity(intent);
    }

    private void downloadCategories(){

        Backendless.Persistence.of(Category.class).find(new AsyncCallback<BackendlessCollection<Category>>() {
            @Override
            public void handleResponse(BackendlessCollection<Category> response) {
                //Log.d(TAG,response.getData()+"");

                displayCategories(response.getData());
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e("my_log","some error "+ fault.getMessage());
            }
        });
    }

    private void displayCategories(List<Category> categories) {
        this.categories=categories;
        CategoryAdapter adapter = new CategoryAdapter(this, categories);
        listView.setAdapter(adapter);
    }
}
