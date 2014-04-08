/* Application: animovie
 * Author: Wouter Eskens
 * API: The Movie Database (TMDb)
 * Database: SQLite 
 * Class: Applied Computer Sciences
 * School: Thomas More Kempen
 * Year: 2013-2014 */

package com.example.animovie.db;

import java.util.Hashtable;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.animovie.model.Movie;
import com.example.animovie.model.Movie_Actor;
import com.example.animovie.model.Movie_Director;
import com.example.animovie.model.Person;

//-------------------------------------------------------------------
// class for the connection to the internet movie database for data retrieval
public class TMDBConnector
{	
//-------------------------------------------------------------------
	// keys for url request
	private final static String SEARCH_MOVIE_KEY = "search/movie";
	private final static String SEARCH_PERSON_KEY = "search/person";
	private final static String MOVIE_KEY = "movie/";
	private final static String MOVIE_CREDITS_KEY = "/credits";
	private final static String CONFIGURATION_KEY = "configuration";
	private final static String PERSON_KEY = "person/";
	private final static String PERSON_CREDITS_KEY = "/movie_credits";
	private final static String API_KEY = "85dbcf832547b31277e001fbff55570c";
	
	private String base_url = "http://api.themoviedb.org/3/";

	// boolean indicating if the singleton has successfully
	// retrieved the servers configuration
	private boolean _configured = false;
	
//-------------------------------------------------------------------
	// image retrieval settings
	// available movie poster sizes
	private String[] _movie_poster_sizes = null;
	// selected movie poster size for download
	private int 	 _movie_poster_id = -1;
	// available person profile picture sizes
	private String[] _person_profile_sizes = null;
	// selected profile picture size for download
	private int 	 _person_profile_id = -1;
	// base_ url to for completing img path
	private String  _img_base_url = null;
	
	Hashtable< Long, Movie > _movie_poster_requests = new Hashtable< Long, Movie >();
	Hashtable< Long, Person > _person_picture_requests = new Hashtable< Long, Person >();
	
//-------------------------------------------------------------------
	// singleton methods
	private static TMDBConnector _instance = new TMDBConnector();
	public static TMDBConnector getInstance() { return _instance; }
	
//-------------------------------------------------------------------
	// making the constructor  private so no other class can instantiate it
	private TMDBConnector() {
		retrieveConfiguration();
	}
	
//-------------------------------------------------------------------
	// retrieve DB configuration
	public void retrieveConfiguration() 
	{
		String url = generateURL(CONFIGURATION_KEY, new URLParam[0]);
		URLRequestor request = new URLRequestor();
		request.SetOnComplete(new Delegate< String >(this, "ConfigurationComplete"));
		request.execute(url);
	}
//-------------------------------------------------------------------
	// interface for reporting when data is received for person details
	public interface PersonDetailsResultListener
	{
		void onPersonDetailsComplete(Person p, LinkedList< Movie_Actor > roles, LinkedList< Movie_Director > directors);
		void onPersonRoleUpdate(Movie_Actor actor);
		void onPersonDirectorUpdate(Movie_Director director);
	}
	
//-------------------------------------------------------------------
	// encapsulation of a details request for a person
	// data received is biography + list of movies
	// class implements MovieBasicInfoResultListener cause it will
	// retrieve the data of the movies received for the person's credits
	public class PersonDetailsResults implements MovieBasicInfoResultListener
	{
		private Person _person = null;
		private LinkedList< Movie_Actor > 	 _roles     = new LinkedList< Movie_Actor >();
		private LinkedList< Movie_Director > _directors = new LinkedList< Movie_Director >();
		private PersonDetailsResultListener _listener;
		
		public PersonDetailsResults(Person person, PersonDetailsResultListener listener)
		{
			_person = person;
			_listener = listener;
		}
		
