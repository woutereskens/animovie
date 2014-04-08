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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.animovie.R;
import com.example.animovie.db.TMDBConnector;
import com.example.animovie.global.Global_Menu;
import com.example.animovie.global.Global_Variables;
import com.example.animovie.model.Person;

public class Person_Search extends Global_Menu implements TMDBConnector.PersonSearchResultListener, Person.Listener {

	private LinkedList<Person> personsUpdated;
	ListView list_view_persons;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_search);
		
		EditText searchBox = (EditText) findViewById(R.id.person_search_search_box);
		searchBox.requestFocus();
		
		list_view_persons = (ListView) findViewById(R.id.person_search_list_people);
		
		list_view_persons.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
                
                Person person = (Person) list_view_persons.getAdapter().getItem(position);
                
                Global_Variables.person_detail = person;
                
                Intent intent = new Intent(getApplicationContext(), Person_Detail.class);
                startActivity(intent);
            }
        });
    }
	
	public void onClickPersonSearch(View v){
    	EditText searchBox = (EditText) findViewById(R.id.person_search_search_box);
        
        String searchBoxText = searchBox.getText().toString();
        
        searchBoxText = searchBoxText.replace(" ", "+");
        
        TMDBConnector.getInstance().searchPerson(searchBoxText, this); 
    }

	@Override
	public void onPersonSearchComplete(LinkedList<Person> persons) {
    	
		ListView person_list = (ListView) findViewById(R.id.person_search_list_people);
		
    	ArrayList<Person> values = new ArrayList<Person>();

        for (int i = 0; i <= persons.size()-1; i++) {
            Person person = persons.get(i);
            person.AddListener(this);
        	values.add(person);
        }
    	
        person_list.setAdapter(new PersonAdapter(this, values));
        
        person_list = (ListView) findViewById(R.id.person_search_list_people);
        
        personsUpdated = persons;
	}

	@Override
	public void onPictureUpdate(Person person) {
		ListView person_list = (ListView) findViewById(R.id.person_search_list_people);
		int index = personsUpdated.indexOf(person);
		int first = person_list.getFirstVisiblePosition();
		int last  = person_list.getLastVisiblePosition();
		if ((index < first) || (last < index)) {
			// item to update is not even visible
			return;
		}
		View view = person_list.getChildAt(index - person_list.getFirstVisiblePosition());
		person_list.getAdapter().getView(index, view, person_list);
	}
}
