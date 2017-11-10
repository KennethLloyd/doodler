import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

public class UI {
	private static Text textArea;
	private String un;
	private Text chatArea;
	private Client client;
	/**
	 * Launch the application.
	 * @param args
	 * @wbp.parser.entryPoint
	 */
	public UI(String uname, Client client){
		this.un = uname;
		this.client= client;
		open();
	}
	public Client getClient(){
		return this.client;
	}
	/**
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		Display display = Display.getDefault();
		Shell shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		
		textArea = new Text(shell, SWT.BORDER);
		textArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode==SWT.CR){
					if(!textArea.getText().isEmpty()){
						String message = textArea.getText()+"\n";
						chatArea.append(un+": "+ message);
						client.send(message);
					}	
				}
			}
		});
		textArea.setBounds(316, 199, 108, 21);
		
		Canvas canvas = new Canvas(shell, SWT.NONE);
		canvas.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION_TEXT));
		canvas.setBounds(86, 21, 224, 178);
		
		List list = new List(shell, SWT.BORDER);
		list.setBounds(9, 21, 71, 102);
		
		ToolBar toolBar = new ToolBar(shell, SWT.FLAT | SWT.RIGHT);
		toolBar.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		toolBar.setBounds(86, 199, 224, 23);
		
		Button btnBlack = new Button(shell, SWT.NONE);
		btnBlack.setBounds(86, 197, 75, 25);
		btnBlack.setText("Black");
		
		Button btnRed = new Button(shell, SWT.NONE);
		btnRed.setBounds(157, 197, 82, 25);
		btnRed.setText("Red");
		
		Button btnRed_1 = new Button(shell, SWT.NONE);
		btnRed_1.setBounds(235, 197, 75, 25);
		btnRed_1.setText("Blue");
		
		chatArea = new Text(shell, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
		chatArea.setBounds(316, 21, 108, 178);

		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

}
