package com.tangguo.tangguoxianjin.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.common.BaseActivity;
import com.tangguo.tangguoxianjin.config.MyConstants;
import com.tangguo.tangguoxianjin.util.LogManager;
import com.tangguo.tangguoxianjin.util.StringUtils;
import com.tangguo.tangguoxianjin.view.TitleLayout;

import static com.tangguo.tangguoxianjin.R.id.wv_load;

public class WebActivity extends BaseActivity {

    private String action = "";
    private final String HTML = "HTML";
    private WebView webView;
    private String web_view_url = "";
    private boolean needClearHistory;
    private ProgressBar networkProgressBar;
    private Class<?> backClass = null;
    private ProgressBar myProgressBar;
    private android.widget.RelativeLayout rlwebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        initIntent();
        initViews();
        ititTitle();
        initListener();
        initData();


    }


    private void initIntent() {

        Intent it = getIntent();
        if (it != null) {
            action = it.getAction();
            web_view_url = it.getStringExtra("web_view_url");
            backClass = (Class) it.getSerializableExtra(MyConstants.CLASS_FROM_ACTIVITY);
        }
    }


    private void initViews() {
        networkProgressBar = (ProgressBar) findViewById(R.id.myProgressBar);
        webView = (WebView) findViewById(wv_load);
        this.rlwebview = (RelativeLayout) findViewById(R.id.rl_webview);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setSavePassword(false);
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setDisplayZoomControls(false); //隐藏Zoom缩放按钮
        }
        //add by xiehy 修复HTML页面加载WEBVIEW 显示问题 不使用加载百分百显示
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        //在Android5.0及5.0以上版本，WebView通过https访问的资源，该资源里面又通过http访问了别的资源，默认WebView阻止的后面的http资源的访问，报Mixed Content
        //以下设置为不阻止，可以访问
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }


    private void ititTitle() {
    }

    private void initListener() {


        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                // 监听下载功能，当用户点击下载链接的时候，直接调用系统的浏览器来下载
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        //----------------------WebViewClient----------------------


        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!TextUtils.isEmpty(url)) {
                    if ("open_login".equals(url)) {
                        return true;
                    }
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                //用javascript隐藏系统定义的404页面信息
                String data = "出错了哦~\n您的页面找不到了！";
                view.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogManager.e("Finished onPageFinished " + url);
                String pageFinishedTitle = view.getTitle();
                if (!TextUtils.isEmpty(pageFinishedTitle) && !webView.canGoBack()) {
                    if (!StringUtils.isEquals(url, pageFinishedTitle)) {
                        setTitle(pageFinishedTitle);
                    }
                } else {
                    if (!StringUtils.isEquals(url, pageFinishedTitle)) {
                        setTitle(pageFinishedTitle);
                    }
                }
            }

            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                super.doUpdateVisitedHistory(view, url, isReload);
                if (needClearHistory) {
                    needClearHistory = false;
                    webView.clearHistory();//清除历史记录
                }
            }


            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });

        //----------------------WebChromeClient----------------------
        WebChromeClient wvcc = new WebChromeClient() {
            //add by xiehy V4.5版本添加进度条功能
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                networkProgressBar.setProgress(newProgress);
                networkProgressBar.postInvalidate();
                if (newProgress == 100) {
                    networkProgressBar.setVisibility(View.GONE);
                }
            }

            //end add
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(title) && !webView.canGoBack()) {
                    if (!StringUtils.isEquals(view.getUrl(), title)) setTitle(title);
                } else {
                    if (!StringUtils.isEquals(view.getUrl(), title)) {
                        setTitle(title);
                    }

                }

            }

        };
        webView.setWebChromeClient(wvcc);

        basePageView.setBack(new TitleLayout.OnBackListener() {

            @Override
            public void onBack() {
                doBack();
            }
        });


        if (StringUtils.isEquals(HTML, action)) {
            webView.loadData(web_view_url, "text/html", "utf-8");
        } else {
            webView.loadUrl(web_view_url);
        }
    }

    private void initData() {

    }


    public void doBack() {
        if (webView != null && webView.canGoBack()) {
            basePageView.setHtmlClose(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (backClass != null) {
                        backFromStart();
                    } else finish();
                }
            });
            webView.goBack();
        } else {
            if (backClass != null) {
                backFromStart();
            } else finish();
        }
    }

    private void backFromStart() {
        finish();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            doBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        if (webView != null) {
            try {
                rlwebview.removeView(webView);
                webView.clearHistory();
                webView.clearCache(true);
                webView.pauseTimers();
                webView.destroy();
                webView = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }


}
