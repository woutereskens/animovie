/* Application: animovie
 * Author: Wouter Eskens
 * API: The Movie Database (TMDb)
 * Database: SQLite 
 * Class: Applied Computer Sciences
 * School: Thomas More Kempen
 * Year: 2013-2014 */

package com.example.animovie.model;

import java.util.LinkedList;

import com.example.animovie.model.Movie.Listener;

import android.graphics.Bitmap;

public class Person 
{
	public interface Listener {
		void onPictureUpdate(Person person);
//		void onDetailsUpdate(Movie m);
	}
	
	private LinkedList< Listener > _listeners = new LinkedList< Listener >();
	public void AddListener(Listener l) { _listeners.add(l); }
	public void RemoveListener(Listener l) { _listeners.remove(l); }
	
	
	private final static int FLAG_PICTURE_AVAILABLE = 0x01 << 3;
	private final static int FLAG_PICTURE_LOADING   = 0x01 << 4;
	private final static int FLAG_PICTURE_LOADED    = 0x01 << 5;
	private final static int FLAG_DEFAULT 			= 0;
	
	private int _flags = FLAG_DEFAULT;
	private void setFlag(int flag, boolean on) {
		if (on) _flags |= flag;
		else _flags &= ~flag;
	}
	private boolean checkFlag(int flag) { return (_flags & flag) > 0; }
	
	private long _id;
	private String _name;
	private String _birth_date;
	private String _info;
	private Bitmap _picture;
	
	public Person() {
        
    }

    public Person (long id, String name, String birth_date, String info, Bitmap picture) {
        this._id = id;
        this._name = name;
        this._birth_date = birth_date;
        this._info = info;
        this._picture = picture;
    }
	
	public long getId() { return _id; }
	public void setId(long id) { _id = id; }
	
	public String getName() { return _name; }
	public void setName(String name) { _name = name; }
	
	public String getBirth_Date() { return _birth_date; }
	public void setBirth_Date(String birth_date) { this._birth_date = birth_date; }
	
	public String getInfo() { return _info; }
	public void setInfo(String info) { this._info = info; }
	
	public Bitmap getPicture() { return _picture; }
	public void setPicture(Bitmap picture) {
		setFlag(FLAG_PICTURE_LOADED, picture != null);
		_picture = picture;
		for(Listener l : _listeners) l.onPictureUpdate(this);
	}
	public boolean pictureAvailable() { return checkFlag(FLAG_PICTURE_AVAILABLE); }
	public void setPictureAvailable(boolean available) { setFlag(FLAG_PICTURE_AVAILABLE, available); }
	public boolean pictureLoading()   { return checkFlag(FLAG_PICTURE_LOADING); }
	public void setPictureLoading(boolean loading) { setFlag(FLAG_PICTURE_LOADING, loading); }
	public boolean pictureLoaded()    { return checkFlag(FLAG_PICTURE_LOADED); }
}
