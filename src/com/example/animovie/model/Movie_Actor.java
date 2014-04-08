/* Application: animovie
 * Author: Wouter Eskens
 * API: The Movie Database (TMDb)
 * Database: SQLite 
 * Class: Applied Computer Sciences
 * School: Thomas More Kempen
 * Year: 2013-2014 */

package com.example.animovie.model;

public class Movie_Actor {
	
	private final static int FLAG_PERSON_RESOLVED = 0x01 << 0;
	private final static int FLAG_MOVIE_RESOLVED  = 0x01 << 1;
	
	public interface Listener {
		void onPersonResolved(Movie_Actor role);
	}
	
	private int _flags = 0;
	private void setFlag(int flag, boolean on) { if (on) _flags |= flag; else _flags &= ~flag; }
	private boolean checkFlag(int flag) { return (_flags & flag) > 0; }
	private long id;
    private long movieId;
    private long actorId;
    private String role;
    
    // runtime reference
    private Person _person = null;
    // runtime reference
    private Movie _movie = null;
            
    public Movie_Actor() {
        
    }

    public Movie_Actor (long id, int movieId, int actorId, String role) {
        this.id = id;
        this.movieId = movieId;
        this.actorId = actorId;
        this.role = role;
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

	public long getMovieId() {
		return movieId;
	}

	public void setMovieId(long l) {
		this.movieId = l;
	}

	public long getActorId() {
		return actorId;
	}

	public void setActorId(long actorId) {
		this.actorId = actorId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public void setPerson(Person person) {
		setFlag(FLAG_PERSON_RESOLVED, person != null);
		_person = person;
	}
	public Person getPerson() { return _person; }
	public boolean personResolved() { return checkFlag(FLAG_PERSON_RESOLVED); }
	
	public Movie getMovie() { return _movie; }
	public void setMovie(Movie movie) { _movie = movie; }
}

