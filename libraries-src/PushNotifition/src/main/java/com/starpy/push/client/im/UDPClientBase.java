package com.starpy.push.client.im;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

import android.os.Process;
import android.util.Log;

public abstract class UDPClientBase implements Runnable {
	private static final String TAG = "push_UDPClientBase";
	protected DatagramSocket ds;
	protected long lastSent = 0L;
	protected long lastReceived = 0L;
	protected int remotePort = 9966;
	protected int appid = 1;
	protected byte[] uuid;
	protected String remoteAddress = null;
	protected ConcurrentLinkedQueue<Message> mq = new ConcurrentLinkedQueue<Message>();

	protected AtomicLong queueIn = new AtomicLong(0L);
	protected AtomicLong queueOut = new AtomicLong(0L);

	protected int bufferSize = 1024;
	/**
	 *  心跳间隔默认50s
	 */
	protected int heartbeatInterval = 50;

	protected byte[] bufferArray;
	protected ByteBuffer buffer;

	protected volatile boolean needReset = true;
	protected volatile boolean started = false;
	protected volatile boolean stoped = false;
	
	protected Thread udpWorkThread;
//	protected Worker worker;
//	protected Thread workerT;
	private long sentPackets;
	private long receivedPackets;
	
	private String lockObj = "";
	private final static long lockWaitTime = 1000 * 6L;//线程循环等待的时间

	public UDPClientBase(byte[] uuid, int appid, String serverAddr, int serverPort) throws Exception {
		if ((uuid == null) || (uuid.length != 16)) {
			throw new IllegalArgumentException("uuid byte array must be not null and length of 16 bytes");
		}
		if ((appid < 1) || (appid > 255)) {
			throw new IllegalArgumentException("appid must be from 1 to 255");
		}
		if ((serverAddr == null) || (serverAddr.trim().length() == 0)) {
			throw new IllegalArgumentException("server address illegal: " + serverAddr);
		}

		this.uuid = uuid;
		this.appid = appid;
		this.remoteAddress = serverAddr;
		this.remotePort = serverPort;
	}

	/**
	 * <p>Description: 消息入队</p>
	 * @author GanYuanrong
	 * @param message
	 * @return
	 * @date 2014年12月6日
	 */
	protected boolean enqueue(Message message) {
		boolean result = this.mq.add(message);
		if (result) {
			this.queueIn.addAndGet(1L);
		}
		return result;
	}

	/**
	 * <p>Description: 消息出队</p>
	 * @author GanYuanrong
	 * @return
	 * @date 2014年12月6日
	 */
	protected Message dequeue() {
		Message m = (Message) this.mq.poll();
		if (m != null) {
			this.queueOut.addAndGet(1L);
		}
		return m;
	}

	private synchronized void init() {
		this.bufferArray = new byte[this.bufferSize];
		this.buffer = ByteBuffer.wrap(this.bufferArray);
	}

	/**
	 * <p>Description: 在可用网络下重置udp socket链接</p>
	 * @author GanYuanrong
	 * @throws SocketException 
	 * @throws Exception
	 * @date 2014年12月6日
	 */
	protected synchronized void resetDatagramSocket() throws SocketException{
		if (!this.needReset) {
			Log.d(TAG, "not need reset...");
			return;
		}

		if (this.ds != null) {
			this.ds.close();
		}
		
		if (hasNetworkConnection()) {
			Log.d(TAG, "has network...");
			this.ds = new DatagramSocket();
			Log.d(TAG, "InetSocketAddress  addr:" + remoteAddress + " port:" + remotePort);
			this.ds.connect(new InetSocketAddress(this.remoteAddress, this.remotePort));
			this.needReset = false;
		}
	}

