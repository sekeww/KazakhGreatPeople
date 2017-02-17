package kz.sekeww.www.kazakhgreatpeople;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class AboutActivity extends AppCompatActivity {

    private String targetURL;
    private TextView textView;
    private ImageView imageView;
    private String image_url;
    private String people_title;
    private String text="";
    ProgressDialog pd;

    private InterstitialAd interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder().addTestDevice("2FC2BC09C96DC70DE1A7EAF9FEFC4941").build();
        mAdView.loadAd(adRequest);

        interstitial = new InterstitialAd(getApplicationContext());
        interstitial.setAdUnitId(getResources().getString(R.string.ads_interstitialBanner_id));

        interstitial.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                finish();
            }
        });

        // Begin loading your interstitial.
        requestNewInterstitial();   // если закоментировать, то утечки не будет

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);


        textView = (TextView) findViewById(R.id.textView);
        imageView  = (ImageView) findViewById(R.id.imageView);

        targetURL = getIntent().getExtras().getString("people_txt_url");
        image_url = getIntent().getExtras().getString("image_url");
        people_title = getIntent().getExtras().getString("people_title");

        collapsingToolbar.setTitle(people_title);
        Glide.with(this).load(image_url).into(imageView);

        BackTask bt=new BackTask();
        bt.execute(targetURL);
    }

    private void requestNewInterstitial() {
        AdRequest adRequest1 = new AdRequest.Builder().addTestDevice("2FC2BC09C96DC70DE1A7EAF9FEFC4941").build();
        interstitial.loadAd(adRequest1);
    }

    @Override
    public void onBackPressed() {
        if (interstitial.isLoaded()) {
            interstitial.show();
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (interstitial.isLoaded()) {
                    interstitial.show();
                } else {
                    // app icon in action bar clicked; goto parent activity.
                    this.finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class BackTask extends AsyncTask<String,Integer,Void> {

        protected void onPreExecute(){
            super.onPreExecute();
            //display progress dialog
            pd = new ProgressDialog(AboutActivity.this);
            pd.setTitle("Мәтін дайындалуда");
            pd.setMessage("күтіңіз...");
            pd.setCancelable(true);
            pd.setIndeterminate(false);
            pd.show();
        }

        protected Void doInBackground(String...params){
            URL url;

            try {
                //create url object to point to the file location on internet
                url = new URL(params[0]);
                //make a request to server
                HttpURLConnection con=(HttpURLConnection)url.openConnection();
                //get InputStream instance
                InputStream is=con.getInputStream();
                //create BufferedReader object
                BufferedReader br=new BufferedReader(new InputStreamReader(is));
                String line;
                //read content of the file line by line
                while((line=br.readLine())!=null){
                    text+=line;
                }

                br.close();

            }catch (Exception e) {
                e.printStackTrace();
                //close dialog if error occurs
                if(pd!=null) pd.dismiss();
            }

            return null;
        }


        protected void onPostExecute(Void result){
            //close dialog
            if(pd!=null)
                pd.dismiss();

            Log.e(AboutActivity.class.getSimpleName(), "text: " + text);

            //display read text in TextVeiw
            textView.setText(text);
        }
    }


}
