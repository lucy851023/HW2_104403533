//���_��_104403533_���2B
import java.awt.*;

public class StorePainting {//�ۦ�w�q"�ϧ�"
	private Color color; //�C��
	private Shape shape; //�Ϊ�
	private Stroke stroke; //�u���j�p
	
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
