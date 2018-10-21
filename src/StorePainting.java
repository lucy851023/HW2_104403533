//莊于萱_104403533_資管2B
import java.awt.*;

public class StorePainting {//自行定義"圖形"
	private Color color; //顏色
	private Shape shape; //形狀
	private Stroke stroke; //線條大小
	
	public StorePainting(Shape s,Color c,Stroke st){
		shape = s;
		color = c;
		stroke = st;
	}
	
	public void setSahpe(Shape s){
		shape = s;
	}
	public void setColor(Color c){
		color = c;
	}
	
	public void setStroke(Stroke st){
		stroke = st;
	}
	
	public Shape getShape(){
		return shape;
	}
	
	public Color getColor(){
		return color;
	}
	
	public Stroke getStroke(){
		return stroke;
	}
}
