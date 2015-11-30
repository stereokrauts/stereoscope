package model.protocol.osc;

public class DummySender implements ISendOscMessages
{
	public boolean received = false;
	public long receivedTimestamp;
	public IOscMessage lastMessage;


	@Override
	public synchronized void sendMessage(final IOscMessage m) {
		this.receivedTimestamp = System.currentTimeMillis();
		TestDelayedMessageSender.nextEarliestReception = this.receivedTimestamp + TestDelayedMessageSender.DELAY_MILLIES;
		this.lastMessage = m;
		this.received = true;
		this.notifyAll();
	}
}