package com.fuyao.myproject.entity;

import java.util.Vector;

public class Queue extends Vector<Schedule> {
	public Queue() {
		super();
		/* 调用Vector 构造方法。*/
	}

	public  void enq(Schedule x) {
		// x插入末尾处
		super.addElement(x);
	}

	public synchronized Schedule deq() {
		//  返回队列内第一个任务
		if (this.empty()) {
            throw new EmptyQueueException();
        }
		Schedule x = super.elementAt(0);
		super.removeElementAt(0);
		return x;
	}

	public synchronized Schedule front() {
		if (this.empty()) {
            throw new EmptyQueueException();
        }
		return super.elementAt(0);
	}
	
	public int length() {
		return super.size();
	}

	public synchronized boolean empty() {
		return super.isEmpty();
	}

	public synchronized void clear() {
		super.removeAllElements();
	}

	public int search(Object x) {
		return super.indexOf(x);
	}

	public class EmptyQueueException extends RuntimeException {
		public EmptyQueueException() {
			super();
		}
	}
}
