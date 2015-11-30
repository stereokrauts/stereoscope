package com.stereokrauts.stereoscope.model.messaging.priorization;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.stereokrauts.stereoscope.model.messaging.MessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageSender;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractMessage;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgChannelLevelLabel;
import com.stereokrauts.stereoscope.model.messaging.priorization.PriorityMessageQueue;

/**
 * no matter in which order the messages are passed in,
 * the high prio message is always returned first.
 */
public final class PriorityQueueTest implements IMessageSender {
	@Test
	public void testPriorityHandling() {
		final AbstractMessage<?> lowPrio = new MsgChannelLevelLabel(0, 0, "test");
		final AbstractMessage<?> highPrio = new MsgChannelLevelChanged(0, 1.0f);
		
		final PriorityMessageQueue queue = new PriorityMessageQueue();
		queue.add(new MessageWithSender(this, lowPrio));
		queue.add(new MessageWithSender(this, highPrio));
		
		assertEquals("High prio packet first", highPrio, queue.poll().getMessage());
		assertEquals("Low prio packet second", lowPrio, queue.poll().getMessage());
		
		queue.add(new MessageWithSender(this, highPrio));
		queue.add(new MessageWithSender(this, lowPrio));
		
		assertEquals("High prio packet first", highPrio, queue.poll().getMessage());
		assertEquals("Low prio packet second", lowPrio, queue.poll().getMessage());
		
	}
}
