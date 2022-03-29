import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/*

  Author: Joshua hartzfeld
  Email: jhartzfeld2020@my.fit.edu
  Course: cse2010
  Section: 14
  Description of this file: Skiplists

 */

/*
Node class for each node that goes inside the skiplist. they will have this structure
with pointers up, down, left, and right with data values of time and activity
*/
 class SkipNodes {
   public int time;
   public String activity;
   public SkipNodes up;
   public SkipNodes down;
   public SkipNodes left;
   public SkipNodes right;

   /*
    * a constructor specifically for the walls of the skiplist since they only have
    * a time value
    */
   public SkipNodes(int time) {
      this.time = time;
      this.activity = "";
      
   }

   /*
    * a alternate constructor for main nodes that have both a time value and an
    * activity value
    */
   public SkipNodes(int time, String activity) {
      this.time = time;
      this.activity = activity;
   }

   /*
    * returns the time
    */
   public int getTime() {
      return this.time;
   }

   /*
    * returns the activity
    */
   public String getActivity() {
      return this.activity;
   }
}

/*
 * constructor starts the list off with a single level woth no nodes and a
 * height of 0
 * extends the "random number generator"
 */
class SkipList extends FakeRandHeight {
   private SkipNodes head;
   private SkipNodes tail;
   public int totalheight;
   FakeRandHeight randHeight = new FakeRandHeight();
   public SkipList() {
      head = new SkipNodes(Integer.MIN_VALUE);
      tail = new SkipNodes(Integer.MAX_VALUE);

      head.right = tail;
      tail.left = head;
      totalheight = 0;
   }

   /*
    * standard get function and standard traversal returns the requested node
    */
   public SkipNodes get(int data) {
      SkipNodes curr = head;
      while (curr.down != null) {
         curr = curr.down;
         while (curr.right != null && curr.right.time <= data) {
            curr = curr.right;
         }
      }
      return curr;
   }

   /*
    * same thing as the get function but returns the activity of the node
    */
   public String getActivity(int data) {
      SkipNodes curr = head;
      while (curr.down != null) {
         curr = curr.down;
         while (curr.right != null && curr.right.time <= data) {
            curr = curr.right;
         }
      }
      return curr.getActivity();
   }

   /*
    * pretty much the same thing as the get function but a boolean version mostly
    * to check to see if a item exists
    */
   public boolean getCheck(int data) {
      SkipNodes curr = head;
      while (curr.down != null) {
         curr = curr.down;
         while (curr.right != null && curr.right.time <= data) {
            curr = curr.right;
         }
      }
      if (curr.time == data) {
         return true;
      }
      else {
         return false;
      }
   }

   /*
    * submap is a method that gets all values between 2 integers. in this case it
    * does exactly that and returns an arraylist of values between the 2 integers
    */
   public ArrayList<SkipNodes> subMap(int key1, int key2) {
      ArrayList<SkipNodes> events = new ArrayList<>();
      SkipNodes curr = head;
      while (curr.down != null) {
         curr = curr.down;
         while (curr.right != null && curr.right.time <= key1) {
            curr = curr.right;
         }
      }
      while (curr.right != null && curr.right.time <= key2) {
         curr = curr.right;
         events.add(curr);
         
      }
      return events;
   }

   /*
    * getactivities from day is straightforward it does simple string manipulation
    * to isolate MM and DD values in the integer
    * and then it its compared in a compareTo and if that compareto is 0 it is
    * saved in the arraylist and returned once finsihed iterating
    */
   public ArrayList<SkipNodes> getActivityFromDay(int day) {
      ArrayList<SkipNodes> events = new ArrayList<>();
      SkipNodes curr = head;

      String second3 = String.format("%04d",day);
      while (curr.down != null) {
         curr = curr.down;

      }
      while (curr.right != null) {
         Integer num = curr.getTime();
         String str = String.format("%08d", num);
         String first3 = str.substring(0, 4);
         if (second3.compareTo(first3) == 0) {
            events.add(curr);
         }
         curr = curr.right;
      }
      return events;
   }

   /*
    * getactivities prior is made to do one thing, it returns an arraylist of nodes
    * that coorespond between 2 thresholds
    * bottom thresh is the minimum value for the day and upperthresh is passed to
    * the method.
    * using string manipulation i was aboe to isolate the first MM and DD digits to
    * compare. if the node was inbetween the 2 values its saved in the list and
    * returned
    */
   public ArrayList<SkipNodes> getActivitesprior(Integer data) {
      ArrayList<SkipNodes> events = new ArrayList<>();
      SkipNodes curr = head;
      String str = String.format("%04d", data);
      String first3 = str.substring(0, 4);
      Integer num = Integer.parseInt(first3);
      num *= 1000;
      Integer bottomthresh = num;
      Integer upperthresh = data;
      //System.out.println(bottomthresh + " " + upperthresh);
      while (curr.down != null) {
         curr = curr.down;
      }
      while (curr.right != null) {
         if (curr.time >= bottomthresh && curr.time <= upperthresh) {
            events.add(curr);
         }
         curr = curr.right;
      }
      return events;
   }

