package harmony.lod.model.impl.slice;

import harmony.lod.model.api.slice.Slice;
import harmony.lod.model.api.slice.StatementTemplate;
import harmony.lod.model.api.symbol.Datatype;
import harmony.lod.model.api.symbol.IRI;
import harmony.lod.model.api.symbol.Lang;
import harmony.lod.model.api.symbol.Literal;
import harmony.lod.model.api.symbol.Resource;
import harmony.lod.model.api.symbol.Symbol;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class StatementTemplateImpl implements StatementTemplate {

	private IRI subject = null;
	private IRI predicate = null;
	private IRI object = null;
	private Literal value = null;
	private Lang lang = null;
	private Datatype datatype = null;

	public StatementTemplateImpl() {
	}

	public StatementTemplateImpl(IRI subject) {
		setSubject(subject);
	}

	public StatementTemplateImpl(IRI subject, IRI predicate) {
		setSubject(subject);
		setPredicate(predicate);
	}

	public StatementTemplateImpl(IRI subject, IRI predicate, Resource object) {
		setSubject(subject);
		setPredicate(predicate);
		if (object instanceof IRI) {
			setObject((IRI) object);
		} else if (object instanceof Literal) {
			setValue((Literal) object);
		}
	}
	
	public StatementTemplateImpl(IRI subject, IRI predicate, Lang lang) {
		setSubject(subject);
		setPredicate(predicate);
		setLang(lang);
	}
	
	public StatementTemplateImpl(IRI subject, IRI predicate, Datatype datatype) {
		setSubject(subject);
		setPredicate(predicate);
		setDatatype(datatype);
	}
	
	public StatementTemplateImpl(IRI subject, IRI predicate, IRI object,
			Literal value, Lang lang, Datatype datatype) {
		setSubject(subject);
		setPredicate(predicate);
		if (object != null) {
			setObject(object);
		} else if (value != null) {
			setValue(value);
		} else if (lang != null) {
			setLang(lang);
		} else if (datatype != null) {
			setDatatype(datatype);
		}
	}

	public Set<Symbol> getSymbolsInSignature() {
		Symbol[] sy = { subject, predicate, object, value, lang, datatype };
		List<Symbol> l = Arrays.asList(sy);
		Set<Symbol> s = new HashSet<Symbol>(l);
		s.remove(null);
		return s;
	}

	public String getSignature() {
		StringBuilder sb = new StringBuilder();
		sb.append("(Statement ");
		sb.append(subject != null ? subject.getSignature() : "?");
		sb.append(" ");
		sb.append(predicate != null ? predicate.getSignature() : "?");
		sb.append(" ");
		if (object != null) {
			sb.append(object.getSignature());
		} else if (value != null) {
			sb.append(value.getSignature());
		} else {
			sb.append("?");
			if (lang != null) {
				sb.append(lang.getSignature());
			} else if (datatype != null) {
				sb.append(datatype.getSignature());
			}
		}
		sb.append(")");
		return sb.toString();
	}

	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(getSignature()).toHashCode();
	}

	public IRI getSubject() {
		return subject;
	}

	public IRI getPredicate() {
		return predicate;
	}

	public IRI getObject() {
		return object;
	}

	public Datatype getDatatype() {
		return datatype;
	}

	public Literal getValue() {
		return value;
	}

	public Lang getLang() {
		return lang;
	}

	public boolean hasSubject() {
		return subject != null;
	}

	public boolean hasPredicate() {
		return predicate != null;
	}

	public boolean hasObject() {
		return object != null;
	}

	public boolean hasValue() {
		return value != null;
	}

	public boolean hasDatatype() {
		return datatype != null;
	}

	public boolean hasLang() {
		return lang != null;
	}

	private void setSubject(IRI subject) {
		this.subject = subject;
	}

	private void setPredicate(IRI predicate) {
		this.predicate = predicate;
	}

	private void setObject(IRI object) {
		this.object = object;
		this.value = null;
		this.lang = null;
		this.datatype = null;
	}

	/**
	 * Overwrites <tt>lang</tt> and <tt>datatype</tt> with values from
	 * <tt>value</tt>
	 * 
	 * @param value
	 *            The <tt>Literal</tt> value of this <tt>StatementTemplate</tt>
	 */
	private void setValue(Literal value) {
		this.value = value;
		this.object = null;
		this.lang = value.getLang();
		this.datatype = value.getDatatype();
	}

	/**
	 * Consequences:
	 * <ul>
	 * <li><tt>object</tt> is set to null
	 * <li><tt>datatype</tt> is set to null
	 * <li>if <tt>value</tt> is not null and <tt>value.getLang()</tt> is not
	 * equal to <tt>lang</tt>, <tt>value</tt> is set to null
	 * </ul>
	 * 
	 * @param lang
	 *            The <tt>lang</tt> of the <tt>Literal</tt> value of this
	 *            <tt>StatementTemplate</tt>
	 */
	private void setLang(Lang lang) {
		this.lang = lang;
		this.datatype = null;
		this.object = null;
		if (this.value != null) {
			if (this.value.getLang() != null) {
				if (!this.value.getLang().equals(lang)) {
					this.value = null;
				}
			} else {
				this.value = null;
			}
		}
	}

	/**
	 * Consequences:
	 * <ul>
	 * <li><tt>object</tt> is set to null
	 * <li><tt>lang</tt> is set to null
	 * <li>if <tt>value</tt> is not null and <tt>value.getDatatype()</tt> is not
	 * equal to <tt>lang</tt>, <tt>value</tt> is set to null
	 * </ul>
	 * 
	 * @param lang
	 *            The <tt>lang</tt> of the <tt>Literal</tt> value of this
	 *            <tt>StatementTemplate</tt>
	 */
	private void setDatatype(Datatype datatype) {
		this.datatype = datatype;
		this.object = null;
		this.lang = null;
		if (this.value != null) {
			if (this.value.getDatatype() != null) {
				if (!this.value.getDatatype().equals(datatype)) {
					this.value = null;
				}
			} else {
				this.value = null;
			}
		}

	}

	/**
	 * Check whether two StatementTemplates are the same.
	 * 
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof StatementTemplate) {
			if (((StatementTemplate) o).hasSubject()) {
				if (hasSubject()) {
					if (((StatementTemplate) o).getSubject().equals(
							getSubject())) {
						// go ahead
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else if (hasSubject()) {
				return false;
			}
			// here subjects are the same

			if (((StatementTemplate) o).hasPredicate()) {
				if (hasPredicate()) {
					if (((StatementTemplate) o).getPredicate().equals(
							getPredicate())) {
						// go ahead
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else if (hasPredicate()) {
				return false;
			}
			// here subjects and predicates are the same

			if (((StatementTemplate) o).hasObject()) {
				if (hasObject()) {
					if (((StatementTemplate) o).getObject().equals(getObject())) {
						// go ahead
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else if (hasObject()) {
				return false;
			} else {
				// check object value here (both have null object iri)
				if (((StatementTemplate) o).hasValue()) {
					if (hasValue()) {
						if (((StatementTemplate) o).getValue().equals(
								getValue())) {
							// go ahead
						} else {
							return false;
						}
					} else {
						return false;
					}
				} else if (hasValue()) {
					return false;
				} else {
					// check lang here (both have null object value)
					if (((StatementTemplate) o).hasLang()) {
						if (hasLang()) {
							if (((StatementTemplate) o).getLang().equals(
									getLang())) {
								// go ahead
							} else {
								return false;
							}
						} else {
							return false;
						}
					} else if (hasLang()) {
						return false;
					} else {
						// check datatype here (both have null object value and
						// null lang)
						if (((StatementTemplate) o).hasDatatype()) {
							if (hasDatatype()) {
								if (((StatementTemplate) o).getDatatype()
										.equals(getDatatype())) {
									// go ahead
								} else {
									return false;
								}
							} else {
								return false;
							}
						} else if (hasDatatype()) {
							return false;
						}
					}
				}
			}

			return true; // finally, they are equal
		} else {
			return false;
		}
	}

	public boolean includes(Slice slice) {
		if (slice instanceof StatementTemplate) {
			if (((StatementTemplate) slice).hasSubject()) {
				if (hasSubject()) {
					if (((StatementTemplate) slice).getSubject().equals(
							getSubject())) {
						// go ahead
					} else {
						return false;
					}
				}
				// go ahead (null subject includes st.subject)
			} else if (hasSubject()) {
				return false;
			}
			// st does not define a subject, if it does, it is equal to
			// this.subject.

			if (((StatementTemplate) slice).hasPredicate()) {
				if (hasPredicate()) {
					if (((StatementTemplate) slice).getPredicate().equals(
							getPredicate())) {
						// go ahead
					} else {
						return false;
					}
				}
				// go ahead (null predicate includes st.predicate)
			} else if (hasPredicate()) {
				return false;
			}
			// st does not define a predicate, if it does, it is equal to
			// this.predicate

			if (((StatementTemplate) slice).hasObject()) {
				if (hasObject()) {
					if (((StatementTemplate) slice).getObject().equals(
							getObject())) {
						// go ahead;
					} else {
						return false;
					}
				} else if (hasValue()) {
					return false;
				} else if (hasLang()) {
					return false;
				} else if (hasDatatype()) {
					return false;
				}
				// (null object/value/lang/datatype includes st.object)
				return true;
			} else if (hasObject()) {
				return false;
			}
			// object is null for both slices
			// check object value
			if (((StatementTemplate) slice).hasValue()) {
				if (hasValue()) {
					if (((StatementTemplate) slice).getValue().equals(
							getValue())) {
						// go ahead
					} else {
						return false;
					}
				} else if (hasLang()) {
					// check if this.lang is equal to st.objectValue.lang
					if (((StatementTemplate) slice).hasLang()) {
						if (((StatementTemplate) slice).getLang().equals(
								getLang())) {
							// go ahead
						} else {
							return false;
						}
					} else if (((StatementTemplate) slice).hasDatatype()) {
						return false; // it has a datatype instead
					}
				} else if (hasDatatype()) {
					// check if this.datatype is equal to
					// st.objectValue.datatype
					if (((StatementTemplate) slice).hasDatatype()) {
						if (((StatementTemplate) slice).getDatatype().equals(
								getDatatype())) {
							// go ahead
						} else {
							return false;
						}
					} else if (((StatementTemplate) slice).hasLang()) {
						return false; // it has a lang instead
					}
				}
			} else if (hasValue()) {
				return false;
			}

			if (((StatementTemplate) slice).hasLang()) {
				if (hasLang()) {
					if (((StatementTemplate) slice).getLang().equals(getLang())) {
						// go ahead
					} else {
						return false;
					}
				} else if (hasDatatype()) {
					return false;
				}
			} else if (hasLang()) {
				return false;
			}

			if (((StatementTemplate) slice).hasDatatype()) {
				if (hasDatatype()) {
					if (((StatementTemplate) slice).getDatatype().equals(
							getDatatype())) {
						// go ahead;
					} else {
						return false;
					}
				} else if (hasLang()) {
					return false;
				}
			} else if (hasDatatype()) {
				return false;
			}

			// If we are here input st is included in this st
			return true;
		} else {
			// Statement templates may include only statement templates
			return false;
		}
	}

	public boolean matchValue() {
		return object == null;
	}

	public boolean matchObject() {
		return value == null && datatype == null && lang == null;
	}

	public boolean isStatement() {
		return (hasSubject() && hasPredicate() && (hasObject() || hasValue()));
	}
}
