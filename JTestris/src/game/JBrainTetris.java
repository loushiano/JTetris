/* 
 * JBrainTetris.java 

 * 
 * Created on January 31, 2002, 10:58 AM 
 */  
  package game;
//package Hw2;   
import java.awt.*;  
import javax.swing.*;  
 
  
/** 
 * 
 * @author  Lews Therin 
 * @version  
 */  
public class JBrainTetris extends JTetris {  
    public Brain mBrain = new JIFSABrain();
    
    public static JTextArea publictext;
    
	//public Brain mBrain = new FunBrain();
    private Brain.Move mMove = new Brain.Move();  
    private int cur_count = -1;  
      
    /** Creates new JBrainTetris */  
    public JBrainTetris(int width, int height) {  
        super(width, height);  
    }  
    
    public void stopGame(){
    	//((JIFSABrain) mBrain).checkSave();
    	//System.out.println(super.count + "\t" + ((JIFSABrain) mBrain).newCases());
    	super.stopGame();
    	super.startButton.doClick();
    }
    
    public void tick(int verb) {  
        if(brainPlay.isSelected()) {  
            board.undo();  
            if(verb == DOWN) {  
                if(cur_count != super.count) {
                    mMove = mBrain.bestMove(board, currentPiece, board.getHeight()-TOP_SPACE, mMove); 
                    cur_count = super.count;  
                }  
                if(mMove == null ||  currentPiece == null) {  
                    stopGame();  
                    return; //game over   
                }
                
                if(mMove.rots > 0 ){
                	Point[] prev = currentPiece.getBody();
                	super.tick(ROTATE);
                	Point[] after = currentPiece.getBody();
                	Piece pv = new Piece(prev);
                	Piece af = new Piece(after);
                	if(!pv.equals(af)){
                		mMove.rots--;
                	}
                }
                
                /**
                if(!currentPiece.equals(mMove.piece)){
                	Point[] prev = currentPiece.getBody();
                	super.tick(ROTATE);
                	Point[] after = currentPiece.getBody();
                	Piece pv = new Piece(prev);
                	Piece af = new Piece(after);
                	if(!pv.equals(af)){
                		rotCount++;
                	}
                	
                }else{
                	if(rotCount != mMove.rots){
                		System.out.println("Unequal! Did: " + rotCount + "  thought: "+mMove.rots );
                	}
                }
  				*/
                if(currentX < mMove.x) super.tick(RIGHT);  
                if(currentX > mMove.x) super.tick(LEFT);  
  
            }  
        }  
        super.tick(verb);  
  
    }  
      
  
  
      
    // Controls   
        protected JSlider adversary;  
        protected JLabel  adStat;  
        protected JSlider happy;  
        protected JLabel  adHappy;  
        protected JCheckBox brainPlay;  
          
        protected String adversaryOn = "Malice Mode On >:-(";  
        protected String adversaryOff = "Malice Mode Off";  
        protected String happyOn = "Happy Mode On :-)";  
        protected String happyOff = "Happy Mode Off";  
          
          
    public java.awt.Container createControlPanel() {  
        java.awt.Container panel2 = Box.createVerticalBox();  
        panel2 = super.createControlPanel();  
          
          
          
        brainPlay = new JCheckBox("Use AI",false);  
        if(testMode) brainPlay.setSelected(true);  
        panel2.add(brainPlay);  
          
         
        
        JBrainTetris.publictext = new JTextArea();
        panel2.add(publictext);
        //JPanel row2 = new JPanel();  
          
         // ADVERSARY slider   
        //row2.add(Box.createVerticalStrut(12));  
        //row2.add(new JLabel("Adversary:"));  
  
        adversary = new JSlider(0, 100, 0); // min, max, current   
        adversary.setPreferredSize(new Dimension(100,15));  
        //row2.add(adversary);  
          
       // JPanel text = new JPanel();  
        adStat = new JLabel(adversaryOff);
        //text.add(adStat = new JLabel(adversaryOff));  
        //panel2.add(text);  
          
        //panel2.add(row2);  
          
        //JPanel row3 = new JPanel();  
          
         // Mr. Happy slider   
        //row3.add(Box.createVerticalStrut(12));  
        //row3.add(new JLabel("Mr. Happy:"));  
  
        happy = new JSlider(0, 100, 0); // min, max, current   
        happy.setPreferredSize(new Dimension(100,15));  
        //row3.add(happy);  
          
        //JPanel text2 = new JPanel();  
        adHappy = new JLabel(happyOff);
        //text2.add(adHappy = new JLabel(happyOff));  
        //panel2.add(text2);  
          
        //panel2.add(row3);  
          
        return(panel2);  
  
          
    }  
    public Piece pickNextPiece() {  
        if(adversary.getValue() == 0 && happy.getValue() == 0) {  
            adStat.setText(adversaryOff);  
            adHappy.setText(happyOff);  
            return(super.pickNextPiece()); // not to mess with the sequence of random numbers for test mode   
        }  
          
        if(adversary.getValue() != 0 && happy.getValue() != 0) {  
            adversary.setValue(0);  
            adversary.repaint();  
        }  
              
        if(random.nextInt(100) <= adversary.getValue()) {  
            adStat.setText(adversaryOn);  
            return getWorstPiece(true);  
        }  
        else  
        {  
            adStat.setText(adversaryOff);  
        }  
        if(random.nextInt(100) <= happy.getValue()) {  
            adHappy.setText(happyOn);  
            return getWorstPiece(false);  
        }  
        else  
        {  
            adHappy.setText(happyOff);  
        }  
        return(super.pickNextPiece());  
      
    }  
      
    private Piece getWorstPiece(boolean hurt_player) {  
        Brain.Move wMove = null;  
        Brain.Move tMove;  
        int index = 0;  
        for(int i = 0; i < pieces.length; i++) {  
            tMove = mBrain.bestMove(board, pieces[i], board.getHeight()-TOP_SPACE, null);  
            if(i == 0) wMove = tMove;  
            if(tMove == null) { // this piece loses the game now   
                return pieces[i];  
            }  
            if((hurt_player && tMove.score >= wMove.score) || (!hurt_player && tMove.score <= wMove.score)) {  
                wMove = tMove;  
                index = i;  
            }  
        }  
        return pieces[index];  
    }  
}  
