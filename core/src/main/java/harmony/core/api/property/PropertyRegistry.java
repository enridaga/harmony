package harmony.core.api.property;


public interface PropertyRegistry {

	public void register(Property property);

	public void unregister(String propertyName);

	public boolean isRegistered(String propertyName);

	public Property getProperty(String name);

	public int size();
}
