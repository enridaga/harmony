package harmony.lod.model.impl.slice;

import harmony.lod.model.api.slice.Path;
import harmony.lod.model.api.slice.Slice;
import harmony.lod.model.api.slice.StatementTemplate;
import harmony.lod.model.api.symbol.IRI;
import harmony.lod.model.api.symbol.Symbol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class PathImpl implements Path {

	private List<StatementTemplate> path = null;

	public PathImpl() {
		path = new ArrayList<StatementTemplate>();
	}
	
	public PathImpl(StatementTemplate... list)
			throws PathException {
		path = new ArrayList<StatementTemplate>();
		for (StatementTemplate t : list) {
			addStep(t);
		}
	}

	public boolean isClosed(){
		return ! path.get(path.size()-1).matchObject();
	}
	
	protected void addStep(StatementTemplate t) throws PathException {
		if (path.size() == 0) {
			path.add(t);
		} else {
			StatementTemplate st = path.get(path.size() - 1);
			IRI o = st.getObject();
			IRI s = t.getSubject();
			if(isClosed()){
				throw new PathException("Path ends with value. Cannot add further steps!");
			}
			if (o == null && s == null) {
				path.add(t);
			} else if (o != null && s != null) {
				if (o.equals(s)) {
					path.add(t);
				} else {
					throw new PathException("Last object does not match the given subject");
				}
			} else {
				throw new PathException("Incompatible subject for last object.");
			}
		}		
	}
	
	public Set<Symbol> getSymbolsInSignature() {
		Set<Symbol> symbols = new HashSet<Symbol>();
		for(StatementTemplate s : path){
			symbols.addAll(s.getSymbolsInSignature());
		}
		symbols.remove(null);
		return symbols;
	}

	public boolean includes(Slice slice) {
		if(slice instanceof Path){
			Path ipath = ((Path) slice);
			if(getLength() == ipath.getLength()){
				List<StatementTemplate> list = ipath.asList();
				for(int i = 0; i< ipath.getLength(); i++){
					if(path.get(i).includes(list.get(i))){
						continue;
					}else{
						return false;
					}	
				}
				return true;
			}
		}
		return false;
	}

	public int getLength() {
		return path.size();
	}

	public List<StatementTemplate> asList() {
		return Collections.unmodifiableList(path);
	}

	public boolean isChain(){
		for(StatementTemplate s : asList()){
			if(!s.isStatement()){
				return false;
			}
		}
		return true;
	}
	
	public boolean equals(Object o){
		if(o instanceof Path){
			if(((Path) o).getLength() == getLength()){
				for(int i=0; i < getLength(); i++){
					if(((Path) o).asList().get(i).equals(asList().get(i))){
						continue;
					}else{
						return false;
					}
				}
			}else{
				return false;
			}
			return true;
		}
		return false;
	}

	public String getSignature() {
		StringBuilder sb = new StringBuilder();
		sb.append("(Path");
		for(StatementTemplate s : path){
			sb.append(" ");
			sb.append(s.getSignature());
		}
		sb.append(")");
		return sb.toString();
	}
	
	public int hashCode(){
		return new HashCodeBuilder(17, 31).append(getSignature()).toHashCode();
	}
}
