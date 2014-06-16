// Name: Yusuke Akiba
// Date: June 1, 2014
// Description: Class of Player

/////IMPORTS/////////////////////////////////////
/////DELETE_SLASHES_TO_ACTIVATE//////////////////
import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
//import javax.swing.JOptionPane;
//import java.io.*; //Don't forget IOException!
/////////////////////////////////////////////////
/**
 * Class of Player to hold player's information
 */
public class Player{
   private String name; // named automatically
   private String nameToDisplay; // only defined by constructor
   private ArrayList<Integer> hand = new ArrayList<Integer>(0);
   
   //Constructer
   /**
    * @param newName String, user,com1-5
    * @param newNameToDisplay String, name to display. User's name which is input, or com1-5
    */
   public Player(String newName,String newNameToDisplay){
      // attributes
      this.name = newName;
      this.nameToDisplay = newNameToDisplay;
   }
//////////////////////////////////////////////////////////////////////////////////////////Methods
   /**
    * Makes the player to draw instructed number of cards
    * @param deck info to draw and update,
    * @param status info execute checkRemainingDeck()
    * @param plsAry info to execute checkRemainingDeck()
    * @param numOfDraw instructs num of drawing
    */
   public int[] drawCards(String[][] deck, Status status, Player[] plsAry, int numOfDraw){
      // create random obj
      Random rand =  new Random();
      ArrayList<Integer> newCardsList = new ArrayList<Integer>();
      int cardNum;
      for(int j=0; j<numOfDraw; j++){
         do {
            Card.checkRemainingDeck(deck,status,plsAry);
            cardNum = rand.nextInt(108);
         }while(deck[cardNum][3].equals("true"));
         hand.add(cardNum);
         newCardsList.add(cardNum);
         deck[cardNum][3] = "true";
      }
      
      // declare Array to store return value
      int[] ary = new int[newCardsList.size()];
      
      // List to Array
      for(int i=0;i<newCardsList.size();i++){
         ary[i] = newCardsList.get(i);
      }
      return ary;
   }
   /**
    * Make the player to put one instructed card down from the hand
    * @param status info to change
    * @param deck to retrieve card info with index
    * @param cardNum index of the instructed card to put
    */
   public void putACard(Status status, String[][] deck, int cardNum){
      //message
      System.out.println((name.equals("user")?"You":("Com"+getName().substring(getName().length()-1,getName().length())))+" put "+Card.showCardInfo(deck,cardNum));
      //remove discarded card from hand
      for(int i=0;i<hand.size();i++){
         if(cardNum == hand.get(i)){
            hand.remove(i);
            break;
         }
      }
      //change status
      switch(deck[cardNum][0]){
         case "WD4"://if it is WD4
            status.addDraw(4);
         case "WC"://if it is WC
            status.setTopCardNum(cardNum);
            status.setCardType(deck[cardNum][0]);
            
            // ask what color is preferable
            status.setColor(getPreferableNextColor(deck));
            
            status.setNum(deck[cardNum][2]);
            break;
         case "draw":
            status.addDraw(2);
         case "num":
            status.setTopCardNum(cardNum);
            status.setCardType(deck[cardNum][0]);
            status.setColor(deck[cardNum][1]);
            status.setNum(deck[cardNum][2]);
            break;
         case "skip":
            
            //call method
            status.addSkip(2);
            
            status.setTopCardNum(cardNum);
            status.setCardType(deck[cardNum][0]);
            status.setColor(deck[cardNum][1]);
            status.setNum(deck[cardNum][2]);
            break;
         case "rev":
            
            //call method to reverse the order 
            status.reverseOrderAry();
            
            status.setTopCardNum(cardNum);
            status.setCardType(deck[cardNum][0]);
            status.setColor(deck[cardNum][1]);
            status.setNum(deck[cardNum][2]);
            break;
            
      }
   }
   /**
    * Print info for debug
    */
   public void showInfo(){
      // for debug
      System.out.print("-------------\nname:\t"+name+"\nNTD:\t"+nameToDisplay+"\nhand("+this.hand.size()+"):\n");
      for(int i=0; i<this.hand.size();i++){
         System.out.println("\t"+this.hand.get(i));
      }
      System.out.println("-------------");
   }
   /**
    * show the players hand for playing
    * @param deck info of deck to invoke CardInfo, which isinline printing method
    */
   public void showHand(String[][] deck){
      if(this.name.equals("user")){
         System.out.println("Your hand ("+this.getHandSize()+") :");
         for(int i=0; i<this.getHandSize();i++){
            System.out.println("\t\t"+Card.showCardInfo(deck,this.getCardNumOfHand(i)));
         }
         System.out.println("------------------------------");
      }
   }
   /**
    * choose best card combination for computer players (currently, the cards are chosen randomly)
    * @param status status info to invoke method putACard()
    * @param deck deck information to invoke method putACard()
    * @param possibleCards selection of cards
    */
   public void putBestCombination(Status status,String[][] deck,int[] possibleCards){
      Random rand = new Random();
      //if draw or not
      if(status.getDraw()>0){
         //random selection as a placeholder
         putACard(status,deck,possibleCards[rand.nextInt(possibleCards.length)]);
      }else{
         //random selection as a placeholder
         putACard(status,deck,possibleCards[rand.nextInt(possibleCards.length)]);
      }
   }
//////////////////////////////////////////////////////////////////////////////////////////Getter
   public String getName(){
      return this.nameToDisplay;
   }
   public int getHandSize(){
      return hand.size();
   }
   public int getHandValue(int id){
      return this.hand.get(id);
   }
   public int getCardNumOfHand(int num){
      return this.hand.get(num);
   }
   /**
    * Find possible cards to put and return it as an array of int(card index in deck).
    * @param status necessary info to judge what cards are possible to put
    * @param deck to retrieve card info by indexnum
    * @return array of int, consists of possible cards to put. could be empty
    */
   public int[] getAryOfPossibleCards(Status status, String[][] deck){
      // create Arraylist object
      ArrayList<Integer> list = new ArrayList<Integer>(0);
      
      // declare variables for eligibility
      int stTCN = status.getTopCardNum();
      String stC = status.getColor();
      String stT = status.getType();
      String stN = status.getNum();
      
      for(int i=0; i<hand.size(); i++) {// check if the hand is possible to put, one by one. If possible, add. If not, do nothing.
         
         // store Current Hand ID foe eligibility
         int cHID = hand.get(i);
         
         // if the last player put draws, check the player has possible draw(s). If same color of Draw 2 or WD4, then list.
         if(status.getDraw()>0){// if draws are active
            if(Card.isSameT(deck,cHID,"WD4")){
               list.add(cHID);
            }else if(Card.isSameT(deck,cHID,"draw")&&Card.isSameC(deck,cHID,stC)){
               list.add(cHID);
            }
         }else{// if draws are inactive
            if(Card.isSameT(deck,cHID,"WD4")||Card.isSameT(deck,cHID,"WC")){// any case of WD4 and WC
               list.add(cHID);
            }else if(Card.isSameT(deck,cHID,stT)&&Card.isSameN(deck,cHID,stN)){// if same number and type
               list.add(cHID);
            }else if(Card.isSameC(deck,cHID,stC)){// if same color
               list.add(cHID);
            }
         }
      }
      
      // declare Array to store return value
      int[] ary = new int[list.size()];
      
      // List to Array
      for(int i=0;i<list.size();i++){
         ary[i] = list.get(i);
      }
      return ary;
   }
   /**
    * Find Secondary possible cards to put.
    * Different procedure from first is required.
    * @param status necessary info to judge what cards are possible to put
    * @param deck to retrieve card info by indexnum
    * @return array of int, consists of possible cards to put. could be empty
    */
   public int[] getPossibleSecondaryCard(Status status, String[][] deck){
      // create Arraylist object
      ArrayList<Integer> list = new ArrayList<Integer>(0);
      
      // declare variables for eligibility
      int stTCN = status.getTopCardNum();
      String stC = status.getColor();
      String stT = status.getType();
      String stN = status.getNum();
      
      for(int i=0;i<hand.size();i++){
         // store Current Hand ID foe eligibility
         int cHID = hand.get(i);
         boolean isMatch = false;
         
         //If the type(WD4,WC,draw...num) and number(none, 0,1) are totally same, available
         if(Card.isSameT(deck,stTCN,cHID)&&Card.isSameN(deck,stTCN,cHID)){isMatch=true;}
         //in case of draw2 of the selected color
         if(Card.isSameT(deck,stTCN,"WD4")&&Card.isSameC(deck,cHID,stC)&&deck[cHID][0].equals("draw")){isMatch=true;}
         
         if(isMatch){list.add(cHID);}
      }
      
      // declare Array to store return value
      int[] ary = new int[list.size()];
      
      // List to Array
      for(int i=0;i<list.size();i++){
         ary[i] = list.get(i);
      }
      return ary;
   }
   /**
    * find what color is the best to next
    * @param deck to retrieve situation of hand by card id
    * @return best color
    */
   private String getPreferableNextColor(String[][] deck){
      if(this.name=="user"){//if the player is the user, ask
      
         // create Scanner object
         Scanner kb = new Scanner(System.in);
         // ask color
         System.out.print("You put a wild card.\nWhich color do you prefer?\n Please input \"red\", \"blue\", \"yellow\", or \"green\": ");
         String newColor = kb.nextLine();
         while((!newColor.equals("red"))&&(!newColor.equals("blue"))&&(!newColor.equals("yellow"))&&(!newColor.equals("green"))){
            System.out.print("You input wrong color. Please input again. ");
            newColor = kb.nextLine();
         }
         
         // return color
         return newColor;
         
      }else{// if the player is a com player, count colors in every color, find the best color
         // declare counter
         int r = 0, b = 0, y = 0, g = 0;
         // count each colors
         for(int i=0;i<this.hand.size();i++){
            switch(deck[hand.get(i)][1]){
               case "red":
                  r++;
                  break;
               case "blue":
                  b++;
                  break;
               case "yellow":
                  y++;
                  break;
               case "green":
                  g++;
                  break;
            }
         }
         int max = Math.max(r,b)>Math.max(y,g)?Math.max(r,b):Math.max(y,g);
         return max==r?"red":max==b?"blue":max==y?"yellow":"green";
      }
   }
   public int doesThePlayerBurst(int id, int num, int max){
      //System.out.println(this.nameToDisplay + " is checked for burst.");
      if((num+this.hand.size())>max){
         return id;
      }else{
         return -1;
      }
   }
//////////////////////////////////////////////////////////////////////////////////////////Setter
   /**
    * For initial distribution. coppy param of ArrayList to hand
    * @param cards cards list created by initialDistribution() in Uno.java
    */
   public void setInitialHand(ArrayList<Integer> cards){//input arraylist
      for(int i=0;i<cards.size();i++){
         this.hand.add(cards.get(i));
      }
   }
}