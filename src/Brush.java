//���_��_104403533_���2B
import java.awt.*;
public class Brush {//�ۦ�w�q����"����"
	private Point myPoint;//�I
	private Color color;//�C��
	private int penSize;//�I���j�p(����ʲ�)
	
	public Brush(Point p,Color c, int ps){
		myPoint = p;
		color = c;
		penSize = ps;
		
	}

	public Point getPoint(){
		return myPoint;
	}
	
	public Color getColor(){
		return color;
	}
	
	public int getPenSize(){
		return penSize;
	}
}
