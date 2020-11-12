package com.example.sibenice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    TextView txtGuess;
    String wordGuess, wordDisplay;
    EditText edtInput;
    TextView txtLettersTried;
    String lettersTried;
    char[] wordDisplayCharArray;
    ArrayList<String> listOfWords;

    void revealLetter(char aaa){

        int indexL =  wordGuess.indexOf(aaa);
        //for

        while(indexL >= 0) {
            wordDisplayCharArray[indexL] = wordGuess.charAt(indexL);
            indexL = wordGuess.indexOf(aaa, indexL +1);
        }
        wordDisplay = String.valueOf(wordDisplayCharArray);
    }
    final String messageWithLettersTried = "Letters tried: ";
    void displayScreen(){
        String fstring;
    fstring= " ";
    for(char c : wordDisplayCharArray){
        fstring += c;
        txtGuess.setText(fstring);
    }

    }
    TextView txtTriesLeft;
    String triesLeft;

    void initializeGame(){

        Collections.shuffle(listOfWords);
        wordGuess = listOfWords.get(0);
        listOfWords.remove(0);

        wordDisplayCharArray = wordGuess.toCharArray();
        for(int i = 0; i < wordDisplayCharArray.length-1; i++)
        {
            wordDisplayCharArray[i] = '_';
        }
        revealLetter(wordDisplayCharArray[0]);
        revealLetter(wordDisplayCharArray[wordDisplayCharArray.length-1]);
        wordDisplay = String.valueOf(wordDisplayCharArray);

        displayScreen();
        edtInput.setText("");
        lettersTried = " ";
        txtLettersTried.setText(messageWithLettersTried);

        triesLeft = " X X X X X ";
        txtTriesLeft.setText(triesLeft);
    }

    Animation rotateAnimation;
    Animation scaleAnimation;
    Animation scaleAndRotateAnimation;

    final String winningMessage = "You are winner";
    final String losingMessage = "you have lost";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtInput = findViewById(R.id.edtInput);
        txtLettersTried = findViewById(R.id.txtLettersTried);
        txtTriesLeft = findViewById(R.id.txtTriesLeft);
        listOfWords = new ArrayList<>();
        txtGuess = findViewById(R.id.txtGuess);


        InputStream myInputStream = null;
        Scanner scanner = null;

        String b = "";
        try {
            myInputStream = getAssets().open("listOfWords");
            scanner = new Scanner(myInputStream);
            while(scanner.hasNext()){
                b = scanner.next();
                listOfWords.add(b);
                      }
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, e.getClass().getSimpleName() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
            if(scanner != null) {
                scanner.close();
            }
            try {
                if(myInputStream != null){
                myInputStream.close();}
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        initializeGame();
        edtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0){
                    isLetterInWord(s.charAt(0));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    void resetGame(View v){
        initializeGame();
    }
    void isLetterInWord(char c){
        if(wordGuess.indexOf(c)>= 0){
            if(wordDisplay.indexOf(c) < 0){
                revealLetter(c);

                displayScreen();
                if(wordDisplay.contains("_")){
                    txtTriesLeft.setText(winningMessage);
                }

            }

        }
        else{
            decreaseAndDisplayTL();
            if(triesLeft.isEmpty()){
                txtTriesLeft.setText(losingMessage);
                txtGuess.setText(wordGuess);
            }
        }
        if(lettersTried.indexOf(c)<0){
            lettersTried += c + ", ";
            String messageD = messageWithLettersTried + lettersTried;
            txtLettersTried.setText(messageD);
        }
    }
    void decreaseAndDisplayTL(){
        if(!triesLeft.isEmpty()){
            triesLeft = triesLeft.substring(0, triesLeft.length()- 2);
            txtTriesLeft.setText(triesLeft);
        }
    }

}