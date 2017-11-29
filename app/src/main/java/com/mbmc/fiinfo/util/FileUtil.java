package com.mbmc.fiinfo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;


public final class FileUtil {

    public static void copy(File source, File target) throws IOException {
        FileChannel sourceChannel = new FileInputStream(source).getChannel();
        FileChannel targetChannel = new FileOutputStream(target, false).getChannel();
        targetChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        sourceChannel.close();
        targetChannel.close();
    }


    private FileUtil() {

    }

}
