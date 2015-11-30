/**
 * 
 */
package com.stereokrauts.stereoscope.webgui.messaging;

/**
 * This class serves as a pre-stage
 * for json objects.
 * 
 * @author jansen
 *
 */
public class FrontendMessage {
	private String msgType;
	private String oscString;
	private float floatValue;
	private String stringValue;
	private boolean booleanValue;
	private byte[] binaryValue;
	private String fileName;
	private int fileSize;
	
	public FrontendMessage() {
		this.setMsgType("undefined");
		this.setFloatValue(-1f);
		this.setStringValue("");
		this.setBooleanValue(false);
	}

	public final String getMsgType() {
		return msgType;
	}

	public final void setMsgType(final String msgType) {
		this.msgType = msgType;
	}

	public final String getOscAddress() {
		return oscString;
	}

	public final void setOscAddress(final String msgString) {
		this.oscString = msgString;
	}

	public final float getFloatValue() {
		return floatValue;
	}

	public final void setFloatValue(final float floatValue) {
		this.floatValue = floatValue;
		this.setMsgType("float");
	}

	public final String getStringValue() {
		return stringValue;
	}

	public final void setStringValue(final String stringValue) {
		this.stringValue = stringValue;
		this.setMsgType("string");
	}

	public final boolean getBooleanValue() {
		return booleanValue;
	}

	public final void setBooleanValue(final boolean b) {
		this.booleanValue = b;
		this.setMsgType("boolean");
	}

	public byte[] getBinaryValue() {
		return binaryValue;
	}

	public void setBinaryValue(byte[] binaryValue) {
		this.binaryValue = binaryValue;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int i) {
		this.fileSize = i;
	}

}
