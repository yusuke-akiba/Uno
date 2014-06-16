// Name: Yusuke Akiba
// Date: June 1, 2014
// Description: 

/////IMPORTS/////////////////////////////////////
/////DELETE_SLASHES_TO_ACTIVATE//////////////////
import java.util.Scanner;
//import java.util.Random;
import java.util.ArrayList;
//import javax.swing.JOptionPane;
import java.io.*; //Don't forget IOException!
/////////////////////////////////////////////////
/**
 * Class of Usershistory to handle user history
 */
public class UsersHistory{
   // field
   private String fileName;
   /**
    * set fileName as given
    * @param fn filename of type String.
    */
   public UsersHistory(String fn){
      this.fileName = fn;
   }
   /**
    * Prints all histories of all players
    */
   public void showAllHistory() throws IOException{
      System.out.println("\n================== Records of All Players ==================\n");
      //flag of if find
      boolean isFound = false;
      // retrieve records
      String[][] rcdAry = readRecords();
      for(int i=0;i<rcdAry.length;i++){
         System.out.print(rcdAry[i][0]+(rcdAry[i][0].length()<9?"\t":"")+"\t\t"+rcdAry[i][1]+" wins / "+rcdAry[i][2]+" games");
         Double rate = (Double.parseDouble(rcdAry[i][1])/Integer.parseInt(rcdAry[i][2])*100);
         System.out.printf("    %.1f%%\n",rate);
      }
      System.out.println("\n============================================================");
   }
   /**
    * Prints given user's history
    * @param name The user's name as given
    */
   public void showUsersHistory(String name) throws IOException{
      System.out.println("\n========================Your History========================\n");
      //flag of if find
      boolean isFound = false;
      // retrieve records
      String[][] rcdAry = readRecords();
      for(int i=0;i<rcdAry.length;i++){
         if(rcdAry[i][0].equals(name)){
            isFound = true;
            System.out.print(rcdAry[i][0]+(rcdAry[i][0].length()<9?"\t":"")+"\t\t"+rcdAry[i][1]+" wins / "+rcdAry[i][2]+" games");
            Double rate = (Double.parseDouble(rcdAry[i][1])/Integer.parseInt(rcdAry[i][2])*100);
            System.out.printf("    %.1f%%\n",rate);
         }
      }
      System.out.println("\n============================================================");
   }
   /**
    * Simply reads all history and store into String[name][win], then return the array.
    */
   private String[][] readRecords() throws IOException{
      // create File object
      File recordFile = new File(fileName);
      // create Scanner object to read file
      Scanner inputFile = new Scanner(recordFile);
      // create ArrayList object to store data
      ArrayList<String> recordsList = new ArrayList<String>(0);
      // read line by line
      while(inputFile.hasNext()){
         recordsList.add(inputFile.nextLine());
      }
      //close file
      inputFile.close();
      // create Array of String to store result
      String[][] recordsAry = new String[recordsList.size()][3];
      
      // convert ArrayList to Array while splitting
      String str, name, win, games, pattern = ":";
      for(int i=0; i<recordsList.size();i++){
         //split by regexp
         recordsAry[i] = recordsList.get(i).split(pattern);
      }
      return recordsAry;
   }
   /**
    * Update history as intended
    * @param isWin if the player won
    * @param name the player's name
    */
   public void writeRecord(boolean isWin, String name) throws IOException{
      //temporary store the last data
      String[][] tempData = readRecords();
      //search the user
      boolean isFound = false;
      for(int i=0;i<tempData.length;i++){
         if(tempData[i][0].equals(name)){
            tempData[i][1] = isWin?(Integer.parseInt(tempData[i][1])+1)+"":tempData[i][1];
            tempData[i][2] = (Integer.parseInt(tempData[i][2])+1)+"";
            isFound = true;
            //System.out.println("recordfound");
         }
      }
      //rewrite
      //create FileWriter/PrintWriter object
      FileWriter fWriter = new FileWriter(fileName);
      PrintWriter outputFile = new PrintWriter(fWriter);
      for(int i=0;i<tempData.length;i++){
         outputFile.println(tempData[i][0]+":"+tempData[i][1]+":"+tempData[i][2]);
      }
      //if new user
      if(!isFound){
         outputFile.println(name+":"+((isWin)?1:0)+":"+1);
      }
      // close file
      outputFile.close();
      
   }
}