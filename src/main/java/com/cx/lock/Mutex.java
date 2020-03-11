package com.cx.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;


/**
 * Created with IntelliJ IDEA.
 * User: CloudCx
 * Date: 2020/2/7
 * Time: 23:25
 * Description: 独占锁实现  调用同步器模板方法
 */
public class Mutex implements Lock {
    //静态内部类 自定义同步器
    private static class Sync extends AbstractQueuedSynchronizer{
        //判断是否处于独占状态
        protected boolean isHeldExclusively(){
            return getState() == 1;
        }
        //当前状态为0时获取锁  设置当前拥有独占访问的线程
        public boolean tryAcquire(int acquires){
            if(compareAndSetState(0,1)){
                //设置当前拥有独占访问的线程
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }
        //释放锁  状态设置为0
        protected boolean tryRelease(int releases){
            if(getState() == 0){
                setExclusiveOwnerThread(null);
            }
            setState(0);
            return true;
        }

        //返回一个condition
        Condition newCondition(){ return new ConditionObject();}
    }

    //将操作代理到sync上
    private final Sync sync = new Sync();

    @Override
    public void lock() {
       sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1,unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }

    /**
     * 判断当前线程是否获取锁
     * @return
     */
    public boolean hasQueuedThreads(){
        return sync.hasQueuedThreads();
    }

    /**
     * 判断是否处于独占状态
     * @return
     */
    public boolean isLocked(){
        return sync.isHeldExclusively();
    }
}