   /* 
   remove method does its magic and removes the item "key" once its found. 
   since were woking with pointers and such i have to keep track of pointers to update. 
   thats what the arraylist is for, everytime the key is the next node over i save the node
   then after i go through everything i need too i then go through all the nodes in a for loop
   and mess with their pointers to effecively delete the node from the list. 
   */
   public boolean remove(int key) {
      ArrayList<SkipNodes> pointerstoupdate = new ArrayList<>();
      SkipNodes curr = this.head;
      // searching for all adjacent nodes that are pointing to the node that is to be deleted
      // if they match they will be put into a list otherwise normal skiplist search algorithm
      SkipNodes item = get(key);
      if (item.getTime() != key) {
         return false;
      }
      while (curr != null) {
         while (curr.right != null && curr.right.time < key) {
            curr = curr.right;
         }
         if (curr.right.time == key) {
            pointerstoupdate.add(curr);
         }
         curr = curr.down;
      }
      // goes through all the pointers i need to update to remove the key value from ALL lists
      for (int i = 0; i < pointerstoupdate.size(); i++) {
         SkipNodes nodetoupdate = pointerstoupdate.get(i);
         SkipNodes nodetodelete = nodetoupdate.right;

         nodetoupdate.right = nodetodelete.right;
         nodetodelete.right.left = nodetoupdate;

         nodetodelete.up = null;
         nodetodelete.down = null;
      }
      checkforEmptyLists();

      return true;
   }

   /*
    * checkforemptylists is exactly what it says it does, loops through each level
    * and counts up the nodes.
    * if a list has 0 nodes and the tolerance counter is surpassed it deletes the
    * list keeping only one empty list above
    */
   public boolean checkforEmptyLists() {
      SkipNodes curr = head;
      
      int tolerance = 0;
      while (curr.down != null) {
         int count = 0;
         while (curr.right != null) {
            curr = curr.right;
            count++;
         }
         if (count <= 1) {
            tolerance++;
            if (tolerance > 2) {
               head.right = null;
               tail.left = null;

               head = head.down;
               head.up = null;
               tail = tail.down;
               tail.up = null;
               totalheight--;
            }
         }
         curr = curr.down;
      }
      return false;
   }

   /*
    * this method cost me hours of my life but i figured it out. the put method has
    * been defeated
    * so first it gets a randheight number. then it finds p which uses the native
    * get function in the skiplist
    * then q is initialized as null
    * here is where the fun happens.
    * for each level of height we run the loop which first checks to see if we need
    * a new level
    * if true it calls newlevel()
    * if not we insert using insertafterabove
    * after that we set p to the closest node that has an up value so we can go to
    * the next level
    * after each loop is over you should have a proper skiplist
    */
   public boolean put(int key, String value) {
      int height = randHeight.get();
      SkipNodes p = get(key);
      SkipNodes q = null;
      for (int i = 0; i <= height; i++) {
         if (i >= totalheight) {
            newlevel();
         }
         q = insertAfterAbove(p, q, key, value);
         while (p.up == null) {
            p = p.left;
         }
         p = p.up;
      }
      return true;
   }

   /*
    * insert after above was a doosie to troubleshoot but it works.
    * first we make a new node with the key and value
    * then with p and q we write new pointers to put it in its proper place. which
    * will be after p
    * for the first level q is somewhat irrelivant but the beauty about this is
    * that we return newnode which in the
    * put method becomes q so each time we set q to be the prev node so for example
    * at the 2nd loop through q will be the below node
    */
   public SkipNodes insertAfterAbove(SkipNodes p, SkipNodes q, int key, String value) {
      SkipNodes newnode = new SkipNodes(key, value);
      newnode.left = p;
      newnode.right = p.right;
      newnode.down = q;

      p.right.left = newnode;
      p.right = newnode;
      if (q != null) {
         q.up = newnode;
      }
      return newnode;
   }
   
   /*
    * when the put method calls for a new level to be made here is where it
    * happens.
    * first skipnodes newhead and newtail are made then in the proper order incase
    * to not lose pointers
    * i rewrite the pointers to act as new heads and tails and since the top level
    * should always be empty
    * i dont have to worry about any extra nodes
    */
   private void newlevel() {
      SkipNodes newhead = new SkipNodes(Integer.MIN_VALUE);
      SkipNodes newtail = new SkipNodes(Integer.MAX_VALUE);

      newhead.right = newtail;
      newhead.down = head;
      
      newtail.left = newhead;
      newtail.down = tail;
      
      head.up = newhead;
      tail.up = newtail;
      
      head = newhead;
      tail = newtail;
      totalheight++;
   }

