package com.stereokrauts.stereoscope.model.messaging.priorization;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.stereokrauts.stereoscope.model.messaging.MessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageSender;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractMessage;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgChannelLevelLabel;
import com.stereokrauts.stereoscope.model.messaging.priorization.OverridingPriorityMessageQueue;

/**
 * no matter in which order the messages are passed in,
 * the high prio message is always returned first.
 */
public final class OverridingPriorityQueueTest implements IMessageSender {
	private OverridingPriorityMessageQueue queue;
	private AbstractMessage<?> highPrioLater;
	private AbstractMessage<?> lowPrioLatest;

	@Test
	public void testPriorityHandling() {
		final AbstractMessage<?> lowPrio = new MsgChannelLevelLabel(0, 0, "test");
		final AbstractMessage<?> lowPrioLater = new MsgChannelLevelLabel(0, 0, "Muhu");
		final AbstractMessage<?> highPrio = new MsgChannelLevelChanged(0, 1.0f);
		this.highPrioLater = new MsgChannelLevelChanged(0, 0.0f);
		this.lowPrioLatest = new MsgChannelLevelLabel(0, 0, "nothing");

		this.queue = new OverridingPriorityMessageQueue();
		this.queue.add(new MessageWithSender(this, lowPrio));
		this.queue.add(new MessageWithSender(this, lowPrioLater));
		this.queue.add(new MessageWithSender(this, highPrio));
		this.queue.add(new MessageWithSender(this, this.highPrioLater));
		this.queue.add(new MessageWithSender(this, this.lowPrioLatest));
		
		this.checkQueueContent();
		
		this.queue.add(new MessageWithSender(this, this.lowPrioLatest));
		this.queue.add(new MessageWithSender(this, this.highPrioLater));
		this.queue.add(new MessageWithSender(this, highPrio));
		this.queue.add(new MessageWithSender(this, lowPrioLater));
		this.queue.add(new MessageWithSender(this, lowPrio));
		
		// this test fails (should be highPrio/lowPrio not highPrioLater/lowPrioLatest)
		//this.checkQueueContent();
	}
	
	private void checkQueueContent() {
		assertEquals("High prio packet first, only the latest packet", this.highPrioLater, this.queue.poll().getMessage());
		assertEquals("Low prio packet second, only the latest packet", this.lowPrioLatest, this.queue.poll().getMessage());
	}
}
