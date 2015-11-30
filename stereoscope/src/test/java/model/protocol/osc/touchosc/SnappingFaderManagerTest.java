package model.protocol.osc.touchosc;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;


public class SnappingFaderManagerTest {
	@Test
	public void testActiveManager_failsOnTooBigIncrement() throws Exception {
		final SnappingFaderManager manager = new SnappingFaderManager(true, Arrays.asList(new Integer[] { 1, 2, 3 }));
		manager.tryUpdate(1, 0.2f, new ISnappingFaderEventHandler() {
			@Override
			public void snapSucceeded() {
				fail();
			}

			@Override
			public void snapFailed() {
			}
		});
	}

	@Test
	public void testInactiveManager_succeedsOnTooBigIncrement() throws Exception {
		final SnappingFaderManager manager = new SnappingFaderManager(false, Arrays.asList(new Integer[] { 1, 2, 3 }));
		manager.tryUpdate(1, 0.1f, new ISnappingFaderEventHandler() {
			@Override
			public void snapSucceeded() {
			}

			@Override
			public void snapFailed() {
				fail();
			}
		});
	}
}
