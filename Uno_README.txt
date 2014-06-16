//////////////////////////////
///// UNO  README.txt ////////
//////////////////////////////
Date:    June 1, 2014
Author:  Yusuke Akiba
//////////////////////////////
1. Introduction
2. Rules
   a. Deck
   b. Special Cards & Wild Cards
   c. discard
   d. Win & Lose
   e. Quit the Game
   

1. Introduction
   
      Uno is a card game which is played with a deck of specially printed cards.
   This program emulates Uno with some special rules (house rules). Detailed
   rules are shown bellow. You can play with 1-5 computer players.
   Please compile the Uno.java file. All Uno.java, Player.java, Status.java,
   Card.java, UsersHistory.java, Validation.java, and usershistories.txt are
   necessary to play. Results of games will be recorded into userhistories.txt.
   
   
2. Rules
   
   a. Deck
   
      A deck consists of 108 cards of numbers from 0 to 9, special cards of Skip,
   Reverse, and Draw Two, and wild cards of wild and wild draw four. Except Wild
   cards, each card has a specific color of red, blue, yellow, green.
   In details, a deck consists of:
      
      i) Number cards of one 0 and two 1-9s for each 4 colors
         ((1+9*2)*4 = 76 cards)
      ii) Each 2 special cards of 3 types for 4 colors
         2*3*4 = 24 cards
      iii) Each 4 cards of Wild cards and Wild Draw Four cards
         4*2 = 8 cards
      
      76 + 24 + 8 = 108 cards in total.
      
      
   b. Special Cards & Wild Cards
      
      Skip ...          Next player in sequence loses a turn.
      
      Reverse ...       Order of play switches directions.
      
      Draw Two ...      Next player in sequence draws two cards and loses a turn.
                        You can escape to draw cards by discarding your card(s)
                        of Draw Two, or Wild Draw Four.
      
      Wild ...          Player declares next color to be played.
      
      Wild Draw Four ...Player declares next color to be played. Next player in
                        sequence draws four cards and loses a turn. You can
                        escape to draw cards by discarding Wild Draw Four or
                        color-matching draw two. In this case, the number of
                        cards to draw is added and the next player has to draw
                        cards.
                        
                        
   c. Discard
      
      In your turn, you have to have at least one card which matches the color
   or number/symbol of the card on the top of discard pile. If you do not have
   any available card, you have to draw one card and you lose the turn. After
   you put the initial card and if you have another available (depends on what
   card you put) you can put another available card. You could put the 3rd or
   more cards subsequently.
      
      
   d. Win & lose
      
      The most typical way to win this game is to discard all of your hand. The
   another way to win is to be a player which has the fewest cards in your hand
   when some one bursts. Burst occurs when someone has to have more than 15
   cards in your hand. If you and another computer player has same numbers of
   cards in your hand when someone occurs, you win.
   
   
   e. Quit the Game
   
      Every time you have a turn to play, you will be asked if you want to keep
   playing. If you want to quit the game, please input "q" at the time.
