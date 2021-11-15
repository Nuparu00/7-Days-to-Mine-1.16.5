package nuparu.sevendaystomine.client.gui.monitor;

public interface IDraggable {

	boolean isDragged();
	
	void setOffsetX(double offset);
	void setOffsetY(double offset);
	
	double getOffsetX();
	double getOffsetY();
	
}
