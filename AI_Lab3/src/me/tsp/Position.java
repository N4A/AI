package me.tsp;

/**
 * @author Duocai Wu
 * @date 2016年12月22日
 * @time 下午3:58:39
 *
 */
public class Position {
	private double x;
	private double y;

	/**
	 * 
	 */
	public Position(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "["+x+","+y+"]";
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}
