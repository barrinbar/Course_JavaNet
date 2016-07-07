package JavaNet;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
public class ParallelQuickSort
{ static String s = "";
  static final int N = 1000;
  public static void main(String[] args)
  { double[] list1 = new double[N];
    for (int i = 0; i < list1.length; i++)
      list1[i] = (int)(Math.random() * (N));
    for (int numberOfThreads=1; numberOfThreads<=4; numberOfThreads++)
    { long startTime = System.currentTimeMillis();
      double[] list = list1.clone();
      quickSort(numberOfThreads, list);
      long endTime = System.currentTimeMillis();
      System.out.println("Time with " + numberOfThreads 
    		  + " threads: " + (endTime - startTime)+ "\n");
      System.out.println("array after " 
    		  + numberOfThreads + " multi threads quicksort\n");
      for (int i=0; i < N; i++)
      { System.out.print(list[i] + " ");
        if ((i+1)%20 == 0) System.out.println();
      }
      System.out.println("\n" + N 
    		  + " integers were sorted\n" + s);
    } 
  }
  public static double[] quickSort(int numberOfThreads, 
		  double[] list)
  { ThreadPoolExecutor executor = 
      new ThreadPoolExecutor(numberOfThreads, numberOfThreads, N,
        TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(N));    
    // ex
    System.out.println("active count " + executor.getActiveCount());
    while (executor.getActiveCount() > 0)
    {  try   { Thread.sleep(10); }
       catch (InterruptedException ex) {  ex.printStackTrace(); } 
    }
    s= s + "active count " + executor.getActiveCount() + " ";
    return list;
  }
  static class Task implements Runnable
  { private int low, high;
    private double[] list;
    ExecutorService executor;
    Task(ExecutorService executor, double[] list, int low, int high)
    { this.executor = executor;
      this.list = list;
      this.low = low;
      this.high = high;
    }
    public void run()
    {  // ex
       // ex
       // ex 
       // ex
       // ex  
       // ex
       // ex
    }
  }
  /** Partition the array list[first..last] */
  private static int partition(double[] list, int first, int last)
  { double pivot = list[first]; // Choose the first element as the pivot
    int low = first + 1; // Index for forward search
    int high = last; // Index for backward search
    while (high > low)
    { // Search forward from left
      while (low <= high && list[low] <= pivot)
        low++;
      // Search backward from right
      while (low <= high && list[high] > pivot)
        high--;
      // Swap two elements in the list
      if (high > low)
      { double temp = list[high];
        list[high] = list[low];
        list[low] = temp;
      }
    }
    while (high > first && list[high] >= pivot)
      high--;
    // Swap pivot with list[high]
    if (pivot > list[high])
    { list[first] = list[high];
      list[high] = pivot;
      return high;
    }
    else
    { return first;
    }
  }
}
