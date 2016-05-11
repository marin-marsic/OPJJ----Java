package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

/**
 * Program that provides support for creating fractals derived from
 * Newton-Raphson iteration.
 * 
 * @author Marin Maršić
 *
 */
public class Newton {

	static ComplexRootedPolynomial rootedPolynomial;
	static ExecutorService pool = Executors.newFixedThreadPool(Runtime
			.getRuntime().availableProcessors() * 8, new ThreadFactory() {

		@Override
		public Thread newThread(Runnable r) {
			Thread thread = Executors.defaultThreadFactory().newThread(r);
			thread.setDaemon(true);
			return thread;
		}
	});

	/**
	 * Method that executes once the program starts.
	 * 
	 * @param args
	 *            Command line arguments. Not needed here.
	 */
	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		System.out
				.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
		System.out
				.println("Please enter at least two roots, one root per line. Enter 'done' when done.");

		int index = 1;
		ArrayList<Complex> complexInputList = new ArrayList<Complex>();

		// READING USER INPUT
		while (true) {
			System.out.printf("Root %d> ", index);
			String line = scanner.nextLine();
			line = line.trim();

			if (line.isEmpty()) {
				continue;
			}

			if (line.equalsIgnoreCase("done")) {
				System.out
						.println("Image of fractal will appear shortly. Thank you.");
				break;
			}

			Complex complex;
			try {
				complex = parse(line);
			} catch (Exception e) {
				System.out.println("Neispravan format.");
				continue;
			}

			index++;
			complexInputList.add(complex);
		}

		// CREATE POLYNOMIAL BASED ON THE GIVEN ROOTS
		Complex[] complexInputArray = new Complex[complexInputList.size()];
		for (int i = 0; i < complexInputList.size(); i++) {
			complexInputArray[i] = complexInputList.get(i);
		}
		rootedPolynomial = new ComplexRootedPolynomial(complexInputArray);

		// FractalViewer.show(getSequentialFractalproducer());
		FractalViewer.show(getParallelFractalproducer());

