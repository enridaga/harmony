package harmony.core.utils;

import harmony.core.utils.Combinator;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CombinatorTest {

	Logger log = LoggerFactory.getLogger(getClass());

	long start = 0;
	long end = 0;

	@Test
	public void c1() {
		List<List<Integer>> result = exec(1, 1);
		Assert.assertTrue(result.size() == (2 * 2));

	}

	@Test
	public void c2() {
		List<List<Integer>> result = exec(2, 2, 3, 3);
		Assert.assertTrue(result.size() == (3 * 3 * 4 * 4));

	}

	@Test
	public void c3() {
		List<List<Integer>> result = exec(19, 19, 19, 19);
		Assert.assertTrue(result.size() == (20 * 20 * 20 * 20));
	}

	@Test
	public void c4() {
		List<List<Integer>> result = exec(3, 3, 3, 3, 3, 3, 3, 3, 3, 3);
		Assert.assertTrue(result.size() == (4 * 4 * 4 * 4 * 4 * 4 * 4 * 4 * 4 * 4));
	}

	private List<List<Integer>> exec(int... dimensions) {

		start = System.currentTimeMillis();
		Combinator c = new Combinator(dimensions);
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		while (c.hasNext()) {
			result.add(c.next());
		}
		end = System.currentTimeMillis();
		log(result);
		return result;
	}

	private void log(List<List<Integer>> result) {
		for (List<Integer> in : result) {
			StringBuilder output = new StringBuilder();
			for (Integer i : in) {
				output.append('[');
				output.append(i);
				output.append(']');
				output.append(' ');
			}
			log.debug(output.toString());
		}
		log.info("Execution time: {}ms", end - start);
	}
}
