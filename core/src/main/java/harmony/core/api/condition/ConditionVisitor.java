package harmony.core.api.condition;

import harmony.core.impl.condition.And;
import harmony.core.impl.condition.AssertFact;
import harmony.core.impl.condition.Bool;
import harmony.core.impl.condition.Equality;
import harmony.core.impl.condition.Exists;
import harmony.core.impl.condition.Forall;
import harmony.core.impl.condition.Not;
import harmony.core.impl.condition.Or;
import harmony.core.impl.condition.Type;
import harmony.core.impl.condition.When;

public interface ConditionVisitor {

	public boolean visit(Not cond);

	public boolean visit(AssertFact cond);

	public boolean visit(Equality cond);

	public boolean visit(And and);

	public boolean visit(Bool bool);

	public boolean visit(Or or);

	public boolean visit(When when);

	public boolean visit(Exists exists);

	public boolean visit(Forall forall);

	public boolean visit(Type type);
}
