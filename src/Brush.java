//莊于萱_104403533_資管2B
import java.awt.*;
public class Brush {//自行定義物件"筆刷"
	private Point myPoint;//點
	private Color color;//顏色
	private int penSize;//點的大小(筆刷粗細)
	
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
