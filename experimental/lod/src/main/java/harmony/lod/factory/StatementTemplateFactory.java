package harmony.lod.factory;

import harmony.lod.model.api.slice.StatementTemplate;

public interface StatementTemplateFactory {

	public StatementTemplate getSPO(String subject, String predicate,
			String object);

	public StatementTemplate getSPV(String subject, String predicate,
			String object);

	public StatementTemplate getSPVL(String subject, String predicate,
			String value, String lang);

	public StatementTemplate getSPVD(String subject, String predicate,
			String value, String lang);

	public StatementTemplate getPD(String predicate, String datatype);

	public StatementTemplate getPL(String predicate, String lang);

	public StatementTemplate getSP(String subject, String predicate);

	public StatementTemplate getPO(String predicate, String object);

	public StatementTemplate getOfType(String clas);

	public StatementTemplate getP(String predicate);
}
