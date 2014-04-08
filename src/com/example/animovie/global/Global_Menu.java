/* Application: animovie
 * Author: Wouter Eskens
 * API: The Movie Database (TMDb)
 * Database: SQLite 
 * Class: Applied Computer Sciences
 * School: Thomas More Kempen
 * Year: 2013-2014 */

package com.example.animovie.global;

import com.example.animovie.DatabaseHelper;
import com.example.animovie.R;
import com.example.animovie.activity.About;
import com.example.animovie.activity.MainActivity;
import com.example.animovie.activity.Movie_Search;
import com.example.animovie.activity.My_Profile;
import com.example.animovie.activity.Person_Search;
import com.example.animovie.activity.Register;
import com.example.animovie.model.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Global_Menu extends Activity {
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	    case R.id.action_home:
	    	if(this.getClass() != MainActivity.class){
	    		Intent intent_home = new Intent(getApplicationContext(), MainActivity.class);
	        	startActivity(intent_home);
	    	}
            return true;
	    case R.id.action_search_movies:
	    	Intent intent_movie_search = new Intent(getApplicationContext(), Movie_Search.class);
        	startActivity(intent_movie_search);
        	return true;
	    case R.id.action_search_people:
	    	Intent intent_people_search = new Intent(getApplicationContext(), Person_Search.class);
        	startActivity(intent_people_search);
        	return true;
	    case R.id.action_my_profile:
	        if(Global_Variables.loggedInUser == null){
	        	openLogin();
	        } else {
	            Intent i = new Intent(getApplicationContext(), My_Profile.class);
                startActivity(i);
	        }
	        return true;
	    case R.id.action_about:
	    	Intent intent_about = new Intent(getApplicationContext(), About.class);
            startActivity(intent_about);
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	private void openLogin() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	LayoutInflater inflater = this.getLayoutInflater();
    	
        final View viewInflater = inflater.inflate(R.layout.dialog_signin, null);
		builder.setTitle(R.string.dialog_signin_title)
            .setIcon(R.drawable.ic_action_person)
        	.setView(viewInflater)
            .setPositiveButton(R.string.dialog_signin_login, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	EditText editUsername = (EditText) viewInflater.findViewById(R.id.dialog_signin_username);
                	EditText editPassword = (EditText) viewInflater.findViewById(R.id.dialog_signin_password);
                	
                	try{
                		User user = DatabaseHelper.getInstance().getUserByUsername(editUsername.getText().toString());
                		
                		if(user.getPassword().equals(editPassword.getText().toString())){
                			Toast toast = Toast.makeText(getApplicationContext(), "Welcome, " + editUsername.getText().toString(), Toast.LENGTH_LONG);
                			toast.show();
                			
                			Global_Variables.loggedInUser = user;
                		} else {
                			Toast toast = Toast.makeText(getApplicationContext(), "Wrong username or password", Toast.LENGTH_LONG);
                    		toast.show();
                    		
                    		Global_Variables.loggedInUser = null;
                		}
                	}
                	catch(Exception e){
                		Toast toast = Toast.makeText(getApplicationContext(), "Wrong username or password", Toast.LENGTH_LONG);
                		toast.show();
                		
                		Global_Variables.loggedInUser = null;
                	}
                }
            })
            .setNegativeButton(R.string.dialog_signin_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            })
            .setNeutralButton(R.string.dialog_signin_register, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					Intent i = new Intent(getApplicationContext(), Register.class);
                	startActivity(i);
				}
			});
		
        AlertDialog dialog = builder.create();
        dialog.show();  
    }
	
	public void onClickViewRegister(View v){
		Intent i = new Intent(getApplicationContext(), Register.class);
    	startActivity(i);
	}
}
