package src;

import java.util.Random;

public class Main {

	/** Число строк первой матрицы. */
	final static int ROW1 = 10;
	/** Число столбцов первой матрицы. */
	final static int COLS1 = 10;
	/** Число строк второй матрицы (должно совпадать с числом столбцов первой матрицы). */
	final static int ROWS2 = COLS1;
	/** Число столбцов второй матрицы. */
	final static int COLS2 = 10;

	/**
	 * заполнение псевдослучайными числами
	 * @param matrix Заполняемая матрица.
	 */
	public static void randomMatrix(final int[][] matrix){
		final Random random = new Random();  // Генератор случайных чисел.

		for (int row = 0; row < matrix.length; ++row)           // Цикл по строкам матрицы.
			for (int col = 0; col < matrix[row].length; ++col)  // Цикл по столбцам матрицы.
				matrix[row][col] = random.nextInt(100);         // Случайное число от 0 до 100.
	}

	/**
	 * Производится выравнивание значений для лучшего восприятия.
	 * @param matrix Выводимая матрица.
	 */
	private static void printMatrix(final int[][] matrix){
		boolean hasNegative = false;  // Признак наличия в матрице отрицательных чисел.
		int     maxValue    = 0;      // Максимальное по модулю число в матрице.

		// Вычисляем максимальное по модулю число в матрице и проверяем на наличие отрицательных чисел.
		for (final int[] row : matrix) {  // Цикл по строкам матрицы.
			for (final int element : row) {  // Цикл по столбцам матрицы.
				int temp = element;
				if (element < 0) {
					hasNegative = true;
					temp = -temp;
				}
				if (temp > maxValue)
					maxValue = temp;
			}
		}

		// Вычисление длины позиции под число.
		int len = Integer.toString(maxValue).length() + 1;  // Одно знакоместо под разделитель (пробел).
		if (hasNegative)
			++len;  // Если есть отрицательные, добавляем знакоместо под минус.

		// Построение строки формата.
		final String formatString = "%" + len + "d";

		// Вывод элементов матрицы в файл.
		for (final int[] row : matrix) {  // Цикл по строкам матрицы.
			for (final int element : row)  // Цикл по столбцам матрицы.
				System.out.print(String.format(formatString, element));

			System.out.print("\n");  // Разделяем строки матрицы переводом строки.
		}
	}

	/**
	 * Вывод результата перемножения
	 *
	 * @param firstMatrix  первая матрица
	 * @param secondMatrix вторая матрица
	 * @param resultMatrix результирующая матрица
	 */
	private static void printResult(
					final int[][] firstMatrix,
					final int[][] secondMatrix,
					final int[][] resultMatrix
	){
			System.out.print("First matrix:\n");
			printMatrix(firstMatrix);

			System.out.print("\nSecond matrix:\n");
			printMatrix(secondMatrix);

			System.out.print("\nResult matrix:\n");
			printMatrix(resultMatrix);
	}

	/** Многопоточное умножение матриц.
	 *
	 * @param matrix1  первая матрица
	 * @param matrix2 вторая матрица
	 * @param threadCount число потоков
	 * @return результирующая матрица
	 */
	public static int[][] threadMultiply(
					final int[][] matrix1,
					final int[][] matrix2,
					int threadCount
	){

		int firstIndex = 0;
		final int rows = matrix1.length;
		final int cols = matrix2[0].length;
		final int[][] result = new int[rows][cols];

		final int cellsToOneThread = (rows * cols) / threadCount;
		final Thread[] threadsArray = new Thread[threadCount];

		// Создание и запуск потоков.
		for (int threadIndex = threadCount - 1; threadIndex >= 0; --threadIndex) {
			int lastIndex = firstIndex + cellsToOneThread;
			if (threadIndex == 0) {
				lastIndex = rows * cols;
			}
			threadsArray[threadIndex] = new Thread(matrix1, matrix2, result, firstIndex, lastIndex);
			threadsArray[threadIndex].start();
			firstIndex = lastIndex;
		}

		try {
			for (Thread thread : threadsArray)
				thread.join();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * вычисление кол-ва процессоров
	 * @param args аргументы командной строки
	 * @return кол-во процессоров для вычислений
	 */
	public static int setProcessors(String[] args){
		int threadsCount = Runtime.getRuntime().availableProcessors();
		if (args.length > 0) {
			try {
				threadsCount = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				return threadsCount;
			}
		}
		return threadsCount;
	}

	/**
	 * количество потоков устанавливается как аргумент командной строки,
	 * при ошибке берется количество доступных процессоров
	 * @param args кол-во потоков
	 */
	public static void main(String[] args)
	{
		int threadsCount = setProcessors(args);

		final int[][] firstMatrix  = new int[ROW1][COLS1];
		final int[][] secondMatrix = new int[ROWS2][COLS2];

		randomMatrix(firstMatrix);
		randomMatrix(secondMatrix);

		int[][] result = threadMultiply(firstMatrix, secondMatrix, threadsCount);
		printResult(firstMatrix, secondMatrix, result);
	}
}
