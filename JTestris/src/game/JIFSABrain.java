package game;
import java.awt.Point;
import java.util.Scanner;

import org.jLOAF.Tetris.TetrisAgent;
import org.jLOAF.casebase.CaseBase;

public class JIFSABrain implements Brain {

	private TetrisAgent m_agent;

	
	public JIFSABrain(){
		super();
		String cb = "passive.cb";
		
		CaseBase casebase = CaseBase.load(cb);
		
		this.m_agent = new TetrisAgent();
		m_agent.train(casebase);
	}
	
	public Brain.Move bestMove(Board board, Piece piece, int limitHeight, Brain.Move move) {
		
		//parse the piece data
		double[][] pieceMat = new double[4][4];
		for(int ii=0; ii< pieceMat.length; ii++){
			for(int jj=0; jj<pieceMat[0].length; jj++){
				pieceMat[ii][jj] = 0.0;
			}
		}
		
		Point[] p_val = piece.getBody();
		if(p_val.length != 4){
			System.out.println("Problem with the piece...");
		}
		for(int ii=0;ii<p_val.length;ii++){
			pieceMat[p_val[ii].x][p_val[ii].y] = 1.0;
		}
		
		//now the board
		double[][] boardMat = new double[20][10];
		
		Scanner scn = new Scanner(board.toString());
		scn.useDelimiter(",");
		int cnt = 0;
		while(scn.hasNext()){
			String currRow = scn.next();
			if(cnt > 3){
				Scanner s2 = new Scanner(currRow);
				s2.useDelimiter("-");
				for(int ii=0;ii<10;ii++){
					boardMat[cnt-4][ii] =  new Double(s2.next());
				}
			}
			cnt++;
		}
	
		String control = this.m_agent.go(boardMat, pieceMat);

		move = new Brain.Move();
		
		Scanner s = new Scanner(control);
		s.useDelimiter(",");
	
		move.x = Integer.parseInt(s.next());
		move.y = Integer.parseInt(s.next());
		move.rots = Integer.parseInt(s.next());
		move.score = Double.parseDouble(s.next());
		
		
				
		return move;
	}
	

}
