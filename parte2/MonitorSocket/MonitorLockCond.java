package parte2.MonitorSocket;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorLockCond {
    private  ReentrantLock mutex = new ReentrantLock();
	private  Condition condR = mutex.newCondition();
	private  Condition condW = mutex.newCondition();
	private int nr,nw;
	
	public MonitorLockCond() {
		nr=0;
		nw=0;
	}
	
	public void requestRead() {
		mutex.lock();
		while(nw > 0) {
			try {
				condR.wait();
			} catch (InterruptedException e) {e.printStackTrace();}
		}
		nr++;
		mutex.unlock();
	}
	public void releaseRead() {
		mutex.lock();
		nr--;
		if (nr == 0)
			condW.signal();
		mutex.unlock();
	}
	public void requestWrite() {
		mutex.lock();
		while (nr > 0 || nw > 0) {
			try {
				condW.wait();
			} catch (InterruptedException e) {e.printStackTrace();}
		}
		nw++;
		mutex.unlock();
	}
	public void releaseWrite() {
		mutex.lock();
		nw--;
		condW.signal();
		condR.signal();
		mutex.unlock();
	}
}
