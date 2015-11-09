
import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JApplet;
import javax.swing.JTextField;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

//This class creates a simple user interface to use with FillingTester

public class SelfIntersectionAppletV10 extends JApplet implements ActionListener
{
	public static void main(String args[])
	{
	          Applet myApplet = new SelfIntersectionAppletV10();
	          myApplet.init();
	}
// hello world
	public int FirstTime;
	private JPanel topPanel = new JPanel();
	GraphPanel graphPanel = new GraphPanel();
	
	private String instructions =
			"This applet computes  minimal " +
				"number of intersections of a free homotopy "+ 
				"class on a surface with boundary and constructs a representative with this minimal intersection. \n"+
				"Enter a surface word and a reduced cyclic word in the letters of the surface word and hit 'return'. " +
				"  For a punctured torus or thrice-punctured sphere, a figure with the corners 'snipped' is drawn. For more complicated surfaces, a circle is drawn.  Each arc of the circle represents an edge of the corresponding fundamental polygon. \n Each arc of the circle is separated by a puncture, denoted by a dash."
				;
	private JLabel Jinstructions = new JLabel("<html>"+ instructions +"</html>"+"\n      \n       \n             ");
	private JLabel JTitle=new JLabel("Self-intersection Applet - by Chris Arettines");
	private JLabel JFiller1=new JLabel("       ");
	private JLabel JFiller2=new JLabel("       ");
	private static final int TEXT_FIELD_WIDTH = 20;
	private JPanel westPanel = new JPanel();
	private JPanel westNorthPanel = new JPanel();
	private JLabel swLabel = new JLabel("Surface Word:");
	private JTextField swTextField = new JTextField(TEXT_FIELD_WIDTH);
	private JLabel fcLabel = new JLabel("Reduced Cyclic Word:");
	private JTextField fcTextField = new JTextField(TEXT_FIELD_WIDTH);
	private JLabel min1Label = new JLabel("Min SI:");
	private JTextField min1TextField = new JTextField(TEXT_FIELD_WIDTH);
	private JPanel westSouthPanel = new JPanel();
	public int[][] PL;
	public int[][] SL2;
	public int[][] SL1;
	public int[][] SL;
	public String sw1;
	public String cw1;
	public int ia;
	public int ib;
	public int[] XCurveCoord;
	public int[] YCurveCoord;
	public int Size;
	public FillingTester C3;
	public String fill;
	public int[] usednum;
	public int letterholder;
	
