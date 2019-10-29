package com.minegame.data;

import com.minegame.world.Cell;

import java.util.Arrays;

/**
 * The MineQueue stores the blocks marked for mining and serves them
 * to the Mants in the FIFO basis.
 */
public class MineQueue {
    private Cell[] mineArr;
    private int rear = -1;
    private int front = -1;
    private int size = 0;

    public MineQueue(int initSize) {
        this.mineArr = new Cell[initSize];
    }

    /**
     * Insert a Cell into the mine queue
     * @param cell The cell to insert
     */
    public void enqueue(Cell cell) {
        ensureCapacity();
        rear = (rear + 1) % mineArr.length;
        mineArr[rear] = cell;
        size += 1;

        if(front < 0) front = 0;
    }

    /**
     * Remove the first Cell in the mine queue
     * @return The first cell in the queue
     */
    public Cell dequeue() throws NullPointerException {
        if(isEmpty()) throw new NullPointerException();
        Cell res = mineArr[front];
        front = (front + 1) % mineArr.length;
        size -= 1;
        return res;
    }

    /**
     * Peek at the first cell of the queue without removing it
     * @return The first cell in the queue
     */
    public Cell peek() {
        return mineArr[front];
    }

    /**
     * Makes sure the mineArr doesn't fill up and cause overrrrlappppp
     */
    private void ensureCapacity() {
        //TODO: Make this thing shrink at size = 1/4 * length also
        if(size >= mineArr.length) {
            //We must expand
            Cell[] newArr = new Cell[mineArr.length * 2];
            System.arraycopy(mineArr, 0, newArr, 0, mineArr.length);

            //Set the new array to the queue array, set front to zero, and rear to the size of the old array
            mineArr = newArr;
            front = 0;
            rear = size - 1;
        }
    }

    /**
     * Returns true if the size of this queue is greater than 0
     * @return size == 0
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears the queue of it's contents. If setNull is true, then it will nullify
     * all indexes in the array (in Theta-n time)
     * @param setNull
     */
    public void clear(boolean setNull) {
        if(setNull) {
            Arrays.fill(mineArr, null);
        }

        front = -1;
        rear = -1;
        size = 0;
    }

}
