import java.util.Comparator;

public class GridObjectComparator implements Comparator<GridObject>{

	@Override
	public int compare(GridObject o1, GridObject o2) {
		return Integer.compare(o1.getViewPriority(), o2.getViewPriority());
	}

}
