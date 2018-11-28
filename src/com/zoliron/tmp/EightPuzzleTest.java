package com.zoliron.tmp;

import com.zoliron.games.eightpuzzle.EightPuzzle;
import com.zoliron.games.eightpuzzle.EightPuzzleSearchable;
import com.zoliron.games.eightpuzzle.EightPuzzleState;
import com.zoliron.searcher.Solution;
import com.zoliron.searcher.algorithms.AStar;



/**
 * @author Ronen Zolicha
 */
public class EightPuzzleTest{



	public static void main(String[] args) throws Exception{
		EightPuzzle puzzle = new EightPuzzle(new String[][]{{" ", "8", "7"}, {"6", "5", "4"}, {"3", "2", "1"}});
		EightPuzzle puzzle1 = new EightPuzzle(new String[][]{{"1", " ", "2"}, {"3", "4", "5"}, {"6", "7", "8"}});
		EightPuzzleSearchable searchable = new EightPuzzleSearchable(puzzle);

		Solution<EightPuzzleState> solution = new AStar<EightPuzzleState>().search(searchable);
		for (EightPuzzleState state : solution){
			System.out.println(state);
			System.out.println(state.getPuzzle());
			System.out.println();
		}
	}



}
