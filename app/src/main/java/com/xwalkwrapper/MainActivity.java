package com.xwalkwrapper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.xwalkwrapper.R;


public class MainActivity extends Activity
        implements XWalkWebViewFragment.OnWebViewListener, View.OnClickListener {

    private View loadingOverlay = null;
    private XWalkWebViewFragment wvFragment = null;
    private View errorMessage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if(!getSharedPreferences(getString(R.string.app_name), Activity.MODE_PRIVATE).getBoolean(getString(R.string.shortcut_created), false)){
            createShortCut();
            getSharedPreferences(getString(R.string.app_name), Activity.MODE_PRIVATE).edit().putBoolean(getString(R.string.shortcut_created), true);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        loadingOverlay = findViewById(R.id.loadingOverlay);
        wvFragment = (XWalkWebViewFragment) getFragmentManager().findFragmentById(R.id.webViewFragment);
        errorMessage = findViewById(R.id.errorMessage);

        // try again button reloads webview
        final Button btnTryAgain = (Button) findViewById(R.id.btnTryAgain);
        btnTryAgain.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnTryAgain:
                wvFragment.reload();
                break;
        }
    }

    @Override
    public void onBackPressed(){
        if (wvFragment != null && !wvFragment.goBack()) {
          super.onBackPressed();
        }
    }

    public void onPageStarted () {
       hideErrorMessage();
        hideKeyboard();
       showLoadingOverlay();
    }

    public void onPageFinished () {
        hideLoadingOverlay();
    }

    public void onPageError () {
        showErrorMessage();
    }


    public void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(loadingOverlay.getWindowToken(), 0);
    }

    public void showLoadingOverlay () {
        loadingOverlay.animate().alpha(1.0f);
    }

    public void hideLoadingOverlay () {
        loadingOverlay.animate().alpha(0.0f);
    }

    public void showErrorMessage () {
        errorMessage.animate().alpha(1.0f);
        errorMessage.setVisibility(errorMessage.VISIBLE);
    }

    public void hideErrorMessage () {
        errorMessage.animate().alpha(0.0f);
        errorMessage.setVisibility(errorMessage.GONE);

    }

    private void createShortCut() {
        Intent shortcutIntent = new Intent(getApplicationContext(),MainActivity.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, R.string.app_name);
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.ic_launcher));
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(intent);
    }
}
