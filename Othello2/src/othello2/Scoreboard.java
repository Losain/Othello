
/**
 * CSIS 2410
 * @author Nathan Clawson 
 * @author Amanda Rabideau
 * 
 * 
 */

package othello2;

import java.awt.Color;

import javax.swing.JButton;

public class Scoreboard
{
	//Method for altering the gui to show colored stones for each color, or whatever. 
	
	public int blackTokenCount(JButton[] JBArray) {
		int blackCount = 0;

		for (JButton ele : JBArray) {
			if(ele.getBackground().equals(Color.BLACK))
				blackCount ++;
		}
		return blackCount;
	}
	
	public int whiteTokenCount(JButton[] JBArray) {
		int whiteCount = 0;
		
		for(JButton ele : JBArray)
			if(ele.getBackground().equals(Color.WHITE))
				whiteCount++;
		
		return whiteCount;
	}
	
	public int resetScores() {
		int scores = 0;
		return scores;
	}
}
