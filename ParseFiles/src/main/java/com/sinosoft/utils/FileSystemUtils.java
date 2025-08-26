package com.sinosoft.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class FileSystemUtils {
    /**
     * 判断文件夹是否存在，不存在则创建
     *
     * @param filePath
     */
    public static void noExistMkdir(String filePath) {

//        Path path = Paths.get(filePath);
//        try {
//            Files.walkFileTree(path,
//                    new SimpleFileVisitor<Path>(){
//                        //  先去遍历删除⽂件
//
//                        @Override
//                        public FileVisitResult visitFile(Path file,
//                                                         BasicFileAttributes attrs)throws IOException {
//                            Files.delete(file);
//                            System.out.printf("⽂件被删除 : %s%n", file);
//                            return FileVisitResult.CONTINUE;
//                        }
//                        //    再去遍历删除⽬录
//
//                        @Override
//                        public FileVisitResult postVisitDirectory(Path dir,
//                                                                  IOException exc)throws IOException {
//                            Files.delete(dir);
//                            System.out.printf("⽂件夹被删除: %s%n", dir);
//                            return FileVisitResult.CONTINUE;
//                        }
//                    });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        File file = new File(filePath);
        file.mkdirs();
    }
}
