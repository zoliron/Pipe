package com.zoliron.games.pipe;

import com.zoliron.games.pipe.model.*;
import com.zoliron.utils.Point;

import java.util.Arrays;
import java.util.List;



/**
 * The {@link PipeBoardParser}.
 *
 * @author Ronen Zolicha
 */
public class PipeBoardParser{



	/**
	 * Parse the specified board.
	 */
	public PipeBoard parse(String problem){
		List<String> rawBoard = Arrays.asList(problem.split("\n"));
		int rowCount = rawBoard.size();
		int columnCount = rawBoard.get(0).length();

		PipeCell[][] board = new PipeCell[rowCount][columnCount];
		for (int y = 0; y < rowCount; y++){
			for (int x = 0; x < columnCount; x++){
				char c = rawBoard.get(y).charAt(x);

				board[y][x] = parse(x, y, c);
			}
		}

		return new PipeBoard(board);
	}



	/**
	 * Parse the specified pipe cell.
	 */
	private PipeCell parse(int x, int y, char c){
		Point coordinates = new Point(x, y);

		switch (c){
			case 's':
				return new SourceCell(coordinates);
			case 'g':
				return new GoalCell(coordinates);
			case ' ':
				return new EmptyCell(coordinates);
			case '|':
				return new IPipeCell(coordinates, 0);
			case '-':
				return new IPipeCell(coordinates, 90);
			case 'L':
				return new LPipeCell(coordinates, 0);
			case 'F':
				return new LPipeCell(coordinates, 90);
			case '7':
				return new LPipeCell(coordinates, 180);
			case 'J':
				return new LPipeCell(coordinates, 270);
			default:
				throw new IllegalStateException("Unknown pipe cell type");
		}
	}



}
