// Name: Yusuke Akiba
// Date: June 1, 2014
// Description: Class of Card

/////IMPORTS/////////////////////////////////////
/////DELETE_SLASHES_TO_ACTIVATE//////////////////
//import java.util.Scanner;
//import java.util.Random;
//import java.util.ArrayList;
//import javax.swing.JOptionPane;
//import java.io.*; //Don't forget IOException!
/////////////////////////////////////////////////
/**
 * Card class to handle stuff related to card.
 * Ideally, deck completly handled by this class, or deck class.
 * All methods are static context.
 */
public class Card{
   public static void allCardsToBeUnUsed(String[][] deck){
      for(int i=0;i<deck.length;i++){
         deck[i][3] = "false";
      }
   }
   public static void allCardToBeUsed(String[][] deck){
      for(int i=0;i<deck.length;i++){
         deck[i][3] = "true";
      }
   }
   
   /**
    * <p>
    * Check if the deck has enough remaining cards.<br>
    * If not, refresh the deck. Used card (in hand, on the discard pile).<br>
    * It does same as when the deck is getting fewer, the card in the discard<br>
    * pile need to be re-shuffled and added to the deck again,<br>
    * except the card on the top of discard pile.
    * </p>
    * @param deck Could be updated.
    * @param status Status information of class Status. Makes exception by checking what card is currently on the top of discard pile.
    * @param plsAry Makes exceptions by checking what cards are currently in the hands of players.
    */
   public static void checkRemainingDeck(String[][] deck, Status status,Player[] plsAry){
      int c = 0;//counter
      for(int i=0;i<deck.length;i++){
         c+=deck[i][3].equals("true")?1:0;
         //System.out.print(c);//ForDebug
         if(c>10){break;}
      }
      if(c<=10){//retrieve deck
         for(int i=0;i<deck.length;i++){
            deck[i][3]="false";// all to false at once
         }
         // make the used cards true
         deck[status.getTopCardNum()][3] = "true";
         for(int i=0;i<plsAry.length;i++){
            for(int j=0;j<plsAry[i].getHandSize();j++){
               deck[plsAry[i].getHandValue(j)][3] = "true";
            }
         }
      }
   }
   /**
    * Return card info in line to print. Invokable from inside of print/println
    * @param deck to retrieve info by id
    * @param cardNum instructed id
    * @return eligible info (inline)
    */
   public static String showCardInfo(String[][] deck, int cardNum){
      String str = "";
      switch(deck[cardNum][0]){
         case "WD4":
            str = "Wild Draw Four";
            break;
         case "WC":
            str = "Wild";
            break;
         case "num":
            str = deck[cardNum][2]+" of "+deck[cardNum][1];
            break;
         case "draw":
            str = "Draw Two of "+deck[cardNum][1];
            break;
         case "skip":
            str = "Skip of "+deck[cardNum][1];
            break;
         case "rev":
            str = "Reverse of "+deck[cardNum][1];
            break;
      }
      return str;
   }
   /**
    * Checks if given cards looks same even if the id is different.
    * @param deck to retrieve card info
    * @param a first card id to compare
    * @param b second card id to compare
    * @return true or false = same or not same
    */
   public static boolean isSame(String[][] deck, int a, int b){
      if(isSameT(deck,a,b)&&isSameC(deck,a,b)&&isSameN(deck,a,b)){
         return true;
      }else{
         return false;
      }
      
   }
   /**
    * Checks if given cards have same type.
    * @param deck to retrieve card info
    * @param a first card id to compare
    * @param b second card id to compare
    * @return true or false = same or not same
    */
   public static boolean isSameT(String[][] deck,int a, int b){
      if(deck[a][0].equals(deck[b][0])){
         return true;
      }else{
         return false;
      }
      
   }
   /**
    * Checks if given cards have instructed type.
    * @param deck to retrieve card info
    * @param a first card id to compare
    * @param b type a has to match
    * @return if match
    */
   public static boolean isSameT(String[][] deck,int a, String b){
      if(deck[a][0].equals(b)){
         return true;
      }else{
         return false;
      }
      
   }
   /**
    * Checks if given cards have same color.
    * @param deck to retrieve card info
    * @param a first card id to compare
    * @param b second card id to compare
    * @return true or false = same or not same
    */
   public static boolean isSameC(String[][] deck,int a, int b){
      if(deck[a][1].equals(deck[b][1])){
         return true;
      }else{
         return false;
      }
      
   }
   /**
    * Checks if given cards have instructed color.
    * @param deck to retrieve card info
    * @param a first card id to compare
    * @param b color a has to match
    * @return if match
    */
   public static boolean isSameC(String[][] deck,int a, String b){
      if(deck[a][1].equals(b)){
         return true;
      }else{
         return false;
      }
      
   }
   /**
    * Checks if given cards have same name(symbol).
    * @param deck to retrieve card info
    * @param a first card id to compare
    * @param b second card id to compare
    * @return true or false = same or not same
    */
   public static boolean isSameN(String[][] deck,int a, int b){
      if(deck[a][2].equals(deck[b][2])){
         return true;
      }else{
         return false;
      }
      
   }
   /**
    * Checks if given cards have instructed name(symbol).
    * @param deck to retrieve card info
    * @param a first card id to compare
    * @param b name a has to match
    * @return if match
    */
   public static boolean isSameN(String[][] deck,int a, String b){
      if(deck[a][2].equals(b)){
         return true;
      }else{
         return false;
      }
      
   }
}