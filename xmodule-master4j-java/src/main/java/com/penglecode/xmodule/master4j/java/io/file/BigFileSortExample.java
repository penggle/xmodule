package com.penglecode.xmodule.master4j.java.io.file;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/10/2 20:23
 */
public class BigFileSortExample {

    public static void iterateBigFile1() throws Exception {
        long start = System.currentTimeMillis();
        File targetFile = new File("d:/temp/stackoverflow.com-Users/Users.xml");
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(targetFile, "r")) {
            String line;
            int totalRows = 0;
            while ((line = randomAccessFile.readLine()) != null) {
                line = line.trim();
                if(line.startsWith("<row")) {
                    totalRows++;
                }
                randomAccessFile.seek(randomAccessFile.getFilePointer());
            }
            System.out.println(totalRows);
        }
        long end = System.currentTimeMillis();
        System.out.println("total cost: " + ((end - start) / 1000) + "s");
    }

    public static void iterateBigFile2() throws Exception {
        long start = System.currentTimeMillis();
        File targetFile = new File("d:/temp/stackoverflow.com-Users/Users.xml");
        long batchChunkSize = 31457280; //30MB
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(targetFile, "r")) {
            try (FileChannel fileChannel = randomAccessFile.getChannel()) {
                long totalFileSize = randomAccessFile.length();
                long lineStartPosition = 0;
                long splitChunkSize;

                MappedByteBuffer byteBuffer;
                CharBuffer charBuffer;

                int totalRows = 0;
                long remainings;
                while(true) {
                    remainings = totalFileSize - lineStartPosition;
                    if(remainings <= 0) {
                        break;
                    }
                    splitChunkSize = Math.min(remainings, batchChunkSize);
                    byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, lineStartPosition, splitChunkSize);
                    if(!byteBuffer.hasRemaining()) {
                        break;
                    }
                    charBuffer = StandardCharsets.UTF_8.decode(byteBuffer);

                    char c;
                    String line;
                    StringBuilder sb = new StringBuilder();
                    int lastLineBreakPosition = 0;
                    while(charBuffer.hasRemaining()) {
                        c = charBuffer.get();
                        if(c == '\n' || c == '\r') {
                            lastLineBreakPosition = charBuffer.position();
                            //遇到换行符
                            if(sb.length() > 0) {
                                line = sb.toString().trim();
                                if(line.startsWith("<row")) {
                                    totalRows++;
                                }
                                sb.setLength(0);
                            }
                        } else {
                            sb.append(c);
                        }
                    }
                    if(lastLineBreakPosition == 0) {
                        line = sb.toString().trim();
                        if(line.startsWith("<row")) {
                            totalRows++;
                        }
                        break;
                    }
                    lineStartPosition += lastLineBreakPosition;
                }
                System.out.println(totalRows);
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("total cost: " + ((end - start) / 1000) + "s");
    }

    public static void iterateBigFile3() throws Exception {
        File targetFile = new File("d:/temp/stackoverflow.com-Users/Users.xml");
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(targetFile, "r")) {
            System.out.println(randomAccessFile.length());
            MappedByteBuffer byteBuffer = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, 1024 * 1024 * 30);
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(byteBuffer);
            System.out.println(charBuffer.length());
        }
    }

    public static void main(String[] args) throws Exception {
        //iterateBigFile1();
        //iterateBigFile2();
        iterateBigFile3();
    }

}
