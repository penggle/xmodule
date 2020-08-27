package com.penglecode.xmodule.master4j.java.io.file;

import com.penglecode.xmodule.common.util.ThreadPoolUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 大文件多线程读取
 * stackoverflow网站用户数，7z文件大小574MB，解压后Users.xml文件大小3.8GB
 * https://archive.org/download/stackexchange/stackoverflow.com-Users.7z
 *
 *
 * 主要思路:
 * 先将大文件分割成一小段一小段的(比如以平均30M大小分割一小段),见splitFile()方法,
 * 然后按分割段通过MappedByteBuffer来做多线程并发操作段内的数据。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/17 15:42
 */
public class BigFileConcurrencyReadExample {

    private static final ExecutorService EXECUTOR_SERVICE = ThreadPoolUtils.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

    protected static List<FileSplitPosition> splitFile(File targetFile, long splitFileSize) throws Exception {
        List<FileSplitPosition> splitPositions = new ArrayList<>();
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(targetFile, "r")) {
            long totalFileSize = randomAccessFile.length();
            int splitCount = totalFileSize % splitFileSize == 0 ? (int) (totalFileSize / splitFileSize) : (int) (totalFileSize / splitFileSize) + 1;
            long startPosition, endPosition = 0;
            for(int i = 0; i < splitCount; i++) {
                startPosition = endPosition;
                endPosition = splitFileSize * (i + 1);
                if(endPosition > totalFileSize) {
                    endPosition = totalFileSize;
                } else { //矫正endPosition，使得endPosition处于一个完整的行开始处
                    //读写指针跳到endPosition处读该行的剩余部分，使得当前指针处于下一个完整行的开始并重新赋给endPosition
                    randomAccessFile.seek(endPosition);
                    randomAccessFile.readLine();
                    endPosition = randomAccessFile.getFilePointer();
                }
                splitPositions.add(new FileSplitPosition(startPosition, endPosition));
            }
        }
        return splitPositions;
    }

    public static void computeDataTotalLines() throws Exception {
        File targetFile = new File("d:/temp/stackoverflow.com-Users/Users.xml");
        long splitFileSize = 41097350;
        long start = System.currentTimeMillis();
        List<FileSplitPosition> splitPositions = splitFile(targetFile, splitFileSize);
        long end = System.currentTimeMillis();
        System.out.println(String.format("【%s】>>> split position cost : %s 毫秒", Thread.currentThread().getName(), (end - start)));
        AtomicLong lineCounter = new AtomicLong(0);
        CountDownLatch countDownLatch = new CountDownLatch(splitPositions.size());
        for(FileSplitPosition splitPosition : splitPositions) {
            //System.out.println(String.format("【%s】>>> counting start : %s", Thread.currentThread().getName(), splitPosition));
            EXECUTOR_SERVICE.submit(new SplitFileLinesCollector(targetFile, splitPosition, lineCounter, countDownLatch));
        }
        countDownLatch.await();
        //总数据行数：12485155
        System.out.println("总数据行数：" + lineCounter.intValue());
    }

    public static void main(String[] args) throws Exception {
        computeDataTotalLines();
    }

    static class SplitFileLinesCollector implements Runnable {

        private final File targetFile;

        private final FileSplitPosition splitPosition;

        private final AtomicLong lineCounter;

        private final CountDownLatch countDownLatch;

        public SplitFileLinesCollector(File targetFile, FileSplitPosition splitPosition, AtomicLong lineCounter, CountDownLatch countDownLatch) {
            this.targetFile = targetFile;
            this.splitPosition = splitPosition;
            this.lineCounter = lineCounter;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            System.out.println(String.format("【%s】>>> couting start, position : %s", Thread.currentThread().getName(), splitPosition));
            long start = System.currentTimeMillis();
            try (RandomAccessFile randomAccessFile = new RandomAccessFile(targetFile, "r")) {
                long mappedBytes = splitPosition.getEndPosition() - splitPosition.getStartPosition();
                MappedByteBuffer mappedByteBuffer = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_ONLY, splitPosition.getStartPosition(), mappedBytes);
                CharBuffer charBuffer = StandardCharsets.UTF_8.decode(mappedByteBuffer); //一定要先decode,否则后面getChar()全是乱码
                List<String> lines = new ArrayList<>((int) mappedBytes);
                StringBuilder sb = new StringBuilder();
                char c;
                String line;
                while(charBuffer.hasRemaining()) {
                    c = charBuffer.get();
                    if(c == '\n' || c == '\r') { //遇到换行符时那么前面收集到的字符串将是一个整行
                        if(sb.length() > 0) {
                            line = sb.toString().trim();
                            if(line.startsWith("<row")) { //如果当前是数据行
                                lines.add(line);
                            }
                            sb.setLength(0);
                        }
                    } else {
                        sb.append(c);
                    }
                }
                lineCounter.addAndGet(lines.size());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
                long end = System.currentTimeMillis();
                System.out.println(String.format("【%s】<<< couting end, cost : %s秒", Thread.currentThread().getName(), (end - start) / 1000));
            }
        }

    }

    static class FileSplitPosition {

        private final long startPosition;

        private final long endPosition;

        public FileSplitPosition(long startPosition, long endPosition) {
            this.startPosition = startPosition;
            this.endPosition = endPosition;
        }

        public long getStartPosition() {
            return startPosition;
        }

        public long getEndPosition() {
            return endPosition;
        }

        @Override
        public String toString() {
            return "[" + startPosition + ", " + endPosition + "]";
        }
    }

}
