package harmony.lod.model.impl.dataset;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import harmony.lod.model.api.dataset.Dataset;

public abstract class AbstractDataset implements Dataset {

	private String name = null;

	public AbstractDataset(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public int hashCode(){
		return new HashCodeBuilder(17, 31).append(getSignature()).toHashCode();
	}
	
	public boolean equals(Object o){
		if(o instanceof Dataset){
			return ((Dataset) o).getSignature().equals(getSignature());
		}
		return false;
	}
	
	@Override
	public boolean isReadOnly() {
		return false;
	}
}
