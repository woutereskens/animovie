/* Application: animovie
 * Author: Wouter Eskens
 * API: The Movie Database (TMDb)
 * Database: SQLite 
 * Class: Applied Computer Sciences
 * School: Thomas More Kempen
 * Year: 2013-2014 */

package com.example.animovie.activity;

import java.util.List;

import com.example.animovie.DatabaseHelper;
import com.example.animovie.R;
import com.example.animovie.global.Global_Menu;
import com.example.animovie.model.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Global_Menu {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
	}
	
	public void onClickRegister(View v){
		EditText editUsername = (EditText) findViewById(R.id.register_username);
		EditText editEmail = (EditText) findViewById(R.id.register_email);
    	EditText editPassword = (EditText) findViewById(R.id.register_password);
    	
    	String username = editUsername.getText().toString();
    	String email = editEmail.getText().toString();
    	String password = editPassword.getText().toString();
    	
    	if(!username.trim().equals("")){
    		if(!email.trim().equals("")){
    			if(!password.trim().equals("")){
    				List<String> user_usernames = DatabaseHelper.getInstance().getUser_Username();
    				
    				boolean usernameExists = false;
    				
    				for(int i=0; i<= user_usernames.size()-1;i++){
    					if(username.equals(user_usernames.get(i))){
    						usernameExists = true;
    					}
    				}
    				
    				if(usernameExists == true){
    					Toast toast = Toast.makeText(getApplicationContext(), "This username already exists", Toast.LENGTH_LONG);
        				toast.show();
    				} else {
    					User user = new User();
    					user.setUsername(username);
    					user.setEmail(email);
    					user.setPassword(password);
    					
    					try{
    						DatabaseHelper.getInstance().insertUser(user);
    						
    						Intent i = new Intent(getApplicationContext(), MainActivity.class);
    				    	startActivity(i);
    						
    						Toast toast = Toast.makeText(getApplicationContext(), "Succesfully registered", Toast.LENGTH_LONG);
            				toast.show();
    					} catch (Exception e){
    						Toast toast = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG);
            				toast.show();
    					}
    				}
    			} else {
    				Toast toast = Toast.makeText(getApplicationContext(), "Fill in a password", Toast.LENGTH_LONG);
    				toast.show();
    			}
    		} else {
    			Toast toast = Toast.makeText(getApplicationContext(), "Fill in a email", Toast.LENGTH_LONG);
    			toast.show();
    		}
    	} else {
    		Toast toast = Toast.makeText(getApplicationContext(), "Fill in a username", Toast.LENGTH_LONG);
			toast.show();
    	}
	}
}
