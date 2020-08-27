package com.penglecode.xmodule.master4j.java.io.file;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * RandomAccessFile允许你来回读写文件，也可以替换文件中的某些部分。FileInputStream和FileOutputStream没有这样的功能。
 * 在RandomAccessFile的某个位置读写之前，必须把文件指针指向该位置。通过seek()方法可以达到这一目标。可以通过调用getFilePointer()获得当前文件指针的位置。
 * RandomAccessFile.read()方法返回当前RandomAccessFile实例的文件指针指向的位置中包含的字节内容。
 * read()方法在读取完一个字节之后，会自动把指针移动到下一个可读字节。这意味着使用者在调用完read()方法之后不需要手动移动文件指针。
 *
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/17 13:26
 */
public class RandomAccessFileExample {

    private static final File FILE = new File("d:/system.properties");

    public static void readFile1() throws Exception {
        try (RandomAccessFile file = new RandomAccessFile(FILE, "r")) {
            long fileSize = file.length();
            System.out.println("文件总大小：" + fileSize + "字节");

            long pos1 = fileSize / 2;
            String line;

            file.seek(pos1); //移动读写指针到pos1处
            line = file.readLine(); //尝试着读取pos1所处的这一行
            System.out.println(line); //一般情况下line都是一个不完整的行

            long pos2 = file.getFilePointer(); //上面readLine()读完之后指针自动移到下一行的行首
            System.out.println("pos1 = " + pos1 + ", pos2 = " + pos2 + ", pos2 - pos1 = " + (pos2 - pos1) + ", line.length = " + line.getBytes().length);

            line = file.readLine(); //读取下一个完整行
            System.out.println(line);
        }
    }

    public static void readFile2() throws Exception {
        File file = new File("d:/logs/app.log");
        List<Map<String,Long>> splitPositions = splitFile(file, 9268);
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            for(int i = 0, len = splitPositions.size(); i < len; i++) {
                Map<String,Long> splitPosition = splitPositions.get(i);
                long startPosition = splitPosition.get("startPosition");
                long endPosition = splitPosition.get("endPosition");
                randomAccessFile.seek(startPosition);
                System.out.println("===============================[" + startPosition + ", " + endPosition + "]==============================");
                String line;
                while(randomAccessFile.getFilePointer() < endPosition && (line = randomAccessFile.readLine()) != null) {
                    line = new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                    System.out.println(line);
                }
            }
        }
    }

    public static void readFile3() throws Exception {
        File file = new File("d:/logs/app.log");
        List<Map<String,Long>> splitPositions = splitFile(file, 9268);
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            for(int i = 0, len = splitPositions.size(); i < len; i++) {
                Map<String,Long> splitPosition = splitPositions.get(i);
                long startPosition = splitPosition.get("startPosition");
                long endPosition = splitPosition.get("endPosition");
                MappedByteBuffer mappedByteBuffer = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_ONLY, startPosition, endPosition - startPosition);
                System.out.println("===============================[" + startPosition + ", " + endPosition + "]==============================");
                CharBuffer charBuffer = StandardCharsets.UTF_8.decode(mappedByteBuffer);
                StringBuilder sb = new StringBuilder();
                char c;
                while(charBuffer.hasRemaining()) {
                    c = charBuffer.get();
                    System.out.println(c);
                }
            }
        }
    }

    public static void readBigFile() throws Exception {
        String file = "d:/temp/stackoverflow.com-Users/Users.xml";
        long start = System.currentTimeMillis();
        long startPosition = 0;
        long endPosition = 41097445;
        long mappedBytes = endPosition - startPosition;
        List<String> lines = new ArrayList<>((int) mappedBytes);
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            MappedByteBuffer mappedByteBuffer = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_ONLY, startPosition, mappedBytes);
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(mappedByteBuffer);
            StringBuilder sb = new StringBuilder();
            char c;
            String line;
            while(charBuffer.hasRemaining()) {
                c = charBuffer.get();
                if(c == '\n' || c == '\r') {
                    //遇到换行符
                    if(sb.length() > 0) {
                        line = sb.toString().trim();
                        if(line.startsWith("<row")) {
                            line = new String(line.trim().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                            lines.add(line);
                            System.out.println(line);
                        }
                        sb.setLength(0);
                    }
                } else {
                    sb.append(c);
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Total " + lines.size() + ", cost : " + (end - start) / 1000);
    }

    public static List<Map<String,Long>> splitFile(File file, int splitFileSize) throws Exception {
        List<Map<String,Long>> splitPositions = new ArrayList<>();
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
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
                Map<String,Long> splitPosition = new LinkedHashMap<>();
                splitPosition.put("startPosition", startPosition);
                splitPosition.put("endPosition", endPosition);
                splitPositions.add(splitPosition);
            }
        }
        return splitPositions;
    }

    public static void main(String[] args) throws Exception {
        //readFile1();
        //readFile2();
        //readFile3();
        readBigFile();
    }

}