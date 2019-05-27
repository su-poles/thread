package com.poles.day4.amino;

import java.util.AbstractList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
*********************************************************************
* 
* @author poles
* @date 2019-05-24 11:07
* @desc 
*
*********************************************************************
*/
public class LockFreeVector<E> extends AbstractList<E> {
    /**
     * Size of the first bucket. sizeof(bucket[i+1]=2*sizeof(bucket[i])
     */
    private static final int FIRST_BUCKET_SIZE = 8;
    /**
     * number of buckets. 30 will allow 8 * ( 2^30 - 1) elements
     */
    private static final int N_BUCKET = 30;

    /**
     * we will have at most N_BUCKET number of buckets. And we have sizeof(buckets.get(i)) = FIRST_BUCKET_SIZE**(i+1)
     */
    private final AtomicReferenceArray<AtomicReferenceArray<E>> buckets;

    private AtomicReference<Descriptor<E>> descriptor;

    static class WriterDescriptor<E> {
        //替换前的值
        public E oldV;
        //当前值
        public E newV;
        //bucket的索引，也就是指当前元素在那个bucket中
        public AtomicReferenceArray<E> addr;
        //当前数组中的下标索引。  这是一个二维数组，外侧数组为addr， 内侧数组为addr_ind, 也就是真正存储值的数组为addr_ind
        public int addr_ind;

        public WriterDescriptor(AtomicReferenceArray<E> addr, int addr_ind, E oldV, E newV){
            this.addr = addr;
            this.addr_ind = addr_ind;
            this.oldV = oldV;
            this.newV = newV;
        }

        /**
         * set newV
         */
        public void doIt(){
            //多个线程重复做，无伤大雅，只要有一个成功了，就OK了
            addr.compareAndSet(addr_ind, oldV, newV);
        }
    }

    /**
     * Descritpor类，里面封装了Vector中要存储的整整元素
     * @param <E>
     */
    static class Descriptor<E>{
        public int size;
        /** 其实真正的元素值被封装在这个对象中，哈哈，封装的很详细的*/
        volatile WriterDescriptor<E> writerop;

        public Descriptor(int Size, WriterDescriptor<E> writerop) {
            this.size = size;
            this.writerop = writerop;
        }

        public void completeWrite(){
            WriterDescriptor<E> tempOp = writerop;
            if (tempOp != null) {
                tempOp.doIt();
                //This is safe since all write to writerop use
                //对于所有使用writerop写入的线程来说，这是一种安全的操作
                writerop = null;  //null as r_value   赋值为null, r_value即赋值号右边的值，即即将要赋予的那个值
            }
        }
    }


    private AtomicReference<Descriptor<E>> decriptor;
    //第一个前导零的个数
    private static final int zeroNumFirst = Integer.numberOfLeadingZeros(FIRST_BUCKET_SIZE);

    /**
     * Constructor
     */
    public LockFreeVector(){
        buckets = new AtomicReferenceArray<>(N_BUCKET);
        buckets.set(0, new AtomicReferenceArray<>(FIRST_BUCKET_SIZE));
        decriptor = new AtomicReference<>(new Descriptor<>(0, null));
    }

