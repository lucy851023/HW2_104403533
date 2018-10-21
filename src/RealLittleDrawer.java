//���_��_104403533_���2B
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.geom.*;

public class RealLittleDrawer extends JFrame{
	private JLabel location;//���Ц�m
	private JLabel draw1;//�iø�Ϥu��j��label
	private JLabel draw2;//�i����j�p�j��label
	private JPanel panel;//����s��panel
	private JPanel canvas;//�e�@��
	private JComboBox drawingTool;//ø�Ϥu�㪺ComboBox
	private static String[] drawingToolNames ={"����","���u","����","�x��","�ꨤ�x��"};//�U��ø�Ϥu��W�r
	private JRadioButton small;//����"�p"
	private JRadioButton middle;//����"��"
	private JRadioButton large;//����"�j"
	private ButtonGroup radioGroup;
	private JCheckBox full;//�O�_��
	private JButton foreground;//"�e����"���s
	private JButton background;//"�I����"���s
	private JButton delete;//"�M���e��"���s
	private Color backgroundColor;//�I���C��
	private Color foregroundColor;//�e���C��
	private ArrayList<StorePainting> myPainting = new ArrayList<>();//�s�񥼶񺡹ϧ�
	private ArrayList<StorePainting> myFullPainting = new ArrayList<>();//�s��񺡹ϧ�
	private ArrayList<Brush> myBrush = new ArrayList<>();//�s�񵧨�
	private StorePainting painting;//�ϧ�
	private Brush brush;//����
	private Stroke lineSize = new BasicStroke(4) ;//�ϧνu���j�p
	private static int start_x;//�_�l�Ix�y��
	private static int start_y;//�_�l�Iy�y��
	private static int end_x;//���Ix�y��
	private static int end_y;//���Iy�y��
	private Graphics2D g2d;
	private int penSize=4;//����j�p �w�]��4
	private int w,h;//�ϧΪ����B�e
	
	public RealLittleDrawer(){
		super("�p�e�a");
		panel = new JPanel();
		canvas = new Canvas1();
		panel.setBackground(Color.WHITE);
		panel.setLayout(new GridLayout(10,1));//�إ߫��s�ƦC�覡
		add(panel,BorderLayout.WEST);//��panel�����JFrame���谼
		canvas.setBackground(Color.ORANGE);//�]�w�e�@���C��
		add(canvas,BorderLayout.CENTER);//��e�@�ϩ�J���JFrame����(��+�k)
		
		draw1 = new JLabel("�iø�Ϥu��j");
		panel.add(draw1);//�Ndraw1�[�ipanel
		
		drawingTool = new JComboBox<String>(drawingToolNames);
		drawingTool.setMaximumRowCount(5);//�@�����5�C
		panel.add(drawingTool);//�NdrawingTool�[�ipanel
		
		draw2 = new JLabel("�i����j�p�j");
		panel.add(draw2);//�Ndraw2�[�ipanel
		
		small = new JRadioButton("�p",true);
		middle = new JRadioButton("��",false);
		large = new JRadioButton("�j",false);
		panel.add(small);//�Nsmall,middle,large�[�ipanel
		panel.add(middle);
		panel.add(large);
		
		radioGroup = new ButtonGroup();
		radioGroup.add(small); //�Nsmall,middle,large�[�i radioGroup
		radioGroup.add(middle);
		radioGroup.add(large);
		RadioButtonHandler radioButtonHandler = new RadioButtonHandler(); 
		small.addActionListener(radioButtonHandler);
		middle.addActionListener(radioButtonHandler);
		large.addActionListener(radioButtonHandler);
		
		full = new JCheckBox("��");
		panel.add(full);//�Nfull�[�i panel
		
		foreground = new JButton("�e����");
		background = new JButton("�I����");
		delete = new JButton("�M���e��");
		
		BackgroundHandler backgroundHandler = new BackgroundHandler();//�إ�"�I��"���s��Handler(backgroundHandler)
		ForegroundHandler foregroundHandler = new ForegroundHandler();//�إ�"�e��"���s��Handler(foregroundHandler)
		ClearHandler clearHandler = new ClearHandler();//�إ�"�M��"���s��Handler(clearHandler)
		foreground.addActionListener(foregroundHandler);//�NforegroundHandler�[�i�e�����s
		background.addActionListener(backgroundHandler);//�NbackgroundHandler�[�i�I�����s
		delete.addActionListener(clearHandler);//�NclearHandler�[�i�M�����s
		panel.add(foreground); //�Nforeground,background,delete�[�ipanel
		panel.add(background);
		panel.add(delete);
		
		location = new JLabel("��Ц�m:");
		add(location,BorderLayout.SOUTH); //��location�����JFrame���U��
		
		MouseHandler mouseHandler = new MouseHandler(); //�إ߷ƹ���Handler
		canvas.addMouseListener(mouseHandler);//�NHandler�[�i�e����
		canvas.addMouseMotionListener(mouseHandler);//�NHandler�[�i�e����
		
	}
		
