package com.poles.day5;import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.locks.ReentrantLock;

/**
*********************************************************************
* 
* @author poles
* @date 2019-05-26 23:27
* @desc 
* 公平锁：公平锁就是先来先到，先申请锁的线程，先获取到锁，不会产生饥饿现象，但是因为要维持排队现象，所以性能上会有所受限:
 *          ReentrantLock lock = new ReentrantLock(true);
 *
* 非公平锁：默认情况下，是非公平锁。
 *          ReentrantLock lock = new ReentrantLock();
 *          ReentrantLock lock = new ReentrantLock(false);
*********************************************************************
*/
public class ReentrantFairLock {
    ReentrantLock lock = new ReentrantLock(true);
}
