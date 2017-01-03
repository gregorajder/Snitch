package com.gregorajdergmail.snitch.model;

import android.content.Context;
import android.content.Intent;
import android.os.FileObserver;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Closer;
import com.gregorajdergmail.snitch.view.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class FileHelper {
    private String path;
    private NotificationService notificationService;
    private String tempPath;

    public void startService(MainActivity mainActivity, String path) {

        NotificationService.newInstance(mainActivity, path);
    }

    boolean addNewFile(String path, NotificationService notificationService) {
        this.path = path;
        this.notificationService = notificationService;
        createTempCopy(path);
        startWatch();
        return true;
    }

    public String getFilePath() {
        return path;
    }

    boolean updateTempCopy() {
        return path != null && createTempCopy(path);
    }

    boolean checkForUpdates() throws IOException {
        File inputFile = new File(path);
        File outputFile = new File(tempPath);

        return !compareContent(inputFile, outputFile);
    }

    private void copyFileUsingFileStreams(File source, File dest) throws IOException {
        Closer closer = Closer.create();
        try {
            InputStream input = closer.register(new FileInputStream(source));
            OutputStream output = closer.register(new FileOutputStream(dest));
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } catch (Throwable e) {
            throw closer.rethrow(e);

        } finally {
            closer.close();
        }
    }

    private boolean createTempCopy(String path) {
        try {
            File inputFile = new File(path);
            File outputFile = new File(notificationService.getCacheDir() + File.separator + "temp");
            tempPath = outputFile.getPath();
            copyFileUsingFileStreams(inputFile, outputFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean compareContent(File inputFile, File tempFile) throws IOException {
        Closer closer = Closer.create();
        try {
            InputStream input = closer.register(new FileInputStream(inputFile));
            InputStream temp = closer.register(new FileInputStream(tempFile));
            String resultInput = CharStreams.toString(new InputStreamReader(input, Charsets.UTF_8));
            String resultTemp = CharStreams.toString(new InputStreamReader(temp, Charsets.UTF_8));
            return resultInput.equals(resultTemp);

        } catch (Throwable e) {
            throw closer.rethrow(e);
        } finally {
            closer.close();
        }
    }

    private void startWatch() {
        FileObserver observer = new MyFileObserver(notificationService, path);
        observer.startWatching();
    }

    void onStopWatching() {
        stopService(notificationService);
    }

    public void stopService(Context context) {
        context.stopService(new Intent(context, NotificationService.class));
        path = null;
        deleteTempFile(context);
    }

    private void deleteTempFile(Context context) {
        File temp = new File(context.getCacheDir() + File.separator + "temp");
        if (temp.exists())
            temp.delete();
    }


}
