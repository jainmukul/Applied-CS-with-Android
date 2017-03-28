/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList();
    private HashSet<String> wordSet = new HashSet();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap();
    private HashMap<Integer, ArrayList<String>> counterToWord = new HashMap();

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            String wordSorted = sortLetters(word);
            int wordLength = word.length();
            if(lettersToWord.containsKey(wordSorted)){
                ArrayList<String> tempList = lettersToWord.get(wordSorted);
                tempList.add(word);
                lettersToWord.put(wordSorted, tempList);
            }else{
                ArrayList<String> tempList = new ArrayList();
                tempList.add(word);
                lettersToWord.put(wordSorted, tempList);
            }
            if(counterToWord.containsKey(wordLength)){
                ArrayList<String> tempList = counterToWord.get(wordLength);
                tempList.add(word);
                counterToWord.put(wordLength, tempList);
            }else{
                ArrayList<String> tempList = new ArrayList();
                tempList.add(word);
                counterToWord.put(wordLength, tempList);
            }
        }
    }

    public String sortLetters(String word){
        char temp[] = word.toCharArray();
        Arrays.sort(temp);
        String result = new String(temp);
        return result;
    }

    public boolean isGoodWord(String word, String base) {
        if(lettersToWord.containsKey(sortLetters(word)) && !word.contains(base))
            return true;
        else
            return false;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String targetWordSorted = sortLetters(targetWord);
        for(String word : wordList){
            int wordLength = word.length();
            int targetWordLength = targetWord.length();
            if(wordLength == targetWordLength){
                if(targetWordSorted.equals(sortLetters(word))){
                    result.add(word);
                }
            }
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(char i='a'; i<='z'; i++) {
            String temp = word + i;
            String tempSorted = sortLetters(temp);
            if (lettersToWord.containsKey(tempSorted )) {
                ArrayList<String> tempList = lettersToWord.get(tempSorted );
                for (int j = 0; j < tempList.size(); j++) {
                    result.add(tempList.get(j));
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        ArrayList<String> availableWords = counterToWord.get(DEFAULT_WORD_LENGTH);
        String word;
        while (true) {
            int randomVar = (int) (Math.random() * ((availableWords.size()) + 1));
            word = availableWords.get(randomVar);
            List<String> anagrams = getAnagramsWithOneMoreLetter(word);
            if(anagrams.size() > MIN_NUM_ANAGRAMS)
                break;
        }
        return word;
    }
}
