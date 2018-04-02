package com.rug.gea;

import com.rug.gea.GUI.BuildingFrame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class ClientMain {

    public static void main(String argc[]) throws IOException {
        BuildingFrame frame = new BuildingFrame();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                frame.close();
            }
        });
    }
}
