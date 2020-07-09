package grezz.easyad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import grezz.lib.easyad.EasyAd;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EasyAd.WebBanner webBanner = findViewById(R.id.myBanner);
        final EasyAd.WebBannerMedRec webBannerMedRec = findViewById(R.id.myMedRecBanner);
        webBannerMedRec.setWebMedRecListener(new EasyAd.WebBannerMedRec.WebMedRecListener() {
            @Override
            public void onLoadListener() {
                Toast.makeText(MainActivity.this, "inters loaded", Toast.LENGTH_SHORT).show();
                webBannerMedRec.show();
            }

            @Override
            public void onErrorListener() {
                Toast.makeText(MainActivity.this, "Error loading", Toast.LENGTH_SHORT).show();
            }
        });
        webBanner.setWebBannerListener(new EasyAd.WebBanner.WebBannerListener() {
            @Override
            public void onLoadListener() {
                Toast.makeText(MainActivity.this, "banner load successfully", Toast.LENGTH_SHORT).show();
                webBanner.show();
            }

            @Override
            public void onErrorListener() {
                Toast.makeText(MainActivity.this, "error banner load", Toast.LENGTH_SHORT).show();
            }
        });
        webBannerMedRec.load();
        webBanner.load();

        final EasyAd.Interstitial inters = new EasyAd.Interstitial(this);
        inters.setInterstitialListener(new EasyAd.Interstitial.InterstitialListener() {
            @Override
            public void onLoadListener() {
                Log.d("Grezz","inside load listener");
                inters.show();
            }

            @Override
            public void onCloseListener() {
                Log.d("Grezz","inside close listener");
            }

            @Override
            public void onErrorListener() {
                Log.d("Grezz","inside error listener");
                Toast.makeText(MainActivity.this, "Error Loading Inters", Toast.LENGTH_SHORT).show();
            }

        });
        inters.load();
    }

}
