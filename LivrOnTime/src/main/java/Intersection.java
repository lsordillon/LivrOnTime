import java.util.ArrayList;

public class Intersection {
	private long id;
	private int x;
	private int y;
	private ArrayList<Troncon> tronconsVersVoisins;
	

	public Intersection(long id, int x, int y) {
		this.id = id;
		this.x = x;
		this.y = y;
	}
}
