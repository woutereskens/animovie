/* Application: animovie
 * Author: Wouter Eskens
 * API: The Movie Database (TMDb)
 * Database: SQLite 
 * Class: Applied Computer Sciences
 * School: Thomas More Kempen
 * Year: 2013-2014 */

package com.example.animovie.model;

import java.util.LinkedList;

import android.graphics.Bitmap;

public class Movie {
	
	public interface Listener {
		void onPosterUpdate(Movie m);
//		void onDetailsUpdate(Movie m);
	}
	
	private LinkedList< Listener > _listeners = new LinkedList< Listener >();
	public void AddListener(Listener l) { _listeners.add(l); }
	public void RemoveListener(Listener l) { _listeners.remove(l); }
	
	private long id = -1;
    private String title = null;
    private String info = null;
    private int year = -1;
    private Bitmap _poster = null;
    
    private static final int STATUS_NONE			  = 0x00;
    private static final int STATUS_LOADED			  = 0x01 << 0;

    private static final int STATUS_INFO_AVAILABLE    = 0x01 << 1;
    private static final int STATUS_INFO_LOADING      = 0x01 << 2;
    private static final int STATUS_INFO_LOADED       = 0x01 << 3;
    
    
    private static final int STATUS_RELEASE_AVAILABLE = 0x01 << 4;
    private static final int STATUS_POSTER_AVAILABLE  = 0x01 << 7;
    private static final int STATUS_POSTER_LOADING    = 0x01 << 8;
    private static final int STATUS_POSTER_LOADED	  = 0x01 << 9;
    private static final int STATUS_DEFAULT           = STATUS_NONE;
    
    
    private int _status = STATUS_NONE;
    private void setFlag(int flag, boolean on) {
    	if (on) {
    		_status |= flag;
    	} else {
    		_status &= ~flag;
    	}
    }
    private boolean checkFlag(int flag) { return (_status & flag) > 0; }
    
    /// returns true if a poster is available for download for this movie
    public boolean posterAvailable()  { return checkFlag(STATUS_POSTER_AVAILABLE); }
    /// returns true if a poster is available and currently downloading
    public boolean posterLoading()    { return checkFlag(STATUS_POSTER_LOADING);   }
    /// return true if the poster is ready to draw
    public boolean posterLoaded()     { return checkFlag(STATUS_POSTER_LOADED);    }
    public boolean releaseAvailable() { return checkFlag(STATUS_RELEASE_AVAILABLE); }
    
    public void setPosterLoading(boolean loading) { setFlag(STATUS_POSTER_LOADING, loading); }
    public void setPosterAvailable(boolean available) { setFlag(STATUS_POSTER_AVAILABLE, available); }
    
    public boolean infoAvailable() { return checkFlag(STATUS_INFO_AVAILABLE); }
    public boolean infoLoading() { return checkFlag(STATUS_INFO_LOADING); }
    public boolean infoLoaded() { return checkFlag(STATUS_INFO_LOADED); }
    
    public Movie() {
        
    }

    public Movie (long id, String title, String info, int year, Bitmap poster) {
        this.id = id;
        this.title = title;
        this.info = info;
        this.year = year;
        this._poster = poster;
    }
    
	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
    	setFlag(STATUS_INFO_AVAILABLE, (info != null));
    	setFlag(STATUS_INFO_LOADED, (info != null) && (!info.equals("null")));
        this.info = info;
    }

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		setFlag(STATUS_RELEASE_AVAILABLE, (year > 0));
		this.year = year;
	}
    
	public String toString() {
		String movie = title + " (" + year + ")";
		return movie;
	}
	
	public Bitmap getPoster() {
		return _poster;
	}
	
	public void setPoster(Bitmap poster) {
		setFlag(STATUS_POSTER_LOADED, poster != null);
		_poster = poster;
		for(Listener l : _listeners) { l.onPosterUpdate(this); }
	}
}

