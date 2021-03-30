package src;

/** Поток вычисления выделенных ячеек для результирующей матрицы */
class Thread extends java.lang.Thread
{
	/** первая матрица */
	private final int[][] firstMatrix;
	/** вторая матрица */
	private final int[][] secondMatrix;
	/** вычисляемая матрица */
	private final int[][] resultMatrix;
	/** индекс начала счета */
	private final int firstIndex;
	/** индекс последней ячейки для расчета */
	private final int lastIndex;
	/** число слагаемых при вычислении ячейки */
	private final int sumLength;

	/**
	 * @param firstMatrix  первая матрица
	 * @param secondMatrix вторая матрица
	 * @param resultMatrix вычисляемая матрица
	 * @param firstIndex   индекс начала счета
	 * @param lastIndex    индекс последней ячейки для расчета
	 */
	public Thread(final int[][] firstMatrix,
								final int[][] secondMatrix,
								final int[][] resultMatrix,
								final int firstIndex,
								final int lastIndex)
	{
		this.firstMatrix = firstMatrix;
		this.secondMatrix = secondMatrix;
		this.resultMatrix = resultMatrix;
		this.firstIndex = firstIndex;
		this.lastIndex = lastIndex;

		sumLength = secondMatrix.length;
	}

	/**
	 * вычисление значения ячейки
	 * @param row строка
	 * @param col столбец
	 */
	private void count(final int row, final int col)
	{
		int sum = 0;
		for (int i = 0; i < sumLength; ++i)
			sum += firstMatrix[row][i] * secondMatrix[i][col];
		resultMatrix[row][col] = sum;
	}

	/** работа потока */
	@Override
	public void run()
	{
		System.out.printf("Thread %s started \n",getName());
		final int colCount = secondMatrix[0].length;
		for (int index = firstIndex; index < lastIndex; ++index)
			count(index / colCount, index % colCount);

		System.out.println("Thread " + getName() + " finished.");
	}
}
