import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ScoreDisplay extends JPanel implements Runnable{
	private Thread t=new Thread(this); 
	private GameClient gc = null;
	
	private ArrayList<JLabel> scores;
	private ArrayList<JLabel> names;
	private JPanel scoreBoard;
	private int numPlayers = 0;
	public ScoreDisplay(GameClient gc) {
		this.gc = gc;
		this.setLayout(new GridLayout(1,0));
		scoreBoard = new JPanel(new GridLayout(0,1));
		System.out.println(numPlayers);
		scoreBoard.setBackground(Color.WHITE);
		scoreBoard.setPreferredSize(new Dimension(200, 100));
		
		scores = new ArrayList<JLabel>();
		names = new ArrayList<JLabel>();
		
		this.add(scoreBoard);
	}
	public JPanel getPanel() {
		return this;
	}
	public void setNumPlayers(String[] names, int numPlayers) {
		this.numPlayers = numPlayers;
		for(int i=0;i<numPlayers;i++) {
			this.addToScorePanel(names[i]);
		}
		t.start();
		
	}
	public void addToScorePanel(String uname) {
		JPanel player1Panel = new JPanel();
		BufferedImage i1=null;
		try {
			i1 = ImageIO.read(new File("./rsc/char"+Integer.toString(names.size()+1)+".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JLabel player1 = new JLabel(new ImageIcon(i1));
		JLabel player1score = new JLabel("0");
		JLabel player1name = new JLabel(uname+": ");
		
		
		player1Panel.add(player1);
		player1Panel.add(player1name);
		player1Panel.add(player1score);
		player1Panel.setBackground(Color.GRAY);
		scores.add(player1score);
		names.add(player1name);
		scoreBoard.add(player1Panel);
		
		
	}
	public void run() {
		
		while(true) {
//			System.out.println("from sd: " + gc.getReceivedScore());
			try {
				t.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (gc.getReceivedScore() == true) {
				for(int i = 0;i<scores.size();i++) {
//					System.out.println("RUN"+gc.scores[i]);
					scores.get(i).setText(gc.scores[i]);
				}
				gc.setReceivedScore(false);
			}
					
		}
	}

}