    /**
     * Add e at end of vector
     * 使用cas操作时无非就是：compareAndSet(index, OldValue, newValue) 不过，这个OldValue一定是null
     * 如果是删除操作，那么newValue就一定是null, 如果假设有覆盖更新方法的话，这个OldValue和newValue就什么都可以啦
     * @param e element added
     * @return
     */
    public void push_back(E e) {
        Descriptor<E> desc;
        Descriptor<E> newd;

        do{
            //取到一个Descriptor对象，这个对象里封装这真正的e这个值
            desc = descriptor.get();
            desc.completeWrite();  //清空之前执行的WriterDescriptor对象，也就是tempOp

            //desc.size是Vector自身的元素个数， FIRST_BUCKET_SIZE为第一个数组的大小
            int pos = desc.size + FIRST_BUCKET_SIZE;
            //获取pos的前导零数量
            int zeroNumPos = Integer.numberOfLeadingZeros(pos);
            //zeorNumFirst为FIRST_BUCKET_SIZE的前导零
            int bucketInd = zeroNumFirst - zeroNumPos;  //哪一个数组
            //判断这个数组是否已经启用，也就是是否初始化，如果没有就要初始化
            if(buckets.get(bucketInd) == null){
                int newlen = 2 * buckets.get(bucketInd - 1).length();
                //在index为bucketInd的地方，初始化一个新的原子引用数组，这个新的数组的长度为newlen
                buckets.compareAndSet(bucketInd, null, new AtomicReferenceArray<>(newlen));
            }

            //在新的数组中的位置，如果是刚初始化的，当然是第一个，如果不是呢，得计算位置。 >>> 无符号右移，就是不考虑符号位，正常补0即可。 如果是有符号的负数右移，那么高位需要补1， 没有无符号左移这个操作。
            //0x80000000为整数最小值，然后无符号右移zeroNumPos, 实际上就是 pos 所在段的最大值， pos所在段指的就是8，16， 31这种段
            //8   0000 0000 0000 0000 0000 0000 00001000        0x80000000 >>> 28 位， 28就是 pos的前导零个数
            //16  0000 0000 0000 0000 0000 0000 00010000        0x80000000 >>> 27 位， 27就是 pos的前导零个数
            //32  0000 0000 0000 0000 0000 0000 00100000        0x80000000 >>> 26 位， 26就是 pos的前导零个数
            //64  ..... 以此类推
            //举例说明：当前vector要增加第15个元素，那么 pos = 15 + 8 = 23 (10111),  前导零zeroNumPos = 27 (32位 - 5位),  0x80000000 >>> 27 = 16 (0000 0000 0000 0000 0000 0000 00010000)
            //然后与 pos进行按位异或，即
            //                        0000 0000 0000 0000 0000 0000 00010000
            //                      ^
            //                        0000 0000 0000 0000 0000 0000 00010111
            //                        --------------------------------------
            //                        0000 0000 0000 0000 0000 0000 00000111     = 7,
            //说白了，第15个元素就插入在第二个buckets中的第7个位置，用二维数组的表达方式就是Vector[1][6]
            //不过这里的Vector元素放在了AtomicReferenceArray<AtomicReferenceArray<E>> 中了
            int idx = (0x80000000 >>> zeroNumPos) ^ pos;

            //终于到了最关键的一步，就是封装新值，外面add进来的是一个E e, 需要将这元素封装一下，真正放入AtomicReferenceArray的元素实际上是Descriptor对象，
            //这一步就是将e封装成Descriptor, push时，也是通过cas的方式设置值，那么oldValue一定是null (addr.compareAndSet(addr_ind, null, newV);)
            newd = new Descriptor<E>(desc.size + 1, new WriterDescriptor<E>(buckets.get(bucketInd), idx, null, e));

            //此时此刻，首先buckets已经通过compareAndSet设置完成了，参考前面的if(buckets.get(bucketInd) == null)...这段代码
            //然后buckets中二维数组的内部数组的值还没有进行设置, 这部的设置是需要通过调用WriterDescriptor的doIt()完成，或者通过Descriptor的completeWrite完成。
            //但是此处Descriptor对象中没有提供WriterDescriptor的get方法，是获取不到WriterDescriptor对象的，所以只能通过Descriptor的completeWriter方法去完成内部数组的赋值操作
            //除了内部数组的赋值操作外，本类中还有一个原子对象需要赋值，就是这个private AtomicReference<Descriptor<E>> descriptor;
            //这里通过while循环不停的进行cas操作，直到将这个描述器中的内容写正确之后，在最后一句才真正调用completeWrite将二维数组的内部数组的值设置进去，完了之后将封装的WriterDescriptor对象，也就是tempOp进行清空操作。
            //while循环的本质就是不断的去占位，怎么占位呢，就是desc.size + 1, 如果新的Descriptor被设置成功，那么相当于size = size + 1, 那么占位就成功了，剩下的就与其它线程不相关了，
            //然后将descriptor对象先get出来，然后调用completeWrite，完成内侧一维数组中的值的更新（也就是把e这个元素放进去），而且只能放一次，一次成功之后就设置writerop = null了
        }while(!decriptor.compareAndSet(desc, newd));
        descriptor.get().completeWrite();
     }

