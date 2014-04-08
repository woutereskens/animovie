/* Application: animovie
 * Author: Wouter Eskens
 * API: The Movie Database (TMDb)
 * Database: SQLite 
 * Class: Applied Computer Sciences
 * School: Thomas More Kempen
 * Year: 2013-2014 */

package com.example.animovie.db;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.example.animovie.db.Delegate;

public class ImageRequestor extends AsyncTask< String, String, ImageRequestor > 
{
	static long REQUEST_ID_GENERATOR = 0;
	public ImageRequestor() {
		_id = REQUEST_ID_GENERATOR++;
	}
	
	private long _id;
	public long id() { return _id; }
	
	private Bitmap _bitmap = null;
	public Bitmap getBitmap() {
		return _bitmap;
	}
	
	private Delegate< ImageRequestor > _onImageRetrieved;
	public void SetOnComplete(Delegate< ImageRequestor > callback) {
		_onImageRetrieved = callback;
	}
	
	@Override
	protected ImageRequestor doInBackground(String... param) {
		_bitmap = downloadBitmap(param[0]);
        return this;
    }

    @Override
    protected void onPostExecute(ImageRequestor result) {
        _onImageRetrieved.invoke(result);
    }

    private Bitmap downloadBitmap(String url) {
        // initilize the default HTTP client object
        final DefaultHttpClient client = new DefaultHttpClient();

        //forming a HttoGet request
        final HttpGet getRequest = new HttpGet(url);
        try {
            HttpResponse response = client.execute(getRequest);

            //check 200 OK for success
            final int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode +
                        " while retrieving bitmap from " + url);
                return null;

            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    // getting contents from the stream
                    inputStream = entity.getContent();

                    // decoding stream data back into image Bitmap that android understands
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    return bitmap;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // You Could provide a more explicit error message for IOException
            getRequest.abort();
            Log.e("ImageDownloader", "Something went wrong while" +
                    " retrieving bitmap from " + url + e.toString());
        }

        return null;
    }
}