package kz.sekeww.www.kazakhgreatpeople;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import me.biubiubiu.justifytext.library.JustifyTextView;

import static kz.sekeww.www.kazakhgreatpeople.R.id.imageView;

public class AboutActivity extends AppCompatActivity {


    private String targetURL;
    private TextView textView;
    private ImageView imageView;
    private String image_url;
    private String text="";
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        collapsingToolbar.setTitle("ABcDeFg");
        textView = (TextView) findViewById(R.id.textView);
        imageView  = (ImageView) findViewById(R.id.imageView);

        targetURL = getIntent().getExtras().getString("people_txt_url");
        image_url = getIntent().getExtras().getString("image_url");

        Glide.with(this).load(image_url).into(imageView);

        BackTask bt=new BackTask();
        bt.execute(targetURL);
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