		scanner.close();
	}

	/**
	 * Method for parallel calculation of the fractals derived from
	 * Newton-Raphson iteration on multiple threads. Stores only data for
	 * specified range of y-coordinates in the array (the rest of the array
	 * remains unchanged).
	 * 
	 * @param reMin
	 *            Minimal value on real axis.
	 * @param reMax
	 *            Maximal value on real axis.
	 * @param imMin
	 *            Minimal value on imaginary axis.
	 * @param imMax
	 *            Maximal value on imaginary axis.
	 * @param width
	 *            Width of the screen that shows fractals.
	 * @param height
	 *            Height of the screen that shows fractals.
	 * @param m
	 *            Number of iterations.
	 * @param ymin
	 *            Value on y-axis to start filling the data array from.
	 *            (included)
	 * @param ymax
	 *            Value on y-axis to stop filling the data array from.
	 *            (included)
	 * @param data
	 *            Array to store the result into.
	 * @param pool
	 *            ExecutorService pool used for parallelization.
	 */
	private static void parallelCalculate(double reMin, double reMax,
			double imMin, double imMax, int width, int height, int m, int ymin,
			int ymax, short[] data, ExecutorService pool) {

		/**
		 * Class which represents one specific job that needs to be executed
		 * parallel to the other ones.
		 * 
		 * @author Marin Maršić
		 *
		 */
		class Job implements Callable<Integer> {
			/**
			 * Value on y-axis to start filling the data array from. (included)
			 */
			int fromY;
			/**
			 * Value on y-axis to stop filling the data array from. (included)
			 */
			int toY;

			/**
			 * Job constructor.
			 * 
			 * @param fromY
			 *            Value on y-axis to start filling the data array from.
			 *            (included)
			 * @param toY
			 *            Value on y-axis to stop filling the data array from.
			 *            (included)
			 */
			public Job(int fromY, int toY) {
				super();
				this.fromY = fromY;
				this.toY = toY;
			}

			@Override
			public Integer call() throws Exception {
				int offset = fromY * width;
				for (int y = fromY; y <= toY; y++) {
					for (int x = 0; x < width; x++) {
						double cre = x * (reMax - reMin) / (width - 1) + reMin;
						double cim = (height - 1 - y) * (imMax - imMin)
								/ (height - 1) + imMin;
						Complex c = new Complex(cre, cim);
						Complex zn = c;

						Complex zn1;
						int iters = 0;
						do {
							zn1 = zn.sub(rootedPolynomial.apply(zn).divide(
									rootedPolynomial.toComplexPolynom()
											.derive().apply(zn)));
							zn = zn1;
							iters++;
						} while (iters < m && zn1.sub(zn).module() < 0.002);
						int index = rootedPolynomial.indexOfClosestRootFor(zn1,
								0.001);
						if (index == -1) {
							data[offset++] = 1;
						} else {
							data[offset++] = (short) (index + 1);
						}
					}
				}
				return 0;
			}
		}

		List<Future<Integer>> results = new ArrayList<>();
		int numberOfThreads = Runtime.getRuntime().availableProcessors() * 8;
		int subjobWidth = (ymax - ymin) / numberOfThreads;
		for (int i = 0; i < numberOfThreads; i++) {
			int fromY;
			int toY;
			if (i < numberOfThreads - 1) {
				fromY = ymin + i * subjobWidth;
				toY = fromY + subjobWidth - 1;
			} else {
				fromY = ymin + i * subjobWidth;
				toY = ymax;
			}
			results.add(pool.submit(new Job(fromY, toY)));
		}

		for (Future<Integer> f : results) {
			while (true) {
				try {
					f.get();
					break;
				} catch (InterruptedException | ExecutionException e) {
				}
			}
		}
	}

	/**
	 *  
	 * @return Returns an object which generates fractals derived from
	 *         Newton-Raphson iteration parallel.
	 */
	private static IFractalProducer getParallelFractalproducer() {
		return new IFractalProducer() {

			@Override
			public void produce(double reMin, double reMax, double imMin,
					double imMax, int width, int height, long requestNo,
					IFractalResultObserver observer) {
				System.out.println("Započinjem izračune...");
				int m = 64;
				short[] data = new short[width * height];
				parallelCalculate(reMin, reMax, imMin, imMax, width, height, m,
						0, height - 1, data, pool);
				System.out.println("Izračuni gotovi...");
				observer.acceptResult(data, (short) (rootedPolynomial
						.toComplexPolynom().order() + 1), requestNo);
				System.out.println("Dojava gotova...");
			}

		};
	}

	/**
	 * Method for parsing user input to complex number. If something's wrong,
	 * {@link IllegalArgumentException} will be thrown.
	 * 
	 * @throws IllegalArgumentException
	 *             if the format of the complex number is not suitable for
	 *             parsing.
	 * @param s
	 *            String to parse.
	 * @return Returns complex number.
	 */
	private static Complex parse(String s) {
		String str = "";
		String[] separate = null;
		int realPositive = 1;
		int imaginaryPositive = 1;

		String sTrimmed = s.trim();

		// first number
		if (sTrimmed.charAt(0) == '+') {
			str = sTrimmed.substring(1);
		} else if (sTrimmed.charAt(0) == '-') {
			realPositive = -1;
			str = sTrimmed.substring(1);
		} else if (!Character.isDigit(s.charAt(0)) && s.charAt(0) != 'i') {
			throw new IllegalArgumentException("Illegal expression.");
		} else {
			str = sTrimmed;
		}

		// second number and the separation
		if (str.contains("+")) {
			separate = str.split("\\+");
		} else if (str.contains("-")) {
			imaginaryPositive = -1;
			separate = str.split("-");
		}

		String realString = "";
		String imaginaryString = "";

		// only one number given
		if (separate == null) {
			if (str.startsWith("i")) {
				realString = "0";
				imaginaryPositive = realPositive;
				realPositive = -1;
				imaginaryString = str.trim();
				imaginaryString = imaginaryString.substring(1);
				if (imaginaryString.equalsIgnoreCase("")) {
					imaginaryString = "1";
				}

			} else {
				realString = str.trim();
				imaginaryString = "0";
			}
		}
		// two numbers given
		else if (separate.length == 2 && separate[1].trim().startsWith("i")) {
			realString = separate[0].trim();
			imaginaryString = separate[1].trim();
			imaginaryString = imaginaryString.substring(1);
			if (imaginaryString.equalsIgnoreCase("")) {
				imaginaryString = "1";
			}
		} else {
			throw new IllegalArgumentException("Illegal expression.");
		}

		// try to parse
		try {
			double real = Double.parseDouble(realString);
			double imaginary = Double.parseDouble(imaginaryString);
			return new Complex(realPositive * real, imaginaryPositive
					* imaginary);
		} catch (Exception e) {
			throw new IllegalArgumentException("Illegal expression.");
		}
	}
}
