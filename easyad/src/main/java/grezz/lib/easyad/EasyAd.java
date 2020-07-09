package grezz.lib.easyad;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Soufiane on 04,July,2020
 * https://www.isoufiane.com
 */
public class EasyAd {
    Context context;
    Activity activity;
    RelativeLayout layoutContainer;
    WebView webView;

    public EasyAd() {
    }

    // init with key
    // lookup the key on server get necessary info .. update shared preferences data

    public static class WebBanner extends RelativeLayout {

        WebView bannerWeb;
        LayoutInflater mInflater;
        WebBannerListener webBannerListener;
        boolean loadError = false;

        public interface WebBannerListener {
            void onLoadListener();

            void onErrorListener();
        }

        public WebBanner(Context context) {
            super(context);
            mInflater = LayoutInflater.from(context);
            init();
        }

        public WebBanner(Context context, AttributeSet attrs) {
            super(context, attrs);
            mInflater = LayoutInflater.from(context);
            init();
        }

        public WebBanner(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            mInflater = LayoutInflater.from(context);
            init();
        }

        public WebBanner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            mInflater = LayoutInflater.from(context);
            init();
        }

        private void init() {
            View view = mInflater.inflate(R.layout.banner_web, this, true);
            bannerWeb = view.findViewById(R.id.banner_web);
            bannerWeb.setVerticalScrollBarEnabled(false);
            bannerWeb.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    if (!loadError) {
                        webBannerListener.onLoadListener();
                        show();
                    }
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    webBannerListener.onErrorListener();
                    loadError = true;
                    hide();
                }

                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                        view.getContext().startActivity(
                                new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            hide();
        }

        public void load() {
            bannerWeb.loadUrl("https://api.grezz.dev/house-ad/banner.php");
        }

        public void show() {
            this.setVisibility(VISIBLE);
        }

        public void hide() {
            this.setVisibility(GONE);
        }

        public void setWebBannerListener(WebBannerListener webBannerListener) {
            this.webBannerListener = webBannerListener;
        }
    }

    public static class WebBannerMedRec extends RelativeLayout {

        LayoutInflater mInflater;
        WebView webView;
        WebMedRecListener webMedRecListener;
        boolean loadError;

        public interface WebMedRecListener {
            void onLoadListener();

            void onErrorListener();
        }

        public WebBannerMedRec(Context context) {
            super(context);
            mInflater = LayoutInflater.from(context);
            init();
        }

        public WebBannerMedRec(Context context, AttributeSet attrs) {
            super(context, attrs);
            mInflater = LayoutInflater.from(context);
            init();
        }

        public WebBannerMedRec(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            mInflater = LayoutInflater.from(context);
            init();
        }

        public WebBannerMedRec(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            mInflater = LayoutInflater.from(context);
            init();
        }

        private void init() {
            View view = mInflater.inflate(R.layout.banner_med_rec_web, this, true);
            view.setVisibility(GONE);
            webView = view.findViewById(R.id.medRec_web);
            webView.setVerticalScrollBarEnabled(false);
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    if (!loadError)
                        webMedRecListener.onLoadListener();
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    webMedRecListener.onErrorListener();
                    loadError = true;
                }

                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                        view.getContext().startActivity(
                                new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        return true;
                    } else {
                        return false;
                    }
                }

            });
        }

        public void load() {
            loadError = false;
            webView.loadUrl("https://api.grezz.dev/house-ad/banner-medRec.php");
        }

        public void show() {
            this.setVisibility(VISIBLE);
        }

        public void hide() {
            this.setVisibility(GONE);
        }

        public void setWebMedRecListener(WebMedRecListener webMedRecListener) {
            this.webMedRecListener = webMedRecListener;
        }

    }

    public static class Interstitial {

        WebView webView;
        ImageView btnClose;
        String url = "https://api.grezz.dev/house-ad/inters.php";
        InterstitialListener interstitialListener;
        Dialog d;

        boolean loadError;


        public Interstitial(Context context) {
            init(context);
        }

        private void init(Context context) {
            d = new Dialog(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            if (inflater == null)
                return;
            View view = inflater.inflate(R.layout.interstitial_web, new RelativeLayout(context), false);
            webView = view.findViewById(R.id.inters_web);
            btnClose = view.findViewById(R.id.inters_close_btn);

            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    interstitialListener.onCloseListener();
                    close();
                }
            });

            webView.setVerticalScrollBarEnabled(false);
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    Log.d("Grezz","page started loading");
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    if (!loadError)
                        interstitialListener.onLoadListener();
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    interstitialListener.onErrorListener();
                    loadError = true;
                }

                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                        view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        return true;
                    } else {
                        return false;
                    }
                }

            });
            d.setContentView(view);

            // Copy Default params then change width & height
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = d.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                lp.copyFrom(window.getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(lp);
            }
            d.create();
            Log.d("Grezz","inside init");
        }

        public void load(){
            webView.loadUrl(url);
        }

        public void show(){
            d.show();
        }

        public void hide(){
            d.hide();
        }

        public void destroy(){
            d.dismiss();
        }

        private void close(){
            d.dismiss();
        }

        // Interface
        public interface InterstitialListener {
            void onLoadListener();
            void onCloseListener();
            void onErrorListener();
        }

        public void setInterstitialListener(InterstitialListener interstitialListener) {
            this.interstitialListener = interstitialListener;
        }
    }

}
