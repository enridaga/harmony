package harmony.planner.graphplan;


public interface Time {

	public PropositionLevel getPropositionLevel();

	public ActionLevel getActionLevel();

	public int step();

	public Time previous();

	public Time next();

	public boolean hasPrevious();

	public boolean hasNext();
	
	public PlanningGraph getPlanningGraph();
}
