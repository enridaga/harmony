package harmony.core.impl.fact;

import harmony.core.api.fact.Fact;
import harmony.core.api.property.Property;
import harmony.core.api.thing.Thing;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class AbstractFact implements Fact {

	private Property property = null;
	
	private Thing[] things = null;
	
	public AbstractFact(Property property, Thing... things){
		this.property = property;
		this.things = things;
	}
	
	public Property getProperty() {
		return property;
	}

	public Thing getThing(int index) throws ArrayIndexOutOfBoundsException{
		return things[index];
	}

	public int indexOf(Thing thing) {
		int i = 0;
		for(Thing m : things){
			if(m.equals(thing)){
				return i;
			}
			i = i + 1;
		}
		return -1;
	}

	public List<Thing> getThings() {
		return Collections.unmodifiableList(Arrays.asList(things));
	}

	public boolean equals(Object o){
		if(o instanceof Fact){	
			if(((Fact) o).getProperty().equals(this.getProperty())){
				int argSize = ((Fact) o).getProperty().getArgSize();
				for(int i = 0; i < argSize; i++){
					if(!((Fact) o).getThing(i).equals(getThing(i))){
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	public int hashCode(){
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(getProperty().getName());
		for(Thing m : getThings()){
			hcb.append(m.getSignature());
		}
		return hcb.toHashCode();
	}
	
	public String toString(){
		StringBuilder b = new StringBuilder();
		b.append("(");
		b.append(property.getName());		
		for(Thing m : getThings()){
			b.append(" ");
			b.append(m.getSignature());
		}
		b.append(")");
		return b.toString();
	}
}
