package DSWeek7;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WordSearch {

    public static void main(String[] args) {
        try {
            WordSearch wordSearch = new WordSearch();

            // Perform the word search
            int totalMatches = wordSearch.solvePuzzle();
            System.out.println("Total matches found: " + totalMatches);
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
    
    public WordSearch() throws IOException {
        puzzleStream = openFile("Enter puzzle file");
        wordStream = openFile("Enter dictionary name:");
        System.out.println("Reading files...");
        readPuzzle();
        readWords();

    
    }

    public int solvePuzzle() {
        int totalMatches = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                for(int rowDelta = -1; rowDelta <= 1; rowDelta++) {
                    for (int colDelta = -1; colDelta <= 1;colDelta++){
                        if (rowDelta!= 0 || colDelta != 0) {
                            totalMatches += solveDirection(row, col, rowDelta, colDelta);
                        }
                    }
                }
            }
        }
        return totalMatches;
    }

    private int rows;
    private int columns;
    private char theBoard[][];
    private String [] theWords;
    private BufferedReader puzzleStream;
    private BufferedReader wordStream;
    private BufferedReader in = new BufferedReader ( new InputStreamReader(System.in));


    private static int prefixSearch ( String [] a, String x) {
        return Arrays.binarySearch(a, x);
    }

    private BufferedReader openFile(String message) {
        String fileName = "";
        FileReader theFile;
        BufferedReader fileIn = null;

        do {
            System.out.println(message + ": ");

            try{
                fileName = in.readLine();
                if (fileName == null) 
                    System.exit(0);
                    theFile = new FileReader (fileName);
                    fileIn = new BufferedReader(theFile);
                

            }
            catch (IOException e) {
                System.err.println("Cannot open" + fileName);
            } 
        } while (fileIn ==null) ;

        System.out.println("Opened " + fileName);
        return fileIn;
    }

    private void readWords() throws IOException {
        List <String> words = new ArrayList<String>();

        String lastWord = null;
        String thisWord;

        while ((thisWord = wordStream.readLine()) != null) {
            if (lastWord != null && thisWord.compareTo(lastWord) < 0) {
                System.out.println("Dictionary is not sorted... skipping");
                continue;
            }
            words.add(thisWord);
            lastWord = thisWord;
        }

        theWords = new String [ (words.size())];
        theWords = words.toArray(theWords);
    }

    private void readPuzzle() throws IOException{
        String oneLine;
        List<String> puzzleLines = new ArrayList<String>();

        if ((oneLine = puzzleStream.readLine()) == null) throw new IOException("No lines in puzzle file"); 
            
        columns = oneLine.length();
        puzzleLines.add(oneLine);

        while ((oneLine = puzzleStream.readLine()) != null) {
            if (oneLine.length() != columns)
                System.err.println("Puzzle is not rectangular: skipping row");
            else 
                puzzleLines.add(oneLine);
        }   

        rows = puzzleLines.size();
        theBoard = new char[rows][columns];

        int r = 0;
        for (String theLine : puzzleLines)
            theBoard [r++] = theLine.toCharArray();
    }

    
    private int solveDirection(int baseRow, int baseCol, int rowDelta, int colDelta) {
        int matches = 0;
        StringBuilder sequence = new StringBuilder();
        for (int row = baseRow, col = baseCol; row >= 0 && row < rows && col >= 0 && col < columns; row += rowDelta, col += colDelta) {
            sequence.append(theBoard[row][col]);
            int prefixIndex = prefixSearch(theWords, sequence.toString());
            if (prefixIndex >= 0) {
                matches++;
                System.out.printf("Found %s at [%d,%d] to [%d,%d]\n", sequence.toString(), baseRow, baseCol, row, col);
            }
            if (prefixIndex < -1) { // If prefix not found, break
                break;
            }
        }
        return matches;
    }
}
