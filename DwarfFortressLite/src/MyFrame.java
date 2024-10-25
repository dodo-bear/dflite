import java.awt.*;
import javax.swing.*;

public class MyFrame extends JFrame {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
GridSquare[][] displayGrid = new GridSquare[32][32];
  char[][] graphicsGrid = new char[32][32];
  WorldGrid world;
  ControlHandler controls;
  GridLayout layout;
  JPanel mainPanel;
  JPanel paintPanel;
  JPanel textPanel;
  JPanel sidePanelLeft;
  JPanel sidePanelRight;
  JPanel[][] panelHolder;

  MyFrame(String title, WorldGrid world) {
	  
	//Window setup, bootleg fullscreen
    setTitle(title);
    this.world = world;
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    setSize ((int)size.getWidth(), (int)size.getHeight());
    setLocationRelativeTo(null);
    
    //create layout for the world grid

    layout = new GridLayout(32, 32);
    panelHolder = new JPanel[32][32];
    
    //Container panel
    mainPanel = new JPanel();
    mainPanel.setBackground(Color.BLACK);
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

    //paintPanel is the world grid display, textPanel is a console box.
    sidePanelLeft = new JPanel();
    sidePanelLeft.setBackground(Color.BLACK);
    sidePanelLeft.setLayout(new BoxLayout(sidePanelLeft, BoxLayout.Y_AXIS));
    sidePanelLeft.setMaximumSize(new Dimension(380, 1000));
    
    sidePanelRight = new JPanel();
    sidePanelRight.setBackground(Color.MAGENTA);
    sidePanelRight.setLayout(new BoxLayout(sidePanelRight, BoxLayout.Y_AXIS));
    //sidePanelRight.add(new JLabel("HI"));
    sidePanelRight.setPreferredSize(new Dimension(1200, 600));
    
    paintPanel = new JPanel();
    textPanel = new JPanel();
    paintPanel.setPreferredSize(new Dimension(400, 400));
    paintPanel.setBackground(Color.BLACK);
    textPanel.setBackground(Color.BLACK);
    sidePanelLeft.add(paintPanel);
    sidePanelLeft.add(textPanel);
    
    //Generate the display grid
    for(int i = 0; i < 32; i ++){
      for(int j = 0; j < 32; j ++)  {
        panelHolder[i][j] = new JPanel();
        panelHolder[i][j].setBackground(Color.BLACK);
        panelHolder[i][j].setPreferredSize(new Dimension(10, 10));
        paintPanel.add(panelHolder[i][j]);
        panelHolder[i][j].add(new JLabel(""));
        
      }
    }
    
    paintPanel.setLayout(layout);
    mainPanel.add(sidePanelLeft);
    mainPanel.add(sidePanelRight);
    mainPanel.setFocusable(true);
    mainPanel.requestFocus();
    controls = new ControlHandler();
    mainPanel.addKeyListener(controls);
    this.add(mainPanel);
    mainPanel.setVisible(true);
    paintPanel.setVisible(true);
  }
  
  public long refreshGraphics(int startX, int startY){ //Tells all visible grid squares to attempt to recalculate their display info
	  long t = System.nanoTime();
	  GridSquare[][] displayGrid = new GridSquare[32][32];
	    int currentSquare = 0;
	    int curX = 0;
	      for (GridSquare[] gridSquareArray : displayGrid) {
	        int curY = 0;
	        for (int i = 0; i < gridSquareArray.length; i++) {
	          try{
	          displayGrid[curX][curY] = world.getGrid()[curX + startX][curY + startY];
	          displayGrid[curX][curY].refreshDisplayInfo();
	          JLabel curlabel = ((JLabel)panelHolder[curX][curY].getComponents()[0]);
	          GraphicsEntity currentGraphics = displayGrid[curX][curY].getGraphics();
	          
	          curlabel.setText(String.valueOf(currentGraphics.getDisplayChar()));
	          curlabel.setForeground(currentGraphics.getDisplayColor());
	          
	          currentSquare++;
	          curY++;
	          }catch(Exception e){
	            System.out.println("Error in refreshGraphics at square " + currentSquare + ": ");
	            e.printStackTrace();
	          }
	        }
	        curX++;
	      }
	     return System.nanoTime() - t;
  }
  public ControlHandler getControls(){
    return controls;
  }
}