/* Application: animovie
 * Author: Wouter Eskens
 * API: The Movie Database (TMDb)
 * Database: SQLite 
 * Class: Applied Computer Sciences
 * School: Thomas More Kempen
 * Year: 2013-2014 */

package com.example.animovie.model;

public class Movie_Director {
	private long id;
    private long movieId;
    private long directorId;
    
    // resolved runtime reference
    private Person _person;
    private Movie _movie;
            
    public Movie_Director() {
        
    }

    public Movie_Director (long id, int movieId, int directorId) {
        this.id = id;
        this.movieId = movieId;
        this.directorId = directorId;
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

	public void setMovieId(long movieId) {
		this.movieId = movieId;
	}

	public long getDirectorId() {
		return directorId;
	}

	public void setDirectorId(long id2) {
		this.directorId = id2;
	}

	public Person getPerson() { return _person; }
	public void setPerson(Person person) { _person = person; }
	
	public Movie getMovie() { return _movie; }
	public void setMovie(Movie movie) { _movie = movie; }
}

