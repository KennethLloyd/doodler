import javax.swing.JLabel;
import javax.swing.JPanel;

public class WordDisplay extends JPanel implements Runnable{
	GameClient gc = null;
	private JLabel answer = null;
	Thread t=new Thread(this); 
	
	public WordDisplay(GameClient gc) {
		// TODO Auto-generated constructor stub
		this.gc = gc;
		answer = new JLabel();
		this.add(answer);
		t.start();
	}

	@Override
	public void run() {

		while(true) {
			if(gc.isTurn) {
				answer.setText(gc.getWord());
			}else {
				answer.setText("_____");
			}
		}
		
		
	}
	
	public Thread getThread() {
		return t;
	}

}