	private class RadioButtonHandler implements ActionListener {//�]�w����ιϧνu�����j�p
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
	
		
	private class BackgroundHandler implements ActionListener //�]�w�I���C��
	   {
	      public void actionPerformed(ActionEvent event)//handle button event
	      {
	    	  backgroundColor = JColorChooser.showDialog(RealLittleDrawer.this,"�C����",Color.ORANGE);   	  
	    	  background.setBackground(backgroundColor);
	    	  canvas.setBackground(backgroundColor);
	    	  
	      }
	   } 
	private class ForegroundHandler implements ActionListener {//�]�w�e���C��
		public void actionPerformed(ActionEvent event){
		foregroundColor = JColorChooser.showDialog(RealLittleDrawer.this,"�C����",Color.WHITE);
		foreground.setBackground(foregroundColor);
		}
	}
	private class ClearHandler implements ActionListener{//�M���e��
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
			start_x = event.getX();//�����_�l�Ix�y��
			start_y = event.getY();//�����_�l�Iy�y��
		}
		public void mouseReleased(MouseEvent event)
		{	
			end_x = event.getX();//�������Ix�y��
			end_y = event.getY();//�������Iy�y��
			w = Math.abs(end_x-start_x);//�p��"��"�ì���
			h = Math.abs(end_y-start_y);//�p��"�e"�ì���
			repaint();	
		}
		public void mouseEntered(MouseEvent event){}
		public void mouseExited(MouseEvent event){}
		public void mouseClicked(MouseEvent event){}
		public void mouseMoved(MouseEvent event){
			location.setText(String.format("��Ц�m:(%d,%d)",event.getX(),event.getY()));
		}
		public void mouseDragged(MouseEvent event)
        {  
		   location.setText(String.format("��Ц�m:(%d,%d)",event.getX(),event.getY()));
		   if(drawingTool.getSelectedIndex()==0){//��ø�Ϥu�㬰�����
			   brush = new Brush(event.getPoint(),foregroundColor,penSize);//����������|�B�e����M����j�p
			   myBrush.add(brush);//�N����O���s�_��
			   repaint();
			   }
		  
        } 
	
	}
	
	private class Canvas1 extends JPanel{
	public void paintComponent(Graphics g)//��@ø��
	   {  
	      super.paintComponent(g); // clears drawing area
	      g2d = (Graphics2D)g;
	        switch(drawingTool.getSelectedIndex())
	        {
	        case 0:
	        break;
	        case 1:
	        	painting = new StorePainting(new Line2D.Double(start_x,start_y, end_x,end_y),foregroundColor,lineSize);//�������u
	        	myPainting.add(painting);//�N�O���O�s
	        break;	  
	        case 2:
	        	if(full.isSelected()){//�P�_�ϧάO���٬O����
	        	painting = new StorePainting(new Ellipse2D.Double(start_x,start_y, w,h),foregroundColor,lineSize);//�������
	        	myFullPainting.add(painting);//�N�O���O�s
	        	}else{
	        	painting = new StorePainting(new Ellipse2D.Double(start_x,start_y, w,h),foregroundColor,lineSize);//�������
		        myPainting.add(painting);//�N�O���O�s	
	        	}
	     
	        break;
	        case 3:
	        	if(full.isSelected())//�P�_�ϧάO���٬O����
	        	{painting = new StorePainting(new Rectangle2D.Double(start_x,start_y, w,h),foregroundColor,lineSize);//�����x��
	        	myFullPainting.add(painting);//�N�O���O�s
	        	}else{
	        		painting = new StorePainting(new Rectangle2D.Double(start_x,start_y, w,h),foregroundColor,lineSize);//�����x��
	        		myPainting.add(painting);//�N�O���O�s
	        	}
	      
	        break;
	        case 4:
	        		if(full.isSelected())//�P�_�ϧάO���٬O����
	        		{
	        			painting = new StorePainting(new RoundRectangle2D.Double(start_x,start_y, w,h,30,30),foregroundColor,lineSize);//�����ꨤ�x��
	        			myFullPainting.add(painting);//�N�O���O�s
	        		}
	        		else{
	        		painting = new StorePainting(new RoundRectangle2D.Double(start_x,start_y, w,h,30,30),foregroundColor,lineSize);//�����ꨤ�x��
	        		myPainting.add(painting);//�N�O���O�s
	        		}
	        		
	        break;
	        	
	        }
	        for(Brush b:myBrush){//�N���ꪺ������@�X��
	        	g2d.setPaint(b.getColor());
	        	g2d.fillOval(b.getPoint().x, b.getPoint().y,b.getPenSize(),b.getPenSize()); 
	        }
	        
	        for(StorePainting p:myPainting){//�N���񺡹ϧΪ�������@�X��
	        	g2d.setPaint(p.getColor());
	        	g2d.setStroke(p.getStroke());
	        	g2d.draw(p.getShape());
	        	
	        }
	        for (StorePainting p:myFullPainting){//�N�񺡤��ϧΪ�������@�X��
	        	g2d.setPaint(p.getColor());
	        	g2d.setStroke(p.getStroke());
	        	g2d.fill(p.getShape());
	        }
	   }
	}
}

