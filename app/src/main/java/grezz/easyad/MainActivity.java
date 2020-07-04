package grezz.easyad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;

import grezz.lib.easyad.EasyAd;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EasyAd.WebBanner webBanner = findViewById(R.id.myBanner);
        final EasyAd.WebInters webInters = findViewById(R.id.myInters);
        webInters.setWebIntersListener(new EasyAd.WebInters.WebIntersListener() {
            @Override
            public void onCloseListener() {
                Toast.makeText(MainActivity.this, "close clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoadListener() {
                Toast.makeText(MainActivity.this, "inters loaded", Toast.LENGTH_SHORT).show();
                webInters.show();
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
            }

            @Override
            public void onErrorListener() {
                Toast.makeText(MainActivity.this, "error banner load", Toast.LENGTH_SHORT).show();
            }
        });
        webInters.load();
        webBanner.load();
    }
}
