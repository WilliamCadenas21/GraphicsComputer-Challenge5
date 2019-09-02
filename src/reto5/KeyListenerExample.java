package reto5;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import matematico.*;

/**
 *
 * @author helmuthtrefftz
 */
public class KeyListenerExample extends JPanel implements KeyListener {

    public static final int FRAME_WIDTH = 900;
    public static final int FRAME_HEIGHT = 900;

    static Point4[] pointsVec = new Point4[20];
    static Edge[] edgesVec = new Edge[20];

    static boolean DEBUG = true;
    static double d = -900;

    public KeyListenerExample() {
        // El panel, por defecto no es "focusable". 
        // Hay que incluir estas líneas para que el panel pueda
        // agregarse como KeyListsener.
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // size es el tamaÃ±o de la ventana.
        Dimension size = getSize();
        // Insets son los bordes y los tÃ­tulos de la ventana.
        Insets insets = getInsets();
        int w = size.width - insets.left - insets.right;
        int h = size.height - insets.top - insets.bottom;

        g.setColor(Color.black);
        g.drawLine(0, h / 2, w, h / 2);// eje x
        g.drawLine(w / 2, 0, w / 2, h);// eje y

        g.setColor(Color.blue);
        show(edgesVec);
        for (Edge edgesVec1 : edgesVec) {
            System.out.println("dibujando");
            Edge e = edgesVec1;
            Point4 p1 = e.p3d1;
            Point4 p2 = e.p3d2;

            int xr1 = w / 2 + (int) p1.x;
            int xr2 = w / 2 + (int) p2.x;
            int yr1 = h / 2 - (int) p1.y;
            int yr2 = h / 2 - (int) p2.y;

            //perspective transform
            int xp1 = (int) ((d * xr1) / p1.z);
            int yp1 = (int) ((d * yr1) / p1.z);
            int xp2 = (int) ((d * xr2) / p2.z);
            int yp2 = (int) ((d * yr2) / p2.z);

            //int xr1 = w / 2 + (int) p1.x;
            //int xr2 = w / 2 + (int) p2.x;
            //int yr1 = h / 2 - (int) p1.y;
            //int yr2 = h / 2 - (int) p2.y;
            //int xr1 = w / 2 + (int) xp1;
            //int xr2 = w / 2 + (int) yp1;
            //int yr1 = h / 2 - (int) xp2;
            //int yr2 = h / 2 - (int) yp2;
            //g.drawLine(xr1, yr1, xr2, yr2);
            g.drawLine(xp1, yp1, xp2, yp2);
            //g.drawLine((int)p1.x,(int)p1.y,(int)p2.x,(int)p2.y);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int tecla = e.getKeyCode();

        switch (tecla) {

            //Moving
            case KeyEvent.VK_D: {
                double[][] aux = {{1, 0, 0, 10}, {0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}};
                Matrix4x4 mt = new Matrix4x4(aux);
                transform(mt);
                break;
            }
            case KeyEvent.VK_A: {
                double[][] aux = {{1, 0, 0, -10}, {0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}};
                Matrix4x4 mt = new Matrix4x4(aux);
                transform(mt);
                break;
            }
            case KeyEvent.VK_W: {
                double[][] aux = {{1, 0, 0, 0}, {0, 1, 0, 10}, {0, 0, 1, 0}, {0, 0, 0, 1}};
                Matrix4x4 mt = new Matrix4x4(aux);
                transform(mt);
                break;
            }
            case KeyEvent.VK_S: {
                double[][] aux = {{1, 0, 0, 0}, {0, 1, 0, -10}, {0, 0, 1, 0}, {0, 0, 0, 1}};
                Matrix4x4 mt = new Matrix4x4(aux);
                transform(mt);
                break;
            }
            case KeyEvent.VK_Q: {
                d-=20;
                break;
            }
            case KeyEvent.VK_E: {
                d+=20;
                break;
            }

            case KeyEvent.VK_R: {
                try {
                    readFile();
                } catch (IOException ex) {
                    Logger.getLogger(KeyListenerExample.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }

            default:
                break;
        }

        if (DEBUG) {
            System.out.println("Aplicando una transformación");
        }

        repaint();
    }

    /**
     *
     * @param mt
     */
    public static void transform(Matrix4x4 mt) {
        for (Edge edgesVec1 : edgesVec) {
            Point4 aux1 = edgesVec1.p3d1;
            Point4 aux2 = edgesVec1.p3d2;
            aux1 = Matrix4x4.times(mt, aux1);
            aux2 = Matrix4x4.times(mt, aux2);
            edgesVec1.p3d1 = aux1;
            edgesVec1.p3d2 = aux2;
        }
    }

    public static void readFile() throws IOException {
        // We need to provide file path as the parameter: 
        // double backquote is to avoid compiler interpret words 
        // like \test as \t (ie. as a escape sequence) 
        File file = new File("casita3d.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        int indice = 0;
        int numberOfPoints = 0;
        int numberOfEdges;

        while ((st = br.readLine()) != null) {
            if (indice == 0) {
                numberOfPoints = Integer.parseInt(st);
                pointsVec = new Point4[numberOfPoints];
            } else if (indice <= numberOfPoints) {
                String[] stVec = st.split(" ");
                Double x = Double.parseDouble(stVec[0]);
                Double y = Double.parseDouble(stVec[1]);
                Double z = Double.parseDouble(stVec[2]);
                Point4 p = new Point4(x, y, z, 1);
                pointsVec[indice - 1] = p;
            } else if (indice == 1 + numberOfPoints) {
                numberOfEdges = Integer.parseInt(st);
                edgesVec = new Edge[numberOfEdges];
            } else if (indice >= 2 + numberOfPoints) {
                String[] stVec = st.split(" ");
                edgesVec[indice - 2 - numberOfPoints] = new Edge(pointsVec[Integer.parseInt(stVec[0])], pointsVec[Integer.parseInt(stVec[1])]);
            }
            indice++;
        }
    }

    public static void show(Point4[] vec) {
        for (Point4 p : vec) {
            System.out.println("x:" + p.x + " y:" + p.y + "z:" + p.z + "w:" + p.w);
        }
    }

    public static void show(Edge[] vec) {
        int i = 0;
        for (Edge edge : vec) {
            Point4 p1 = edge.p3d1;
            Point4 p2 = edge.p3d2;
            System.out.println("edgeNumber" + i++);
            System.out.println("point1:" + p1.x + "," + p1.y + "," + p1.z + " point:" + p2.x + "," + p2.y + "," + p2.z);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * In the main method, the frame and panels are created. And the
     * KeyListeners are added.
     *
     * @param args
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        if (DEBUG) {
            System.out.println("Comenzando el programa...");
        }
        if (DEBUG) {
            System.out.println("Leyendo el archivo");
        }

        readFile();

        KeyListenerExample kle = new KeyListenerExample();

        // Create a new Frame
        JFrame frame = new JFrame("Ejemplo KeyListener");
        // Upon closing the frame, the application ends
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Add a panel called DibujarCasita3D
        frame.add(kle);
        // Asignarle tamaño
        frame.setSize(KeyListenerExample.FRAME_WIDTH, KeyListenerExample.FRAME_HEIGHT);
        // Put the frame in the middle of the window
        frame.setLocationRelativeTo(null);
        // Show the frame
        frame.setVisible(true);
    }
}
