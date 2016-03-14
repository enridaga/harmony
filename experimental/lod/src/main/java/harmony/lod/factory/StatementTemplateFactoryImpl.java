package harmony.lod.factory;

import harmony.lod.model.api.slice.StatementTemplate;
import harmony.lod.model.impl.slice.StatementTemplateImpl;

final class StatementTemplateFactoryImpl implements StatementTemplateFactory {

	private LODFactory lod;

	public StatementTemplateFactoryImpl(LODFactory lodFactory) {
		lod = lodFactory;
	}

	@Override
	public StatementTemplate getSPO(String subject, String predicate,
			String object) {
		return new StatementTemplateImpl(lod.getSymbolFactory().iri(subject),
				lod.getSymbolFactory().iri(predicate), lod.getSymbolFactory()
						.iri(object));
	}

	@Override
	public StatementTemplate getSPV(String subject, String predicate,
			String value) {
		return new StatementTemplateImpl(lod.getSymbolFactory().iri(subject),
				lod.getSymbolFactory().iri(predicate), lod.getSymbolFactory()
						.literal(value));
	}

	@Override
	public StatementTemplate getSPVL(String subject, String predicate,
			String value, String lang) {
		return new StatementTemplateImpl(lod.getSymbolFactory().iri(subject),
				lod.getSymbolFactory().iri(predicate), lod.getSymbolFactory()
						.literall(value, lang));
	}

	@Override
	public StatementTemplate getSPVD(String subject, String predicate,
			String value, String datatype) {
		return new StatementTemplateImpl(lod.getSymbolFactory().iri(subject),
				lod.getSymbolFactory().iri(predicate), lod.getSymbolFactory()
						.literald(value, datatype));
	}

	@Override
	public StatementTemplate getOfType(String clas) {
		return new StatementTemplateImpl(null, lod.getSymbolFactory().iri(
				"http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), lod
				.getSymbolFactory().iri(clas));
	}

	@Override
	public StatementTemplate getPD(String predicate, String datatype) {
		return new StatementTemplateImpl(null, lod.getSymbolFactory().iri(
				predicate), lod.getSymbolFactory().datatype(datatype));
	}

	@Override
	public StatementTemplate getPL(String predicate, String lang) {
		return new StatementTemplateImpl(null, lod.getSymbolFactory().iri(
				predicate), lod.getSymbolFactory().lang(lang));
	}

	@Override
	public StatementTemplate getPO(String predicate, String object) {
		return new StatementTemplateImpl(null, lod.getSymbolFactory().iri(
				predicate), lod.getSymbolFactory().iri(object));
	}

	@Override
	public StatementTemplate getSP(String subject, String predicate) {
		return new StatementTemplateImpl(lod.getSymbolFactory().iri(subject),
				lod.getSymbolFactory().iri(predicate));
	}

	@Override
	public StatementTemplate getP(String predicate) {
		return new StatementTemplateImpl(null, lod.getSymbolFactory().iri(predicate));
	}
}
