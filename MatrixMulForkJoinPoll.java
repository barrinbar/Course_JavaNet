package JavaNet;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class MatrixMulForkJoinPoll {
	
	/** Main method */
	public static void main(String[] args) {
		Runtime runtime = Runtime.getRuntime();
		int numOfThreads = runtime.availableProcessors();
		System.out.println("Number of Available Processors: " + numOfThreads);
		final int N = 10;
		// Create matrix1
		double[][] matrix1 = new double[N][N];
		for (int i = 0; i < matrix1.length; i++)
			for (int j = 0; j < matrix1[i].length; j++) {
				matrix1[i][j] = 1;
			}
		// Create matrix2
		double[][] matrix2 = new double[N][N];
		for (int i = 0; i < matrix1.length; i++)
			for (int j = 0; j < matrix1[i].length; j++) {
				matrix2[i][j] = 1;
			}

		// Multiply two matrices and print the result
		long startTime = System.currentTimeMillis();
		double[][] resultMatrix = multiplyMatrix(numOfThreads, matrix1, matrix2);
		long endTime = System.currentTimeMillis();
		System.out.println("\nTime with " + numOfThreads + " threads: " + (endTime - startTime) + " ms");
		System.out.println("\nThe multiplication of the matrices is ");
		printResult(matrix1, matrix2, resultMatrix, '*');
	}

	/** The method for multiplying two matrices */
	public static double[][] multiplyMatrix(int numberOfThreads, double[][] m1, double[][] m2) {
		double[][] result = new double[m1.length][m2[0].length];

		// Create fork join pool
		ForkJoinPool pool = new ForkJoinPool();

		RecursiveAction task = new MulTask(numberOfThreads, m1, m2, result);
		pool.invoke(task);
		
		// Shut down the pool
		pool.shutdown();
		return result;
	}

	private static class MulTask extends RecursiveAction{
		private static final long serialVersionUID = 1L;
		private int numOfThreads;
		private double[][] m1;
		private double[][] m2;
		private double[][] result;

		MulTask(int numOfThreads, double[][] m1, double[][] m2, double[][] result) {
			this.numOfThreads = numOfThreads;
			this.m1 = m1;
			this.m2 = m2;
			this.result = result;
		}

		@Override
		protected void compute() {
			int quota = m1.length / numOfThreads;
			int i;
			RecursiveAction[] tasks = new MulQuota[numOfThreads+1];
		      for (i = 0; i < numOfThreads; i++)
		        tasks[i] = new MulQuota(i * quota, (i * quota) + quota);
		      
		      // Handle leftovers
		      tasks[numOfThreads] = new MulQuota(i * quota, m1.length);
		      
		      // Run all tasks
		      invokeAll(tasks);
		}
		
		public class MulQuota extends RecursiveAction{
			private static final long serialVersionUID = 1L;
			private int startRow, endRow;
			
			public MulQuota(int startRow, int endRow) {
				this.startRow = startRow;
				this.endRow = endRow;
			}

			@Override
			protected void compute() {
				for (int i = startRow; i < endRow; i++)
					for (int j = 0; j < result.length; j++)
						for (int k = 0; k < result[0].length; k++)
							result[i][j] += m1[i][k] * m2[k][j];
			}
		}
}
	
	/** Print result */
	public static void printResult(double[][] m1, double[][] m2, double[][] m3, char op) {
		for (int i = 0; i < m1.length; i++) {
			for (int j = 0; j < m1[0].length; j++)
				System.out.print(" " + m1[i][j]);
			if (i == m1.length / 2)
				System.out.print("  " + op + "  ");
			else
				System.out.print("     ");
			for (int j = 0; j < m2[0].length; j++)
				System.out.print(" " + m2[i][j]);
			if (i == m1.length / 2)
				System.out.print("  =  ");
			else
				System.out.print("     ");
			for (int j = 0; j < m3[0].length; j++)
				System.out.print(" " + m3[i][j]);
			System.out.println();
		}
	}
}
