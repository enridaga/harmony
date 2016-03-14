package harmony.lod.model.impl.slice;

import harmony.lod.model.api.slice.Slice;
import harmony.lod.model.api.slice.Statement;
import harmony.lod.model.api.slice.StatementTemplate;
import harmony.lod.model.api.symbol.Datatype;
import harmony.lod.model.api.symbol.IRI;
import harmony.lod.model.api.symbol.Lang;
import harmony.lod.model.api.symbol.Literal;
import harmony.lod.model.api.symbol.Symbol;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * An RDF Statement, defining subject, predicate and object or value
 * 
 * @author Enrico Daga
 * 
 */
public class StatementImpl implements Statement {

	private IRI subject = null;
	private IRI predicate = null;
	private IRI object = null;
	private Literal value = null;

	public StatementImpl(IRI subject, IRI predicate, IRI object)
			throws StatementException {
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
		if (this.subject == null || this.predicate == null
				|| this.object == null) {
			throw new StatementException(
					"Statements must have subject, predicate and object (not null): "
							+ subject + " " + predicate + " " + object);
		}
	}

	public StatementImpl(IRI subject, IRI predicate, Literal value)
			throws SliceException {
		this.subject = subject;
		this.predicate = predicate;
		this.value = value;
		if (this.subject == null || this.predicate == null
				|| this.value == null) {
			throw new StatementException(
					"Statements must have subject, predicate and value (not null): "
							+ subject + " " + predicate + " " + value);
		}
	}

	public Set<Symbol> getSymbolsInSignature() {
		Symbol[] sy = { subject, predicate, object, value };
		List<Symbol> l = Arrays.asList(sy);
		Set<Symbol> s = new HashSet<Symbol>(l);
		s.remove(null);
		return s;
	}

	public boolean includes(Slice slice) {
		return equals(slice);
	}

	public IRI getSubject() {
		return this.subject;
	}

	public IRI getPredicate() {
		return this.predicate;
	}

	public IRI getObject() {
		return this.object;
	}

	public Datatype getDatatype() {
		return (value != null) ? value.getDatatype() : null;
	}

	public Lang getLang() {
		return (value != null) ? value.getLang() : null;
	}

	public Literal getValue() {
		return value;
	}

	public boolean hasSubject() {
		return true;
	}

	public boolean hasPredicate() {
		return true;
	}

	public boolean hasObject() {
		return (object != null);
	}

	public boolean hasValue() {
		return (value != null);
	}

	public boolean hasDatatype() {
		return (value != null) ? (value.getDatatype() != null) : false;
	}

	public boolean hasLang() {
		return (value != null) ? (value.getLang() != null) : false;
	}

	public boolean equals(Object o) {
		if (o instanceof StatementTemplate) {
			if (getSubject().equals(((StatementTemplate) o).getSubject())
					&& getPredicate().equals(
							((StatementTemplate) o).getPredicate())) {
				if (hasObject()) {
					return getObject().equals(
							((StatementTemplate) o).getObject());
				} else {
					return getValue()
							.equals(((StatementTemplate) o).getValue());
				}
			}
		}
		return false;
	}

	public boolean matchValue() {
		return value != null;
	}

	public boolean matchObject() {
		return object != null;
	}

	public boolean isStatement() {
		return true;
	}

	public String getSignature() {
		StringBuilder sb = new StringBuilder();
		sb.append("(Statement");
		sb.append(" ");
		sb.append(subject.getSignature());
		sb.append(" ");
		sb.append(predicate.getSignature());
		sb.append(" ");
		if (object != null) {
			sb.append(object.getSignature());
		} else {
			sb.append(value.getSignature());
		}
		sb.append(")");
		return sb.toString();
	}

	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(getSignature()).toHashCode();
	}
}
