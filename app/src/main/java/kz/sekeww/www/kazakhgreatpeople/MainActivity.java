package kz.sekeww.www.kazakhgreatpeople;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton batyry;
    private ImageButton akyny;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        batyry = (ImageButton) findViewById(R.id.batyryImageButton);
        batyry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBatyryClick();
            }
        });

        akyny = (ImageButton) findViewById(R.id.akynyImageButton);
        akyny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAkynyClick();
            }
        });
    }

    private void onAkynyClick() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,DetailsActivity.class);
        String strName = "Akyn";
        intent.putExtra("STRING_I_NEED",strName);
        startActivity(intent);
    }

    private void onBatyryClick() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,DetailsActivity.class);
        String strName = "Batyr";
        intent.putExtra("STRING_I_NEED",strName);
        startActivity(intent);
    }
}
