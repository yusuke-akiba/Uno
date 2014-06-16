// Name: Yusuke Akiba
// Date: June 1, 2014
// Description: Uno with some special odd rules

/////IMPORTS/////////////////////////////////////
/////DELETE_SLASHES_TO_ACTIVATE//////////////////
import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
//import javax.swing.JOptionPane;
import java.io.*; //Don't forget IOException!
/////////////////////////////////////////////////

/**
 * @author Yusuke Akiba
 * @version 1.1
 * @since June 1, 2014
 * <p>Uno Game with House Rules<p>
 * <p>User can play the game with up to 5 com players.<br>
 * Every game result(win/lose) will be recorded to the file
 * "usershistories.txt" with the user name.
 * Users also can see their history and all histories.
 * </p>
 */
public class Uno{
   /**
    * <p>The main routine.</p>
    */
   public static void main(String[] args) throws IOException{
      
      // create Rcanner object
      Scanner kb = new Scanner(System.in);
      
      // create Random object
      Random rand = new Random();
      
      // create UsersHistory object
      UsersHistory hisObj = new UsersHistory("usershistories.txt");
      
      // declare variables
      String userInput = "";// Store user input temprarily
      int numOfPls = 0;
      String userName = "defaultUser"; // name to display. Other than to display or search history, user's name is always "user"
      Player[] plsAry;// the index will never be changed. The order is stored in orderArray of status
      int indexOfCurrentPlayer;// to store player's id during each turn
      int maxHandSize = 15;//the player who has more than 15 card will lose
      boolean giveUp = false;
      boolean isSomeoneWin = false;// flag to check if someone wins
      boolean isSomebodyBurst = false;//flag to check if somebody bursts
      
      // ask user to input username
      do{
         print("What is your name? ");
         userName = kb.nextLine();
      }while(!(userName.length()>0));
      // Validate userName. The name cannot end with colon since record file uses colon to seperate fields
      String lastCharOfUserName = userName.substring(userName.length()-1,userName.length());
      while(lastCharOfUserName.equals(":")){
         print("Please do not use semicolon as the last of your name.\nPlease input your name. ");
         userName = kb.nextLine();
         //get last characer
         lastCharOfUserName = userName.substring(userName.length()-1,userName.length());
      }
      
      //ask user which to start game or to view history the user wants. 
      print("Hello,"+userName+". Input \"g\" to start new game, input \"h\" to view your history. ");
      String gameOrHistory = kb.nextLine();
      
      if(gameOrHistory.equals("h")){//when user wnats to view history
         //ask the user if want see all records or personal record.
         println("Do you want to see records of all players? or only yours?");
         String allOrMine;
         
         do{//listen and validate
            print("Please input \"all\" or \"mine\": ");
            allOrMine = kb.nextLine();
         }while(!(allOrMine.equals("all")||allOrMine.equals("mine")));
         
         if(allOrMine.equals("all")){// user wants see all records
            hisObj.showAllHistory();// call method to show
         }else{// the user wants see own records only
            hisObj.showUsersHistory(userName);//call method to show
         }
      }else if(gameOrHistory.equals("g")){
         // Create a deck
         // After this, all cards are delt as an int(0-107).
         // The number points the specific element of the array of "deck"
         // Call method to create a deck
         String[][] deck = createDeck();
         
         // ask user to input a number of players (1-5)
         print("How many computer players do you need(1-5)? ");
         numOfPls = Validation.parseInt(kb.nextLine(),-1)+1;
         while(numOfPls<2||numOfPls>6){
            print("Please input a number from 1 to 5: ");
            userInput = kb.nextLine();
            numOfPls = Validation.parseInt(userInput,-1)+1;
         }
         
         // create players array of player objects and name
         plsAry = createPlsAry(userName,numOfPls);
         
         // distribute seven cards to all players
         initialDistribution(plsAry,deck);
         println("All "+numOfPls+" players have seven cards each now.");
         
         // draw one card to put on the top of discard pile. Keep drawing until num is drawn.
         int initialCard;
         do{
            initialCard = drawOneCard(deck);
         }while(!(deck[initialCard][0].equals("num")));
         // show info
         println("The card on the top of the discard pile is: "+Card.showCardInfo(deck,initialCard));
         
         // create status class
         Status status = new Status(initialCard,deck[initialCard][0],deck[initialCard][1],deck[initialCard][2],plsAry);
         
         while(true){// need a break to quit game
            
            // show status
            status.showStatus(deck,plsAry);
            
            // determine the current player by id
            // get Current Player ID for use
            int cPID = status.getCurrentPlayerID();
            //if it is the user, show instruction.
            if(cPID==0){
               print("It is your turn.\nInput \"q\" to quit. Press enter to continue: ");
               if(kb.nextLine().equals("q")){
                  // quit the game with flag
                  println("You quit the game.");
                  giveUp = true;
                  break;
               }else{
                  println("Game continues.");
                  // show users hand
                  println("Your hand ("+plsAry[0].getHandSize()+") :");
                  for(int i=0; i<plsAry[0].getHandSize();i++){
                     println("\t\t"+Card.showCardInfo(deck,plsAry[0].getCardNumOfHand(i)));
                  }
                  println("------------------------------");
               }
            }
            
            // check status and list possible card from the hand.
            // If there is nothing to put and it is the user, tell it and force to confirm to draw a card.
            //    If the last player put draw card, it will be more than one.
            // If there is nothing to put and it is a computer, just draw cards.
            //    If the last player put draw card, it will be more than one.
            // If there are something to put and it is the user, ask what to put.
            // If there are something to put and it is computer player, call method to put cards appropriately.
            if(status.getDraw()>0){// If the last player put any draw cards.
               
               //regardless of situation, list possible cards (situation is determined by the method)
               int[] possibleCards = plsAry[cPID].getAryOfPossibleCards(status,deck);
               
               //check the player has possible card to put
               if(possibleCards.length == 0){//if nothing, then draw cards
                  //check if it is the user
                  if(cPID==0){// If it is user, tell the user has nothing to put. then draw cards.
                     // show info and ask to confirm.
                     print("You have no draw card to put! You have to draw "+status.getDraw()+" cards. Press enter.");
                     kb.nextLine();
                     // check if the player could burst. if true, end game.
                     if(plsAry[cPID].doesThePlayerBurst(cPID,status.getDraw(),maxHandSize)>=0){
                        println(plsAry[cPID].getName()+" bursts!");
                        break;
                     }// if not, draw
                     // draw cards
                     println("Drawing new cards ... ");
                     int[] drawnCAry = plsAry[cPID].drawCards(deck,status,plsAry,status.getDraw());
                     status.clearDraw();
                     // show info.
                     print("The new cards are:\t");
                     for(int i=0; i<drawnCAry.length;i++){
                        if(i>0){print("\t\t\t");};
                        println(Card.showCardInfo(deck,drawnCAry[i]));
                     }
                  }else{//If it is a com player, just draw cards and go to next player.
                     //show process to the user
                     println(plsAry[cPID].getName() + " had nothing to put and drew "+status.getDraw()+" cards!");
                     // check if the player could burst, if true, end game
                     if(plsAry[cPID].doesThePlayerBurst(cPID,status.getDraw(),maxHandSize)>=0){
                        println(plsAry[cPID].getName()+" bursts!");
                        break;
                     }
                     //if not, draw cards
                     // draw same number of cards as draw atribute of status, and clear draw.
                     plsAry[cPID].drawCards(deck,status,plsAry,status.getDraw());
                     status.clearDraw();
                  }
               }else{// If the player has something to put
                  // check if it is the user 
                  if(cPID==0){//if the player is the user, ask what to put
                     //show the user how many card to put the user has.
                     print("You have " + possibleCards.length +" card"+((possibleCards.length==0)?"":"s")+" to put!");
                     println("Please select the first card to put from following:");
                     //Show selection
                     for(int i=0;i<possibleCards.length;i++){
                        println("\t\t["+i+"]  "+Card.showCardInfo(deck,possibleCards[i]));
                     }                     // ask to input indicator, and convert to card num
                     print("Which card do you prefer to put?\nPlease input a number in []: ");
                     int cardNumToPut = Validation.parseInt(kb.nextLine(),-1);
                     // if wrongNumber, keep asking
                     while(cardNumToPut<0||cardNumToPut>=possibleCards.length){
                        print("Wrong number. Please input a number in []: ");
                        Validation.parseInt(kb.nextLine(),-1);
                     }
                     // convert idedx to cardID
                     cardNumToPut = possibleCards[cardNumToPut];
                     // put the card
                     plsAry[cPID].putACard(status,deck,cardNumToPut);
                     while(true){
                        // check if there is more possible cards and ask if the user want to put
                        int[] secondaryPossibleCards = plsAry[cPID].getPossibleSecondaryCard(status,deck);
                        if(secondaryPossibleCards.length==0){break;}// If there is nothing to put, quit the turn.
                        //If ther is somehing, ask again.
                        //show hand first
                        plsAry[cPID].showHand(deck);
                        //show the user how many card to put the user has.
                        println("You have " + secondaryPossibleCards.length +" more card"+((secondaryPossibleCards.length==0)?"":"s")+" to put!");
                        println("Please select the next card to put from following:");
                        //Show selection
                        for(int i=0;i<secondaryPossibleCards.length;i++){
                           println("\t\t["+i+"]   " + Card.showCardInfo(deck,secondaryPossibleCards[i]));
                        }
                        //last choice is to quit
                        println("\t\t["+secondaryPossibleCards.length+"]  Quit. Put nothing.");
                        // ask to input indicator, and convert to card num
                        print("Which card do you prefer to put? input a number inside of [] ");
                        // declare variable to store user input, then listen. invoke validation to parse the String input to int.
                        int secondaryCardNumToPut = Validation.parseInt(kb.nextLine(),-1);
                        //keep asking until appropriate number is input
                        while(secondaryCardNumToPut<0||secondaryCardNumToPut>secondaryPossibleCards.length){
                           print("Wrong number. please retry: ");
                           secondaryCardNumToPut = Validation.parseInt(kb.nextLine(),-1);
                        }
                        //check if the user quit to put
                        //num of length is just equal to last choise. 
                        if(secondaryPossibleCards.length!=secondaryCardNumToPut){//if different, the user chose to put
                           // convert index to cardNum
                           secondaryCardNumToPut = secondaryPossibleCards[secondaryCardNumToPut];
                           // put the card (method shows info to output)
                           plsAry[cPID].putACard(status,deck,secondaryCardNumToPut);
                        }else{//if same, the user chose to quit
                           println("You quit to put.");
                           break;
                        }
                        
                     }  
                  }else{//if it is a com player
                     //com player has some card to put
                     //determine the best order of draws.
                     //in any case choose preferable color
                     //if there is wd4 put it the last and declare the preferable color
                     //if there is not wd4
                     plsAry[cPID].putBestCombination(status,deck,possibleCards);// currently random choise
                     while(true){//while the com has any card to put, keep putting randomly. (never quit)
                        int[] secondaryPossibleCards = plsAry[cPID].getPossibleSecondaryCard(status,deck);
                        if(secondaryPossibleCards.length==0){break;}
                        plsAry[cPID].putBestCombination(status,deck,secondaryPossibleCards);
                     }
                  }
               }
            }else{// If the last player did not put any draw card.
               //list possible cards
               int[] possibleCards = plsAry[cPID].getAryOfPossibleCards(status,deck);
               // check these is possible card to put initially
               if(possibleCards.length == 0){//if have nothing to put, then draw one
                  //Check if it is the user
                  if(cPID==0){// If it is user, tell the user has nothing to put. then draw one card.
                     // show info and ask to confirm
                     print("You have nothing to put! You have to draw one card. Press enter.");
                     kb.nextLine();
                     // check if the player could burst
                     if(plsAry[cPID].doesThePlayerBurst(cPID,status.getDraw(),maxHandSize)>=0){
                        println(plsAry[cPID].getName()+" bursts!");
                        break;
                     }
                     // draw one card
                     print("Drawing a new card ... ");
                     int[] drawnCAry = plsAry[cPID].drawCards(deck,status,plsAry,1);
                     // show info.
                     println("The new card is:\t"+Card.showCardInfo(deck,drawnCAry[0]));
                  }else{// if it is a com player,just draw one card and go to the next olayer.
                     //show process to the user
                     println(plsAry[cPID].getName() + " had nothing to put and drew one card!");
                     // check if the player could burst
                     if(plsAry[cPID].doesThePlayerBurst(cPID,status.getDraw(),maxHandSize)>=0){
                        println(plsAry[cPID].getName()+" bursts!");
                        break;
                     }
                     //draw one card
                     plsAry[cPID].drawCards(deck,status,plsAry,1);
                  }
               }else{// If something to put
                  if(cPID==0){//if it is the user, ask what to put.
                     //show the user how many card to put the user has.
                     println("You have " + possibleCards.length +" card"+((possibleCards.length==0)?"":"s")+" to put!");
                     println("Please select the first card to put from following:");
                     //Show selection
                     for(int i=0;i<possibleCards.length;i++){
                        println("\t\t["+i+"]  "+Card.showCardInfo(deck,possibleCards[i]));
                     }
                     print("Which card do you prefer to put? ");
                     // ask to input indicator, and convert to card num
                     int cardNumToPut;
                     do {
                        cardNumToPut = Integer.parseInt(kb.nextLine());
                        if(cardNumToPut<0||cardNumToPut>=possibleCards.length){
                           print("Wrong number. please retry: ");
                        }
                     }while(cardNumToPut<0||cardNumToPut>=possibleCards.length);
                     cardNumToPut = possibleCards[cardNumToPut];
                     // put the card
                     plsAry[cPID].putACard(status,deck,cardNumToPut);
                     while(true){
                        // check if there is more possible cards and ask if the user want to put
                        int[] secondaryPossibleCards = plsAry[cPID].getPossibleSecondaryCard(status,deck);
                        if(secondaryPossibleCards.length==0){break;}// If there is nothing to put, quit the turn.
                        
                        //If ther is somehing, ask again.
                        //show hand first
                        plsAry[cPID].showHand(deck);
                        //show the user how many card to put the user has.
                        println("You have " + secondaryPossibleCards.length +" more card"+((secondaryPossibleCards.length==0)?"":"s")+" to put!");
                        println("Please select the next card to put from following:");
                        //Show selection
                        for(int i=0;i<secondaryPossibleCards.length;i++){
                           println("\t\t["+i+"]   " + Card.showCardInfo(deck,secondaryPossibleCards[i]));
                        }
                        //last choice to quit
                        println("\t\t["+secondaryPossibleCards.length+"]  Quit. Put nothing.");
                        
                        print("Which card do you prefer to put? input a number inside of [] ");
                        // ask to input indicator, and convert to card num
                        int secondaryCardNumToPut = -1;
                        secondaryCardNumToPut = Integer.parseInt(kb.nextLine());
                        while(secondaryCardNumToPut<0||secondaryCardNumToPut>secondaryPossibleCards.length){
                           print("Wrong number. please retry: ");
                           secondaryCardNumToPut = Integer.parseInt(kb.nextLine());
                        }
                        if(secondaryPossibleCards.length!=secondaryCardNumToPut){
                           secondaryCardNumToPut = secondaryPossibleCards[secondaryCardNumToPut];
                           // put the card
                           plsAry[cPID].putACard(status,deck,secondaryCardNumToPut);
                           // feedback what the user chose.
                           //println("You put "+crdObj.showCardInfo(deck,secondaryCardNumToPut));
                        }else{
                           println("You quit to put.");
                           break;
                        }
                     }//while the player has any card to put. ends
                  }else{//if it is a com player, call a method to find best combination
                     //com player has some card to put
                     //determine the best combination of cards, and put the first card.
                     plsAry[cPID].putBestCombination(status,deck,possibleCards);
                     //keep putting as long as there is any card to put.
                     while(true){
                        int[] secondaryPossibleCards = plsAry[cPID].getPossibleSecondaryCard(status,deck);
                        if(secondaryPossibleCards.length==0){break;}
                        plsAry[cPID].putBestCombination(status,deck,secondaryPossibleCards);
                     }
                  }
               }
            }
            // winning check
            for(int i=0; i<plsAry.length;i++){
               if(plsAry[i].getHandSize() == 0){
                  println(plsAry[i].getName()+" has discarded all hand!\n");
                  isSomeoneWin=true;
               }
            }
            if(isSomeoneWin){break;}
            // burst check
            for(int i=0; i<plsAry.length;i++){
               int burst = plsAry[i].doesThePlayerBurst(i,status.getDraw(),maxHandSize);
               if(burst>=0){
                  println(plsAry[burst].getName()+" burst!");
                  isSomebodyBurst=true;
               }
            }
            if(isSomebodyBurst){break;}
            //set order array for the next turn. move current player to the end.
            if(status.getSkip()>0){
               for(int i=0;i<status.getSkip();i++){
                  status.goToTheNextPlayer();
               }
            }else{
               status.goToTheNextPlayer();
            }
            status.clearSkip();
            // Check how many deck remaining.
            Card.checkRemainingDeck(deck, status, plsAry);
         }//While(Turns) Ends
         
         String winner = plsAry[0].getName();
         //Determine the user won or lost. show messages.
         if(!giveUp){
            for(int i=0,j=1;Math.max(i,j)<plsAry.length;){
               if(plsAry[i].getHandSize()>plsAry[j].getHandSize()){
                  winner = plsAry[j].getName();
                  i++;
               }else{
                  winner = plsAry[i].getName();
                  j++;
               }
               
               
            }
            println("WINNER IS "+winner);
         }
         if((!giveUp)&&winner.equals(plsAry[0].getName())){//if user won
            println("\n Congratulations!  You won this game!\n");
            hisObj.writeRecord(true,plsAry[0].getName());
         }else if(giveUp){
            println("Quitting game is considered as a lost.");
            hisObj.writeRecord(false,plsAry[0].getName());
         }else{
            println("\nYou lost!\n");
            hisObj.writeRecord(false,plsAry[0].getName());
         }
         
         
         
      }else{//when the user selects other than g or h
         println("You input a wrong command!");
      }//If(view history or start game) ends
      
   }// main ends
   
   
   
   
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////Methods
   /**
    * <p>Creates a deck and return it as a multi-dimensional(2d) array of type String</p>
    * @return A two dimentional array of 108 elements which consist of card information in each.
   */
   public static String[][] createDeck(){// Declaring the Deck Starts
      // individual cards consist of contents: type, color, num, isUsed(flag to check the card is )
      String[][] deck = new String[108][4];
      for(int i=0;i<deck.length;i++){// set type
         if(i<4){
            deck[i][0] = "WD4";
         }else if(i<8){
            deck[i][0] = "WC";
         }else if(i<16){
            deck[i][0] = "skip";
         }else if(i<24){
            deck[i][0] = "rev";
         }else if(i<32){
            deck[i][0] = "draw";
         }else{
            deck[i][0] = "num";
         }
      }
      for(int i=0;i<deck.length;i++){// set color
         if(i<8){
            deck[i][1] = "none";
         }else if(i<32){
            if(i%8<2){
               deck[i][1] = "red";
            }else if(i%8<4){
               deck[i][1] = "blue";
            }else if(i%8<6){
               deck[i][1] = "yellow";
            }else{
               deck[i][1] = "green";
            }
         }else{
            if(i<51){
               deck[i][1] = "red";
            }else if(i<70){
               deck[i][1] = "blue";
            }else if(i<89){
               deck[i][1] = "yellow";
            }else{
               deck[i][1] = "green";
            }
         }
      }
      for(int i=0,j=0;i<deck.length;i++){// set number
         if(i<32){
            deck[i][2] = "none";
         }else{
            if(j<1){
               deck[i][2] = "0";
               j++;
            }else{
               deck[i][2] = "" + ((j+1)/2);
               j++;
               if(j>18){
                  j=0;
               }
            }
         }
      }
      for(int i=0;i<deck.length;i++){// set isUsed
         deck[i][3] = "false";
      }
      return deck;
   }
   /**
      Creates player objects and returns an array of players.<br>
      e.g.<br>
      {"user", "com", "com2"}
      @param userName The user name which the user input
      @param numOfPls Number of players including the user
      @return Player[][] A two dimensional array of Player
   */
   public static Player[] createPlsAry(String userName,int numOfPls){
      Player[] playersAry = new Player[numOfPls];
      for(int i=0; i<numOfPls; i++){
         if( i == 0){
            playersAry[i] = new Player("user",userName);
         }else{
            playersAry[i] = new Player(("com" + i),("com" + i));
         }
      }
      return playersAry;
   }
   
