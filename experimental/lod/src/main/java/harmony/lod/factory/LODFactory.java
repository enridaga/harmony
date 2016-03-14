package harmony.lod.factory;

public interface LODFactory {

	public StatementTemplateFactory getStatementTemplateFactory();

	public FrameFactory getFrameFactory();

	public SymbolFactory getSymbolFactory();

	public DatasetFactory getDatasetFactory();

	public PathFactory getPathFactory();
}
