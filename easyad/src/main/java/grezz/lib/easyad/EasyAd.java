package grezz.lib.easyad;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.http.SslError;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Soufiane on 04,July,2020
 * https://www.isoufiane.com
 */
@SuppressLint("SetJavaScriptEnabled")
public class EasyAd {

    // url format : https://api.grezz.dev/house-ad/banner.php?key=EASAD-JKLksFJKD982KJJSHD2

    private String BASE_URL = "https://api.grezz.dev/house-ad/";
    private String URL_AD_TYPE_BANNER = "banner.php";
    private String URL_AD_TYPE_INTERSTITIAL = "inters.php";
    private String URL_AD_TYPE_BANNER_MED_REC = "banner-medRec.php";

    private String EASAD_PREF = "EASAD_PREFERENCES_KEY";
    private String BANNER_LABEL = "EASAD_BANNER_KEY";
    private String BANNER_MED_REC_LABEL = "EASAD_BANNER_MED_REC_KEY";
    private String INTERSTITIAL_LABEL = "EASAD_INTERSTITIAL_KEY";

    private String bannerKey;
    private String bannerMedRecKey;
    private String interstitialKey;
    private Context context;

    public enum AdType {
        BANNER,
        BANNER_MED_REQ,
        INTERSTITIAL
    }

    public EasyAd(Context context) {
        this.context = context;
    }

    public EasyAd setBannerKey(String bannerKey) {
        this.bannerKey = bannerKey;
        return this;
    }

    public EasyAd setInterstitialKey(String interstitialKey) {
        this.interstitialKey = interstitialKey;
        return this;
    }

    public EasyAd setBannerMedRecKey(String bannerMedRecKey) {
        this.bannerMedRecKey = bannerMedRecKey;
        return this;
    }

    public void build() {
        SharedPreferences prefs = context.getSharedPreferences(EASAD_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if (bannerKey != null)
            editor.putString(BANNER_LABEL, bannerKey);
        if (bannerMedRecKey != null)
            editor.putString(BANNER_MED_REC_LABEL, bannerMedRecKey);
        if (interstitialKey != null)
            editor.putString(INTERSTITIAL_LABEL, interstitialKey);
        editor.apply();
    }

    private String getKey(String LABEL) {
        SharedPreferences prefs = context.getSharedPreferences(EASAD_PREF, Context.MODE_PRIVATE);
        return prefs.getString(LABEL, "-1");
    }

    private String buildUrl(AdType adType) {
        String url = "-1";
        switch (adType) {
            case BANNER:
                if (!getKey(BANNER_LABEL).equals("-1"))
                    url = BASE_URL + URL_AD_TYPE_BANNER + "?key=" + getKey(BANNER_LABEL);
                break;
            case BANNER_MED_REQ:
                if (!getKey(BANNER_MED_REC_LABEL).equals("-1"))
                    url = BASE_URL + URL_AD_TYPE_BANNER_MED_REC + "?key=" + getKey(BANNER_MED_REC_LABEL);
                break;
            case INTERSTITIAL:
                if (!getKey(INTERSTITIAL_LABEL).equals("-1"))
                    url = BASE_URL + URL_AD_TYPE_INTERSTITIAL + "?key=" + getKey(INTERSTITIAL_LABEL);
        }
        Log.d("Grezz", "url: " + url);
        return url;
    }

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
            hide();
            View view = mInflater.inflate(R.layout.banner_web, this, true);
            WebSettings webSettings = bannerWeb.getSettings();
            webSettings.setJavaScriptEnabled(true);
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
                    Log.d("Grezz", "error loading banner");
                    hide();
                }

                @Override
                public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                    super.onReceivedHttpError(view, request, errorResponse);
                    webBannerListener.onErrorListener();
                    loadError = true;
                    Log.d("Grezz", "error loading banner");
                    hide();
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    super.onReceivedSslError(view, handler, error);
                    webBannerListener.onErrorListener();
                    loadError = true;
                    Log.d("Grezz", "error loading banner");
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
            bannerWeb.loadUrl(new EasyAd(getContext()).buildUrl(AdType.BANNER));
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
            hide();
            View view = mInflater.inflate(R.layout.banner_med_rec_web, this, true);
            view.setVisibility(GONE);
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webView = view.findViewById(R.id.medRec_web);
            webView.setVerticalScrollBarEnabled(false);
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    if (!loadError) {
                        show();
                        webMedRecListener.onLoadListener();
                    }
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    webMedRecListener.onErrorListener();
                    loadError = true;
                    hide();
                }

                @Override
                public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                    super.onReceivedHttpError(view, request, errorResponse);
                    webMedRecListener.onErrorListener();
                    loadError = true;
                    hide();
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    super.onReceivedSslError(view, handler, error);
                    webMedRecListener.onErrorListener();
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
        }

        public void load() {
            loadError = false;
            webView.loadUrl(new EasyAd(getContext()).buildUrl(AdType.BANNER_MED_REQ));
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
        InterstitialListener interstitialListener;
        Dialog d;
        Context context;

        boolean loadError;


        public Interstitial(Context context) {
            this.context = context;
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

            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webView.setVerticalScrollBarEnabled(false);
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    Log.d("Grezz", "page started loading");
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
                    hide();
                }

                @Override
                public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                    super.onReceivedHttpError(view, request, errorResponse);
                    interstitialListener.onErrorListener();
                    loadError = true;
                    hide();
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    super.onReceivedSslError(view, handler, error);
                    interstitialListener.onErrorListener();
                    loadError = true;
                    hide();
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
            Log.d("Grezz", "inside init");
        }

        public void load() {
            webView.loadUrl(new EasyAd(context).buildUrl(AdType.INTERSTITIAL));
        }

        public void show() {
            d.show();
        }

        void hide() {
            d.hide();
        }

        public void destroy() {
            d.dismiss();
        }

        private void close() {
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
