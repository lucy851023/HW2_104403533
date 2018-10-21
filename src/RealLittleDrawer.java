//莊于萱_104403533_資管2B
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.geom.*;

public class RealLittleDrawer extends JFrame{
	private JLabel location;//放游標位置
	private JLabel draw1;//【繪圖工具】的label
	private JLabel draw2;//【筆刷大小】的label
	private JPanel panel;//放按鈕的panel
	private JPanel canvas;//畫作區
	private JComboBox drawingTool;//繪圖工具的ComboBox
	private static String[] drawingToolNames ={"筆刷","直線","橢圓形","矩形","圓角矩形"};//各類繪圖工具名字
	private JRadioButton small;//筆刷"小"
	private JRadioButton middle;//筆刷"中"
	private JRadioButton large;//筆刷"大"
	private ButtonGroup radioGroup;
	private JCheckBox full;//是否填滿
	private JButton foreground;//"前景色"按鈕
	private JButton background;//"背景色"按鈕
	private JButton delete;//"清除畫面"按鈕
	private Color backgroundColor;//背景顏色
	private Color foregroundColor;//前景顏色
	private ArrayList<StorePainting> myPainting = new ArrayList<>();//存放未填滿圖形
	private ArrayList<StorePainting> myFullPainting = new ArrayList<>();//存放填滿圖形
	private ArrayList<Brush> myBrush = new ArrayList<>();//存放筆刷
	private StorePainting painting;//圖形
	private Brush brush;//筆刷
	private Stroke lineSize = new BasicStroke(4) ;//圖形線條大小
	private static int start_x;//起始點x座標
	private static int start_y;//起始點y座標
	private static int end_x;//終點x座標
	private static int end_y;//終點y座標
	private Graphics2D g2d;
	private int penSize=4;//筆刷大小 預設為4
	private int w,h;//圖形的長、寬
	
	public RealLittleDrawer(){
		super("小畫家");
		panel = new JPanel();
		canvas = new Canvas1();
		panel.setBackground(Color.WHITE);
		panel.setLayout(new GridLayout(10,1));//建立按鈕排列方式
		add(panel,BorderLayout.WEST);//把panel放到整個JFrame的西側
		canvas.setBackground(Color.ORANGE);//設定畫作區顏色
		add(canvas,BorderLayout.CENTER);//把畫作區放入整個JFrame中間(中+右)
		
		draw1 = new JLabel("【繪圖工具】");
		panel.add(draw1);//將draw1加進panel
		
		drawingTool = new JComboBox<String>(drawingToolNames);
		drawingTool.setMaximumRowCount(5);//一次顯示5列
		panel.add(drawingTool);//將drawingTool加進panel
		
		draw2 = new JLabel("【筆刷大小】");
		panel.add(draw2);//將draw2加進panel
		
		small = new JRadioButton("小",true);
		middle = new JRadioButton("中",false);
		large = new JRadioButton("大",false);
		panel.add(small);//將small,middle,large加進panel
		panel.add(middle);
		panel.add(large);
		
		radioGroup = new ButtonGroup();
		radioGroup.add(small); //將small,middle,large加進 radioGroup
		radioGroup.add(middle);
		radioGroup.add(large);
		RadioButtonHandler radioButtonHandler = new RadioButtonHandler(); 
		small.addActionListener(radioButtonHandler);
		middle.addActionListener(radioButtonHandler);
		large.addActionListener(radioButtonHandler);
		
		full = new JCheckBox("填滿");
		panel.add(full);//將full加進 panel
		
		foreground = new JButton("前景色");
		background = new JButton("背景色");
		delete = new JButton("清除畫面");
		
		BackgroundHandler backgroundHandler = new BackgroundHandler();//建立"背景"按鈕的Handler(backgroundHandler)
		ForegroundHandler foregroundHandler = new ForegroundHandler();//建立"前景"按鈕的Handler(foregroundHandler)
		ClearHandler clearHandler = new ClearHandler();//建立"清除"按鈕的Handler(clearHandler)
		foreground.addActionListener(foregroundHandler);//將foregroundHandler加進前景按鈕
		background.addActionListener(backgroundHandler);//將backgroundHandler加進背景按鈕
		delete.addActionListener(clearHandler);//將clearHandler加進清除按鈕
		panel.add(foreground); //將foreground,background,delete加進panel
		panel.add(background);
		panel.add(delete);
		
		location = new JLabel("游標位置:");
		add(location,BorderLayout.SOUTH); //把location放到整個JFrame的下方
		
		MouseHandler mouseHandler = new MouseHandler(); //建立滑鼠的Handler
		canvas.addMouseListener(mouseHandler);//將Handler加進畫布區
		canvas.addMouseMotionListener(mouseHandler);//將Handler加進畫布區
		
	}
		
