package harmony.lod.factory;

import harmony.lod.model.api.slice.Path;
import harmony.lod.model.api.slice.StatementTemplate;
import harmony.lod.model.impl.slice.PathException;
import harmony.lod.model.impl.slice.PathImpl;

import java.util.ArrayList;
import java.util.List;

final class PathFactoryImpl implements PathFactory {
	private LODFactory factory;

	public PathFactoryImpl(LODFactory factory) {
		this.factory = factory;
	}

	@Override
	public Path get(StatementTemplate... statementTemplate)
			throws PathException {
		return new PathImpl(statementTemplate);
	}

	@Override
	public Path getP(String... predicates) throws PathException {
		List<StatementTemplate> t = new ArrayList<StatementTemplate>();
		for(String predicate : predicates){
			t.add(factory.getStatementTemplateFactory().getP(predicate));
		}
		return new PathImpl(t.toArray(new StatementTemplate[t.size()]));
	}
}
