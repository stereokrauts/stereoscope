package com.stereokrauts.stereoscope.model.messaging.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.stereokrauts.stereoscope.model.messaging.message.CouldNotCalculateException;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgChannelLevelLabel;

/**
 * This class tests the AbstractMessage address equals/hashCode functions which
 * are used for message overriding.
 * @author theide
 *
 */
public final class AddressesEqualTest {
	
	@Test
	public void testEqualAddresses() throws CouldNotCalculateException {
		final MsgChannelLevelChanged channelFiftyMsgOne = new MsgChannelLevelChanged(50, 1.0f);
		final MsgChannelLevelChanged channelFiftyMsgTwo = new MsgChannelLevelChanged(50, 0.5f);
		
		assertTrue(channelFiftyMsgOne.addressEquals(channelFiftyMsgTwo));
		assertTrue(channelFiftyMsgTwo.addressEquals(channelFiftyMsgOne));
		assertEquals(channelFiftyMsgOne.addressHashCode(), channelFiftyMsgTwo.addressHashCode());
	}
	
	@Test
	public void testUnequalAddresses() throws CouldNotCalculateException {
		final MsgChannelLevelChanged channelFiftyMsgOne = new MsgChannelLevelChanged(50, 1.0f);
		final MsgChannelLevelChanged channelFourtyMsgTwo = new MsgChannelLevelChanged(40, 0.5f);
		
		assertFalse(channelFiftyMsgOne.addressEquals(channelFourtyMsgTwo));
		assertFalse(channelFourtyMsgTwo.addressEquals(channelFiftyMsgOne));
		assertFalse(channelFiftyMsgOne.addressHashCode() == channelFourtyMsgTwo.addressHashCode());
	}
	
	@Test
	public void testUnequalMessages() throws CouldNotCalculateException {
		final MsgChannelLevelChanged channelFiftyMsgOne = new MsgChannelLevelChanged(50, 1.0f);
		final MsgChannelLevelLabel channelFourtyMsgTwo = new MsgChannelLevelLabel(50, 0, "Test");
		
		assertFalse(channelFiftyMsgOne.addressEquals(channelFourtyMsgTwo));
		assertFalse(channelFourtyMsgTwo.addressEquals(channelFiftyMsgOne));
		assertFalse(channelFiftyMsgOne.addressHashCode() == channelFourtyMsgTwo.addressHashCode());
	}
}
