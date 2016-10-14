package leti.asd.db;

import javax.swing.*;

/**
 * Created by nikolaikobyzev on 14.10.16.
 */

public class GUI extends JFrame {
    private static final int WINDOW_WIDTH = 507;
    private static final int WINDOW_HEIGHT = 555;
    private static final int POS_X = 800;
    private static final int POS_Y = 300;


    GUI(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("TransportDB");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocation(POS_X, POS_Y);
        setResizable(false);

    }
}
