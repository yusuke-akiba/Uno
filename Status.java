// Name: Yusuke Akiba
// Date: June 1, 2014
// Description: Class of Status
//              Status consists of type,color,number,orderArray

/////IMPORTS/////////////////////////////////////
/////DELETE_SLASHES_TO_ACTIVATE//////////////////
//import java.util.Scanner;
import java.util.Random;
//import java.util.ArrayList;
//import javax.swing.JOptionPane;
//import java.io.*; //Don't forget IOException!
/////////////////////////////////////////////////

/**
 * <p>Class of Status to hold/handle information of status of the game.</p>
 */
public class Status{
   // Create random object
   private Random rand = new Random();
   // Field
   private int topCardNum;
   private String cardType;
   private String color;
   private String number;
   private int draw = 0;
   private int skip = 0;
   private int[] orderAry;//indicates players indexes
   
   // Constructor
   /**
    * @param topCardNum int, index of a card on top of the discard pile
    * @param cardType String, a type the next discard should match
    * @param color String, a color the next discard should match
    * @param number String, a number the next discard should match
    * @param plsAry Player[], to make the intitial order of play
    */
   public Status(int topCardNum,String cardType,String color,String number,Player[] plsAry){
      this.topCardNum = topCardNum;
      this.cardType = cardType;
      this.color = color;
      this.number = number;
      //create order array
      this.orderAry = new int[plsAry.length];
      //choose first player.
      int index = rand.nextInt(plsAry.length);
      for(int i=0; i<plsAry.length;i++,index++){
         if(index<plsAry.length){
            this.orderAry[index] = i;
         }else{
            index=0;
            this.orderAry[index] = i;
         }
      }
   }
   
/////////////////////////////////////////////////////////Methods
   /**
    * Shows status (for debug)
    */
   public void showStatus(){
      // for debug
      System.out.println("-------------");
      System.out.println("cardType:\t"+cardType+"\ncolor:\t\t"+color+"\nnumber:\t\t"+number+"\norderArray:");
      for(int i=0; i<orderAry.length;i++){
         System.out.println("\t\t"+orderAry[i]);
      }
      System.out.println("-------------");
   }
   /**
    * Shows status in eligible format for playing
    * @param deck info of deck
    * @param plsAry info of players
    */
   public void showStatus(String[][] deck,Player[] plsAry){
      System.out.println("------- Current Status -------");
      System.out.println("Color: \t"+color);
      System.out.println("Draws: \t"+draw);
      System.out.print("Top of the Pile:  ");
      switch(deck[topCardNum][0]){
         case "WD4":
            System.out.print("Wild Draw Four");
            break;
         case "WC":
            System.out.print("Wild");
            break;
         case "num":
            System.out.print(deck[topCardNum][2]+" of "+deck[topCardNum][1]);
            break;
         case "draw":
            System.out.print("Draw Two of "+deck[topCardNum][1]);
            break;
         case "skip":
            System.out.print("Skip of "+deck[topCardNum][1]);
            break;
         case "rev":
            System.out.print("Reverse of "+deck[topCardNum][1]);
            break;
      }
      System.out.print("\nPlayer Order:\t");
      for(int i=0;i<orderAry.length;i++){
         System.out.print(plsAry[orderAry[i]].getName()+"("+plsAry[orderAry[i]].getHandSize()+") -> ");
      }
      System.out.println("\n------------------------------");
   
   }
   
/////////////////////////////////////////////////////////Getter
   public int getTopCardNum(){
      return topCardNum;
   }
   public String getType(){
      return cardType;
   }
   public String getColor(){
      return color;
   }
   public String getNum(){
      return number;
   }
   public int getDraw(){
      return draw;
   }
   public int getSkip(){
      return skip;
   }
   public int[] getOrderAry(){
      return orderAry;
   }
   public int getPlayersIDFromOrderAry(int indexInOrderAry){// return players ID (static number. user = 0, com1 = 1)
      int indexInPlayersAry = orderAry[indexInOrderAry];
      return indexInPlayersAry;
   }
   public int getCurrentPlayerID(){
      return orderAry[0];
   }
   public String getCurrentPlayerName(Player[] plsAry){
      return plsAry[orderAry[0]].getName();
   }
   public int getNumOfPlayers(){
      return orderAry.length;
   }
/////////////////////////////////////////////////////////Setter
   public void setTopCardNum(int num){
      this.topCardNum = num;
   }
   public void setCardType(String type){
      this.cardType = type;
   }
   public void setColor(String c){
      this.color = c;
   }
   public void setNum(String num){
      this.number = num;
   }
   public void addDraw(int num){
      draw += num;
   }
   public void addSkip(int num){
      skip += num;
   }
   public void clearSkip(){
      skip =0;
   }
   public void clearDraw(){
      draw = 0;
   }
   /**
    * moves order array once
    */
   public void goToTheNextPlayer(){
      int currentPlayer = orderAry[0];
      for(int i=0;i<(getNumOfPlayers()-1);i++){
         orderAry[i] = orderAry[i+1];
      }
      orderAry[orderAry.length-1] = currentPlayer;
   }
   public void reverseOrderAry(){
      int[] newAry = new int[orderAry.length];
      newAry[0] = orderAry[0];
      for(int i=1;i<orderAry.length;i++){
         newAry[newAry.length-i] = orderAry[i];
      }
      orderAry = newAry;
      // Show Info.
      System.out.println("Playing order was reversd.");
   }
}
