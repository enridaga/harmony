package harmony.planner.bestfirst;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

public class OpenNodes<T extends Node> implements SortedSet<T>, Set<T> {
	Comparator<T> comparator = null;

	List<T> nodes = null;

	public OpenNodes(Comparator<T> comparator) {
		this.comparator = comparator;
		this.nodes = new ArrayList<T>();
	}

	private boolean sorted = false;

	protected boolean sort() {
		if (!sorted) {
			Collections.sort(nodes, comparator);
			sorted = true;
			return true;
		}
		return false;
	}

	@Override
	public boolean add(T e) {
		if (nodes.contains(e)) {
			return false;
		}
		sorted = false;
		return nodes.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		boolean changed = false;
		for (T t : c) {
			changed = add(t);
		}
		if (changed) {
			sorted = false;
		}
		return changed;
	}

	@Override
	public void clear() {
		nodes = new ArrayList<T>();
	}

	@Override
	public boolean contains(Object o) {
		return nodes.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return nodes.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return nodes.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		sort();
		return nodes.iterator();
	}

	@Override
	public boolean remove(Object o) {
		nodes.remove(o);
		// Ordering does not change on removal
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return nodes.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean retained = nodes.retainAll(c);
		if (retained) {
			sorted = false;
		}
		return retained;
	}

	@Override
	public int size() {
		return nodes.size();
	}

	@Override
	public Object[] toArray() {
		return nodes.toArray();
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] a) {
		return nodes.toArray(a);
	}

	@Override
	public Comparator<? super T> comparator() {
		return comparator;
	}

	@Override
	public T first() {
		sort();
		return nodes.get(0);
	}

	@Override
	public SortedSet<T> headSet(T toElement) {
		throw new UnsupportedOperationException(
				"This implementation of SortedSet does not support this method.");
	}

	@Override
	public T last() {
		sort();
		return nodes.get(nodes.size() - 1);
	}

	@Override
	public SortedSet<T> subSet(T fromElement, T toElement) {
		throw new UnsupportedOperationException(
				"This implementation of SortedSet does not support this method.");
	}

	@Override
	public SortedSet<T> tailSet(T fromElement) {
		throw new UnsupportedOperationException(
				"This implementation of SortedSet does not support this method.");
	}

}
