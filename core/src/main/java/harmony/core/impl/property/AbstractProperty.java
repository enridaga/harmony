package harmony.core.impl.property;

import harmony.core.api.property.Property;
import harmony.core.api.thing.Thing;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public abstract class AbstractProperty implements Property {

	private String name = null;
	private List<Class<? extends Thing>> argTypes = null;
	private int hashCode = 0;

	public AbstractProperty(String name, Class<? extends Thing> ... argTypes) {
		this.name = name;
		this.argTypes = Arrays.asList(argTypes);
		this.hashCode = getHashCodeBuilder().toHashCode();
	}

	public AbstractProperty(String name, int argSize) {
		@SuppressWarnings("unchecked")
		Class<? extends Thing>[] argTypes = new Class[argSize];
		Arrays.fill(argTypes, Thing.class);
		this.name = name;
		this.argTypes = Arrays.asList(argTypes);
		this.hashCode = getHashCodeBuilder().toHashCode();
	}
	
	public String getName() {
		return this.name;
	}

	public List<Class<? extends Thing>> getArgTypes() {
		return this.argTypes;
	}

	public Class<? extends Thing> getArgType(int index) {
		return argTypes.get(index);
	}

	public int getArgIndex(Class<? extends Thing> argType) {
		return this.argTypes.indexOf(argType);
	}

	public int getArgSize() {
		return this.argTypes.size();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Property) {
			if (((Property) obj).getName().equals(this.getName())) {
				return true;
			}
		}
		return false;
	}
	
	protected abstract HashCodeBuilder getHashCodeBuilder();
	
	public int hashCode(){
		return hashCode;
	}
}
