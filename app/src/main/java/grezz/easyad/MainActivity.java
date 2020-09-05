package grezz.easyad;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import grezz.lib.easyad.EasyAd;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new EasyAd(this)
                .setBannerKey("had-app-D4FEF7B87D45AD6B")
                .setBannerMedRecKey("had-app-8D9B24F82E26F254")
                .setInterstitialKey("had-app-DC2AFE543935FCF4")
                .build();

        final EasyAd.WebBanner webBanner = findViewById(R.id.myBanner);
        final EasyAd.WebBannerMedRec webBannerMedRec = findViewById(R.id.myMedRecBanner);
        final EasyAd.Interstitial inters = new EasyAd.Interstitial(this);

        webBanner.setWebBannerListener(new EasyAd.WebBanner.WebBannerListener() {
            @Override
            public void onLoadListener() {
                Toast.makeText(MainActivity.this, "banner load successfully", Toast.LENGTH_SHORT).show();
                if (webBanner.isLoaded())
                    webBanner.show();
            }

            @Override
            public void onErrorListener() {
                Toast.makeText(MainActivity.this, "error banner load", Toast.LENGTH_SHORT).show();
            }
        });
        webBanner.load();

        webBannerMedRec.setWebMedRecListener(new EasyAd.WebBannerMedRec.WebMedRecListener() {
            @Override
            public void onLoadListener() {
                Toast.makeText(MainActivity.this, "Banner loaded", Toast.LENGTH_SHORT).show();
                if (webBannerMedRec.isLoaded())
                    webBannerMedRec.show();
            }

            @Override
            public void onErrorListener() {
                Toast.makeText(MainActivity.this, "Error loading Banner", Toast.LENGTH_SHORT).show();
            }
        });
        webBannerMedRec.load();

        inters.setInterstitialListener(new EasyAd.Interstitial.InterstitialListener() {
            @Override
            public void onLoadListener() {
                Log.d("Grezz", "inside load listener");
                Toast.makeText(MainActivity.this, "Inters Loaded", Toast.LENGTH_SHORT).show();
                if (inters.isLoaded())
                    inters.show();
            }

            @Override
            public void onCloseListener() {
                Log.d("Grezz", "inside close listener");
            }

            @Override
            public void onErrorListener() {
                Log.d("Grezz", "inside error listener");
                Toast.makeText(MainActivity.this, "Error Loading Inters", Toast.LENGTH_SHORT).show();
            }

        });
        inters.load();
    }

}
