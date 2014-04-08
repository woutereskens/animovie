/* Application: animovie
 * Author: Wouter Eskens
 * API: The Movie Database (TMDb)
 * Database: SQLite 
 * Class: Applied Computer Sciences
 * School: Thomas More Kempen
 * Year: 2013-2014 */

package com.example.animovie.db;

import java.io.BufferedReader;
import com.example.animovie.db.Delegate;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.StatusLine;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONObject;

public class URLRequestor extends AsyncTask< String, String, String >
{
	private Delegate< String > _onResponseComplete;
	
	public void SetOnComplete(Delegate < String > delegate) {
		_onResponseComplete = delegate;
	}
	
	@Override
	protected String doInBackground(String... url)
	{
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		String responseString = null;
		
	    BufferedReader in = null;
	    try 
	    {
	        response = client.execute(new HttpGet(url[0]));
	        StatusLine status = response.getStatusLine();
	        // check if status was ok
	        if (status.getStatusCode() == HttpStatus.SC_OK)
	        {
	        	ByteArrayOutputStream out = new ByteArrayOutputStream();
	        	response.getEntity().writeTo(out);
	        	out.close();
	        	responseString = out.toString();
	        } else {
	        	// request failed!
	        	response.getEntity().getContent().close();
	        	throw new IOException(status.getReasonPhrase());
	        }
	    } 
	    catch (Exception e) {
	    	Log.e("HttpRequestError", e.toString());
	    }
	    return responseString;
	}
	
	@Override
	protected void onPostExecute(String response)
	{
		super.onPostExecute(response);
		// notify the listeners that the reponse was complete
		_onResponseComplete.invoke(response);
	}
}