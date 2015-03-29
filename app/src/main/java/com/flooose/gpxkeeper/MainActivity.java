package com.flooose.gpxkeeper;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;


public class MainActivity extends ActionBarActivity {

    public static final String AUTHORIZATION_URL = "https://runkeeper.com/apps/authorize";
    public static final String TOKEN_URL = "https://runkeeper.com/apps/token";
    public static final String DE_AUTHORIZE_URL = "https://runkeeper.com/apps/de-authorize";
    public static final String DEBUG_TAG = "GPXKEEPER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    // When user clicks button, calls AsyncTask.
    // Before attempting to fetch the URL, makes sure that there is a network connection.
    public void network(View view) {
        // Gets the URL from the UI's text field.
        //String stringUrl = urlText.getText().toString();
        ConnectivityManager connMgr = (ConnectivityManager)
            getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(MainActivity.AUTHORIZATION_URL);
        } else {
            //textView.setText("No network connection available.");
        }
    }

    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 1000;
        String contentAsString = "";

        try {
            OAuthClientRequest.AuthenticationRequestBuilder requestBuilder = OAuthClientRequest.authorizationLocation(MainActivity.AUTHORIZATION_URL);
            requestBuilder.setClientId(GPXKeeperOAuthData.ClientID);
            requestBuilder.setResponseType("code");
            requestBuilder.setRedirectURI("gpxkeeper://oauthresponse");
            OAuthClientRequest request = requestBuilder.buildQueryMessage();
            URL url = new URL(request.getLocationUri());
            Intent authenticate = new Intent(Intent.ACTION_VIEW, Uri.parse(request.getLocationUri()));
            startActivityForResult(authenticate, 0);
            //HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            //conn.setReadTimeout(10000 /* milliseconds */);
            //conn.setConnectTimeout(15000 /* milliseconds */);
            //conn.setRequestMethod("GET");
            //conn.setDoInput(true);
            // Starts the query
            //int response = conn.getResponseCode();
            //is = conn.getInputStream();


            // Convert the InputStream into a string
            //contentAsString = readIt(is, len);
            //Log.d(DEBUG_TAG, contentAsString);

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } catch (OAuthSystemException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
            return contentAsString;

        }
    }

     // Uses AsyncTask to create a task away from the main UI thread. This task takes a
     // URL string and uses it to create an HttpUrlConnection. Once the connection
     // has been established, the AsyncTask downloads the contents of the webpage as
     // an InputStream. Finally, the InputStream is converted into a string, which is
     // displayed in the UI by the AsyncTask's onPostExecute method.
     private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //textView.setText(result);
       }
    }

    @Override
    protected void onResume(){
        super.onResume();
        int blub = 2;
        int bla = blub *1;

    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent){
        int blub = 2;
        //Uri uri = intent.getData();
        int bla = blub + 1;
        //uri.toString();
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        int blub = 2;
        //Uri uri = intent.getData();
        int bla = blub + 1;
        OAuthClientRequest request = null;

//request = OAuthClientRequest.tokenLocation();
        //uri.toString();
    }
}

