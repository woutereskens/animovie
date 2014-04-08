/* Application: animovie
 * Author: Wouter Eskens
 * API: The Movie Database (TMDb)
 * Database: SQLite 
 * Class: Applied Computer Sciences
 * School: Thomas More Kempen
 * Year: 2013-2014 */

package com.example.animovie.activity;

import java.util.ArrayList;
import com.example.animovie.R;
import com.example.animovie.model.Movie;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieAdapter extends BaseAdapter
{
	private Activity activity;
    private ArrayList<Movie> data;
    private static LayoutInflater inflater=null;
 
    public MovieAdapter(Activity a, ArrayList<Movie> movies) {
        activity = a;
        data=movies;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
	
	public int getCount() {
        return data.size();
    }
	
	public Object getItem(int position) {
        return data.get(position);
    }
	
	public long getItemId(int position) {
        return position;
    }
	
	public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.movie_search_item, null);

        ImageView image = (ImageView) vi.findViewById(R.id.movie_search_list_item_poster); // image
        TextView title = (TextView) vi.findViewById(R.id.movie_search_list_item_title); // title
        
        Movie movie = null;
        movie = data.get(position);
        
        // Setting all values in listview
        image.setImageBitmap(movie.getPoster());
        title.setText(movie.getTitle() + " (" + movie.getYear() + ")");
        return vi;
	}
}