	private class RadioButtonHandler implements ActionListener {//設定筆刷及圖形線條的大小
		public void actionPerformed(ActionEvent event){ //handle radio button event
			if(event.getSource()==small){
				lineSize = new BasicStroke(4); 
				penSize = 4;
			}
			if(event.getSource()==middle){
				lineSize = new BasicStroke(10); 
				penSize = 10;
				}
			if(event.getSource()==large){
				lineSize = new BasicStroke(16);  
				penSize = 16;
			}	
		}
	}
	
		
	private class BackgroundHandler implements ActionListener //設定背景顏色
	   {
	      public void actionPerformed(ActionEvent event)//handle button event
	      {
	    	  backgroundColor = JColorChooser.showDialog(RealLittleDrawer.this,"顏色選擇",Color.ORANGE);   	  
	    	  background.setBackground(backgroundColor);
	    	  canvas.setBackground(backgroundColor);
	    	  
	      }
	   } 
	private class ForegroundHandler implements ActionListener {//設定前景顏色
		public void actionPerformed(ActionEvent event){
		foregroundColor = JColorChooser.showDialog(RealLittleDrawer.this,"顏色選擇",Color.WHITE);
		foreground.setBackground(foregroundColor);
		}
	}
	private class ClearHandler implements ActionListener{//清除畫面
		public void actionPerformed(ActionEvent event){
			myBrush.clear();
			myPainting.clear();
			myFullPainting.clear();
			start_x = start_y = end_x = end_y = -100;
			repaint();
		}
	}
	private class MouseHandler implements MouseMotionListener,MouseListener{ //handle Mouse event
		public void mousePressed(MouseEvent event)
		{
			start_x = event.getX();//紀錄起始點x座標
			start_y = event.getY();//紀錄起始點y座標
		}
		public void mouseReleased(MouseEvent event)
		{	
			end_x = event.getX();//紀錄終點x座標
			end_y = event.getY();//紀錄終點y座標
			w = Math.abs(end_x-start_x);//計算"長"並紀錄
			h = Math.abs(end_y-start_y);//計算"寬"並紀錄
			repaint();	
		}
		public void mouseEntered(MouseEvent event){}
		public void mouseExited(MouseEvent event){}
		public void mouseClicked(MouseEvent event){}
		public void mouseMoved(MouseEvent event){
			location.setText(String.format("游標位置:(%d,%d)",event.getX(),event.getY()));
		}
		public void mouseDragged(MouseEvent event)
        {  
		   location.setText(String.format("游標位置:(%d,%d)",event.getX(),event.getY()));
		   if(drawingTool.getSelectedIndex()==0){//當繪圖工具為筆刷時
			   brush = new Brush(event.getPoint(),foregroundColor,penSize);//紀錄筆刷路徑、前景色和筆刷大小
			   myBrush.add(brush);//將筆刷記錄存起來
			   repaint();
			   }
		  
        } 
	
	}
	
	private class Canvas1 extends JPanel{
	public void paintComponent(Graphics g)//實作繪圖
	   {  
	      super.paintComponent(g); // clears drawing area
	      g2d = (Graphics2D)g;
	        switch(drawingTool.getSelectedIndex())
	        {
	        case 0:
	        break;
	        case 1:
	        	painting = new StorePainting(new Line2D.Double(start_x,start_y, end_x,end_y),foregroundColor,lineSize);//紀錄直線
	        	myPainting.add(painting);//將記錄保存
	        break;	  
	        case 2:
	        	if(full.isSelected()){//判斷圖形是填滿還是未填滿
	        	painting = new StorePainting(new Ellipse2D.Double(start_x,start_y, w,h),foregroundColor,lineSize);//紀錄橢圓
	        	myFullPainting.add(painting);//將記錄保存
	        	}else{
	        	painting = new StorePainting(new Ellipse2D.Double(start_x,start_y, w,h),foregroundColor,lineSize);//紀錄橢圓
		        myPainting.add(painting);//將記錄保存	
	        	}
	     
	        break;
	        case 3:
	        	if(full.isSelected())//判斷圖形是填滿還是未填滿
	        	{painting = new StorePainting(new Rectangle2D.Double(start_x,start_y, w,h),foregroundColor,lineSize);//紀錄矩形
	        	myFullPainting.add(painting);//將記錄保存
	        	}else{
	        		painting = new StorePainting(new Rectangle2D.Double(start_x,start_y, w,h),foregroundColor,lineSize);//紀錄矩形
	        		myPainting.add(painting);//將記錄保存
	        	}
	      
	        break;
	        case 4:
	        		if(full.isSelected())//判斷圖形是填滿還是未填滿
	        		{
	        			painting = new StorePainting(new RoundRectangle2D.Double(start_x,start_y, w,h,30,30),foregroundColor,lineSize);//紀錄圓角矩形
	        			myFullPainting.add(painting);//將記錄保存
	        		}
	        		else{
	        		painting = new StorePainting(new RoundRectangle2D.Double(start_x,start_y, w,h,30,30),foregroundColor,lineSize);//紀錄圓角矩形
	        		myPainting.add(painting);//將記錄保存
	        		}
	        		
	        break;
	        	
	        }
	        for(Brush b:myBrush){//將筆刷的紀錄實作出來
	        	g2d.setPaint(b.getColor());
	        	g2d.fillOval(b.getPoint().x, b.getPoint().y,b.getPenSize(),b.getPenSize()); 
	        }
	        
	        for(StorePainting p:myPainting){//將未填滿圖形的紀錄實作出來
	        	g2d.setPaint(p.getColor());
	        	g2d.setStroke(p.getStroke());
	        	g2d.draw(p.getShape());
	        	
	        }
	        for (StorePainting p:myFullPainting){//將填滿之圖形的紀錄實作出來
	        	g2d.setPaint(p.getColor());
	        	g2d.setStroke(p.getStroke());
	        	g2d.fill(p.getShape());
	        }
	   }
	}
}

