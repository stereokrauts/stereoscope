package model.protocol.osc.touchosc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class SnappingFaderTest {
	@Test
	public void testSnapFails_beyondDelta() throws Exception {
		executeTestCase(new SnappingFader(0.0f), 1.0f, false);
	}

	@Test
	public void testSnapSucceeds() throws Exception {
		executeTestCase(new SnappingFader(0.0f), 0.01f, true);
	}

	@Test
	public void testSnapSucceeds_onDelta() throws Exception {
		executeTestCase(new SnappingFader(0.0f), 0.04f, true);
	}

	@Test
	public void testSnapSucceeds_inASeriesOfSmallIncrements() throws Exception {
		final SnappingFader theFader = new SnappingFader(0.0f);
		executeTestCase(theFader, 0.04f, true);
		executeTestCase(theFader, 0.08f, true);
		executeTestCase(theFader, 0.12f, true);
		executeTestCase(theFader, 0.30f, false);
	}

	private Boolean result = null;
	private void executeTestCase(final SnappingFader fader, final double newValue, final boolean shouldSucceed) {
		result = null;
		fader.tryUpdate(newValue, new ISnappingFaderEventHandler() {
			@Override
			public void snapSucceeded() {
				result = true;
			}

			@Override
			public void snapFailed() {
				result = false;
			}
		});

		assertNotNull("At least one handler must be called.", result);
		assertEquals(shouldSucceed, result);
	}

}
