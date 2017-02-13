package kz.sekeww.www.kazakhgreatpeople;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private ListView listView;
    private List<Category> categories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

//        ActivityCompat.requestPermissions(CategoryActivity.this,
//                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                1);

        Backendless.initApp(this, Konst.APP_ID,Konst.ANDROID_KEY,"v1");
        downloadCategories();

        listView  = (ListView) findViewById(R.id.listView);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                openPeople(position);
            }
        });


    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(CategoryActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void openPeople(int position) {
        Intent intent = new Intent(this, PeopleActivity.class);
        Log.d("myLog1",categories.get(position).getImage()+"");
        intent.putExtra("category_id",categories.get(position).getObjectId());
        startActivity(intent);
    }

    private void downloadCategories(){

        Backendless.Persistence.of(Category.class).find(new AsyncCallback<BackendlessCollection<Category>>() {
            @Override
            public void handleResponse(BackendlessCollection<Category> response) {
                //Log.d("my_log",response+" my response is");

                displayCategories(response.getData());
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                //Log.e("my_log","some error "+ fault.getMessage());
            }
        });
    }

    private void displayCategories(List<Category> categories) {
        this.categories=categories;
        CategoryAdapter adapter = new CategoryAdapter(this, categories);
        listView.setAdapter(adapter);
    }
}