	public synchronized void startUdpThread() throws Exception {
		if (this.started) {
			return;
		}
		init();

		this.udpWorkThread = new Thread(this, "udp-push-thread");
		udpWorkThread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				Log.e(TAG, "uncaughtException:" + e.getMessage());
			}
		});
		this.udpWorkThread.start();
	//	this.receiverT.setDaemon(true);//设置守护进程
		this.started = true;
	}

	public void stopUdpThread() throws Exception{
		this.stoped = true;
		this.needReset = true;
		this.started = false;
		Log.d(TAG, "stopUdpThread");
		synchronized (lockObj) {
			this.stoped = true;
			lockObj.notifyAll();//通知接收线程走完
		}
		if (this.ds != null) {
			this.ds.close();
			this.ds = null;
		}
		if (this.udpWorkThread != null) {
			this.udpWorkThread.interrupt();
		}
	}

	public void run() {
		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
		while (!this.stoped) {
			Log.d(TAG, "while...running");
			if (!hasNetworkConnection()) {
				trySystemSleep();
				this.stoped = true;
			} else {
				try {
					synchronized (lockObj) {
						Log.d(TAG, "wait lock receive data...");
						lockObj.wait(lockWaitTime);
					}
					if (stoped) {
						break;
					}
					resetDatagramSocket();
					heartbeat();
					receiveData();
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					if (stoped) {
						break;
					}
					e.printStackTrace();
				}
			}
		}
		needReset = true;
		started = false;
		stoped = true;
		if (this.ds != null) {
			this.ds.close();
			this.ds = null;
		}
		Log.d(TAG, "udp thread end");
	}

	/**
	 * <p>Description: 发送UDP心跳，心跳间隔默认50s</p>
	 * @author GanYuanrong
	 * @throws IOException 
	 * @date 2014年12月6日
	 */
	private void heartbeat() throws IOException {
		if (System.currentTimeMillis() - this.lastSent < this.heartbeatInterval * 1000) {
			Log.d(TAG, "currentTimeMillis < heart beat Interval ...");
			return;
		}
		byte[] buffer = new byte[21];
		ByteBuffer.wrap(buffer).put((byte) Message.version).put((byte) this.appid).put((byte) 0).put(this.uuid).putChar('\000');
		send(buffer);
	}

	/**
	 * <p>Description: 接收UDP数据包</p>
	 * @author GanYuanrong
	 * @throws IOException 
	 * @date 2014年12月6日
	 */
	private void receiveData(){
		DatagramPacket dp = new DatagramPacket(this.bufferArray, this.bufferArray.length);
		try {
			this.ds.setSoTimeout(5000);
			Log.d(TAG, "receiving message...");
			this.ds.receive(dp);//接收数据，该方法会阻塞线程，直到timeout
		} catch (IOException e) {
			//e.printStackTrace();
			//Log.d(TAG, "not receive message,exception:SocketException or IOException");
			return;
		}
	
		if ((dp.getLength() <= 0) || (dp.getData() == null) || (dp.getData().length == 0)) {
			return;
		}
		byte[] data = new byte[dp.getLength()];
		
		System.arraycopy(dp.getData(), 0, data, 0, dp.getLength());
		try {
			Log.d(TAG, "push data:" + new String(data,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Message m = new Message(dp.getSocketAddress(), data);
		if (!m.checkFormat()) {
			Log.d(TAG, "format error");
			return;
		}
		this.receivedPackets += 1L;
		this.lastReceived = System.currentTimeMillis();
		try {
			ackServer(m);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (m.getCmd() == 0) {
			return;
		}
		enqueue(m);
		Log.d(TAG, "wakeup worker...");
	//	this.worker.wakeup();
		handleEvent();
	}

	private void ackServer(Message m) throws IOException{
		if (m.getCmd() == 16) {
			byte[] buffer = new byte[21];
			ByteBuffer.wrap(buffer).put((byte) Message.version).put((byte) this.appid).put((byte) 16).put(this.uuid).putChar('\000');
			send(buffer);
		}
		if (m.getCmd() == 17) {
			byte[] buffer = new byte[29];
			byte[] data = m.getData();
			ByteBuffer.wrap(buffer).put((byte) Message.version).put((byte) this.appid).put((byte) 17).put(this.uuid).putChar('\b').put(data, 5, 8);
			send(buffer);
		}
		if (m.getCmd() == 32) {
			byte[] buffer = new byte[21];
			ByteBuffer.wrap(buffer).put((byte) Message.version).put((byte) this.appid).put((byte) 32).put(this.uuid).putChar('\000');
			send(buffer);
		}
	}

	private void send(byte[] data) throws IOException{
		if (data == null) {
			return;
		}
		if (this.ds == null) {
			return;
		}
		Log.d(TAG, "send data packet ...");
		DatagramPacket dp = new DatagramPacket(data, data.length,InetAddress.getByName(remoteAddress),remotePort);
		//dp.setSocketAddress(this.ds.getRemoteSocketAddress());
		//dp.setPort(this.ds.getPort());
		this.ds.send(dp);
		this.lastSent = System.currentTimeMillis();
		this.sentPackets += 1L;
	}



	/**
	 * <p>Description: 处理消息事件</p>
	 * @author GanYuanrong
	 * @throws Exception
	 * @date 2014年12月6日
	 */
	private void handleEvent() {
		Message m = null;
		for (;;) {
			m = UDPClientBase.this.dequeue();
			if (m == null) {
				Log.d(TAG, "dequeue return..");
				return;
			}
			if (m.checkFormat()) {
				UDPClientBase.this.onPushMessage(m);
			}
		}
	}
	public abstract boolean hasNetworkConnection();
	
	public abstract void trySystemSleep();

	public abstract void onPushMessage(Message paramMessage);
	
	
	public long getSentPackets() {
		return this.sentPackets;
	}

	public long getReceivedPackets() {
		return this.receivedPackets;
	}

	public void setServerPort(int port) {
		this.remotePort = port;
	}

	public int getServerPort() {
		return this.remotePort;
	}

	public void setServerAddress(String addr) {
		this.remoteAddress = addr;
	}

	public String getServerAddress() {
		return this.remoteAddress;
	}

	public void setBufferSize(int bytes) {
		this.bufferSize = bytes;
	}

	public int getBufferSize() {
		return this.bufferSize;
	}

	public long getLastHeartbeatTime() {
		return this.lastSent;
	}

	public long getLastReceivedTime() {
		return this.lastReceived;
	}

	public void setHeartbeatInterval(int second) {
		if (second <= 0) {
			return;
		}
		this.heartbeatInterval = second;
	}

	public int getHeartbeatInterval() {
		return this.heartbeatInterval;
	}
}
