package com.minegame.data;

import java.util.Arrays;

/**
 * The CellQueue stores the cells and serves them
 * in the FIFO basis.
 */
public class JobQueue {
    private Job[] jobs;
    private int rear = -1;
    private int front = -1;
    private int size = 0;

    public JobQueue(int initSize) {
        this.jobs = new Job[initSize];
    }

    /**
     * Insert a Cell into the mine queue
     * @param job The job to insert
     */
    public void enqueue(Job job) {
        ensureCapacity();
        rear = (rear + 1) % jobs.length;
        jobs[rear] = job;
        size += 1;

        if(front < 0) front = 0;
    }

    /**
     * Remove the first Cell in the mine queue
     * @return The first cell in the queue
     */
    public Job dequeue() throws NullPointerException {
        if(isEmpty()) throw new NullPointerException();
        Job res = jobs[front];
        front = (front + 1) % jobs.length;
        size -= 1;
        return res;
    }

    /**
     * Peek at the first cell of the queue without removing it
     * @return The first cell in the queue
     */
    public Job peek() {
        return jobs[front];
    }

    /**
     * Makes sure the mineArr doesn't fill up and cause overrrrlappppp
     */
    private void ensureCapacity() {
        //TODO: Make this thing shrink at size = 1/4 * length also
        if(size >= jobs.length) {
            //We must expand
            Job[] newArr = new Job[jobs.length * 2];
            System.arraycopy(jobs, 0, newArr, 0, jobs.length);

            //Set the new array to the queue array, set front to zero, and rear to the size of the old array
            jobs = newArr;
            front = 0;
            rear = size - 1;
        }
    }

    /**
     * Returns true if the size of this queue is greater than 0
     * @return size == 0
     */
    public boolean isEmpty() {
        return size <= 0;
    }

    /**
     * Clears the queue of it's contents. If setNull is true, then it will nullify
     * all indexes in the array (in Theta-n time)
     * @param setNull
     */
    public void clear(boolean setNull) {
        if(setNull) {
            Arrays.fill(jobs, null);
        }
        front = -1;
        rear = -1;
        size = 0;
    }

    /**
     * Get the size of the queue
     * @return returns the size of the queue as an integer
     */
    public int size() {
        return size;
    }

    /**
     * Get will return the job at the specified index
     * @param i The index to access, starting from the front of the queue
     * @return Returns the Job at index
     */
    public Job get(int i) {
        return jobs[front + i];
    }
}
