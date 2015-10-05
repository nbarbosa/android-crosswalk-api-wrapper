package com.xwalkwrapper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewFragment extends Fragment {

    public WebView wv = null;

    private OnWebViewListener webviewListener;

    public WebViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_web_view, container, false);

        wv = (WebView) view.findViewById(R.id.webview);

        wv.setWebViewClient(new WebViewClient() {
            @Override
             public void onPageStarted(WebView view, String url, Bitmap favicon) {
                webviewListener.onPageStarted();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                webviewListener.onPageFinished();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                view.stopLoading();
                webviewListener.onPageError();
            }
        });

        WebSettings webSettings = wv.getSettings();

        // optimal settings
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSaveFormData(false);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        wv.setWebChromeClient(new WebChromeClient() {

            // always grant permission for audio recording
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                Log.d(getString(R.string.app_name), "onPermissionRequest");
                getActivity().runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        if(request.getOrigin().toString().contains(getString(R.string.load_url))) {
                            request.grant(request.getResources());
                        } else {
                            request.deny();
                        }
                    }
                });
            }
            // write web console messages to app console
            public boolean onConsoleMessage(ConsoleMessage m) {
                Log.d(getString(R.string.app_name), m.message() + " line " + m.lineNumber());
                return true;
            }
        });

        wv.loadUrl(getString(R.string.load_url));
        return view;
    }

    public void reload() {
        wv.reload();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            webviewListener = (OnWebViewListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnWebViewListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        webviewListener = null;
    }

    public boolean goBack() {
        if (wv.canGoBack()) {
            wv.goBack();
            return true;
        }
        return false;
    }

    public interface OnWebViewListener {
        public void onPageStarted();
        public void onPageFinished();
        public void onPageError();
    }
}
