package JavaNet;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class Exercise29_I
{ static final int N = 1000000;
  static final int THRESHOLD = 4;
  static int numberOfTasks = 0;
  public static void main(String[] args)
  { double[] list = new double[N];
    for (int i = 0; i < list.length; i++)
      list[i] = (int)(Math.random() * (N));
      int size = (int)(Math.random() * (N+1)); 
      //size = N;
      int threshold = size / 
    	  (THRESHOLD * Runtime.getRuntime().availableProcessors());
      long startTime = System.currentTimeMillis();
      parallelQuickSort(list, 0, size-1, threshold);
      long endTime = System.currentTimeMillis();
      System.out.println("array after sort\n");
      for (int i=0; i < Math.min(100, size); i++)
      { System.out.print(list[i] + " ");
        if ((i+1)%20 == 0) System.out.println();
      }
      System.out.println("\n" + size  + " numbers were sorted\n");
      System.out.println("quick sort time " 
              + (endTime - startTime)+ "\n");
      System.out.println("number of processors are: "
    	      + Runtime.getRuntime().availableProcessors());
      System.out.println("number of generated tasks are: "
    	      + numberOfTasks);
      System.out.println("threshold:  " + threshold);
  }
  public static void parallelQuickSort(double[] list,
		  int first, int last, int threshold)
  { 
	SortTask mainTask = new SortTask(list, first, last);
	mainTask.setThreshold(threshold);
	ForkJoinPool pool = new ForkJoinPool();
	pool.invoke(mainTask);
  }
  private static class SortTask extends RecursiveAction
  { private static final long serialVersionUID = 1L;
    private double[] list;
    private int first;
    private int last;
    private int threshold;
    SortTask(double[] list, int first, int last)
    { this.list = list;
      this.first = first;
      this.last = last; 
    }
    protected void setThreshold(int threshold)
    { this.threshold = threshold; 
    }
    @Override
    protected void compute()
    {
    	if (last > first)
    		quickSort(list);
        else
        {
        	int temp = partition(list, first, last);

        	/*// Recursively sort the two halves
            new SortTask(list, first, temp - 1).fork();
            new SortTask(list, temp + 1, last).fork();*/

        	invokeAll(new SortTask(list, first, temp - 1),
        			new SortTask(list, temp + 1, last));
            numberOfTasks = numberOfTasks + 2;
        }
    }
  }
  /** Partition the array list[first..last] */
  private static int partition(double[] list, int first, int last)
  { double pivot = list[first]; // Choose the first element as the pivot
    int low = first + 1; // Index for forward search
    int high = last; // Index for backward search
    while (high > low) {       // Search forward from left
      while (low <= high && list[low] <= pivot)  low++;
      // Search backward from right
      while (low <= high && list[high] > pivot)  high--;
      // Swap two elements in the list
      if (high > low)           {
        double temp = list[high];
        list[high] = list[low];
        list[low] = temp;       }
                        }
    while (high > first && list[high] >= pivot)  high--;
    // Swap pivot with list[high]
    if (pivot > list[high])     {
      list[first] = list[high];
      list[high] = pivot;
      return high;              }
    else  return first; 
  }
  public static void quickSort(double[] list)
  { quickSort(list, 0, list.length - 1);
  }
  public static void quickSort(double[] list, int first, int last)
  { if (last > first)                         {
	int pivotIndex = partition(list, first, last);
	quickSort(list, first, pivotIndex - 1);
	quickSort(list, pivotIndex + 1, last);     }
  }
}  
