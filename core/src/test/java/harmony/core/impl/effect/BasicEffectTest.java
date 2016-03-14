package harmony.core.impl.effect;

import junit.framework.Assert;
import harmony.core.api.effect.GroundEffect;
import harmony.core.api.fact.Fact;
import harmony.core.api.property.Property;
import harmony.core.api.thing.Thing;
import harmony.core.impl.effect.BasicEffect;
import harmony.core.impl.fact.BasicFact;
import harmony.core.impl.property.BasicProperty;
import harmony.core.impl.state.StaticState;
import harmony.core.impl.thing.Something;

import org.junit.Test;

public class BasicEffectTest {

	static Property friend;
	static Property loves;
	static Property hates;
	static Property inPlace;

	static Thing enrico;
	static Thing francesca;
	static Thing guinness;
	static Thing mk;
	static Thing moricone;

	static Fact[] state1;
	static Thing[] things1;
	static {
		friend = new BasicProperty("friend", 2);
		loves = new BasicProperty("loves", 2);
		hates = new BasicProperty("hates", 2);
		inPlace = new BasicProperty("inPlace", 2);

		enrico = new Something("enrico");
		francesca = new Something("francesca");
		mk = new Something("mk");
		moricone = new Something("moricone");
		guinness = new Something("guinnes");

		state1 = new Fact[] {
				new BasicFact(inPlace, enrico, mk),
				new BasicFact(loves, enrico, guinness) };
		things1 = new Thing[]{
				enrico, francesca, mk, moricone, guinness
		};
	}

	@Test
	public void test() {
		StaticState s = new StaticState(state1, things1);
		
		BasicEffect be = new BasicEffect();
		be.toAdd(new BasicFact(loves, enrico, francesca));
		be.toAdd(new BasicFact(inPlace, enrico, moricone));
		be.toRemove(new BasicFact(inPlace, enrico, mk));
		
		GroundEffect gf = be.asGroundEffect(s);

		Assert.assertTrue(gf.add().length == 2);
		Assert.assertTrue(gf.remove().length == 1);
		
		// before
		Assert.assertTrue(s.getFacts().size() == 2);
		Assert.assertTrue(s.getFactRegistry().contains(new BasicFact(inPlace, enrico, mk)));
		Assert.assertTrue(s.getFactRegistry().contains(new BasicFact(loves, enrico, guinness)));
		
		s.apply(gf);
		
		// after
		Assert.assertTrue(s.getFacts().size() == 3);
		Assert.assertFalse(s.getFactRegistry().contains(new BasicFact(inPlace, enrico, mk)));
		Assert.assertTrue(s.getFactRegistry().contains(new BasicFact(loves, enrico, guinness)));
		Assert.assertTrue(s.getFactRegistry().contains(new BasicFact(loves, enrico, francesca)));
		Assert.assertTrue(s.getFactRegistry().contains(new BasicFact(inPlace, enrico, moricone)));
	}
}