		public void retrieve() {
			// first get the person basic profile info
			URLParam[] details_params = new URLParam[0];
			String details_url = generateURL(PERSON_KEY + Long.toString(_person.getId()), details_params);
			Log.i("test", "Retrieve person info: " + details_params);
			URLRequestor request_details = new URLRequestor();
			request_details.SetOnComplete(new Delegate< String >(this, "onDetailsRetrieved"));
			request_details.execute(details_url);
			
			// second get the list of movie he played / directed
			URLParam[] credits_params = new URLParam[0];
			String credits_url = generateURL((PERSON_KEY + Long.toString(_person.getId()) + PERSON_CREDITS_KEY), credits_params);
			Log.i("test", "Retrieve person credits: " + credits_url);
			URLRequestor request_credits = new URLRequestor();
			request_credits.SetOnComplete(new Delegate< String >(this, "onCreditsRetrieved"));
			request_credits.execute(credits_url);
		}
		
		@SuppressWarnings("unused")
		private void onDetailsRetrieved(String response) {
			JSONObject jsonPerson = null;
			try {
				jsonPerson = new JSONObject(response);
				String bio = jsonPerson.getString("biography");
				_person.setInfo(bio);
				String name = jsonPerson.getString("name");
				_person.setName(name);
				_listener.onPersonDetailsComplete(_person, _roles, _directors);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		@SuppressWarnings("unused")
		private void onCreditsRetrieved(String response) {
			try {
				JSONObject jsonCredits = new JSONObject(response);
				JSONArray cast = jsonCredits.getJSONArray("cast");
				for(int i = 0; i < cast.length(); ++i) {
					JSONObject movie = cast.getJSONObject(i);
					long id = movie.getLong("id");
					Movie_Actor role = new Movie_Actor();
					role.setRole(movie.getString("character"));
					role.setMovieId(id);
					role.setActorId(_person.getId());
					
					Movie m = new Movie();
					m.setId(id);
					MovieBasicInfoResults r = new MovieBasicInfoResults(m, this);
					r.retrieve();
					
					_roles.add(role);
				}
				JSONArray crew = jsonCredits.getJSONArray("crew");
				for(int i = 0; i < crew.length(); ++i) {
					JSONObject movie = crew.getJSONObject(i);
					String job = movie.getString("job");
					if (job.equalsIgnoreCase("director")) {
						Movie_Director director = new Movie_Director();
						long id = movie.getLong("id");
						director.setDirectorId(_person.getId());
						director.setMovieId(id);
						
						Movie m = new Movie();
						m.setId(id);
						MovieBasicInfoResults r = new MovieBasicInfoResults(m, this);
						r.retrieve();
						
						
						_directors.add(director);
					}
				}
				_listener.onPersonDetailsComplete(_person, _roles, _directors);
			}
			catch(JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onMovieBasicInfoComplete(Movie m) {
			for(int i = 0; i < _roles.size(); ++i) {
				Movie_Actor role = _roles.get(i);
				if(role.getMovieId() == m.getId()) {
					role.setMovie(m);
					_listener.onPersonRoleUpdate(role);
				}
			}
			for(int i = 0; i < _directors.size(); ++i) {
				Movie_Director director = _directors.get(i);
				if(director.getMovieId() == m.getId()) {
					director.setMovie(m);
					_listener.onPersonDirectorUpdate(director);
				}
			}
		}
	}
	
//-------------------------------------------------------------------
	// interface for reporting back for a basic info request of person
	public interface PersonBasicInfoResultListener
	{
		void onPersonBasicInfoComplete(Person p);
	}
	
//-------------------------------------------------------------------
	// encapsulation of a basic info request for a person
	// only sets the name and biography of the person
	public class PersonBasicInfoResults
	{
		private Person _person = null;
		private PersonBasicInfoResultListener _listener;
		
		public PersonBasicInfoResults(Person person, PersonBasicInfoResultListener listener) {
			_person = person;
			_listener = listener;
		}
		
		public void retrieve() {
			URLParam[] details_params = new URLParam[0];
			String details_url = generateURL(PERSON_KEY + Long.toString(_person.getId()), details_params);
			Log.i("request", "Retrieve person basic info: " + details_params);
			URLRequestor request_details = new URLRequestor();
			request_details.SetOnComplete(new Delegate< String >(this, "onDetailsRetrieved"));
			request_details.execute(details_url);
		}
		
		@SuppressWarnings("unused")
		private void onDetailsRetrieved(String response) {
			JSONObject jsonPerson = null;
			try {
				jsonPerson = new JSONObject(response);
				String bio = jsonPerson.getString("biography");
				_person.setInfo(bio);
				String name = jsonPerson.getString("name");
				_person.setName(name);
				_listener.onPersonBasicInfoComplete(_person);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

//-------------------------------------------------------------------
	public interface MovieBasicInfoResultListener
	{
		void onMovieBasicInfoComplete(Movie m);
	}
	
//-------------------------------------------------------------------
	public class MovieBasicInfoResults
	{
		private Movie _movie = null;
		private MovieBasicInfoResultListener _listener;
		
		public MovieBasicInfoResults(Movie movie, MovieBasicInfoResultListener listener) {
			_movie = movie;
			_listener = listener;
		}
		
		public void retrieve() {
			URLParam[] details_params = new URLParam[0];
			String details_url = generateURL(MOVIE_KEY + Long.toString(_movie.getId()), details_params);
			Log.i("request", "Retrieve movie basic info: " + details_params);
			URLRequestor request_details = new URLRequestor();
			request_details.SetOnComplete(new Delegate< String >(this, "onDetailsRetrieved"));
			request_details.execute(details_url);
		}
		
		@SuppressWarnings("unused")
		private void onDetailsRetrieved(String response) {
			JSONObject jsonMovie = null;
			try {
				jsonMovie = new JSONObject(response);
				String info = jsonMovie.getString("overview");
				String title = jsonMovie.getString("title");
				String release = jsonMovie.getString("release_date");
				int year = -1;
				if (release.matches("^[0-9]*-[0-9]*-[0-9]*$")) {
					year = Integer.parseInt(release.split("-")[0]);
				}
				_movie.setInfo(info);
				_movie.setTitle(title);
				_movie.setYear(year);
				_listener.onMovieBasicInfoComplete(_movie);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

//-------------------------------------------------------------------
	public interface MovieDetailsResultListener
	{
//		void onMovieSearchResult(Movie movie);
		void onMovieDetailsComplete(Movie m, LinkedList< Movie_Actor > actors, LinkedList< Movie_Director > directors);
		void onActorUpdate(Movie_Actor actor);
		void onDirectorUpdate(Movie_Director director);
	}
	
//-------------------------------------------------------------------
	public class MovieDetailsResults implements PersonBasicInfoResultListener
	{
		private Movie _movie = null;
		private LinkedList< Movie_Actor > _roles = new LinkedList< Movie_Actor >();
		private LinkedList< Movie_Director > _directors = new LinkedList< Movie_Director >();
		private MovieDetailsResultListener _listener;
		
		public MovieDetailsResults(Movie movie, MovieDetailsResultListener listener)
		{
			_movie = movie;
			_listener = listener;
		}
		
		public void retrieve() {
			URLParam[] details_params = new URLParam[0];
			String details_url = generateURL(MOVIE_KEY + Long.toString(_movie.getId()), details_params);
			Log.i("test", "Retrieve movie info: " + details_params);
			URLRequestor request_details = new URLRequestor();
			request_details.SetOnComplete(new Delegate< String >(this, "onDetailsRetrieved"));
			request_details.execute(details_url);
			
			URLParam[] credits_params = new URLParam[0];
			String credits_url = generateURL((MOVIE_KEY + Long.toString(_movie.getId()) + MOVIE_CREDITS_KEY), credits_params);
			Log.i("test", "Retrieve movie credits: " + credits_url);
			URLRequestor request_credits = new URLRequestor();
			request_credits.SetOnComplete(new Delegate< String >(this, "onCreditsRetrieved"));
			request_credits.execute(credits_url);
		}
		
		@SuppressWarnings("unused")
		private void onDetailsRetrieved(String response) {
			JSONObject jsonMovie = null;
			try {
				jsonMovie = new JSONObject(response);
				String info = jsonMovie.getString("overview");
				_movie.setInfo(info);
				_listener.onMovieDetailsComplete(_movie, _roles, _directors);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		@SuppressWarnings("unused")
		private void onCreditsRetrieved(String response) {
			try {
				JSONObject jsonCredits = new JSONObject(response);
				JSONArray cast = jsonCredits.getJSONArray("cast");
				for(int i = 0; i < cast.length(); ++i) {
					JSONObject actor = cast.getJSONObject(i);
					long id = actor.getLong("id");
					Movie_Actor role = new Movie_Actor();
					role.setRole(actor.getString("character"));
					role.setMovieId(_movie.getId());
					role.setActorId(id);
					
					Person p = new Person();
					p.setId(id);
					PersonBasicInfoResults r = new PersonBasicInfoResults(p, this);
					r.retrieve();
					
					_roles.add(role);
				}
				JSONArray crew = jsonCredits.getJSONArray("crew");
				for(int i = 0; i < crew.length(); ++i) {
					JSONObject member = crew.getJSONObject(i);
					String job = member.getString("job");
					if (job.equalsIgnoreCase("director")) {
						Movie_Director director = new Movie_Director();
						long id = member.getLong("id");
						director.setDirectorId(id);
						director.setMovieId(_movie.getId());
						
						Person p = new Person();
						p.setId(id);
						PersonBasicInfoResults r = new PersonBasicInfoResults(p, this);
						r.retrieve();
						
						_directors.add(director);
					}
				}
				_listener.onMovieDetailsComplete(_movie, _roles, _directors);
			}
			catch(JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onPersonBasicInfoComplete(Person p) {
			for(int i = 0; i < _roles.size(); ++i) {
				Movie_Actor role = _roles.get(i);
				if (role.getActorId() == p.getId()) {
					Log.v("retrieved", "actor name from id ('" + p.getId() + "') resolving " + p.getName());
					role.setPerson(p);
					_listener.onActorUpdate(role);
				}
			}
			for(int i = 0; i < _directors.size(); ++i) {
				Movie_Director director = _directors.get(i);
				if (director.getDirectorId() == p.getId()) {
					Log.v("retrieved", "actor name from id ('" + p.getId() + "') resolving " + p.getName());
					director.setPerson(p);
					_listener.onDirectorUpdate(director);
				}
			}
		}
	}
	
	public interface MovieSearchResultListener
	{
//		void onMovieSearchResult(Movie movie);
		void onMovieSearchComplete(LinkedList< Movie > movies);
	}

//-------------------------------------------------------------------
	public class MovieSearchResults
	{	
		private MovieSearchResultListener _listener = null;
		private int _current_page = 0;
		private int _total_pages = 0;
		private int _total_results = 0;
		private boolean _busy = false;
		private String _query = null;
		
		public MovieSearchResults(String query, MovieSearchResultListener listener)
		{
			super();
			_query = query;
			_listener = listener;
		}
		
		public void retrieve() {
			if (_busy) return;
			if ((_total_pages > 0) && (_current_page > _total_pages)) return;
			++_current_page;
			try {
				URLParam[] params = new URLParam[2];
				params[0] = new URLParam("query", _query);
				params[1] = new URLParam("page", Integer.toString(_current_page));
				String url = generateURL(SEARCH_MOVIE_KEY, params);
				Log.v("test", "Searching movie: " + url);
				URLRequestor request = new URLRequestor();
				request.SetOnComplete(new Delegate< String >(this, "onPageRetrieved"));
				_busy = true;
				request.execute(url);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		@SuppressWarnings("unused")
		private void onPageRetrieved(String responseString) {
			LinkedList<Movie> movies = new LinkedList<Movie>();
			try {
				JSONObject response = new JSONObject(responseString);
				int page_retrieved = response.getInt("page");
				if (page_retrieved != _current_page) return;
				_total_pages = response.getInt("total_pages");
				_total_results = response.getInt("total_results");
				JSONArray results = response.getJSONArray("results");
				for(int r = 0; r < results.length(); ++r) {
					JSONObject jsonMovie = results.getJSONObject(r);
					long id = jsonMovie.getLong("id");
					String title = jsonMovie.getString("title");
					String release = jsonMovie.getString("release_date");
					int year = -1;
					if (release.matches("^[0-9]*-[0-9]*-[0-9]*$")) {
						year = Integer.parseInt(release.split("-")[0]);
					}
					Movie movie = new Movie();
					movie.setId(id);
					movie.setTitle(title);
					movie.setYear(year);
					String poster = jsonMovie.getString("poster_path");
					movie.setPosterAvailable((poster != null) && (!poster.equals("null")));
					if (movie.posterAvailable()) {
						// loading the poster
						TMDBConnector.getInstance().retrieveMoviePoster(movie, poster);
					}
					movies.add(movie);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			_listener.onMovieSearchComplete(movies);
		}
	}

//-------------------------------------------------------------------
	public interface PersonSearchResultListener
	{
		void onPersonSearchComplete(LinkedList< Person> person);
	}
	
//-------------------------------------------------------------------
	public class PersonSearchResults
	{	
		private PersonSearchResultListener _listener = null;
		private int _current_page = 0;
		private int _total_pages = 0;
		private int _total_results = 0;
		private boolean _busy = false;
		private String _query = null;
		
		public PersonSearchResults(String query, PersonSearchResultListener listener)
		{
			super();
			_query = query;
			_listener = listener;
		}
		
		public void retrieve() {
			if (_busy) return;
			if ((_total_pages > 0) && (_current_page > _total_pages)) return;
			++_current_page;
			try {
				URLParam[] params = new URLParam[2];
				params[0] = new URLParam("query", _query);
				params[1] = new URLParam("page", Integer.toString(_current_page));
				String url = generateURL(SEARCH_PERSON_KEY, params);
				Log.v("test", "Searching person: " + url);
				URLRequestor request = new URLRequestor();
				request.SetOnComplete(new Delegate< String >(this, "onPageRetrieved"));
				_busy = true;
				request.execute(url);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		@SuppressWarnings("unused")
		private void onPageRetrieved(String responseString) {
			LinkedList< Person > persons = new LinkedList< Person >();
			try {
				JSONObject response = new JSONObject(responseString);
				int page_retrieved = response.getInt("page");
				if (page_retrieved != _current_page) return;
				_total_pages = response.getInt("total_pages");
				_total_results = response.getInt("total_results");
				JSONArray results = response.getJSONArray("results");
				for(int r = 0; r < results.length(); ++r) {
					JSONObject jsonPerson = results.getJSONObject(r);
					long id = jsonPerson.getLong("id");
					String name = jsonPerson.getString("name");
//					String release = jsonMovie.getString("release_date");
//					int year = -1;
//					if (release.matches("^[0-9]*-[0-9]*-[0-9]*$")) {
//						year = Integer.parseInt(release.split("-")[0]);
//					}
					Person person = new Person();
					person.setId(id);
					person.setName(name);
//					actor.setYear(year);
					String picture = jsonPerson.getString("profile_path");
					person.setPictureAvailable((picture != null) && (!picture.equals("null")));
					if (person.pictureAvailable()) {
						// loading the poster
						TMDBConnector.getInstance().retrievePersonPicture(person, picture);
					}
					persons.add(person);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			_listener.onPersonSearchComplete(persons);
		}
	}
	
//-------------------------------------------------------------------
	// surpressing unused warning this cause we will call it using Reflection
	@SuppressWarnings("unused")
	private void ConfigurationComplete(String response) 
	{
		try {
			JSONObject images = new JSONObject(response);
			images = images.getJSONObject("images");
			_img_base_url = images.getString("base_url");
			JSONArray poster_sizes = images.getJSONArray("poster_sizes");
			_movie_poster_sizes = new String[poster_sizes.length()];
			for(int i = 0; i < poster_sizes.length(); ++i) {
				Log.v("poster size", (String)poster_sizes.get(i));
				_movie_poster_sizes[i] = (String)poster_sizes.get(i);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//-------------------------------------------------------------------
	private LinkedList< URLRequestor > _requests;
	
//-------------------------------------------------------------------
	public String generateURL(String request, URLParam[] params) {
		String param = URLParam.construct(params, false);
		return base_url + request + "?api_key=" + API_KEY + param;
	}
	
//-------------------------------------------------------------------
	public MovieSearchResults searchMovie(String query, MovieSearchResultListener listener) {
		MovieSearchResults results = new MovieSearchResults(query, listener);
		results.retrieve();
		return results;
	}
	
//-------------------------------------------------------------------
	public void getMoreResults(MovieSearchResults results) {
		results.retrieve();
	}
	
//-------------------------------------------------------------------
	public PersonSearchResults searchPerson(String query, PersonSearchResultListener listener) {
		PersonSearchResults results = new PersonSearchResults(query, listener);
		results.retrieve();
		return results;
	}

//-------------------------------------------------------------------
	public MovieDetailsResults retrieveMovieDetails(Movie movie, MovieDetailsResultListener listener) 
	{
		MovieDetailsResults req = new MovieDetailsResults(movie, listener);
		req.retrieve();
		return req;
	}

//-------------------------------------------------------------------
	public PersonDetailsResults retrievePersonDetails(Person person, PersonDetailsResultListener listener) 
	{
		PersonDetailsResults req = new PersonDetailsResults(person, listener);
		req.retrieve();
		return req;
	}
	
//-------------------------------------------------------------------
	public void onSearchPersonComplete(String response) {
		try {
			JSONObject page = new JSONObject(response);
			JSONArray movies = page.getJSONArray("results");
			
			for(int m = 0; m < movies.length(); ++m) {
				JSONObject movie = movies.getJSONObject(m);
				Log.v("movie", movie.getString("name"));
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//-------------------------------------------------------------------
	private void retrieveMoviePoster(Movie movie, String path) 
	{
		// do nothing if there is no poster available
		if (!movie.posterAvailable()) return;
		// do nothing if already loading poster
		if(movie.posterLoading()) return;
		// do nothing if the poster was already loaded
		if (movie.posterLoaded()) return;
		try {
			String url = _img_base_url + _movie_poster_sizes[0] + path;
			Log.v("image", "retrieving poster: " + url);
			ImageRequestor r = new ImageRequestor();
			Delegate< ImageRequestor > del = new Delegate< ImageRequestor >(this, "onMoviePosterRetrieved");
			r.SetOnComplete(del);
			movie.setPosterLoading(true);
			r.execute(url);
			// TODO check if the poster is already being retrieved
			_movie_poster_requests.put(r.id(), movie);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//-------------------------------------------------------------------
	private void retrievePersonPicture(Person person, String path) 
	{
		// do nothing if there is no poster available
		if (!person.pictureAvailable()) return;
		// do nothing if already loading poster
		if(person.pictureLoading()) return;
		// do nothing if the poster was already loaded
		if (person.pictureLoaded()) return;
		try {
			String url = _img_base_url + _movie_poster_sizes[0] + path;
			Log.v("image", "retrieving picture: " + url);
			ImageRequestor r = new ImageRequestor();
			Delegate< ImageRequestor > del = new Delegate< ImageRequestor >(this, "onPersonPictureRetrieved");
			r.SetOnComplete(del);
			person.setPictureLoading(true);
			r.execute(url);
			// TODO check if the poster is already being retrieved
			_person_picture_requests.put(r.id(), person);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//-------------------------------------------------------------------
	public void onMoviePosterRetrieved(ImageRequestor r) 
	{
		Movie m = _movie_poster_requests.get(r.id());
		_movie_poster_requests.remove(r.id());
		m.setPoster(r.getBitmap());
		Log.v("image", "retrieved img for movie '" + m.getTitle() + "' with size: " + m.getPoster().getWidth() + "x" + m.getPoster().getHeight());
	}
	
//-------------------------------------------------------------------
	public void onPersonPictureRetrieved(ImageRequestor r) 
	{
		Person p = _person_picture_requests.get(r.id());
		_movie_poster_requests.remove(r.id());
		p.setPicture(r.getBitmap());
		Log.v("image", "retrieved img for person '" + p.getName() + "' with size: " + p.getPicture().getWidth() + "x" + p.getPicture().getHeight());
	}
//-------------------------------------------------------------------
}