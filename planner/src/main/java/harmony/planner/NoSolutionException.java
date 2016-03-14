package harmony.planner;

@SuppressWarnings("serial")
public class NoSolutionException extends Exception {

	private SearchReport searchReport = null;

	public NoSolutionException() {
	}

	public NoSolutionException(Throwable cause) {
		super(cause);
	}

	public NoSolutionException(SearchReport searchReport) {
		super("No solution");
		this.searchReport = searchReport;
	}

	public NoSolutionException(Throwable cause, SearchReport searchReport) {
		super("No solution", cause);
		this.searchReport = searchReport;
	}

	@SuppressWarnings("unchecked")
	public <T extends SearchReport> T getSearchReport() {
		return (T) this.searchReport;
	}
}
