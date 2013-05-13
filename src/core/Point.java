package core;



/**
 *    
 * Une classe point normale!
 */


public final class Point implements Cloneable {
	public int x;
	public int y;
	
	public Point(int a, int b) {
		x = a;
		y = b;
	}
	
	public Point(){
		x = 0;
		y = 0;
	}
	
	public int get_x(){
		return x;
	}
	
	public int get_y(){
		return y;
	}
	
	public int distance_square(Point p){
		return (p.x - x)*(p.x - x)+(p.y - y)*(p.y - y);
	}
	
	public void translate(Point p) {
		x += p.x;
		y += p.y;
	}

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + this.x;
        hash = 17 * hash + this.y;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Point other = (Point) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }
        

    @Override
    public String toString() {
        return "Point{" + "x=" + x + ", y=" + y + '}';
    }

    @Override
    public Object clone()
    {
        return new Point(x, y);
    }
    
    

}
