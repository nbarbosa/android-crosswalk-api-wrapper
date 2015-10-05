package com.xwalkwrapper;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xwalk.core.XWalkNavigationHistory;
import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;
import org.xwalk.core.internal.XWalkSettings;
import org.xwalk.core.internal.XWalkViewBridge;

import java.lang.reflect.Method;

public class XWalkWebViewFragment extends Fragment {

    public XWalkView wv = null;

    private OnWebViewListener webviewListener;

    public XWalkWebViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.xwalk_fragment_web_view, container, false);

        wv = (XWalkView) view.findViewById(R.id.xwalkwebview);

        wv.setResourceClient(new XWalkResourceClient(wv) {
                    @Override
                    public void onReceivedLoadError(XWalkView view, int errorCode, String description, String failingUrl){
                        webviewListener.onPageError();
                    }
                });

        wv.setUIClient(new XWalkUIClient(wv) {

                    @Override
                    public void onPageLoadStarted(XWalkView view, java.lang.String url){
                        webviewListener.onPageStarted();
                    }

                    @Override
                    public void onPageLoadStopped(XWalkView view, String url, XWalkUIClient.LoadStatus status){
                        if (status == LoadStatus.FINISHED) {
                            webviewListener.onPageFinished();
                        } else {
                            webviewListener.onPageError();
                        }
                    }

                });

        try
        {
            Method ___getBridge = XWalkView.class.getDeclaredMethod("getBridge");

            ___getBridge.setAccessible(true);
            XWalkViewBridge xWalkViewBridge = null;

            xWalkViewBridge = (XWalkViewBridge)___getBridge.invoke(wv);
            XWalkSettings xWalkSettings = xWalkViewBridge.getSettings();

            xWalkSettings.setMediaPlaybackRequiresUserGesture(false);
            xWalkSettings.setJavaScriptEnabled(true);
            xWalkSettings.setDomStorageEnabled(true);
            xWalkSettings.setAcceptLanguages(getResources().getConfiguration().locale.toString().replaceAll("_","-"));
            xWalkSettings.setAllowFileAccessFromFileURLs(true);
            xWalkSettings.setAllowUniversalAccessFromFileURLs(true);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            webviewListener.onPageError();
        }


        wv.load(getString(R.string.load_url), null);
        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
        return view;
    }

    public void reload() {
        wv.reload(XWalkView.RELOAD_IGNORE_CACHE);
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
        if (wv.getNavigationHistory().canGoBack()) {
            wv.getNavigationHistory().navigate(XWalkNavigationHistory.Direction.BACKWARD, 1);
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
