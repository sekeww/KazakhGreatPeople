package kz.sekeww.www.kazakhgreatpeople;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private ListView listView;
    private List<Category> categories;
    private LinearLayout linearLayout;
    private ProgressDialog pd;
    private int thePosition;

    private InterstitialAd interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        listView  = (ListView) findViewById(R.id.listView);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        Backendless.initApp(this, Konst.APP_ID,Konst.ANDROID_KEY,"v1");
        MobileAds.initialize(getApplicationContext(),getResources().getString(R.string.ads_app_id));

        interstitial = new InterstitialAd(getApplicationContext());
        interstitial.setAdUnitId(getResources().getString(R.string.ads_interstitialBanner_id));

        interstitial.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                openPeople(thePosition);
            }
        });

        // Begin loading your interstitial.
        requestNewInterstitial();

        downloadCategories();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                thePosition = position;
                if (interstitial.isLoaded()) {
                    interstitial.show();
                } else {
                    openPeople(position);
                }
            }
        });
    }

    private void requestNewInterstitial() {
        AdRequest adRequest1 = new AdRequest.Builder().addTestDevice("2FC2BC09C96DC70DE1A7EAF9FEFC4941").addTestDevice("27B1B3B72C8B485FEA61CFA654562346").build();
        interstitial.loadAd(adRequest1);
    }

    private void openPeople(int position) {
        Intent intent = new Intent(this, PeopleActivity.class);
        Log.d("myLog1",categories.get(position).getImage()+"");
        intent.putExtra("category_id",categories.get(position).getObjectId());
        startActivity(intent);
    }

    private void downloadCategories(){

        pd = new ProgressDialog(CategoryActivity.this);
        pd.setTitle("Сервермен байланыс орнатылуда...");
        pd.setMessage("Ғаламтор қосылымын тексеріңіз!\nБұл 2 минут уақыт алуы мүмкін...");
        pd.setCancelable(true);
        pd.setIndeterminate(false);
        pd.show();

        Backendless.Persistence.of(Category.class).find(new AsyncCallback<BackendlessCollection<Category>>() {

            @Override
            public void handleResponse(BackendlessCollection<Category> response) {
                //Log.d("my_log",response+" my response is");
                if(pd!=null)
                    pd.dismiss();
                displayCategories(response.getData());
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if(pd!=null)
                    pd.dismiss();
                Snackbar snackbar = Snackbar
                        .make(linearLayout, "Ғаламтор қосылымы жоқ!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Қайталау", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                downloadCategories();
                            }
                        });

// Changing message text color
                snackbar.setActionTextColor(Color.RED);

// Changing action button text color
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                snackbar.show();
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