    /**
     * Remove the last element in the vector
     */
    public E pop_back() {
        Descriptor<E> desc;
        Descriptor<E> newd;
        E elem;
        do {
            desc = descriptor.get();
            desc.completeWrite();
            int pos = desc.size + FIRST_BUCKET_SIZE;
            int zeroNumPos = Integer.numberOfLeadingZeros(pos);
            int bucketInd = zeroNumFirst - zeroNumPos;

            int idx = (0x80000000 >>> zeroNumPos) ^ pos;

            //终于到了最关键的一步，就是封装新值，外面add进来的是一个E e, 需要将这元素封装一下，真正放入AtomicReferenceArray的元素实际上是Descriptor对象，
            //这一步就是将e封装成Descriptor, push时，也是通过cas的方式设置值，那么oldValue一定是null (addr.compareAndSet(addr_ind, null, newV);)
            elem = buckets.get(bucketInd).get(idx);
            //感觉这地方特别想LinkedList中的头指针那种感觉，我也还没理解透，整个Vector实际上就是一个Descriptor这个东西在维护，这句话是我迷迷糊糊加的，可以不用参考
            newd = new Descriptor<E>(desc.size - 1, null);

        }while(!descriptor.compareAndSet(desc, newd));

        return elem;
     }

    @Override
    public E get(int index) {
        int pos = index + FIRST_BUCKET_SIZE;
        int zeroNumPos = Integer.numberOfLeadingZeros(pos);
        int bucketInd = zeroNumFirst - zeroNumPos;
        int idx = (0x80000000 >>> zeroNumPos) ^ pos;
        return buckets.get(bucketInd).get(idx);
    }

    @Override
    public E set(int index, E e){
        int pos = index + FIRST_BUCKET_SIZE;
        int zeroNumPos = Integer.numberOfLeadingZeros(pos);
        int bucketInd = zeroNumFirst - zeroNumPos;
//        int idx = (0x80000000 >>> zeroNumPos) ^ pos;
        int idx = Integer.highestOneBit(pos) ^ pos;
        AtomicReferenceArray<E> bucket = buckets.get(bucketInd);
        while (true) {
            E oldValue = bucket.get(idx);
            if(bucket.compareAndSet(idx, oldValue, e)){
                return oldValue;
            }
        }
    }

    @Override
    public int size() {
        return descriptor.get().size;
    }

    @Override
    public boolean add(E e) {
        push_back(e);
        return true;
    }

    public static void main(String[] args) {
        //先明确点，java的 << >> >>>都是针对补码操作的，因为java在内存中只存储补码

//        System.out.println(2 >> 2);
//        0000 0000 0000 0000    0000 0000 0000 0010   == 2
          // 2 << 30
//       1000 0000 0000 0000    0000 0000 0000 0000
         //这个东西被认为定义成-2146473648， 因为在64位系统中没有任何数值的原码可以转成1000 0000 0000 0000    0000 0000 0000 0000这种补码

        //二进制中最小的数值为1111 1111 1111 1111 1111 1111 1111 1111  记住，java中int占4个字节，也就是4 * 8 = 32位。

        //左移时不会影响其符号位， 整数没有符号位，所以可以随便移动，一不小心把1移到最高位就成为负数了
        //但是负数有符号位，左移时符号位不动，你大爷还是你大爷，哦，不对，是符号位还是符号位
        //正数没有符号位，所以右移也不影响
        //负数有符号位，右移时就要区分有符号右移还是无符号右移

//        int a = 2;
//        System.out.println(Integer.toBinaryString(2));
//        System.out.println(2 << 30);
//        System.out.println(Integer.toBinaryString(2 << 30));
//        String 二进制字符串 = "01111111111111111111111111111111";  //第1位为符号位，所以这是正数里的最大值，也就是后面31个1， 也就是2^32-1
        String 二进制字符串 = "11111111111111111111111111111111";  //第1位为符号位，所以这是正数里的最大值，也就是后面31个1， 也就是2^32-1
//        System.out.println(Integer.parseInt(二进制字符串, 2));


//        String str = "01111111111111111111111111111110";  //32位，第一位为符号位
//        int strNum = Integer.parseInt(str, 2);
//        System.out.println(strNum);
//        int strLeftMove = strNum << 1;  //11111111111111111111111111111100
//                                        //10000000000000000000000000000100   //左移之后，存储时需要补码，就成了-4了
//        System.out.println(strLeftMove);

        System.out.println(-2 << 2);
    }
}
