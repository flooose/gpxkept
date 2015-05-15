package com.flooose.gpxkeeper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends ActionBarActivity {

    public static final String AUTHORIZATION_URL = "https://runkeeper.com/apps/authorize";
    public static final String TOKEN_URL = "https://runkeeper.com/apps/token";
    public static final String DE_AUTHORIZE_URL = "https://runkeeper.com/apps/de-authorize";
    public static final String DEBUG_TAG = "GPXKEEPER";
    public static final String GPX_KEEPER_URI = "gpxkeeper://oauthresponse";

    private PlaceholderFragment placementholderFragment;
    private GPXFiles gpxFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        gpxFiles = new GPXFiles(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));

        GPXFilesAdapter fileArrayAdapter = new GPXFilesAdapter(this, gpxFiles.files());

        placementholderFragment = new PlaceholderFragment(fileArrayAdapter);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, placementholderFragment)
                    .commit();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        GPXFilesAdapter fileArrayAdapter = new GPXFilesAdapter(this, gpxFiles.files());

        placementholderFragment.setFileArrayAdapter(fileArrayAdapter);
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

    public boolean oAuthenticated() {
        return PreferenceManager.getDefaultSharedPreferences(this).getString("oauth_token", null) != null;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private ArrayAdapter<File> fileArrayAdapter;
        private ListView fileListView;

        public PlaceholderFragment(ArrayAdapter<File> fileArrayAdapter) {
            this.fileArrayAdapter = fileArrayAdapter;
        }

        public void setFileArrayAdapter(ArrayAdapter<File> fileArrayAdapter){
            fileListView.setAdapter(fileArrayAdapter);
        }

        private void setButtonText(View rootView){
            Button button = (Button) rootView.findViewById(R.id.oAuthButton);
            if(oAuthenticated()){
                button.setText(R.string.deauthenticate);
            } else {
                button.setText(R.string.setup);
            }
        }

        public boolean oAuthenticated() {
            return PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("oauth_token", null) != null;
        }

        private String getOauthToken(){
            return PreferenceManager.getDefaultSharedPreferences(this.getActivity()).getString("oauth_token", "");
        }

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container,
                                 Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            //setButtonText(rootView);

            if(oAuthenticated()) {
                fileListView = (ListView) rootView.findViewById(R.id.gpx_file_list_view);
                fileListView.setAdapter(fileArrayAdapter);
                fileListView.setOnItemClickListener(new ListView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        GPXUploadDialog gpxUploadDialog = null;
                        try {
                            gpxUploadDialog = new GPXUploadDialog((File) adapterView.getItemAtPosition(i));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        gpxUploadDialog.show(getActivity().getFragmentManager(), "hello");
                    }
                });
            }
            return rootView;
        }
    }

    // When user clicks button, calls AsyncTask.
    // Before attempting to fetch the URL, makes sure that there is a network connection.
    public void oAuthAction(View view) {
        if (!oAuthenticated()) {
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                new DownloadWebpageTask().execute(MainActivity.AUTHORIZATION_URL);
            } else {
                //textView.setText("No network connection available.");
            }
        } else {

        }
    }

    private String downloadUrl() throws IOException { // we can probably return null
        InputStream is = null;
        String contentAsString = "";

        try {
            OAuthClientRequest.AuthenticationRequestBuilder requestBuilder = OAuthClientRequest.authorizationLocation(MainActivity.AUTHORIZATION_URL);
            requestBuilder.setClientId(GPXKeeperOAuthData.CLIENT_ID);
            requestBuilder.setResponseType("code");
            requestBuilder.setRedirectURI(GPX_KEEPER_URI);
            OAuthClientRequest request = requestBuilder.buildQueryMessage();
            Intent authenticate = new Intent(Intent.ACTION_VIEW, Uri.parse(request.getLocationUri()));
            startActivityForResult(authenticate, 0);
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
            try {
                return downloadUrl();
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
    protected void onActivityResult(int requestCode, int responseCode, Intent intent){}

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        boolean authingApp = intent.getDataString() != null;

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() && authingApp) {
            new DownloadToken(intent).execute(MainActivity.AUTHORIZATION_URL);
        } else {
            //textView.setText("No network connection available.");
        }
    }

    public String validateToken(Intent intent) {
        // Gets the URL from the UI's text field.
        OAuthClientRequest request = null;
        String authCode = Uri.parse(((Intent) intent).getDataString()).getQueryParameter("code");
        try {
            request = OAuthClientRequest.tokenLocation(TOKEN_URL).
                    setClientId(GPXKeeperOAuthData.CLIENT_ID).
                    setGrantType(GrantType.AUTHORIZATION_CODE).
                    setClientSecret(GPXKeeperOAuthData.CLIENT_SECRET).
                    setRedirectURI(GPX_KEEPER_URI).
                    setCode(authCode).
                    buildBodyMessage();
        } catch (OAuthSystemException e) {
            e.printStackTrace();
        }
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        OAuthJSONAccessTokenResponse response = null;
        try {
            response = oAuthClient.accessToken(request);
        } catch (OAuthSystemException e) {
            e.printStackTrace();
        } catch (OAuthProblemException e) {
            e.printStackTrace();
        } finally {
        }

        String token = response.getAccessToken();
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor e = p.edit();
        e.putString("oauth_token", token);
        e.commit();
        return token;
    }

    private class DownloadToken extends AsyncTask<String, Void, String> {
        private Intent intent;
        public DownloadToken(Intent intent){

            this.intent = intent;

        }

        @Override
        protected String doInBackground(String... urls) {
            return validateToken(intent);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //textView.setText(result);
        }
    }
}