	public void init()
	{	
		FirstTime=0;
		this.setSize(1000,650);
		layoutGUI();
	} 
	public void layoutGUI()
	{
		BorderLayout topPanelLayout = new BorderLayout();
		topPanel.setLayout(topPanelLayout);
		add(topPanel);
		topPanel.add(JTitle, BorderLayout.NORTH);
		topPanel.add(Jinstructions, BorderLayout.SOUTH);
		BorderLayout westLayout = new BorderLayout();
		westPanel.setLayout(westLayout);
		GridLayout westNorthLayout = new GridLayout(8,2);
		westNorthPanel.add(JFiller2);
		westNorthPanel.add(JFiller1);
		westNorthPanel.setLayout(westNorthLayout);
		westNorthPanel.add(swLabel);
		westNorthPanel.add(swTextField);
		westNorthPanel.add(fcLabel);
		westNorthPanel.add(fcTextField);
		westNorthPanel.add(min1Label);
		westNorthPanel.add(min1TextField);
		swTextField.addActionListener(this);
		fcTextField.addActionListener(this);
		GridLayout westSouthLayout = new GridLayout(1,2);
		westSouthPanel.setLayout(westSouthLayout);
		westPanel.add(westNorthPanel,BorderLayout.NORTH);
		westPanel.add(westSouthPanel,BorderLayout.CENTER);
		topPanel.add(westPanel, BorderLayout.WEST);
		topPanel.add(graphPanel, BorderLayout.CENTER);
	}
	class GraphPanel extends JPanel 
	{
		public void paintComponent(Graphics g) 
		{	
			if(sw1==null && cw1==null){return;}
			//change PicSize to make the picture bigger/smaller if you'd like.	
			int PicSize=400;		
			Size=400;	
			this.setSize(Size+50,Size+100);
			String c1=cw1.toLowerCase();
			int ia=0;
			int ib=0;
			XCurveCoord=new int[2*(cw1.length())];
			YCurveCoord=new int[2*(cw1.length())];
			for (int i=0;i<cw1.length();i++)
			{
				if (c1.charAt(i)=='a') ia++;
				if (c1.charAt(i)=='b') ib++;
			}	
			if(sw1.contentEquals("abAB")||sw1.contentEquals("aAbB"))
			{
				//initialize the coordinates for all the lines that will eventually be drawn
				if (sw1.contentEquals("abAB"))
				{
					for(int i=0;i<ia;i++)
					{
						XCurveCoord[i]=20;
						YCurveCoord[i]=(Size-40)*(ia-i)/(ia+1)+40;
						XCurveCoord[2*cw1.length()-ib-1-i]=Size+20;
						YCurveCoord[2*cw1.length()-ib-1-i]=(Size-40)*(ia-i)/(ia+1)+40;
		
					}
				    for(int j=0;j<ib;j++)
				    {
				    	YCurveCoord[j+ia]=20;
				    	XCurveCoord[j+ia]=(Size-40)*(j+1)/(ib+1)+40;
				    	YCurveCoord[2*cw1.length()-1-j]=Size+20;
				    	XCurveCoord[2*cw1.length()-1-j]=(Size-40)*(j+1)/(ib+1)+40;

				    }
				}
				if (sw1.contentEquals("aAbB"))
				{
					for(int i=0;i<ia;i++)
					{
						XCurveCoord[i]=20;
						YCurveCoord[i]=(Size-40)*(ia-i)/(ia+1)+40;
						XCurveCoord[ia+i]=(Size-40)*(i+1)/(ia+1)+40;
						YCurveCoord[ia+i]=20;
				    }
				    for(int j=0;j<ib;j++)
				    {
				    	XCurveCoord[2*cw1.length()-ib-1-j]=Size+20;
						YCurveCoord[2*cw1.length()-ib-1-j]=(Size-40)*(ib-j)/(ib+1)+40;
				    	YCurveCoord[2*cw1.length()-1-j]=Size+20;
				    	XCurveCoord[2*cw1.length()-1-j]=(Size-40)*(j+1)/(ib+1)+40;
				    }
				}
				
				g.drawString(""+sw1.charAt(0), 5, 20+Size/2);
				g.drawString(""+sw1.charAt(1), 20+Size/2, 15);
				g.drawString(""+sw1.charAt(2), Size+25, 20+Size/2);
				g.drawString(""+sw1.charAt(3), 20+Size/2, Size+35);				
				g.drawString(cw1+" , " + fill, 20, Size+50);
				g.drawString(""+C3.getMinInt()+" intersections total", 20, Size+70);
			    for(int k=0;k<cw1.length();k++)
			    {
					if(k>=cw1.length()){ g.setColor(Color.red);}
					g.drawLine(XCurveCoord[SL[k][0]],YCurveCoord[SL[k][0]],XCurveCoord[SL[k][1]],YCurveCoord[SL[k][1]]);
			    	int mpx=(XCurveCoord[SL[k][0]]+XCurveCoord[SL[k][1]])/2;
			    	int mpy=(YCurveCoord[SL[k][0]]+YCurveCoord[SL[k][1]])/2;
			    	double a=XCurveCoord[SL[k][0]];
			    	double b=YCurveCoord[SL[k][0]];
			    	double c=XCurveCoord[SL[k][1]];
			    	double d=YCurveCoord[SL[k][1]];
			    	double atheta=Math.toRadians(45);
			    	double ltheta=Math.atan((d-b)/(c-a));
			    	Double temp1=10*Math.sin(ltheta+Math.PI/2-atheta);
			    	Double temp2=10*Math.cos(ltheta+Math.PI/2-atheta);
		
			    if(XCurveCoord[SL[k][0]]==20 && ltheta>=0)
			    {
			    	g.drawLine(mpx,mpy,mpx-temp2.intValue(),mpy-temp1.intValue());
			    	g.drawLine(mpx,mpy,mpx-temp1.intValue(),mpy+temp2.intValue());
			    }
			    if(XCurveCoord[SL[k][0]]==20 && ltheta<0)
			    {
			    	g.drawLine(mpx,mpy,mpx-temp2.intValue(),mpy-temp1.intValue());
			    	g.drawLine(mpx,mpy,mpx-temp1.intValue(),mpy+temp2.intValue());
			    }
			    if(XCurveCoord[SL[k][0]]==Size+20 && ltheta>=0)
			    {
			    	g.drawLine(mpx,mpy,mpx+temp2.intValue(),mpy+temp1.intValue());
			    	g.drawLine(mpx,mpy,mpx+temp1.intValue(),mpy-temp2.intValue());
			    }
			    if(XCurveCoord[SL[k][0]]==Size+20 && ltheta<0)
			    {
			    	g.drawLine(mpx,mpy,mpx+temp2.intValue(),mpy+temp1.intValue());
			    	g.drawLine(mpx,mpy,mpx+temp1.intValue(),mpy-temp2.intValue());
			    }
			    if(YCurveCoord[SL[k][0]]==20 && ltheta>=0)
			    { 
			    	g.drawLine(mpx,mpy,mpx-temp2.intValue(),mpy-temp1.intValue());
			    	g.drawLine(mpx,mpy,mpx-temp1.intValue(),mpy+temp2.intValue());
			    }
			    if(YCurveCoord[SL[k][0]]==20 && ltheta<0)
			    { 
			    	g.drawLine(mpx,mpy,mpx+temp2.intValue(),mpy+temp1.intValue());
			    	g.drawLine(mpx,mpy,mpx+temp1.intValue(),mpy-temp2.intValue());
			    }
			    if(YCurveCoord[SL[k][0]]==Size+20 && ltheta>=0)
			    {
			    	g.drawLine(mpx,mpy,mpx+temp2.intValue(),mpy+temp1.intValue());
			    	g.drawLine(mpx,mpy,mpx+temp1.intValue(),mpy-temp2.intValue());
			    }
			    if(YCurveCoord[SL[k][0]]==Size+20 && ltheta<0)
			    {
			    	g.drawLine(mpx,mpy,mpx-temp2.intValue(),mpy-temp1.intValue());
			    	g.drawLine(mpx,mpy,mpx-temp1.intValue(),mpy+temp2.intValue());
			    }	
			   }
			    int[] XArray = {20, 40, PicSize, 20+PicSize, 20+PicSize, PicSize,     40, 20};
			    int[] YArray = {40, 20, 20,   40,      PicSize,    20+PicSize, PicSize+20,PicSize   };
			    g.setColor(Color.black);
			    g.drawPolygon (XArray, YArray, 8);	  
				g.fillOval(XCurveCoord[SL[0][0]]-4,YCurveCoord[SL[0][0]]-4,8,8);
			}	
			if(!(sw1.contentEquals("abAB")||sw1.contentEquals("aAbB")))
			{
				g.drawString(cw1+" , " + fill, 20, Size+50);
				g.drawString(""+C3.getMinInt()+" intersections total", 20, Size+70);
				//attempting to draw circle
				int[] Circle1X=new int[sw1.length()];
			    int[] Circle1Y=new int[sw1.length()];
				int[] Circle2X=new int[sw1.length()];
				int[] Circle2Y=new int[sw1.length()];
				int[] CircleDivX=new int[sw1.length()];
				int[] CircleDivY=new int[sw1.length()];
				for(int i=0;i<sw1.length();i++)
				{
					Circle1X[i]= 20+Math.round(Size/2+Math.round((Size/2-10)*Math.cos(Math.PI+2*Math.PI*i/(sw1.length()))));
				 	Circle1Y[i]= 20+Math.round(Size/2+Math.round((Size/2-10)*Math.sin(Math.PI+2*Math.PI*i/(sw1.length()))));
				   	Circle2X[i]= 20+Math.round(Size/2+Math.round((10+Size/2)*Math.cos(Math.PI+2*Math.PI*i/(sw1.length()))));
				   	Circle2Y[i]= 20+Math.round(Size/2+Math.round((10+Size/2)*Math.sin(Math.PI+2*Math.PI*i/(sw1.length()))));
				   	CircleDivX[i]=20+Math.round(Size/2+Math.round((Size/2)*Math.cos(Math.PI+2*Math.PI*i/(sw1.length()))));
			    	CircleDivY[i]=20+Math.round(Size/2+Math.round((Size/2)*Math.sin(Math.PI+2*Math.PI*i/(sw1.length()))));
				    g.drawString(""+sw1.charAt(i),20+Math.round(Size/2+Math.round((Size/2+10)*Math.cos(2*Math.PI/(2*sw1.length())+Math.PI+2*Math.PI*i/(sw1.length())))),20+Math.round(Size/2+Math.round((Size/2+10)*Math.sin(2*Math.PI/(2*sw1.length())+Math.PI+2*Math.PI*i/(sw1.length())))));  
				} 
				for(int i=0;i<sw1.length();i++)
				{	g.drawOval(20,20,Size,Size);
			    	g.drawLine(Circle1X[i],Circle1Y[i],Circle2X[i],Circle2Y[i]);				 
			    }		
				for (int i=0;i<2*cw1.length();i++)
				{
					//need to know whether an inverse letter has been "used up" already
					if(PL[i][0]!=letterholder)
					{
						usednum[letterholder]=1;
						letterholder=PL[i][0];
					}		
					if(usednum[C3.barN.get(PL[i][0])]==0)
					{
						XCurveCoord[i]=20+Math.round(Size/2+Math.round(Size/2*Math.cos(1.0*(1+PL[i][1])/(C3.getNumPoints(PL[i][0])+1)*2*Math.PI/(sw1.length())+ Math.PI+2*Math.PI*PL[i][0]/(sw1.length()))));
						YCurveCoord[i]=20+Math.round(Size/2+Math.round(Size/2*Math.sin(1.0*(1+PL[i][1])/(C3.getNumPoints(PL[i][0])+1)*2*Math.PI/(sw1.length())+ Math.PI+2*Math.PI*PL[i][0]/(sw1.length()))));
					}
					else 
					{
						XCurveCoord[i]=20+Math.round(Size/2+Math.round(Size/2*Math.cos(1.0*(C3.getNumPoints(PL[i][0])-PL[i][1])/(C3.getNumPoints(PL[i][0])+1)*2*Math.PI/(sw1.length())+ Math.PI+2*Math.PI*PL[i][0]/(sw1.length()))));
						YCurveCoord[i]=20+Math.round(Size/2+Math.round(Size/2*Math.sin(1.0*(C3.getNumPoints(PL[i][0])-PL[i][1])/(C3.getNumPoints(PL[i][0])+1)*2*Math.PI/(sw1.length())+ Math.PI+2*Math.PI*PL[i][0]/(sw1.length()))));
					}				
				}
				g.fillOval(XCurveCoord[SL[0][0]]-4,YCurveCoord[SL[0][0]]-4,8,8);	
				for(int k=0;k<cw1.length();k++)
			    {
					if(k<cw1.length()){g.setColor(Color.black);}
					if(k>=cw1.length()){ g.setColor(Color.red);}
					g.drawLine(XCurveCoord[SL[k][0]],YCurveCoord[SL[k][0]],XCurveCoord[SL[k][1]],YCurveCoord[SL[k][1]]);
				   	int mpx=(XCurveCoord[SL[k][0]]+XCurveCoord[SL[k][1]])/2;
				   	int mpy=(YCurveCoord[SL[k][0]]+YCurveCoord[SL[k][1]])/2;
				   	double a=XCurveCoord[SL[k][0]];
				   	double b=YCurveCoord[SL[k][0]];
			    	double c=XCurveCoord[SL[k][1]];
			    	double d=YCurveCoord[SL[k][1]];
			    	double atheta=Math.toRadians(45);
			    	double ltheta=Math.atan((d-b)/(c-a));
			    	Double temp1=10*Math.sin(ltheta+Math.PI/2-atheta);
			    	Double temp2=10*Math.cos(ltheta+Math.PI/2-atheta);
				    if(c>=a&& d>=b)
				    {
				    	g.drawLine(mpx,mpy,mpx-temp2.intValue(),mpy-temp1.intValue());
				    	g.drawLine(mpx,mpy,mpx-temp1.intValue(),mpy+temp2.intValue());
				    }
				    if(c<a && d<b)
				    {
				    	g.drawLine(mpx,mpy,mpx+temp2.intValue(),mpy+temp1.intValue());
				    	g.drawLine(mpx,mpy,mpx+temp1.intValue(),mpy-temp2.intValue());
				    }
				    if(c>=a && d<b)
				    {
				    	g.drawLine(mpx,mpy,mpx-temp2.intValue(),mpy-temp1.intValue());
				    	g.drawLine(mpx,mpy,mpx-temp1.intValue(),mpy+temp2.intValue());
				    }
				    if(c<a && d>=b)
				    {
				    	g.drawLine(mpx,mpy,mpx+temp2.intValue(),mpy+temp1.intValue());
				    	g.drawLine(mpx,mpy,mpx+temp1.intValue(),mpy-temp2.intValue());
				    }
			    }			
			}
		    String s1="";
			String s2="";
			for (int i=0;i<2*cw1.length();i++)
			{
				s1=s1+C3.toLetter.get(PL[i][0])+""+PL[i][1]+" , ";
			}
				s1=s1+"  ";
			for (int i=0;i<cw1.length();i++)
			{
				s2=s2+C3.toLetter.get(PL[SL[i][0]][0])+""+PL[SL[i][0]][1]+" , "+C3.toLetter.get(PL[SL[i][1]][0])+""+PL[SL[i][1]][1]+"\n";
			}
			min1TextField.setText(""+C3.getMinInt());
		}
		
	}
		
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		FirstTime=1;
		this.sw1 = swTextField.getText();
		this.cw1 = fcTextField.getText();
		this.runAlgorithm(sw1, cw1);
		this.repaint();
	}
	public void runAlgorithm(String sw,String cw)
	{
		System.out.println("Running algorithm on "+sw+" ,,,, "+cw);
		C3=new FillingTester(sw,cw);
		while(C3.getDone()==0)
		{
			C3.findIntersection();
		}
		PL=C3.getPL();
		SL=C3.getSL();
		usednum=new int[sw.length()];
		letterholder=PL[0][0];
		C3.InitIntMat();
		fill=C3.DoesItFill(cw)?"fills":"does not fill";
		System.out.println("***************************");
		System.out.println(fill);
		System.out.println("***************************");	
	}
	
	private class MyWindowListener implements WindowListener {

		  public void windowClosing(WindowEvent arg0) {
		    System.exit(0);
		  }

		  public void windowOpened(WindowEvent arg0) {
		  }

		  public void windowClosed(WindowEvent arg0) {
		  }

		  public void windowIconified(WindowEvent arg0) {
		  }

		  public void windowDeiconified(WindowEvent arg0) {
		  }

		  public void windowActivated(WindowEvent arg0) {
		  }

		  public void windowDeactivated(WindowEvent arg0) {
		  }

	}
	
	
}