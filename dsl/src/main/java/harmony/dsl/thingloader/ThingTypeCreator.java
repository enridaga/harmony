package harmony.dsl.thingloader;

import harmony.core.impl.thing.Something;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThingTypeCreator {
	public static final int KIND_CLASS = 1;
	public static final int KIND_INTERFACE = 2;

	private String supertype = null;
	private List<String> interfaces = null;
	private String packageName = "";
	private String name;

	private StringBuffer sb = null;
	private int thisKind = 1;

	public ThingTypeCreator() {
		this(KIND_CLASS);
	}

	public ThingTypeCreator(int kind) {
		switch (kind) {
		case KIND_CLASS:
		case KIND_INTERFACE:
			thisKind = kind;
			break;
		default:
			throw new IllegalArgumentException(
					"Kind not supported, only KIND_CLASS and KIND_INTERFACE");
		}
		supertype = Something.class.getCanonicalName();
		interfaces = new ArrayList<String>();
		sb = new StringBuffer();
	}

	public ThingTypeCreator(int kind, String packageName, String name,
			String supertype) {
		this(kind, packageName, name);
		this.supertype = supertype;
	}

	public ThingTypeCreator(int kind, String fullName) {
		this(kind, fullName.substring(0, fullName.lastIndexOf(".")), fullName
				.substring(fullName.lastIndexOf(".") + 1));
	}

	public ThingTypeCreator(int kind, String packageName, String name) {
		this(kind);
		this.name = name;
		this.packageName = packageName;
	}

	public void setSupertype(String supertype) {
		this.supertype = supertype;
	}

	public void addInterface(String toImplement) {
		this.interfaces.add(toImplement);
	}

	public List<String> getInterfaces() {
		return Collections.unmodifiableList(interfaces);
	}

	public String getSupertype() {
		return supertype;
	}

	public void setPackage(String packageName) {
		this.packageName = packageName;
	}

	public String getPackage() {
		return this.packageName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public String getCanonicalName() {
		return getPackage() + "." + getName();
	}

	public void appendCode(String sourceCodeFragment) {
		sb.append(sourceCodeFragment);
	}

	public boolean isClass() {
		return thisKind == KIND_CLASS;
	}

	public boolean isInterface() {
		return thisKind == KIND_INTERFACE;
	}

	public String buildSourceCode() {
		if (this.getName() == null) {
			throw new IllegalStateException("Name cannot be null");
		}
		StringBuilder sbill = new StringBuilder();
		if (getPackage() != null && !getPackage().equals("")) {
			sbill.append("package");
			sbill.append(" ");
			sbill.append(getPackage());
			sbill.append("; ");
		}
		sbill.append("\npublic ");
		if(isInterface()){
			sbill.append("interface ");
		}else{
			sbill.append("class ");
		}
		sbill.append(getName());
		if(isClass()){
			sbill.append(" extends ");
			sbill.append(getSupertype());
		}
		if (!interfaces.isEmpty()) {
			if(isInterface()){
				sbill.append(" extends ");
			}else{
				sbill.append(" implements ");
			}
			boolean first = true;
			for (String i : interfaces) {
				if (!first) {
					sbill.append(", ");
				}
				sbill.append(i);
				first = false;
			}
		}
		sbill.append("{");
		if(isClass()){
			sbill.append("\n public " + name + "(String stringId) { super(stringId); } ");
		}
		sbill.append(sb);
		sbill.append("}");
		return sbill.toString();
	}

	public CharSequenceJavaFileObject asJavaFileObject() {
		return new CharSequenceJavaFileObject(getCanonicalName(),
				buildSourceCode());
	}
}
