/* Application: animovie
 * Author: Wouter Eskens
 * API: The Movie Database (TMDb)
 * Database: SQLite 
 * Class: Applied Computer Sciences
 * School: Thomas More Kempen
 * Year: 2013-2014 */

package com.example.animovie.activity;

import java.util.ArrayList;
import java.util.LinkedList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.example.animovie.R;
import com.example.animovie.db.TMDBConnector;
import com.example.animovie.global.Global_Menu;
import com.example.animovie.global.Global_Variables;
import com.example.animovie.model.Movie;

public class Movie_Search 
		extends Global_Menu 
		implements TMDBConnector.MovieSearchResultListener, Movie.Listener
{
	
	private LinkedList<Movie>moviesUpdated;
	private ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movie_search);
		
		EditText searchBox = (EditText) findViewById(R.id.movie_search_search_box);
		searchBox.requestFocus();
		
		listView = (ListView) findViewById(R.id.movie_search_list_movies);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
                
                Movie movie = (Movie) listView.getAdapter().getItem(position);
                
                Global_Variables.movie_detail = movie;
                
                Intent intent = new Intent(getApplicationContext(), Movie_Detail.class);
                startActivity(intent);
            }
        });
    }
    
    public void onClickMovieSearch(View v){
    	EditText searchBox = (EditText) findViewById(R.id.movie_search_search_box);
        
        String searchBoxText = searchBox.getText().toString();
        
        searchBoxText = searchBoxText.replace(" ", "+");
        
        TMDBConnector.getInstance().searchMovie(searchBoxText, this); 
    }
    
    @Override
    public void onMovieSearchComplete(LinkedList<Movie> movies)
    {
    	ListView movie_list = (ListView) findViewById(R.id.movie_search_list_movies);
    	
    	ArrayList<Movie> values = new ArrayList<Movie>();

        for (int i = 0; i <= movies.size()-1; i++) {
            Movie m = movies.get(i);
            m.AddListener(this);
        	values.add(m);
        }
    	
        movie_list.setAdapter(new MovieAdapter(this, values));
        
        movie_list = (ListView) findViewById(R.id.movie_search_list_movies);
        
        moviesUpdated=movies;   
    }

	@Override
	public void onPosterUpdate(Movie m) {
		ListView movie_list = (ListView) findViewById(R.id.movie_search_list_movies);
		int index = moviesUpdated.indexOf(m);
		int first = movie_list.getFirstVisiblePosition();
		int last  = movie_list.getLastVisiblePosition();
		if ((index < first) || (last < index)) {
			// item to update is not even visible
			return;
		}
		View view = movie_list.getChildAt(index - movie_list.getFirstVisiblePosition());
		movie_list.getAdapter().getView(index, view, movie_list);
	}
}
