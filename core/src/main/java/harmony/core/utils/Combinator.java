package harmony.core.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Combinator implements Iterator<List<Integer>> {

	private List<Integer> dimensions = null;
	private List<Integer> values = null;

	private int position = 0;

	private boolean hasNext = true;

	public Combinator(int... is) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i : is) {
			list.add(new Integer(i));
		}
		init(list);
	}

	public Combinator(List<Integer> dimensions) {
		init(dimensions);
	}

	private void init(List<Integer> dimensions) {
		this.dimensions = dimensions;
		this.values = new ArrayList<Integer>();
		Iterator<Integer> i = dimensions.iterator();
		while (i.hasNext()) {
			int dim = i.next();
			if(dim < 0){
				hasNext = false;
				return;
			}
			values.add(0);
		}

		if (dimensions.isEmpty()) {
			hasNext = false;
		}

	}

	private boolean increment() {
		if (!canIncrement(position)) {
			int incrThis = getNextIncrementable(position);
			if (incrThis != -1) {
				position = incrThis;
				increment(position);
				resetBefore(position);
				position = 0;
				return true;
			}
			return false; // Finished
		} else {
			increment(position);
			return true;
		}
	}

	private int getNextIncrementable(int after) {
		for (int x = after + 1; x < values.size(); x++) {
			if (!values.get(x).equals(dimensions.get(x))) {
				return x;
			}
		}
		return -1;
	}

	private boolean canIncrement(int position) {
		return dimensions.get(position) > values.get(position);
	}

	private boolean increment(int position) {
		if (canIncrement(position)) {
			// Increment its value
			int newValue = values.get(position) + 1;
			values.remove(position);
			values.add(position, newValue);
			return true;
		}
		return false;
	}

	private void resetBefore(int position) {
		for (int x = 0; x < position; x++) {
			reset(x);
		}
	}

	private void reset(int position) {
		int newValue = 0;
		values.remove(position);
		values.add(position, newValue);

	}

	public boolean hasNext() {
		return hasNext;
	}

	public List<Integer> next() {
		List<Integer> current = new ArrayList<Integer>(values);
		hasNext = increment();
		return current;

	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
}
