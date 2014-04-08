/* Application: animovie
 * Author: Wouter Eskens
 * API: The Movie Database (TMDb)
 * Database: SQLite 
 * Class: Applied Computer Sciences
 * School: Thomas More Kempen
 * Year: 2013-2014 */

package com.example.animovie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.animovie.model.Movie;
import com.example.animovie.model.Movie_Actor;
import com.example.animovie.model.Movie_Director;
import com.example.animovie.model.Movie_User;
import com.example.animovie.model.Person;
import com.example.animovie.model.User;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static DatabaseHelper instance = null;
	
	public static DatabaseHelper getInstance(){
		return instance;
	}
	
    // Database Version
    private static final int DATABASE_VERSION = 2;
    // Database Name
    private static final String DATABASE_NAME = "animovie";
        
    // executed when instantiating DatabaseHelper
    // -> if Database doesn't exist, call onCreate
    // -> if DATABASE_VERSION is higher than this version, call onUpgrade
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        assert(instance==null);
        instance=this;
    }
    
    // method is executed when the database is created
    // create tables and fill with data
    @Override
    public void onCreate(SQLiteDatabase db) {
    	//movie
        String CREATE_TABLE_MOVIE = "CREATE TABLE movie (" +
                "id INTEGER PRIMARY KEY," +
                "title TEXT," +
                "info TEXT," +
                "year INTEGER," +
                "poster BLOB"
                + ")";
        db.execSQL(CREATE_TABLE_MOVIE);
        insertMovies(db);
        
        
        //movie_user
        String CREATE_TABLE_MOVIE_USER = "CREATE TABLE movie_user (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "movieId INTEGER," +
                "userId INTEGER," +
                "score FLOAT," +
                "seen INTEGER"
                + ")";
        db.execSQL(CREATE_TABLE_MOVIE_USER);
        insertMovie_User(db);
        
        
        //user
        String CREATE_TABLE_USER = "CREATE TABLE user (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username STRING," +
                "password STRING," +
                "email STRING"
                + ")";
        db.execSQL(CREATE_TABLE_USER);
        insertUser(db);
        
        
        //movie_director
        String CREATE_TABLE_MOVIE_DIRECTOR = "CREATE TABLE movie_director (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "movieId INTEGER," +
                "directorId INTEGER"
                + ")";
        db.execSQL(CREATE_TABLE_MOVIE_DIRECTOR);
        insertMovie_Director(db);
        
        
        //director
        //String CREATE_TABLE_DIRECTOR = "CREATE TABLE director (" +
        //        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
        //        "name STRING," +
        //        "birth_date STRING," +
        //        "info STRING"
        //        + ")";
        //db.execSQL(CREATE_TABLE_DIRECTOR);
        //insertDirector(db);
        
        //movie_actor
        String CREATE_TABLE_MOVIE_ACTOR = "CREATE TABLE movie_actor (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "movieId INTEGER," +
                "actorId INTEGER," + 
                "role STRING"
                + ")";
        db.execSQL(CREATE_TABLE_MOVIE_ACTOR);
        insertMovie_Actor(db);
        
        
        //actor
        //String CREATE_TABLE_ACTOR = "CREATE TABLE actor (" +
        //        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
        //        "name STRING," +
        //        "birth_date STRING," +
        //        "info STRING"
        //        + ")";
        //db.execSQL(CREATE_TABLE_ACTOR);
        //insertActor(db);
        
        //person
        String CREATE_TABLE_PERSON = "CREATE TABLE person(" +
        		"id INTEGER PRIMARY KEY," + 
        		"name STRING," +
        		"birth_date STRING," + 
        		"info STRING," +
        		"picture BLOB"
        		+ ")";
        db.execSQL(CREATE_TABLE_PERSON);
        insertPerson(db);
    }
            
    private void insertMovies(SQLiteDatabase db) {
       db.execSQL("INSERT INTO movie (id, title, info, year) VALUES (1, 'God Zoekt Zoon', 'God searches for a new son.', 2005);");
    }
    private void insertMovie_User(SQLiteDatabase db)
    {
       db.execSQL("INSERT INTO movie_user (movieId, userId, score, seen) VALUES ('1', '1', '7.0', '1');");
    }
    private void insertUser(SQLiteDatabase db)
    {
       db.execSQL("INSERT INTO user (id, username, password, email) VALUES (1, 'Wouter', 'wouter', 'wouter.eskens@hotmail.com');");
    }
    private void insertMovie_Director(SQLiteDatabase db)
    {
       db.execSQL("INSERT INTO movie_director (movieId, directorId) VALUES ('1', '1');");
    }
    //private void insertDirector(SQLiteDatabase db)
    //{
    //   db.execSQL("INSERT INTO director (name, birth_date, info) VALUES ('David Fincher', '17/09/1965', 'Bryan Singer (born September 17, 1965) is an American film director, writer and producer. Singer won critical acclaim for his work on The Usual Suspects, and is especially well-known among fans of the science fiction and superhero genres for his work on the X-Men films and Superman Returns.');");
    //}
    private void insertMovie_Actor(SQLiteDatabase db)
    {
       db.execSQL("INSERT INTO movie_actor (movieId, actorId, role) VALUES ('1', '1', 'Jezus');");
    }
    //private void insertActor(SQLiteDatabase db)
    //{
    //   db.execSQL("INSERT INTO actor (name, birth_date, info) VALUES ('Hugh Jackman', '12/10/1968', 'Born in Sydney, to English parents, and the youngest of five children, Jackman has a communications degree with a journalism major from the University of Technology Sydney. After graduating, he pursued drama at the Western Australian Academy of Performing Arts, immediately after which he was offered a starring role in the ABC-TV prison drama .');");
    //}
    private void insertPerson(SQLiteDatabase db){
    	db.execSQL("INSERT INTO person (id, name, birth_date, info) VALUES ('1', 'Wouter Eskens', '07-07-1993', 'Wouter Eskens, born in Turnhout.');");
    }
    
    
    // this method is executed when the database is upgraded
    // drop all tables and create them again
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS movie");
        db.execSQL("DROP TABLE IF EXISTS movie_user");
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS movie_director");
        db.execSQL("DROP TABLE IF EXISTS movie_actor");
        db.execSQL("DROP TABLE IF EXISTS person");
        // Create tables again
        onCreate(db);
    }
    
    //-------------------------------------------------------------------------------------------------
    //  CRUD Operations
    //-------------------------------------------------------------------------------------------------
    
    // insert-methode met ContentValues
    public long insertMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        
        values.put("id", movie.getId());
        values.put("title", movie.getTitle());
        values.put("info", movie.getInfo());
        values.put("year", movie.getYear());

        Bitmap image = movie.getPoster();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, out);
        
        values.put("poster", out.toByteArray());

        long id = db.insert("movie", null, values);
        
        db.close();
        return id;
    }
    
    
    public long insertMovie_User(Movie_User movie_user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        
        values.put("movieId", movie_user.getMovieId());
        values.put("userId", movie_user.getUserId());
        values.put("score", movie_user.getScore());
        values.put("seen", movie_user.getSeen());

        long id = db.insert("movie_user", null, values);
        
        db.close();
        return id;
    }
    
    public long insertMovie_Director(Movie_Director movie_director) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
               
        values.put("movieId", movie_director.getMovieId());
        values.put("directorId", movie_director.getDirectorId());

        long id = db.insert("movie_director", null, values);
        
        db.close();
        return id;
    }
    
    public long insertMovie_Actor(Movie_Actor movie_actor) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        
        values.put("movieId", movie_actor.getMovieId());
        values.put("actorId", movie_actor.getActorId());
        values.put("role", movie_actor.getRole());


        long id = db.insert("movie_actor", null, values);
        
        db.close();
        return id;
    }
    
    public long insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        values.put("email", user.getEmail());


        long id = db.insert("user", null, values);
        
        db.close();
        return id;
    }
    
    //public long insertDirector(Director director) {
    //    SQLiteDatabase db = this.getWritableDatabase();

    //    ContentValues values = new ContentValues();
        
    //    values.put("name", director.getName());
    //    values.put("birth_date", director.getBirth_date());
    //    values.put("info", director.getInfo());


    //    long id = db.insert("director", null, values);
        
    //    db.close();
    //    return id;
    //}
    
    //public long insertActor(Actor actor) {
    //    SQLiteDatabase db = this.getWritableDatabase();

    //    ContentValues values = new ContentValues();
        
    //    values.put("name", actor.getName());
    //    values.put("birth_date", actor.getBirth_date());
    //    values.put("info", actor.getInfo());


    //    long id = db.insert("actor", null, values);
        
    //    db.close();
    //    return id;
    //}
    
    public long insertPerson(Person person){
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	ContentValues values = new ContentValues();
    	
    	values.put("id", person.getId());
    	values.put("name", person.getName());
    	values.put("birth_date", person.getBirth_Date());
    	values.put("info", person.getInfo());
    	
    	Bitmap image = person.getPicture();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, out);
        
        values.put("picture", out.toByteArray());
        
        long id = db.insert("person", null, values);
        
        db.close();
        return id;
    }

    // update-methode met ContentValues
    public boolean updateMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        
        values.put("title", movie.getTitle());
        values.put("info", movie.getInfo());
        values.put("year", movie.getYear());
        
        Bitmap image = movie.getPoster();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, out);
        
        values.put("poster", out.toByteArray());

        int numrows = db.update(
                "movie", 
                values, 
                "id = ?",
                new String[] { String.valueOf(movie.getId()) });
        
        db.close();
        return numrows > 0;
    }
    
    
    public boolean updateMovie_User(Movie_User movie_user) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        
        values.put("movieId", movie_user.getMovieId());
        values.put("userId", movie_user.getUserId());
        values.put("score", movie_user.getScore());
        values.put("seen", movie_user.getSeen());

        int numrows = db.update(
                "movie_user", 
                values, 
                "id = ?",
                new String[] { String.valueOf(movie_user.getId()) });
        
        db.close();
        return numrows > 0;
    }
    
    public boolean updateMovie_Director(Movie_Director movie_director) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        
        values.put("movieId", movie_director.getMovieId());
        values.put("directorId", movie_director.getDirectorId());
        
        int numrows = db.update(
                "movie_director", 
                values, 
                "id = ?",
                new String[] { String.valueOf(movie_director.getId()) });
        
        db.close();
        return numrows > 0;
    }
    
    public boolean updateMovie_Actor(Movie_Actor movie_actor) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        
        values.put("movieId", movie_actor.getMovieId());
        values.put("actorId", movie_actor.getActorId());
        values.put("role", movie_actor.getRole());

        int numrows = db.update(
                "movie_actor", 
                values, 
                "id = ?",
                new String[] { String.valueOf(movie_actor.getId()) });
        
        db.close();
        return numrows > 0;
    }
    
    public boolean updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        values.put("email", user.getEmail());


        int numrows = db.update(
                "user", 
                values, 
                "id = ?",
                new String[] { String.valueOf(user.getId()) });
        
        db.close();
        return numrows > 0;
    }
    
    //public boolean updateDirector(Director director) {
    //    SQLiteDatabase db = this.getWritableDatabase();
 
    //    ContentValues values = new ContentValues();
        
    //    values.put("name", director.getName());
    //    values.put("birth_date", director.getBirth_date());
    //    values.put("info", director.getInfo());


    //    int numrows = db.update(
    //            "director", 
    //            values, 
    //            "id = ?",
    //            new String[] { String.valueOf(director.getId()) });
    //    
    //    db.close();
    //    return numrows > 0;
    //}
    
    //public boolean updateActor(Actor actor) {
    //    SQLiteDatabase db = this.getWritableDatabase();
 
    //    ContentValues values = new ContentValues();
        
    //    values.put("name", actor.getName());
    //    values.put("birth_date", actor.getBirth_date());
    //    values.put("info", actor.getInfo());


    //    int numrows = db.update(
    //            "actor", 
    //            values, 
    //            "id = ?",
    //            new String[] { String.valueOf(actor.getId()) });
        
    //    db.close();
    //    return numrows > 0;
    //}
    

  public boolean updatePerson(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        
    	values.put("name", person.getName());
    	values.put("birth_date", person.getBirth_Date());
    	values.put("info", person.getInfo());
    	
    	Bitmap image = person.getPicture();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, out);
        
        values.put("picture", out.toByteArray());

        int numrows = db.update(
                "person", 
                values, 
                "id = ?",
                new String[] { String.valueOf(person.getId()) });
        
        db.close();
        return numrows > 0;
    }
    
    // delete-method
    public boolean deleteMovie(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int numrows = db.delete(
                "movie",
                "id = ?",
                new String[] { String.valueOf(id) });
        
        db.close();
        return numrows > 0;
    }
    
    public boolean deleteMovie_User(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int numrows = db.delete(
                "movie_user",
                "id = ?",
                new String[] { String.valueOf(id) });
        
        db.close();
        return numrows > 0;
    }
    
    public boolean deleteMovie_Director(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int numrows = db.delete(
                "movie_director",
                "id = ?",
                new String[] { String.valueOf(id) });
        
        db.close();
        return numrows > 0;
    }
    
    public boolean deleteMovie_Actor(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int numrows = db.delete(
                "movie_actor",
                "id = ?",
                new String[] { String.valueOf(id) });
        
        db.close();
        return numrows > 0;
    }
    
    public boolean deleteUser(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int numrows = db.delete(
                "user",
                "id = ?",
                new String[] { String.valueOf(id) });
        
        db.close();
        return numrows > 0;
    }
    
    //public boolean deleteDirector(long id) {
    //    SQLiteDatabase db = this.getWritableDatabase();

    //    int numrows = db.delete(
    //            "director",
    //            "id = ?",
    //            new String[] { String.valueOf(id) });
        
    //    db.close();
    //    return numrows > 0;
    //}
    
    //public boolean deleteActor(long id) {
    //    SQLiteDatabase db = this.getWritableDatabase();

    //    int numrows = db.delete(
    //            "actor",
    //            "id = ?",
    //            new String[] { String.valueOf(id) });
        
    //    db.close();
    //    return numrows > 0;
    //}
    
    public boolean deletePerson(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int numrows = db.delete(
                "person",
                "id = ?",
                new String[] { String.valueOf(id) });
        
        db.close();
        return numrows > 0;
    }

    // query-methode
    public Movie getMovie(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(
                "movie",      // table name
                new String[] { "id", "title", "info", "year", "poster"}, // columns
                "id = ?",  // selection
                new String[] { String.valueOf(id) }, // selection parameters
                null,           // groupby
                null,           // having
                null,           // sorting
                null);          // ??
    
        Movie movie = null;
        if (cursor.moveToFirst()) {
        	Bitmap bitmap = BitmapFactory.decodeByteArray(cursor.getBlob(4) , 0, cursor.getBlob(4).length);
        	
            movie = new Movie(
            	Long.parseLong(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getInt(3),
                bitmap);
        }
        cursor.close();
        db.close();
        return movie;
    }
    
    public Movie_User getMovie_User(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(
                "movie_user",      // table name
                new String[] { "id", "movieId", "userId", "score", "seen"}, // columns
                "id = ?",  // selection
                new String[] { String.valueOf(id) }, // selection parameters
                null,           // groupby
                null,           // having
                null,           // sorting
                null);          // ??
    
        Movie_User movie_user = new Movie_User();
        
        if (cursor.moveToFirst()) {
        	movie_user = new Movie_User(
            	Long.parseLong(cursor.getString(0)),
                cursor.getInt(1),
                cursor.getInt(2),
                Float.parseFloat(cursor.getString(3)),
                cursor.getInt(4));
        }
        cursor.close();
        db.close();
        return movie_user;
    }
    
    public Movie_User getMovie_User_By_Movie_id_and_User_id(long movie_id, long user_id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(
                "movie_user",      // table name
                new String[] { "id", "movieId", "userId", "score", "seen"}, // columns
                "movieId = ? AND userId = ?", // selection
                new String[] { String.valueOf(movie_id), String.valueOf(user_id) }, // selection parameters
                null,           // groupby
                null,           // having
                null,           // sorting
                null);          // ??
    
        Movie_User movie_user = null;
        
        if (cursor.moveToFirst()) {
        	movie_user = new Movie_User(
            	Long.parseLong(cursor.getString(0)),
                cursor.getInt(1),
                cursor.getInt(2),
                Float.parseFloat(cursor.getString(3)),
                cursor.getInt(4));
        }
        cursor.close();
        db.close();
        return movie_user;
    }
    
    public Movie_Director getMovie_Director(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(
                "movie_director",      // table name
                new String[] { "id", "movieId", "directorId"}, // columns
                "id = ?",  // selection
                new String[] { String.valueOf(id) }, // selection parameters
                null,           // groupby
                null,           // having
                null,           // sorting
                null);          // ??
    
        Movie_Director movie_director = new Movie_Director();
        
        if (cursor.moveToFirst()) {
        	movie_director = new Movie_Director(
            	Long.parseLong(cursor.getString(0)),
                cursor.getInt(1),
                cursor.getInt(2));
        }
        cursor.close();
        db.close();
        return movie_director;
    }
    
    public Movie_Actor getMovie_Actor(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(
                "movie_actor",      // table name
                new String[] { "id", "movieId", "actorId", "role"}, // columns
                "id = ?",  // selection
                new String[] { String.valueOf(id) }, // selection parameters
                null,           // groupby
                null,           // having
                null,           // sorting
                null);          // ??
    
        Movie_Actor movie_actor = new Movie_Actor();
        
        if (cursor.moveToFirst()) {
        	movie_actor = new Movie_Actor(
            	Long.parseLong(cursor.getString(0)),
                cursor.getInt(1),
                cursor.getInt(2),
                cursor.getString(3));
        }
        cursor.close();
        db.close();
        return movie_actor;
    }
    
    public User getUser(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(
                "user",      // table name
                new String[] { "id", "username", "password", "email"}, // columns
                "id = ?",  // selection
                new String[] { String.valueOf(id) }, // selection parameters
                null,           // groupby
                null,           // having
                null,           // sorting
                null);          // ??
    
        User user = new User();
        
        if (cursor.moveToFirst()) {
             user = new User(
            	Long.parseLong(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3));
        }
        cursor.close();
        db.close();
        return user;
    }
    
    
    public User getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(
                "user",      // table name
                new String[] { "id", "username", "password", "email"}, // columns
                "username = ?",  // selection
                new String[] { String.valueOf(username) }, // selection parameters
                null,           // groupby
                null,           // having
                null,           // sorting
                null);          // ??
    
        User user = new User();
        
        if (cursor.moveToFirst()) {
             user = new User(
            	Long.parseLong(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3));
        }else{
        	return null;
        }
        cursor.close();
        db.close();
        return user;
    }
    
    
    
    /*public Director getDirector(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(
                "director",      // table name
                new String[] { "id", "name", "birth_date", "info"}, // columns
                "id = ?",  // selection
                new String[] { String.valueOf(id) }, // selection parameters
                null,           // groupby
                null,           // having
                null,           // sorting
                null);          // ??
    
        Director director = new Director();
        
        if (cursor.moveToFirst()) {
        	director = new Director(
            	Long.parseLong(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3));
        }
        cursor.close();
        db.close();
        return director;
    }*/
    
    /*public Actor getActor(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(
                "actor",      // table name
                new String[] { "id", "name", "birth_date", "info"}, // columns
                "id = ?",  // selection
                new String[] { String.valueOf(id) }, // selection parameters
                null,           // groupby
                null,           // having
                null,           // sorting
                null);          // ??
    
        Actor actor = new Actor();
      
        if (cursor.moveToFirst()) {
        	actor = new Actor(
            	Long.parseLong(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3));
        }
        cursor.close();
        db.close();
        return actor;
    }*/
    
    public Person getPerson(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(
                "person",      // table name
                new String[] { "id", "name", "birth_date", "info", "picture"}, // columns
                "id = ?",  // selection
                new String[] { String.valueOf(id) }, // selection parameters
                null,           // groupby
                null,           // having
                null,           // sorting
                null);          // ??
    
        Person person = null;
        
        if (cursor.moveToFirst()) {
        	Bitmap bitmap = BitmapFactory.decodeByteArray(cursor.getBlob(4) , 0, cursor.getBlob(4).length);
        	
        	person = new Person(
            	Long.parseLong(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                bitmap);
        }
        cursor.close();
        db.close();
        return person;
    }
       
    // <<<<<<<<<<<<<<<<<<<<LIST>>>>>>>>>>>>>>>>>>>>
    public List<Movie> getAllMovies() {
        List<Movie> movie_list = new ArrayList<Movie>();
        
        String selectQuery = "SELECT * FROM movie ORDER BY title";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
            	Bitmap bitmap = BitmapFactory.decodeByteArray(cursor.getBlob(4) , 0, cursor.getBlob(4).length);
            	
            	Movie movie = new Movie(
                    	Long.parseLong(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        bitmap);
            	movie_list.add(movie);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return movie_list;
    }
    
    public List<Movie> getAllMoviesByUser(long userId){
    	List<Movie> movie_list = new ArrayList<Movie>();

        SQLiteDatabase db = this.getReadableDatabase();
    	
    	Cursor cursor = db.rawQuery("SELECT * FROM Movie_User WHERE userId = ?" ,new String[] { String.valueOf(userId) });
    	
    	if (cursor.moveToFirst()) {
            do {
            	movie_list.add(getMovie(Long.parseLong(String.valueOf(cursor.getInt(1)))));
            } while (cursor.moveToNext());
        }
    	
    	cursor.close();
        db.close();
        return movie_list;
    }
    
    public List<Movie_User> getMovie_User() {
        List<Movie_User> movie_user_list = new ArrayList<Movie_User>();
        
        String selectQuery = "SELECT * FROM movie_user";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
            	Movie_User movie_user = new Movie_User(
                		Long.parseLong(cursor.getString(0)),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        Float.parseFloat(cursor.getString(3)),
                        cursor.getInt(4));
            	movie_user_list.add(movie_user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return movie_user_list;
    }
    
    
    public List<Movie_Director> getMovie_Director() {
        List<Movie_Director> movie_director_list = new ArrayList<Movie_Director>();
        
        String selectQuery = "SELECT * FROM movie_director";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
            	Movie_Director movie_director = new Movie_Director(
                		Long.parseLong(cursor.getString(0)),
                        cursor.getInt(1),
                        cursor.getInt(2));
            	movie_director_list.add(movie_director);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return movie_director_list;
    }
    
    
    public List<Movie_Actor> getMovie_Actor() {
        List<Movie_Actor> movie_actor_list = new ArrayList<Movie_Actor>();
        
        String selectQuery = "SELECT * FROM movie_actor";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
            	Movie_Actor movie_actor = new Movie_Actor(
                		Long.parseLong(cursor.getString(0)),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getString(3));
            	movie_actor_list.add(movie_actor);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return movie_actor_list;
    }
    
    
    
    public List<User> getAllUsers() {
        List<User> user_list = new ArrayList<User>();
        
        String selectQuery = "SELECT * FROM user ORDER BY username";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
            	User user = new User(
                		Long.parseLong(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3));
            	user_list.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return user_list;
    }
    
    
    //public List<Director> getAllDirectors() {
    //    List<Director> director_list = new ArrayList<Director>();
        
    //    String selectQuery = "SELECT * FROM director ORDER BY name";

    //    SQLiteDatabase db = this.getReadableDatabase();
    //    Cursor cursor = db.rawQuery(selectQuery, null);

    //    if (cursor.moveToFirst()) {
    //        do {
    //        	Director director = new Director(
    //            		Long.parseLong(cursor.getString(0)),
    //                    cursor.getString(1),
    //                    cursor.getString(2),
    //                    cursor.getString(3));
    //        	director_list.add(director);
    //        } while (cursor.moveToNext());
    //    }

    //    cursor.close();
    //    db.close();
    //    return director_list;
    //}
    
    //public List<Actor> getAllActors() {
    //    List<Actor> actor_list = new ArrayList<Actor>();
        
    //    String selectQuery = "SELECT * FROM actor ORDER BY name";

    //    SQLiteDatabase db = this.getReadableDatabase();
    //    Cursor cursor = db.rawQuery(selectQuery, null);

    //    if (cursor.moveToFirst()) {
    //        do {
    //        	Actor actor = new Actor(
    //            		Long.parseLong(cursor.getString(0)),
    //                    cursor.getString(1),
    //                    cursor.getString(2),
    //                    cursor.getString(3));
    //        	actor_list.add(actor);
    //        } while (cursor.moveToNext());
    //    }

    //    cursor.close();
    //    db.close();
    //    return actor_list;
    //}
    
    public List<Person> getAllPersons() {
        List<Person> person_list = new ArrayList<Person>();
        
        String selectQuery = "SELECT * FROM person ORDER BY name";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
            	Bitmap bitmap = BitmapFactory.decodeByteArray(cursor.getBlob(4) , 0, cursor.getBlob(4).length);
            	
            	Person person = new Person(
                		Long.parseLong(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        bitmap);
            	person_list.add(person);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return person_list;
    }
    
//----------------------------------------------------------------------
    // rawQuery-methode
    public List<String> getMovie_Title() {
        List<String> movie_title_list = new ArrayList<String>();
        
        String selectQuery = "SELECT * FROM movie ORDER BY name";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
            	movie_title_list.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return movie_title_list;
    }
    
    public List<String> getUser_Username() {
        List<String> user_username_list = new ArrayList<String>();
        
        String selectQuery = "SELECT * FROM user ORDER BY username";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
            	user_username_list.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return user_username_list;
    }
    
    //public List<String> getDirector_Name() {
    //    List<String> director_name_list = new ArrayList<String>();
        
    //    String selectQuery = "SELECT * FROM director ORDER BY name";

    //    SQLiteDatabase db = this.getReadableDatabase();
    //    Cursor cursor = db.rawQuery(selectQuery, null);

    //    if (cursor.moveToFirst()) {
    //        do {
    //        	director_name_list.add(cursor.getString(1));
    //        } while (cursor.moveToNext());
    //    }

    //    cursor.close();
    //    db.close();
    //    return director_name_list;
    //}
    
    //public List<String> getActor_Name() {
    //    List<String> actor_name_list = new ArrayList<String>();
        
    //    String selectQuery = "SELECT * FROM actor ORDER BY name";

    //    SQLiteDatabase db = this.getReadableDatabase();
    //    Cursor cursor = db.rawQuery(selectQuery, null);

    //    if (cursor.moveToFirst()) {
    //        do {
    //        	actor_name_list.add(cursor.getString(1));
    //        } while (cursor.moveToNext());
    //    }

    //    cursor.close();
    //    db.close();
    //    return actor_name_list;
    //}
    
    public List<String> getPerson_Name() {
        List<String> person_name_list = new ArrayList<String>();
        
        String selectQuery = "SELECT * FROM person ORDER BY name";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
            	person_name_list.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return person_name_list;
    }
    
    // rawQuery-methode
    public int getMovie_Count() {
        String selectQuery = "SELECT * FROM movie";
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int amount = cursor.getCount();
        
        cursor.close();
        db.close();
        return amount;
    }
    
    public int getUser_Count() {
        String selectQuery = "SELECT * FROM user";
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int amount = cursor.getCount();
        
        cursor.close();
        db.close();
        return amount;
    }
    
    //public int getDirector_Count() {
    //    String selectQuery = "SELECT * FROM director";
        
    //    SQLiteDatabase db = this.getReadableDatabase();
    //    Cursor cursor = db.rawQuery(selectQuery, null);
    //    int amount = cursor.getCount();
        
    //    cursor.close();
    //    db.close();
    //    return amount;
    //}
    
    //public int getActor_Count() {
    //    String selectQuery = "SELECT * FROM actor";
        
    //    SQLiteDatabase db = this.getReadableDatabase();
    //    Cursor cursor = db.rawQuery(selectQuery, null);
    //    int amount = cursor.getCount();
        
    //    cursor.close();
    //    db.close();
    //    return amount;
    //}

    public int getPerson_Count() {
        String selectQuery = "SELECT * FROM person";
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int amount = cursor.getCount();
        
        cursor.close();
        db.close();
        return amount;
    }
}