   /*
    * iterates and prints the entire skiplist with respective level indicators
    * (S#)...
    */
   public void print() {
      SkipNodes curr = this.head;
      int height = totalheight;
      while (curr != null) {
         SkipNodes first = curr;
         System.out.printf("(S%d) ", height--);
         first = first.right;
         while (first.right != null) {
            System.out.printf("%08d:%s ", first.time, first.activity);
            first = first.right;
         }
         curr = curr.down;
         System.out.println();
      }
   }
}

public class HW5 {

   /*
    * This is where the magic happens. Input is taken here and depending on the first token on each line it is 
    * executed with its appropriate skiplist method. each switch case will execute its instructions and output in the same case
    */
   public static void main(String[] args) throws FileNotFoundException {
      SkipList Skiplist = new SkipList();

      File inputFile = new File(args[0]);
      Scanner fileReader = new Scanner(inputFile);
      while (fileReader.hasNextLine()) {
         String line = fileReader.nextLine();
         Scanner lineReader = new Scanner(line);
         String input = lineReader.next();
         int key = 0;
         String value = "";
         int startTime = 0;
         int endTime = 0;
         switch (input) {
            case "AddActivity":
               key = lineReader.nextInt();
               value = lineReader.next();
               Boolean check = Skiplist.getCheck(key);
               if (check == true) {
                  System.out.printf("AddActivity %08d %s ExistingActivityError:%s%n", key, value,
                        Skiplist.getActivity(key));
               } else {
                  Skiplist.put(key, value);
                  System.out.printf("AddActivity %08d %s%n", key, value);
               }
               break;

            case "RemoveActivity":
               key = lineReader.nextInt();
               String activity = Skiplist.getActivity(key);
               Boolean tf = Skiplist.remove(key);
               if (tf == false) {
                  System.out.printf("RemoveActivity %08d NoActivtyError%n", key);
               } else {
                  System.out.printf("RemoveActicty %08d %s%n", key, activity);
               }
               break;

            case "GetActivity":
               key = lineReader.nextInt();
               Boolean check2 = Skiplist.getCheck(key);
               if (check2 == true) {
                  System.out.printf("GetActivity %08d %s%n", key, Skiplist.getActivity(key));
               } else {
                  System.out.printf("GetActivity %08d none%n", key);
               }
               break;

            case "GetActivitiesBetweenTimes":
               startTime = lineReader.nextInt();
               endTime = lineReader.nextInt();
               ArrayList<SkipNodes> events = new ArrayList<>(Skiplist.subMap(startTime, endTime));

               if (events.size() == 0) {
                  System.out.printf("GetActivitiesBetweenTimes %08d %08d none%n", startTime, endTime);
               } else {
                  System.out.printf("GetActivitiesBetweenTimes %08d %08d ", startTime, endTime);
                  for (int i = 0; i < events.size(); i++) {
                     System.out.printf("%08d:%s ", events.get(i).time, events.get(i).activity);
                  }
                  System.out.println();
               }
               break;

            case "GetActivitiesForOneDay":
               int day = lineReader.nextInt();
               ArrayList<SkipNodes> dayevents = new ArrayList<>(Skiplist.getActivityFromDay(day));
               if (dayevents.size() == 0) {
                  System.out.printf("GetActivitiesForOneDay %04d none%n", day);
               } else {
                  System.out.printf("GetActivitiesForOneDay %04d ", day);
                  for (int i = 0; i < dayevents.size(); i++) {
                     System.out.printf("%08d:%s ", dayevents.get(i).time, dayevents.get(i).activity);
                  }
                  System.out.println();
               }
               break;

            case "GetActivitiesFromEarlierInTheDay":
               Integer daykey = lineReader.nextInt();
               ArrayList<SkipNodes> priorevents = new ArrayList<>(Skiplist.getActivitesprior(daykey));

               if (priorevents.size() == 0) {
                  System.out.printf("GetActivitiesFromEarlierInTheDay %08d none%n", daykey);
               } else {
                  System.out.printf("GetActivitiesFromEarlierInTheDay %08d ", daykey);
                  for (int i = 0; i < priorevents.size(); i++) {
                     System.out.printf("%08d:%s ", priorevents.get(i).time, priorevents.get(i).activity);
                  }
                  System.out.println();
               }
               break;

            case "PrintSkipList":
               System.out.println("PrintSkipList");
               Skiplist.print();
            break;
         }
      }
   }
}
   
