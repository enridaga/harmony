package harmony.lod.factory;

public class LOD implements LODFactory {

	private FrameFactory frameFactory;

	private StatementTemplateFactory statementTemplateFactory;

	private SymbolFactory symbolFactory;

	private DatasetFactoryImpl datasetFactory;

	private PathFactoryImpl pathfactory;

	public LOD() {
		symbolFactory = new SymbolFactoryImpl();
		datasetFactory = new DatasetFactoryImpl();
		statementTemplateFactory = new StatementTemplateFactoryImpl(this);
		pathfactory = new PathFactoryImpl(this);
		frameFactory = new FrameFactoryImpl(this);
	}

	@Override
	public FrameFactory getFrameFactory() {
		return frameFactory;
	}

	@Override
	public StatementTemplateFactory getStatementTemplateFactory() {
		return statementTemplateFactory;
	}

	@Override
	public SymbolFactory getSymbolFactory() {
		return symbolFactory;
	}
	
	@Override
	public DatasetFactory getDatasetFactory() {
		return datasetFactory;
	}
	
	@Override
	public PathFactory getPathFactory() {
		return pathfactory;
	}
}
