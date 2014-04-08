/* Application: animovie
 * Author: Wouter Eskens
 * API: The Movie Database (TMDb)
 * Database: SQLite 
 * Class: Applied Computer Sciences
 * School: Thomas More Kempen
 * Year: 2013-2014 */

package com.example.animovie.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.example.animovie.R;
import com.example.animovie.global.Global_Menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class Quiz extends Global_Menu {
	
	private ArrayList<String> movie_titles_private = new ArrayList<String>();
	private String previous_image_name = null;
	private int amount_right = 0;
	private int index = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quiz);
		
		//Instantiate all views
		ImageView image_view = (ImageView) findViewById(R.id.quiz_picture);
		RadioButton radio_button_answer_0 = (RadioButton) findViewById(R.id.quiz_answer_0);
		RadioButton radio_button_answer_1 = (RadioButton) findViewById(R.id.quiz_answer_1);
		RadioButton radio_button_answer_2 = (RadioButton) findViewById(R.id.quiz_answer_2);
		TextView text_view_amount_right = (TextView) findViewById(R.id.quiz_amount_right);
		
		//Set the amount right to zero
		text_view_amount_right.setText("Amount right: " + amount_right + "/" + index);
		
		//Make new ArrayList
		ArrayList<String> movie_titles = new ArrayList<String>();
		
		//Fill up ArrayList
		movie_titles.add("9");
		movie_titles.add("A Scanner Darkly");
		movie_titles.add("Aladdin");
		movie_titles.add("Alvin And The Chipmunks");
		movie_titles.add("Avatar");
		movie_titles.add("Bambi");
		movie_titles.add("Beauty And The Beast");
		movie_titles.add("Beowulf");
		movie_titles.add("Bolt");
		movie_titles.add("Brave");
		movie_titles.add("Cars");
		movie_titles.add("Cloudy With A Chance Of Meatballs");
		movie_titles.add("Corpse Bride");
		movie_titles.add("Despicable Me");
		movie_titles.add("Finding Nemo");
		movie_titles.add("Frozen");
		movie_titles.add("Happy Feet");
		movie_titles.add("How To Train Your Dragon");
		movie_titles.add("Ice Age Continental Drift");
		movie_titles.add("Kung Fu Panda");
		movie_titles.add("Madagascar 3");
		movie_titles.add("Megamind");
		movie_titles.add("Monsters University");
		movie_titles.add("Open Season");
		movie_titles.add("Paranorman");
		movie_titles.add("Puss In Boots");
		movie_titles.add("Rango");
		movie_titles.add("Rapunzel");
		movie_titles.add("Ratatouille");
		movie_titles.add("Rio");
		movie_titles.add("Rise Of The Guardians");
		movie_titles.add("Snowwite And The Seven Dwarfs");
		movie_titles.add("Space Jam");
		movie_titles.add("The Adventures Of Tintin");
		movie_titles.add("The Croods");
		movie_titles.add("The Incredibles");
		movie_titles.add("The Little Mermaid");
		movie_titles.add("The Lorax");
		movie_titles.add("The Nightmare Before Christmas");
		movie_titles.add("The Prince Of Egypt");
		movie_titles.add("The Princess And The Frog");
		movie_titles.add("The Simpsons Movie");
		movie_titles.add("The Smurfs");
		movie_titles.add("TMNT");
		movie_titles.add("Toy Story");
		movie_titles.add("Turbo");
		movie_titles.add("Up");
		movie_titles.add("Walking With Dinosaurs");
		movie_titles.add("WALL-E");
		movie_titles.add("Wreck-It Ralph");
		
		//Generate a random image
		Random random = new Random();
		int random_image_int = random.nextInt(movie_titles.size()-1);
		String imgName = "quiz_" + random_image_int;
		int id = getResources().getIdentifier(imgName, "drawable", getPackageName());  
		image_view.setImageResource(id);
		
		//Set the image name for check
		String image_name = movie_titles.get(random_image_int);
		previous_image_name = image_name;
		
		//Generate another answer (2/3)
		int random_title_int_1 = random.nextInt(movie_titles.size()-1);
		
		while(random_title_int_1 == random_image_int){
			random_title_int_1 = random.nextInt(movie_titles.size()-1);
		}
		
		//Generate the third answer (3/3)
		int random_title_int_2 = random.nextInt(movie_titles.size()-1);
		
		while(random_title_int_2 == random_image_int || random_title_int_2 == random_title_int_1){
			random_title_int_2 = random.nextInt(movie_titles.size()-1);
		}
		
		//Make a new ArrayList for shuffle
		ArrayList<String> answers = new ArrayList<String>();
		
		//Fill up the ArrayList
		answers.add(image_name);
		answers.add(movie_titles.get(random_title_int_1));
		answers.add(movie_titles.get(random_title_int_2));
		
		//Shuffle the ArrayList
		Collections.shuffle(answers);
		
		//Set the text of the answers
		radio_button_answer_0.setText(answers.get(0));
		radio_button_answer_1.setText(answers.get(1));
		radio_button_answer_2.setText(answers.get(2));
		
		//Set the private ArrayList
		movie_titles_private = movie_titles;
	}
	
	public void onClickNext(View v){
		//Add the index of the question
		index++;
		
		//Instantiate all views
		ImageView image_view = (ImageView) findViewById(R.id.quiz_picture);
		RadioButton radio_button_answer_0 = (RadioButton) findViewById(R.id.quiz_answer_0);
		RadioButton radio_button_answer_1 = (RadioButton) findViewById(R.id.quiz_answer_1);
		RadioButton radio_button_answer_2 = (RadioButton) findViewById(R.id.quiz_answer_2);
		TextView text_view_amount_right = (TextView) findViewById(R.id.quiz_amount_right);
		
		//Add a boolean correct
		boolean correct = false;
		
		//If the first radiobutton was checked and the answer was correct
		if(radio_button_answer_0.isChecked() && radio_button_answer_0.getText().equals(previous_image_name)){
			correct = true;
		}
		
		//If the second radiobutton was checked and the answer was correct
		if(radio_button_answer_1.isChecked() && radio_button_answer_1.getText().equals(previous_image_name)){
			correct = true;
		}
		
		//If the third radiobutton was checked and the answer was correct
		if(radio_button_answer_2.isChecked() && radio_button_answer_2.getText().equals(previous_image_name)){
			correct = true;
		}
		
		//If the answer was correct add 1 to the amount right
		if(correct == true){
			amount_right +=1;
		}
		
		//Set the amount_right text
		text_view_amount_right.setText("Amount right: " + amount_right + "/" + index);
		
		//Generate a random image
		Random random = new Random();
		int random_image_int = random.nextInt(movie_titles_private.size()-1);
		String imgName = "quiz_" + random_image_int;
		int id = getResources().getIdentifier(imgName, "drawable", getPackageName());  
		image_view.setImageResource(id);
		
		//Set the image name for check
		String image_name = movie_titles_private.get(random_image_int);
		previous_image_name = image_name;
		
		//Generate another answer (2/3)
		int random_title_int_1 = random.nextInt(movie_titles_private.size()-1);
		
		while(random_title_int_1 == random_image_int){
			random_title_int_1 = random.nextInt(movie_titles_private.size()-1);
		}

		//Generate the third answer (3/3)
		int random_title_int_2 = random.nextInt(movie_titles_private.size()-1);
		
		while(random_title_int_2 == random_image_int || random_title_int_2 == random_title_int_1){
			random_title_int_2 = random.nextInt(movie_titles_private.size()-1);
		}
		
		//Make a new ArrayList for shuffle
		ArrayList<String> answers = new ArrayList<String>();
				
		//Fill up the ArrayList
		answers.add(image_name);
		answers.add(movie_titles_private.get(random_title_int_1));
		answers.add(movie_titles_private.get(random_title_int_2));
				
		//Shuffle the ArrayList
		Collections.shuffle(answers);
				
		//Set the text of the answers
		radio_button_answer_0.setText(answers.get(0));
		radio_button_answer_1.setText(answers.get(1));
		radio_button_answer_2.setText(answers.get(2));
	}
	
	public void onClickExit(View v){
		//Go to my_profile Activity
		Intent intent_my_profile = new Intent(getApplicationContext(), My_Profile.class);
    	startActivity(intent_my_profile);
	}
}
