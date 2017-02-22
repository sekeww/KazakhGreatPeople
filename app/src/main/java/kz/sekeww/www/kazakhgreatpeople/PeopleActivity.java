package kz.sekeww.www.kazakhgreatpeople;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

public class PeopleActivity extends AppCompatActivity {

    private String categoryId;
    private List<People> peoples;
    private ListView listView;
    private ProgressDialog pd;
    private LinearLayout linearLayoutPeeopleAct;
    private int thePosition;

    private InterstitialAd interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        linearLayoutPeeopleAct = (LinearLayout) findViewById(R.id.linearLayoutPeeopleAct);
        listView = (ListView) findViewById(R.id.listView);

        categoryId = getIntent().getExtras().getString("category_id");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder().addTestDevice("2FC2BC09C96DC70DE1A7EAF9FEFC4941").addTestDevice("27B1B3B72C8B485FEA61CFA654562346").build();
        mAdView.loadAd(adRequest);

        interstitial = new InterstitialAd(getApplicationContext());
        interstitial.setAdUnitId(getResources().getString(R.string.ads_interstitialBanner_id));

        interstitial.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                onPeopleClick(thePosition);
            }
        });

        // Begin loading your interstitial.
        requestNewInterstitial();   // если закоментировать, то утечки не будет

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                thePosition = position;
                if (interstitial.isLoaded()) {
                    interstitial.show();
                } else {
                    onPeopleClick(position);
                }
            }
        });

        //Log.d("myLog",categoryId+"");
        downloadPeople(categoryId);
    }

    private void requestNewInterstitial() {
        AdRequest adRequest1 = new AdRequest.Builder().addTestDevice("2FC2BC09C96DC70DE1A7EAF9FEFC4941").addTestDevice("27B1B3B72C8B485FEA61CFA654562346").build();
        interstitial.loadAd(adRequest1);
    }

    private void onPeopleClick(int position) {

            Intent intent = new Intent(this, AboutActivity.class);
            //Log.d("myLog1",peoples.get(position).getAbout()+"");
            intent.putExtra("people_txt_url", peoples.get(position).getAbout());
            intent.putExtra("image_url", peoples.get(position).getImage());
            intent.putExtra("people_title", peoples.get(position).getTitle());
            startActivity(intent);
    }

    private void downloadPeople(final String categoryId) {
       // Log.d("myLog","going To Download People");

        pd = new ProgressDialog(PeopleActivity.this);
        pd.setTitle("Сервермен байланыс орнатылуда...");
        pd.setMessage("Ғаламтор қосылымын тексеріңіз!\nБұл 2 минут уақыт алуы мүмкін...");
        pd.setCancelable(true);
        pd.setIndeterminate(false);
        pd.show();

        String whereClause = "category.objectId = " + "'" + categoryId + "'";

        BackendlessDataQuery query = new BackendlessDataQuery();
        query.setPageSize(50);
        query.setWhereClause(whereClause);

        Backendless.Persistence.of(People.class).find(query, new AsyncCallback<BackendlessCollection<People>>() {
            @Override
            public void handleResponse(BackendlessCollection<People> response) {
               // Log.d("myLog",response.getData()+"");
                if(pd!=null)
                    pd.dismiss();
                displayPeople(response.getData());
            }



            @Override
            public void handleFault(BackendlessFault fault) {
                if(pd!=null)
                    pd.dismiss();
                Snackbar snackbar = Snackbar
                        .make(linearLayoutPeeopleAct, "Ғаламтор қосылымы жоқ!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Қайталау", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                downloadPeople(categoryId);
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

    private void displayPeople(List<People> peoples) {
        this.peoples=peoples;
        PeopleAdapter adapter = new PeopleAdapter(this, peoples);
        listView.setAdapter(adapter);
    }
}
