package com.example.maishacarparkingshades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    WebView webView;
    private String webUrl = "https://aa93-102-217-157-9.ngrok-free.app/maishashades/main/";
    ProgressBar progressBarWeb;
    ProgressDialog progressDialog;
    RelativeLayout relativeLayout;
    Button btnNoInternetConnection;

    public void buttonPrint(){

        PrintManager printManager = (PrintManager) getSystemService(PRINT_SERVICE);

        PrintDocumentAdapter printDocumentAdapter = webView.createPrintDocumentAdapter("Print1");

        printManager.print("PrintTest1", printDocumentAdapter, new  PrintAttributes.Builder().build());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.myWebView);
        progressBarWeb = (ProgressBar) findViewById(R.id.progressBar);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Please Wait");

        btnNoInternetConnection = (Button) findViewById(R.id.btnNoConnection);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);


        checkConnection();

            //Webview stuff
            webView = findViewById(R.id.myWebView);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                progressBarWeb.setVisibility(View.VISIBLE);
                progressBarWeb.setProgress(newProgress);
                setTitle("Loading ...");
                progressDialog.show();
                if (newProgress == 100) {
                    progressBarWeb.setVisibility(View.GONE);
                    setTitle(view.getTitle());
                    progressDialog.dismiss();
                }


                super.onProgressChanged(view, newProgress);
            }


        });
        btnNoInternetConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
            }
        });


    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();

        } else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are sure you want to Exit?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finishAffinity();
                        }
                    }).show();
            }
    }

    public void checkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if(wifi.isConnected()){
            webView.loadUrl(webUrl);
            webView.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);

        } else if (mobileNetwork.isConnected()) {
            webView.loadUrl(webUrl);
            webView.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);

        }
        else{
            webView.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);

        }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.nav_previous:
                onBackPressed();
                break;
            case R.id.nav_next:

                if(webView.canGoForward()){
                    webView.goForward();
                }
                break;
            case R.id.nav_reload:
                checkConnection();
                break;
            case R.id.nav_print:
                buttonPrint();

                break;

        }
        return super.onOptionsItemSelected(item);
    }
}

