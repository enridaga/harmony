package harmony.core.api.effect;

public interface CompositeEffect extends Effect {

	public void append(Effect effect);
}
