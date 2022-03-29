class FakeRandHeight {
   static int[] height = { 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 4, // sequence of height
         0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0 };
   int count; // number of times get() has been called

   public FakeRandHeight() {
      count = 0;
   }

   // return the next number in the sequence, recycle the sequence if needed
   public int get() {
      return height[count++ % 31];
   }

   // testing get()
   /*
    * public static void main(String[] args)
    * {
    * FakeRandHeight randHeight = new FakeRandHeight();
    * 
    * for (int i=0; i < 40; i++)
    * {
    * System.out.println(i + " " + randHeight.get());
    * }
    * }
    */
}