package harmony.planner.test;

import harmony.core.api.condition.Condition;
import harmony.core.api.effect.Effect;
import harmony.core.api.fact.Fact;
import harmony.core.api.operator.Operator;
import harmony.core.api.operator.OperatorException;
import harmony.core.api.property.Property;
import harmony.core.api.thing.Thing;
import harmony.core.impl.condition.And;
import harmony.core.impl.condition.AssertFact;
import harmony.core.impl.condition.Equality;
import harmony.core.impl.condition.Not;
import harmony.core.impl.effect.BasicEffect;
import harmony.core.impl.effect.CompositeEffectImpl;
import harmony.core.impl.effect.NotEffect;
import harmony.core.impl.fact.BasicFact;
import harmony.core.impl.operator.AbstractOperator;
import harmony.core.impl.property.BasicProperty;
import harmony.core.impl.thing.Something;

public class RocketDomain extends PlannerInputTest {

	public static final class Rocket extends Something {
		public Rocket(String id) {
			super(id);
		}
	}

	public static final class Cargo extends Something {
		public Cargo(String id) {
			super(id);
		}
	}

	public static final class Place extends Something {
		public Place(String id) {
			super(id);
		}
	}

	@SuppressWarnings("unchecked")
	public static final Property hasFuel = new BasicProperty("hasFuel",
			Rocket.class);

	@SuppressWarnings("unchecked")
	public static final Property at = new BasicProperty("at", Thing.class,
			Place.class);

	@SuppressWarnings("unchecked")
	public static final Property in = new BasicProperty("in", Cargo.class,
			Rocket.class);

	@SuppressWarnings("unchecked")
	public static final Operator Move = new AbstractOperator("Move",
			Rocket.class, Place.class, Place.class) {

		@Override
		public Condition getPrecondition(final Thing... things)
				throws OperatorException {
			And and = new And();
			and.append(new Not(new Equality(things[1], things[2])));
			and.append(new AssertFact(new BasicFact(at, things[0], things[1])));
			and.append(new AssertFact(new BasicFact(hasFuel, things[0])));
			return and;
		}

		@Override
		public Effect getEffect(final Thing... things) throws OperatorException {
			CompositeEffectImpl and = new CompositeEffectImpl();
			and.append(new BasicEffect(new BasicFact(at, things[0], things[2])));
			and.append(new NotEffect(new BasicEffect(new BasicFact(at,
					things[0], things[1]))));
			and.append(new NotEffect(new BasicEffect(new BasicFact(hasFuel,
					things[0]))));
			return and;
		}
	};

	@SuppressWarnings("unchecked")
	public static final Operator Unload = new AbstractOperator("Unload",
			Rocket.class, Place.class, Cargo.class) {

		@Override
		public Condition getPrecondition(final Thing... things)
				throws OperatorException {
			And and = new And();
			and.append(new AssertFact(at, things[0], things[1]));
			and.append(new AssertFact(in, things[2], things[0]));
			return and;
		}

		@Override
		public Effect getEffect(final Thing... things) throws OperatorException {
			CompositeEffectImpl cei = new CompositeEffectImpl();
			cei.append(new BasicEffect(at, things[2], things[1]));
			cei.append(new NotEffect(new BasicEffect(in, things[2], things[0])));
			return cei;
		}
	};

	@SuppressWarnings("unchecked")
	public final Operator Load = new AbstractOperator("Load", Rocket.class,
			Place.class, Cargo.class) {

		@Override
		public Condition getPrecondition(final Thing... things)
				throws OperatorException {
			And and = new And();
			and.append(new AssertFact(at, things[0], things[1]));
			and.append(new AssertFact(at, things[2], things[1]));
			return and;
		}

		@Override
		public Effect getEffect(final Thing... things) throws OperatorException {
			CompositeEffectImpl cei = new CompositeEffectImpl();
			cei.append(new BasicEffect(in, things[2], things[0]));
			cei.append(new NotEffect(new BasicEffect(at, things[2], things[1])));
			return cei;
		}
	};

	public final static Rocket R0 = new Rocket("R0");
	public final static Rocket R1 = new Rocket("R1");
	public final static Rocket R2 = new Rocket("R2");
	public final static Rocket R3 = new Rocket("R3");
	public final static Rocket R4 = new Rocket("R4");
	public final static Rocket R5 = new Rocket("R5");
	public final static Rocket R6 = new Rocket("R6");

	public final static Place Paris = new Place("Paris");
	public final static Place London = new Place("London");
	public final static Place Amsterdam = new Place("Amsterdam");
	public final static Place Rome = new Place("Rome");
	public final static Place Dublin = new Place("Dublin");
	public final static Place Madrid = new Place("Madrid");

	public final static Cargo Fruit0 = new Cargo("Fruit0");
	public final static Cargo Fruit1 = new Cargo("Fruit1");
	public final static Cargo Fruit2 = new Cargo("Fruit2");

	public final static Cargo Flowers0 = new Cargo("Flowers0");
	public final static Cargo Flowers1 = new Cargo("Flowers1");
	public final static Cargo Flowers2 = new Cargo("Flowers2");

	public final static Cargo Fish0 = new Cargo("Fish0");
	public final static Cargo Fish1 = new Cargo("Fish1");
	public final static Cargo Fish2 = new Cargo("Fish2");

	public final static Cargo Meat0 = new Cargo("Meat0");
	public final static Cargo Meat1 = new Cargo("Meat1");
	public final static Cargo Meat2 = new Cargo("Meat2");

	public final static Thing[] Objects = new Thing[] { R0, R1, R2, R3, R4, R5,
			R6, Paris, London, Rome, Amsterdam, Madrid, Dublin, Fruit0, Fruit1,
			Fruit2, Flowers0, Flowers1, Flowers2, Fish0, Fish1, Fish2, Meat0,
			Meat1, Meat2 };

	public RocketDomain(Fact[] init, Fact[] goalf) {
		super(init, goalf, Objects);
	}

	@Override
	public Operator[] getOperators() {
		return new Operator[] { Load, Unload, Move };
	}

	@Override
	public Property[] getProperty() {
		return new Property[] { at, in, hasFuel };
	}

	public static Fact at(Rocket r, Place p) {
		return new BasicFact(at, r, p);
	}

	public static Fact at(Cargo c, Place p) {
		return new BasicFact(at, c, p);
	}

	public static Fact in(Cargo c, Rocket r) {
		return new BasicFact(in, c, r);
	}

	public static Fact hasFuel(Rocket r) {
		return new BasicFact(hasFuel, r);
	}

}
