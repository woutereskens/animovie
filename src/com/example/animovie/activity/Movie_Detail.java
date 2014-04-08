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

import com.example.animovie.DatabaseHelper;
import com.example.animovie.R;
import com.example.animovie.db.TMDBConnector;
import com.example.animovie.db.TMDBConnector.MovieDetailsResultListener;
import com.example.animovie.global.Global_Menu;
import com.example.animovie.global.Global_Variables;
import com.example.animovie.model.Movie;
import com.example.animovie.model.Movie_Actor;
import com.example.animovie.model.Movie_Director;
import com.example.animovie.model.Movie_User;
import com.example.animovie.model.User;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Movie_Detail extends Global_Menu implements MovieDetailsResultListener { 
	private DatabaseHelper database_helper;
	private User user;
	
	private Movie _movie;
	private LinkedList<Movie_Actor> _actors;
	private LinkedList<Movie_Director> _directors;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movie_detail);
		
		database_helper = Global_Variables.database_helper;
		user = Global_Variables.loggedInUser;
		
		Movie movie = Global_Variables.movie_detail;
		
		ImageView movie_poster = (ImageView) findViewById(R.id.movie_detail_poster);
		TextView movie_title = (TextView) findViewById(R.id.movie_detail_title);
		TextView movie_info = (TextView) findViewById(R.id.movie_detail_info);
		
		movie_poster.setImageBitmap(movie.getPoster());
		movie_title.setText(movie.getTitle() + " (" + movie.getYear() + ")");
		
		if(movie.infoAvailable()){
			movie_info.setText(movie.getInfo());
		} else {
			movie_info.setText("No info to be displayed");
		}
		
		TMDBConnector.getInstance().retrieveMovieDetails(movie, this);
		
		Button button_add_movie = (Button) findViewById(R.id.movie_detail_add_movie);
		RatingBar movie_detail_rating_bar = (RatingBar) findViewById (R.id.movie_detail_rating_bar);
		
		addListenerOnRatingBar();
		
		if(Global_Variables.loggedInUser != null){
			Movie_User movie_user = database_helper.getMovie_User_By_Movie_id_and_User_id(movie.getId(), user.getId());
			
			if(movie_user == null){
				button_add_movie.setEnabled(true);
				movie_detail_rating_bar.setEnabled(false);
			} else {
				movie_detail_rating_bar.setEnabled(true);
				
				TextView movie_detail_rating = (TextView) findViewById(R.id.movie_detail_rating);
				movie_detail_rating.setText("Your current rating: " + movie_user.getScore() + "/5");
			}
		} else {
			button_add_movie.setEnabled(false);
			movie_detail_rating_bar.setEnabled(false);
		}
	}

	public void addListenerOnRatingBar() {
		 
		RatingBar movie_detail_rating_bar = (RatingBar) findViewById (R.id.movie_detail_rating_bar);
	 
		//if rating value is changed,
		//display the current rating value in the result (textview) automatically
		movie_detail_rating_bar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float rating,
				boolean fromUser) {
				
				Movie movie = Global_Variables.movie_detail;
				
				Movie_User movie_user = new Movie_User();
				
				movie_user = database_helper.getMovie_User_By_Movie_id_and_User_id(movie.getId(), user.getId());
				
				movie_user.setScore(rating);
				
				try{
					database_helper.updateMovie_User(movie_user);
				} catch (Exception e){
					Toast toast = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG);
	        		toast.show();
				}
	 
				finish();
				startActivity(getIntent());
			}
		});
	  }
	
	
	@Override
	public void onMovieDetailsComplete(Movie movie, LinkedList<Movie_Actor> actors,
			LinkedList<Movie_Director> directors) {
		_movie = movie;
		_actors = actors;
		_directors = directors;
		
		ImageView movie_poster = (ImageView) findViewById(R.id.movie_detail_poster);
		TextView movie_title = (TextView) findViewById(R.id.movie_detail_title);
		TextView movie_info = (TextView) findViewById(R.id.movie_detail_info);
		
		movie_poster.setImageBitmap(movie.getPoster());
		movie_title.setText(movie.getTitle() + " (" + movie.getYear() + ")");
		
		if(movie.infoAvailable()){
			movie_info.setText(movie.getInfo());
		} else {
			movie_info.setText("No info to be displayed");
		}
		
		ArrayList<String> director_list = new ArrayList<String>();
		
		for (int i = 0; i <= directors.size()-1; i++) {
            Movie_Director movie_director = directors.get(i);
            
            if(movie_director.getPerson() != null){
            	director_list.add(movie_director.getPerson().getName());
            } else {
            	director_list.add(Long.toString(movie_director.getDirectorId()));
            }     
        }
		
		ArrayList<String> actor_list = new ArrayList<String>();
		
		for (int i = 0; i <= actors.size()-1; i++) {
            Movie_Actor movie_actor = actors.get(i);
            
            if(movie_actor.getPerson() != null){
            	actor_list.add(movie_actor.getPerson().getName() + " - " + movie_actor.getRole());
            } else {
            	actor_list.add(movie_actor.getActorId() + " - " + movie_actor.getRole());
            }
        }
		
		Spinner spinner_directors = (Spinner) findViewById(R.id.movie_detail_directors);
		ArrayAdapter<String> adapter_directors = new ArrayAdapter <String>(this, android.R.layout.simple_spinner_item, director_list);
		spinner_directors.setAdapter(adapter_directors);
		
		Spinner spinner_actors = (Spinner) findViewById(R.id.movie_detail_actors);
		ArrayAdapter<String> adapter_actors = new ArrayAdapter <String>(this, android.R.layout.simple_spinner_item, actor_list);
		spinner_actors.setAdapter(adapter_actors);
		
		Global_Variables.movie_detail = movie;
	}

	@Override
	public void onActorUpdate(Movie_Actor actor) {
		// TODO Auto-generated method stub
		onMovieDetailsComplete(_movie, _actors, _directors);
	}

	@Override
	public void onDirectorUpdate(Movie_Director director) {
		// TODO Auto-generated method stub
		onMovieDetailsComplete(_movie, _actors, _directors);
	}
	
	public void onClickAddMovie(View v){
		Movie movie = Global_Variables.movie_detail;
		
		if(database_helper.getMovie(movie.getId()) == null){
			try{
				database_helper.insertMovie(movie);
				
				if(database_helper.getMovie_User_By_Movie_id_and_User_id(movie.getId(), user.getId()) == null){
					Movie_User movie_user = new Movie_User();
					movie_user.setMovieId(Integer.parseInt(String.valueOf(movie.getId())));
					movie_user.setUserId(Integer.parseInt(String.valueOf(user.getId())));
					movie_user.setScore(0);
					movie_user.setSeen(1);
						
					database_helper.insertMovie_User(movie_user);
				}
				Toast toast = Toast.makeText(getApplicationContext(), "Succesfully added " + movie.getTitle() + " to your movie collection", Toast.LENGTH_LONG);
        		toast.show();
			} catch (Exception e){
				Toast toast = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG);
        		toast.show();
			}
		} else {
			if(database_helper.getMovie_User_By_Movie_id_and_User_id(movie.getId(), user.getId()) == null){
				Movie_User movie_user = new Movie_User();
				movie_user.setMovieId(Integer.parseInt(String.valueOf(movie.getId())));
				movie_user.setUserId(Integer.parseInt(String.valueOf(user.getId())));
				movie_user.setScore(0);
				movie_user.setSeen(1);
				
				try{
					database_helper.insertMovie_User(movie_user);
					
					Toast toast = Toast.makeText(getApplicationContext(), "Succesfully added " + movie.getTitle() + " to your movie collection", Toast.LENGTH_LONG);
	        		toast.show();
				} catch (Exception e){
					Toast toast = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG);
	        		toast.show();
				}
			}
		}
		
		finish();
		startActivity(getIntent());
	}
}
