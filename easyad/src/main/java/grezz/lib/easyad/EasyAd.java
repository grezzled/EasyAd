package grezz.lib.easyad;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
            });
        }

        public void load() {
            bannerWeb.loadUrl("https://isoufiane.com");
        }

        public void show(){
            this.setVisibility(VISIBLE);
        }

        public void hide(){
            this.setVisibility(GONE);
        }

        public void setWebBannerListener(WebBannerListener webBannerListener) {
            this.webBannerListener = webBannerListener;
        }
    }

    public static class WebInters extends RelativeLayout {

        LayoutInflater mInflater;
        WebView webView;
        ImageView closeBtn;
        WebIntersListener webIntersListener;
        boolean loadError;

        public interface WebIntersListener {
            void onCloseListener();

            void onLoadListener();

            void onErrorListener();
        }

        public WebInters(Context context) {
            super(context);
            mInflater = LayoutInflater.from(context);
            init();
        }

        public WebInters(Context context, AttributeSet attrs) {
            super(context, attrs);
            mInflater = LayoutInflater.from(context);
            init();
        }

        public WebInters(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            mInflater = LayoutInflater.from(context);
            init();
        }

        public WebInters(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            mInflater = LayoutInflater.from(context);
            init();
        }

        private void init() {
            View view = mInflater.inflate(R.layout.inters_web, this, true);
            view.setVisibility(GONE);
            webView = view.findViewById(R.id.inters_web);
            WebSettings webSettings = webView.getSettings();
            closeBtn = view.findViewById(R.id.ad_close_btn);
            closeBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    webIntersListener.onCloseListener();
                    //Reload WebView to bring new Ad
//                    webView.reload();
                    hide();
                }
            });
        }

        public void load() {
            loadError = false;
            webView.loadUrl("https://isoufiane.com");
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    if (!loadError)
                        webIntersListener.onLoadListener();
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    webIntersListener.onErrorListener();
                    loadError = true;
                }
            });
        }

        public void show() {
            this.setVisibility(VISIBLE);
        }

        public void hide() {
            this.setVisibility(GONE);
        }

        public void setWebIntersListener(WebIntersListener webIntersListener) {
            this.webIntersListener = webIntersListener;
        }

    }
}
