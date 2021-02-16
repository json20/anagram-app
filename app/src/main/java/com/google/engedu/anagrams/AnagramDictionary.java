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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 4;
    private static final int MAX_WORD_LENGTH = 7;
    private static int wordLength = DEFAULT_WORD_LENGTH;
    private Random random = new Random();
    ArrayList<String> wordList;
    HashSet<String> wordSet;
    HashMap<String, ArrayList<String>> lettersToWord;
    HashMap<Integer, ArrayList<String>> sizeToWords;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        wordList = new ArrayList<>();
        wordSet = new HashSet<>();
        lettersToWord = new HashMap<>();
        sizeToWords = new HashMap<>();
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordSet.add(word);
            wordList.add(word);
            String sortedWord = sortLetters(word);
            if (sizeToWords.containsKey(word.length())) {
                sizeToWords.get(word.length()).add(word);
            }
            else {
                sizeToWords.put(word.length(), new ArrayList<String>());
                sizeToWords.get(word.length()).add(word);
            }

            if (lettersToWord.containsKey(sortedWord)) {
                lettersToWord.get(sortedWord).add(word);
            }
            else {
                lettersToWord.put(sortedWord, new ArrayList<String>());
                lettersToWord.get(sortedWord).add(word);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && !word.contains(base);
    }

    public List<String> getAnagrams(String targetWord) {
        String sortedWord = sortLetters(targetWord);
        ArrayList<String> result = new ArrayList<String>();
        if (lettersToWord.containsKey(sortedWord)) {
            ArrayList<String> hashedList = lettersToWord.get(sortedWord);
            result = hashedList;
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 97; i < 123; i += 1) {
            char ch = (char) i;
            String sortedWord = sortLetters(word + ch);
            if (lettersToWord.containsKey(sortedWord)) {
                ArrayList<String> hashedList = lettersToWord.get(sortedWord);
                for (String x : hashedList) {
                    result.add(x);
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        ArrayList<String> wordList2 = sizeToWords.get(wordLength);
        int startIndex = random.nextInt(wordList2.size());
        boolean loop = false;
        for (int i = startIndex; i < wordList2.size(); i += 1) {
            String word = wordList2.get(i);
            String sortedWord = sortLetters(word);
            ArrayList<String> hashedList = lettersToWord.get(sortedWord);
            if (hashedList.size() >= MIN_NUM_ANAGRAMS) {
                if (wordLength < MAX_WORD_LENGTH) {
                    wordLength += 1;
                }
                return word;
            }
            if (loop && (i == wordList2.size() - 1)) {
                break;
            }
            if (i == wordList2.size() - 1) {
                i = 0;
                loop = true;
            }
        }
        if (wordLength < MAX_WORD_LENGTH) {
            wordLength += 1;
        }
        return "pear";
    }

    private String sortLetters(String s) {
        char[] charArray = s.toCharArray();
        Arrays.sort(charArray);
        return new String(charArray);
    }


}
