// Name: Yusuke Akiba
// Date: May 23, 2014
// Description: 

/////IMPORTS/////////////////////////////////////
/////DELETE_SLASHES_TO_ACTIVATE//////////////////
//import java.util.Scanner;
//import java.util.Random;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
//import java.util.ArrayList;
//import javax.swing.JOptionPane;
//import java.io.*; //Don't forget IOException!
/////////////////////////////////////////////////
/**
 * Class to validate user input by using regular expression.
 * Currently, only have method to validate and retrieve int by input.
 */
public class Validation{
   private static long nLong;
   private static int nInt;
   /**
      Check if the string has integer inside
   */
   private static boolean hasInt(String str){
      Pattern p = Pattern.compile("(-?\\d+)");
      Matcher m = p.matcher(str);
      if(m.find()&&m.group(0).length()<=11){//if not too long for type long, continue
         nLong = Long.parseLong(m.group(0));
         if(nLong<=(long)Integer.MAX_VALUE&&nLong>=(long)Integer.MIN_VALUE){// if surely int, continue
            nInt = (int)nLong;
            return true;
         }else{
            return false;
         }
      }else{
         return false;
      }
   }
   /**
      if the string has int, return only the int as an integer value.
      If not, return the instructed num (may as a flag)
   */
   public static int parseInt(String str,int num){
      return hasInt(str)?nInt:num;
   }
}