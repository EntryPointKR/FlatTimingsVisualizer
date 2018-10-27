package com.github.entrypointkr.ftvisualizer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;

/**
 * Created by JunHyeong on 2018-10-28
 */
public class FlatTimingsVisualizer {
    public static void main(String[] args) throws IOException {
        JFileChooser chooser = new JFileChooser();
        JFrame frame = new JFrame();
        chooser.setFileFilter(new FileNameExtensionFilter("Text file", "txt"));
        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            byte[] readed = Files.readAllBytes(file.toPath());
            HttpURLConnection ex = (HttpURLConnection) (new URL("https://timings.spigotmc.org/paste")).openConnection();
            ex.setDoOutput(true);
            ex.setRequestMethod("POST");
            ex.setInstanceFollowRedirects(false);
            OutputStream out = ex.getOutputStream();
            out.write(readed);
            out.close();

            StringBuilder builder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(ex.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (builder.length() > 0) {
                        builder.append('\n');
                    }
                    builder.append(line);
                }
            }
            String json = builder.toString();
            String target = "key\"";
            int position = json.indexOf(target) + target.length();
            int valuePos = json.indexOf('\"', position) + 1;
            String value = json.substring(valuePos, json.indexOf('\"', valuePos));
            String address = "https://timings.spigotmc.org/?url=" + value;
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(URI.create(address));
            } else {
                System.out.println("Address: " + address);
            }
        }
        System.exit(0);
    }
}
