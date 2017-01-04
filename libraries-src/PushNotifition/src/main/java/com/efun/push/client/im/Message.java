package com.efun.push.client.im;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

public final class Message {
	public static int version = 1;
	public static final int SERVER_MESSAGE_MIN_LENGTH = 5;
	public static final int CLIENT_MESSAGE_MIN_LENGTH = 21;
	public static final int CMD_0x00 = 0;
	public static final int CMD_0x10 = 16;
	public static final int CMD_0x11 = 17;
	public static final int CMD_0x20 = 32;
	protected SocketAddress address;
	protected byte[] data;

	public Message(SocketAddress address, byte[] data) {
		this.address = address;
		this.data = data;
	}

	public int getContentLength() {
		return ByteBuffer.wrap(this.data, 3, 2).getChar();
	}

	public int getCmd() {
		byte b = this.data[2];
		return b & 0xFF;
	}

	public boolean checkFormat() {
		if ((this.address == null) || (this.data == null) || (this.data.length < 5)) {
			return false;
		}
		int cmd = getCmd();
		if ((cmd != 0) && (cmd != 16) && (cmd != 17) && (cmd != 32)) {
			return false;
		}
		int dataLen = getContentLength();
		if (this.data.length != dataLen + 5) {
			return false;
		}
		if ((cmd == 16) && (dataLen != 0)) {
			return false;
		}

		if ((cmd == 17) && (dataLen != 8)) {
			return false;
		}

		if ((cmd == 32) && (dataLen < 1)) {
			return false;
		}
		return true;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public byte[] getData() {
		return this.data;
	}

	public void setSocketAddress(SocketAddress address) {
		this.address = address;
	}

	public SocketAddress getSocketAddress() {
		return this.address;
	}

	public static void setVersion(int v) {
		if ((v < 1) || (v > 255)) {
			return;
		}
		version = v;
	}

	public static int getVersion() {
		return version;
	}
}