   /**
    * Distrbute 7 cards for each players.
    * @param plsAry Created players array by createPlsAry().
    * @param deck The deck array, to retrieve and handle information of cards
   */
   public static void initialDistribution(Player[] plsAry,String[][] deck){
      //Distribute seven cards to all players
      Random rand = new Random();
      for(int i=0;i<plsAry.length;i++){
         ArrayList<Integer> cards = new ArrayList<Integer>(0);
         int newCardNum;
         for(int j=0;j<7;j++){
            do{
               newCardNum = rand.nextInt(108);
            }while(deck[newCardNum][3].equals("true"));
            cards.add(newCardNum);
            deck[newCardNum][3]="true";
         }
         plsAry[i].setInitialHand(cards);
         cards.clear();
      }
   }
   
   /**
    * Choose the first card on the top of the discard pile. Keep drawing until a card of number is drawn.
    * @param deck The deck information. Will be updated in this method.
    * @return An index(subscript) number of the drawn card
    */
   public static int drawOneCard(String[][] deck){
      //draw one card and change the card's status
      Random rand = new Random();
      int newCardNum;
      do{
         newCardNum = rand.nextInt(108);
      }while(deck[newCardNum][3].equals("true"));
      deck[newCardNum][3]="true";
      return newCardNum;
   }
   
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////Utility
   /**
      Utility. Only available for String output.
      @param str String you want to println.
   */
   public static void println(String str){
      System.out.println(str);
   }
   
   /**
      Utility. Only available for String output.
      @param str String you want to print.
   */
   public static void print(String str){
      System.out.print(str);
   }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////For Debug
   
   /**
    * For debug. Prints all deck information including what cards are used.
    * @param deck Deck information to print.
   */
   public static void showDeckInfo(String[][] deck){
      // show all deck status to debug
      println("--DEBUG--Deck------");
      for(int i=0;i<deck.length;i++){
         for(int j=0;j<deck[i].length;j++){
            print(deck[i][j]+"\t");
         }
            print("\n");
      }
      println("-------------------");
   }
   
   /**
    * For debug. Prints all players' information.
    * @param plsAry Players' information to print.
   */
   public static void showPlsInfo(Player[] plsAry){
      // show all players info to debug
      println("--DEBUG--Players---");
      for(int i=0;i<plsAry.length;i++){
         plsAry[i].showInfo();
      }
      println("-------------------");
   }
}
