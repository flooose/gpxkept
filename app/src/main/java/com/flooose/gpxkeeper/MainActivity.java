/*
    Copyright 2015-2080 christopher floess

    This file is part of gpxkept.

    gpxkept is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    gpxkept is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with gpxkept.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.flooose.gpxkeeper;

import android.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

// All of this should be in its own object
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends FragmentActivity implements GPXDetailDialog.GPXDetailDialogListener {

    public static final String AUTHORIZATION_URL = "https://runkeeper.com/apps/authorize";
    public static final String TOKEN_URL = "https://runkeeper.com/apps/token";
    public static final String DE_AUTHORIZE_URL = "https://runkeeper.com/apps/de-authorize";
    public static final String DEBUG_TAG = "GPXKEEPER";
    public static final String GPX_KEEPER_URI = "gpxkeeper://oauthresponse";

    public FileListFragment fileListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            fileListFragment = new FileListFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fileListFragment)
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
    protected void onResume(){
        super.onResume();
        fileListFragment.setFileArrayAdapter();
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
        return getOAuthToken() != null;
    }

    public String getOAuthToken() {
        return PreferenceManager.getDefaultSharedPreferences(this).getString("oauth_token", null);
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
            new RunKeeperRequest(PreferenceManager.getDefaultSharedPreferences(this).getString("oauth_token", null), this).deauthorize(this);
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

        String dataString = null;
        if (intent.getData() instanceof Uri) {
            dataString = intent.getData().getQueryParameter("code");
        }

        if (dataString != null) {
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

        @Override
        protected void onPostExecute(String result) {
            onResume();
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog){
        ((GPXDetailDialog) dialog).gpxFile.delete();
        fileListFragment.setFileArrayAdapter();
    }
}

