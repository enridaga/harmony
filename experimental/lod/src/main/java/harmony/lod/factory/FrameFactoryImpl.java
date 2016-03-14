package harmony.lod.factory;

import harmony.lod.model.api.slice.Frame;
import harmony.lod.model.api.slice.StatementTemplate;
import harmony.lod.model.impl.slice.FrameException;
import harmony.lod.model.impl.slice.FrameImpl;

import java.util.HashSet;
import java.util.Set;

final class FrameFactoryImpl implements FrameFactory {

	private LODFactory factory;
	private StatementTemplateFactory s;
	
	public FrameFactoryImpl(LODFactory factory) {
		this.factory = factory;
		this.s = this.factory.getStatementTemplateFactory();
	}

	@Override
	public Frame get(StatementTemplate... st) throws FrameException {
		return new FrameImpl(st);
	}

	private Set<StatementTemplate> stSet(String...predicates){
		Set<StatementTemplate> st = new HashSet<StatementTemplate>();
		for (String p : predicates) {
			st.add(s.getP(p));
		}
		return st;
	}
	
	@Override
	public Frame getP(String... predicates) throws FrameException {
		Set<StatementTemplate> st = stSet(predicates);
		return new FrameImpl(st.toArray(new StatementTemplate[st.size()]));
	}
	
	@Override
	public Frame getTP(String type, String... predicates) throws FrameException {
		Set<StatementTemplate> st = stSet(predicates);
		st.add(s.getOfType(type));
		return new FrameImpl(st.toArray(new StatementTemplate[st.size()]));
	}
}
