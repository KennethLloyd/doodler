import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Tutorial extends JFrame{
	private Container container;
	private Container mainPane;
	private JLabel label;
	private ArrayList<ImageIcon> ii=null;
	private int pointer = 0;
	public Tutorial(){
		super("Tutorial");
		this.setPreferredSize(new Dimension(800,700));
		this.setResizable(false);
		
		this.container = this.getContentPane();
		
		mainPane = new JPanel();
		mainPane.setLayout(new BorderLayout());
		ii = new ArrayList<ImageIcon>();
		
		
		JPanel buttonPane = new JPanel(new BorderLayout());
		JButton btnNext = new JButton(new ImageIcon("./rsc/next.png"));
		JButton btnPrev = new JButton(new ImageIcon("./rsc/prev.png"));
		btnNext.setBackground(Color.DARK_GRAY);
		btnPrev.setBackground(Color.DARK_GRAY);
		btnNext.setFocusPainted(false);
		btnPrev.setFocusPainted(false);
		btnPrev.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				if(pointer<=1){
					pointer=1;
					
				}
				pointer--;
				label.setIcon(ii.get(pointer));
			}
		});
		btnNext.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				if(pointer>=4){
					pointer=3;
				}
				pointer++;
				label.setIcon(ii.get(pointer));
			}
		});
		JLabel manual = new JLabel("MANUAL",JLabel.CENTER);
		manual.setFont(new Font("SERIF",Font.PLAIN,25));
		manual.setForeground(Color.CYAN);
		buttonPane.add(manual, BorderLayout.CENTER);
		buttonPane.add(btnNext,BorderLayout.EAST);
		buttonPane.add(btnPrev,BorderLayout.WEST);
		buttonPane.setBackground(Color.DARK_GRAY);
		buttonPane.setPreferredSize(new Dimension(200,30));
		mainPane.add(buttonPane,BorderLayout.SOUTH);
		
		setIcons();
		label = new JLabel("",ii.get(0), JLabel.CENTER);
		label.setPreferredSize(new Dimension(300,400));
		
		mainPane.setBackground(Color.BLACK);
		mainPane.add(label, BorderLayout.CENTER );
		
		
		this.container.add(mainPane);
		this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
		
		this.setVisible(true);
		this.pack();
	}
	private void setIcons() {
		for(int i = 0;i<5;i++){
			ImageIcon image = new ImageIcon("./rsc/tutorials/"+Integer.toString(i+1)+"v.jpg");
			ii.add(image);
		}
	}
}
