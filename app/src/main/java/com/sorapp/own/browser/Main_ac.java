package com.sorapp.own.browser;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaSync;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.swiperefreshlayout.widget.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Main_ac extends AppCompatActivity
{



    //Global Variables Start

    //Components
    WebView MAIN_AC_WEBVIEW;
    EditText URL_BOX;
    SwipeRefreshLayout REFRESH_LAYOUT;
    ProgressBar LOAD_PROGRESS_BAR;
    ImageButton HOME_PAGE_BTN;
    RelativeLayout INTERNET_ERROR;

    //Opration Variables
    String Home_URL="http://sorapp.ir";

    //File Variables
    String CACHE_PATH= Environment.getExternalStorageDirectory().getAbsolutePath()+"/Own";

    //Global Variables End





    //Main Function Start
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_ac);
        Get_All_Components();
        GET_URL(Home_URL);
    }
    //Main Function End



    //OnBack Click Start
    @Override
    public void onBackPressed()
    {
        if(MAIN_AC_WEBVIEW.canGoBack())
        {
            MAIN_AC_WEBVIEW.goBack();
        }
        else
        {
            Intent intent = new Intent();
            intent.setAction(intent.ACTION_MAIN);
            intent.addCategory(intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }
    //OnBack Click End



    //On CLick Home Button Event Start
    public void On_Click_HOME_Btn(View v)
    {
        GET_URL(Home_URL);
    }
    //On CLick Home Button Event End



    //Get URLBox Events Start
    private void GET_URL_BOX_EVENTS()
    {
        URL_BOX.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {

                if(KeyEvent.KEYCODE_ENTER==keyCode)
                {
                    GET_URL(URL_BOX.getText().toString());
                }

                return false;
            }
        });
    }
    //Get URLBox Events End




    //Get Refresh Webbrowser Events Start
    private void GET_REFRESH_LAYOUT_EVENTS()
    {
        REFRESH_LAYOUT.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                MAIN_AC_WEBVIEW.loadUrl(MAIN_AC_WEBVIEW.getUrl().toString());
            }
        });
    }
    //Get Refresh Webbrowser Events End




    //Get All Components Start
    private void Get_All_Components()
    {
        GetLoadHomePage();

        MAIN_AC_WEBVIEW=(WebView)findViewById(R.id.main_ac_webview);
        Get_Webview_Default_Setting();

        REFRESH_LAYOUT=(SwipeRefreshLayout)findViewById(R.id.refresh_layout);
        GET_REFRESH_LAYOUT_EVENTS();

        URL_BOX=(EditText) findViewById(R.id.URL_BOX);
        GET_URL_BOX_EVENTS();

        LOAD_PROGRESS_BAR=(ProgressBar) findViewById(R.id.main_ac_top_progressbar);

        OnClick_Set_HomePage();

        INTERNET_ERROR=(RelativeLayout) findViewById(R.id.main_ac_internet_error_ly);

    }
    //Get All Components End





    //Show Server Error Start
    public void ShowError()
    {
        INTERNET_ERROR.setVisibility(View.VISIBLE);
        MAIN_AC_WEBVIEW.setVisibility(View.GONE);
    }
    //Show Server Error End





    //Get Check DeepLink Start
    public void Get_DeepLink()
    {
        Uri DeepLink=getIntent().getData();
        if(DeepLink!=null)
        {
            GET_URL(DeepLink.toString());
        }
    }
    //Get Check DeepLink End




    //Show Server Error Start
    public void GoneError()
    {
        INTERNET_ERROR.setVisibility(View.GONE);
        MAIN_AC_WEBVIEW.setVisibility(View.VISIBLE);
    }
    //Show Server Error End




    //GetLoad HomePage Start
    public void GetLoadHomePage()
    {
        SharedPreferences save=getSharedPreferences("Own_Browser",MODE_PRIVATE);
        Home_URL=save.getString("HomePage","http://sorapp.ir");
    }
    //GetLoad HomePage End



    //Get Cache Path Start
    public void GET_CACHE_PATH()
    {
        Log.i("Errt",CACHE_PATH);
        File file=new File(CACHE_PATH);
        if(!file.exists())
        {
            file.mkdir();
        }
    }
    //Get Cache Path End



    //Webview Default Setting Start
    public void Get_Webview_Default_Setting()
    {
        GET_CACHE_PATH();

        //Setting
        MAIN_AC_WEBVIEW.getSettings().setJavaScriptEnabled(true);
        MAIN_AC_WEBVIEW.getSettings().setAllowContentAccess(true);
        MAIN_AC_WEBVIEW.getSettings().setLoadWithOverviewMode(true);
        MAIN_AC_WEBVIEW.getSettings().setAllowFileAccess(true);
        MAIN_AC_WEBVIEW.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        MAIN_AC_WEBVIEW.getSettings().setDatabaseEnabled(true);
        MAIN_AC_WEBVIEW.getSettings().setDefaultTextEncodingName("utf-8");
        MAIN_AC_WEBVIEW.getSettings(). setSupportZoom(true);
        MAIN_AC_WEBVIEW.getSettings(). setAppCachePath(CACHE_PATH);

        //Get Browsers Client
        GetWebviewClient();
        GetChromeClient();

    }
    //Webview Default Setting End




    //Chosee FIle Web View
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private ValueCallback<Uri[]> mFilePathCallback;
    private Uri mCapturedImageURI = null;
    private String mCameraPhotoPath;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            Uri[] results = null;
            // Check that the response is a good one
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }
            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            if (requestCode == FILECHOOSER_RESULTCODE) {
                if (null == this.mUploadMessage) {
                    return;
                }
                Uri result = null;
                try {
                    if (resultCode != RESULT_OK) {
                        result = null;
                    } else {
                        // retrieve from the private variable if the intent is null
                        result = data == null ? mCapturedImageURI : data.getData();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "activity :" + e,
                            Toast.LENGTH_LONG).show();
                }
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }
        return;
    }
    //Chosee FIle Web View






    //Get Chrome WebClient Start
    private void GetChromeClient()
    {
        MAIN_AC_WEBVIEW.setWebChromeClient(new WebChromeClient(){


            @Override
            public void onProgressChanged(WebView view, int newProgress)
            {
                LOAD_PROGRESS_BAR.setProgress(newProgress);

                if(newProgress>=80)
                {
                    REFRESH_LAYOUT.setRefreshing(false);
                }

                super.onProgressChanged(view, newProgress);
            }


            public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath, WebChromeClient.FileChooserParams fileChooserParams)
            {
                // Double check that we don't have any existing callbacks
                if (mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePath;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        Log.e("", "Unable to create Image File", ex);
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }
                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");
                Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }
                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
                return true;
            }


            private File createImageFile() throws IOException {
                // Create an image file name
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_";
                File storageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                File imageFile = File.createTempFile(
                        imageFileName,  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );
                return imageFile;
            }



        });
    }
    //Get Chrome WebClient End




    //Get Webview Client Function Start
    private void GetWebviewClient()
    {
        MAIN_AC_WEBVIEW.setWebViewClient(new WebViewClient(){


            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
                ShowError();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error)
            {
                ShowError();
            }

            @Override
            public void onLoadResource(WebView view, String url)
            {
                GoneError();
                super.onLoadResource(view, url);
            }
        });
    }
    //Get Webview Client Function End




    //Set Home Page Event Start
    public void OnClick_Set_HomePage()
    {

        HOME_PAGE_BTN=(ImageButton) findViewById(R.id.main_ac_home_btn);
        HOME_PAGE_BTN.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                Home_URL=URL_BOX.getText().toString().trim();
                SharedPreferences save=getSharedPreferences("Own_Browser",MODE_PRIVATE);
                SharedPreferences.Editor editor=save.edit();
                editor.putString("HomePage",URL_BOX.getText().toString().trim());
                editor.commit();
                Toast.makeText(getApplicationContext(), "Home page is set this website", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }
    //Set Home Page Event End




    //Get Load URL With Web Browser Start
    private void GET_URL(String URL)
    {
        String Load="";
        if(URL.contains("http") || URL.contains("https"))
        {
            Load=URL;
        }
        else
        {
            Load="http://"+URL;
        }
        URL_BOX.setText(Load);
        MAIN_AC_WEBVIEW.loadUrl(Load);
    }
    //Get Load URL With Web Browser End


    //When Onpuse App Use This Function
    @Override
    protected void onPause()
    {
        SharedPreferences data_storage= getSharedPreferences("Own_Browser",MODE_PRIVATE);
        SharedPreferences.Editor editor=data_storage.edit();
        editor.putString("Last_URL",MAIN_AC_WEBVIEW.getUrl());
        editor.commit();
        super.onPause();
    }


    @Override
    protected void onResume()
    {
        SharedPreferences data_storage= getSharedPreferences("Own_Browser",MODE_PRIVATE);
        MAIN_AC_WEBVIEW.loadUrl(data_storage.getString("Last_URL",Home_URL));
        URL_BOX.setText(data_storage.getString("Last_URL",Home_URL));
        Get_DeepLink();
        super.onResume();
    }
}