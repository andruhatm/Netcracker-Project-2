package src;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static src.Main.*;

public class MainTest {
	final int[][] firstMatrix  = new int[3][3];
	final int[][] secondMatrix = new int[3][3];
	private int[][] result = new int[firstMatrix.length][secondMatrix[0].length];
	private int[][] result1 = new int[firstMatrix.length][secondMatrix[0].length];

	@Before
	public void setUp() {
		randomMatrix(firstMatrix);
		randomMatrix(secondMatrix);
	}

	@Test
	public void setProcessorsRightArgsTest() {
		String[] args = {"6"};
		int threadsCount = setProcessors(args);
		Assert.assertEquals(threadsCount,6);
	}

	@Test
	public void setProcessorsWrongArgsTest() {
		String[] args = {"df"};
		int threadsCount = setProcessors(args);
		Assert.assertEquals(threadsCount,8);
	}

	@Test
	public void multiplyByThreads() {

		for (int row = 0; row < result1.length; ++row) {
			for (int col = 0; col < result1[0].length; ++col) {
				int sum = 0;
				for (int i = 0; i < secondMatrix.length; ++i) {
					sum += firstMatrix[row][i] * secondMatrix[i][col];
				}
				result1[row][col] = sum;
			}
		}

		result = threadMultiply(firstMatrix,secondMatrix,6);

		for (int row = 0; row < result.length; ++row)
			for (int cols = 0; cols < result[0].length; ++cols)
				Assert.assertEquals(result[row][cols],result1[row][cols]);
	}
}
