package harmony.lod.model.impl.slice;

import harmony.lod.model.api.slice.Frame;
import harmony.lod.model.api.slice.Slice;
import harmony.lod.model.api.slice.StatementTemplate;
import harmony.lod.model.api.symbol.IRI;
import harmony.lod.model.api.symbol.Symbol;
import harmony.lod.model.impl.symbol.IRIImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class FrameImpl implements Frame {

	private Set<StatementTemplate> stt = null;

	private IRI subject = null;

	private Set<IRI> types = null;

	private int hashCode;

	public FrameImpl(StatementTemplate... statementTemplates)
			throws FrameException {
		stt = new HashSet<StatementTemplate>();
		types = new HashSet<IRI>();

		for (StatementTemplate st : statementTemplates) {
			addStatementTemplate(st);
		}
		buildHashCode();
	}

	public FrameImpl(Frame parent, StatementTemplate... statementTemplates)
			throws FrameException {
		stt = new HashSet<StatementTemplate>();
		types = new HashSet<IRI>();
		for(StatementTemplate s : parent.asSet()){
			addStatementTemplate(s);
		}
		for(StatementTemplate s : statementTemplates){
			addStatementTemplate(s);
		}
		buildHashCode();
	}
	
	public FrameImpl(Frame... frames)
			throws FrameException {
		stt = new HashSet<StatementTemplate>();
		types = new HashSet<IRI>();
		for(Frame f : frames){
			for(StatementTemplate s : f.asSet()){
				addStatementTemplate(s);
			}
		}
		buildHashCode();
	}
	
	private StatementTemplate cloneInFrame(StatementTemplate st) {
		// Overwrite it
		if (st.hasObject()) {
			st = new StatementTemplateImpl(getSubject(), st.getPredicate(),
					st.getObject());
		} else if (st.hasValue()) {
			st = new StatementTemplateImpl(getSubject(), st.getPredicate(),
					st.getValue());
		} else if (st.hasLang()) {
			st = new StatementTemplateImpl(getSubject(), st.getPredicate(),
					st.getLang());
		} else if (st.hasDatatype()) {
			st = new StatementTemplateImpl(getSubject(), st.getPredicate(),
					st.getDatatype());
		} else {
			st = new StatementTemplateImpl(getSubject(), st.getPredicate());
		}
		return st;
	}

	protected void addStatementTemplate(StatementTemplate st)
			throws FrameException {
		Set<StatementTemplate> statements = new HashSet<StatementTemplate>();

		if (st.hasSubject()) {
			if (hasSubject() && !st.equals(getSubject())) {
				throw new FrameException("Incompatible subjects");
			}
			subject = st.getSubject();
		} else if (hasSubject()) {
			st = cloneInFrame(st);

			// Fix subject on any already added template
			for (StatementTemplate a : asSet()) {
				statements.add(cloneInFrame(a));
			}
		}
		statements.add(st);

		for (StatementTemplate stat : statements) {
			if (stat.hasPredicate() && stat.hasObject()) {
				if (st.getPredicate()
						.equals(new IRIImpl(
								"http://www.w3.org/1999/02/22-rdf-syntax-ns#type"))) {
					synchronized (types) {
						types.add(st.getObject());
					}
				}
			}

			// check inclusion
			StatementTemplate includedBy = null;
			StatementTemplate includes = null;
			for (StatementTemplate s : stt) {
				if (s.includes(stat)) {
					includedBy = s;
				} else if (stat.includes(s)) {
					includes = s;
				}
			}

			synchronized (stt) {
				if (includedBy != null) {
					stt.remove(includedBy);
				}
				if (includes == null) {
					stt.add(stat);
				}
			}
		}
	}

	public Set<Symbol> getSymbolsInSignature() {
		Set<Symbol> symbols = new HashSet<Symbol>();
		for (StatementTemplate st : asSet()) {
			symbols.addAll(st.getSymbolsInSignature());
		}
		return Collections.unmodifiableSet(symbols);
	}

	public boolean includes(Slice slice) {
		if (slice instanceof Frame) {
			Frame f = (Frame) slice;
			Set<StatementTemplate> checked = new HashSet<StatementTemplate>();
			Set<StatementTemplate> inset = f.asSet();
			Set<StatementTemplate> set = asSet();

			for (StatementTemplate s : inset) {
				boolean included = false;
				for (StatementTemplate s1 : set) {
					if (s1.includes(s)) {
						checked.add(s1);
						included = true;
						continue;
					}
				}
				if (!included) {
					return false;
				}
			}
			return checked.equals(set);
		}
		return false;
	}

	public Set<StatementTemplate> asSet() {
		return Collections.unmodifiableSet(stt);
	}

	public boolean hasSubject() {
		return (subject != null);
	}

	public IRI getSubject() {
		return subject;
	}

	public boolean hasTypes() {
		return !types.isEmpty();
	}

	public Set<IRI> getTypes() {
		return Collections.unmodifiableSet(types);
	}

	public Set<IRI> getPredicates() {
		Set<IRI> predicates = new HashSet<IRI>();
		for (StatementTemplate st : asSet()) {
			predicates.add(st.getPredicate());
		}
		predicates.remove(null);
		return Collections.unmodifiableSet(predicates);
	}

	public boolean equals(Object o) {
		if (o instanceof Frame) {
			if (((Frame) o).hasSubject()) {
				if (((Frame) o).getSubject().equals(getSubject())) {
					// go ahead
				} else {
					return false;
				}
			}
			Set<StatementTemplate> inset = ((Frame) o).asSet();
			Set<StatementTemplate> set = asSet();
			if (set.size() == inset.size()) {
				for (StatementTemplate s : set) {
					if (inset.contains(s)) {
						// go ahead
					} else {
						return false;
					}
				}
				for (StatementTemplate s : inset) {
					if (set.contains(s)) {
						// go ahead
					} else {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	private String signature = null;

	public String getSignature() {
		if (signature == null) {
			Set<String> set = new HashSet<String>();
			for (StatementTemplate t : asSet()) {
				set.add(t.getSignature());
			}
			String[] arr = set.toArray(new String[set.size()]);
			Arrays.sort(arr);
			StringBuilder sb = new StringBuilder();
			sb.append("(Frame");
			for (String s : arr) {
				sb.append(" ");
				sb.append(s);
			}
			sb.append(")");
			this.signature = sb.toString();
		}
		return this.signature;
	}

	private void buildHashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(stt);
		this.hashCode = hcb.toHashCode();
	}

	public int hashCode() {
		return hashCode;
	}
}
