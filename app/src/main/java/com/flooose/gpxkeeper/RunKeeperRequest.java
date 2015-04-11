package com.flooose.gpxkeeper;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

/**
 * Created by chris on 06.04.15.
 */
public class RunKeeperRequest {
    private static String RUNKEEPER_API_URL = "https://api.runkeeper.com";
    private String accessToken;
    private Context applicationContext;

    public RunKeeperRequest(String accessToken, Context applicationContext){
        this.accessToken = accessToken;
        this.applicationContext = applicationContext;
    }

    public void send(Activity activity) throws OAuthSystemException, OAuthProblemException {
        OAuthClientRequest bearerRequest = new OAuthBearerClientRequest(RUNKEEPER_API_URL + "/user")
                .setAccessToken(accessToken).buildQueryMessage();
        ConnectivityManager connMgr = (ConnectivityManager)
                activity.getSystemService(activity.getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            FetchContent fetcher =  new FetchContent(bearerRequest);
            fetcher.execute(MainActivity.AUTHORIZATION_URL);
        } else { }
    }

    private class FetchContent extends AsyncTask<String, Void, String> {
        private OAuthClientRequest bearerRequest;
        private int toastLength = Toast.LENGTH_SHORT;

        public FetchContent(OAuthClientRequest bearerRequest) {
            this.bearerRequest = bearerRequest;
        }

        @Override
        protected String doInBackground(String... urls) {
            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

            OAuthResourceResponse resourceResponse = null;
            try {
                resourceResponse = oAuthClient.resource(bearerRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
                return "Activity successfully posted";
            } catch (OAuthProblemException e) {
                toastLength = Toast.LENGTH_LONG;
                e.printStackTrace();
                return e.getMessage();
            } catch (OAuthSystemException e) {
                toastLength = Toast.LENGTH_LONG;
                e.printStackTrace();
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast toast = Toast.makeText(
                    applicationContext, result, toastLength);
            toast.show();
        }
    }
}
