/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matematico;

/**
 *
 * @author will
 */
public class Edge {
    public Point3 p1, p2;
    public Point4 p3d1, p3d2;

    public Edge(Point4 p3d1, Point4 p3d2) {
        this.p3d1 = p3d1;
        this.p3d2 = p3d2;
    }

    public Edge(Point3 p1, Point3 p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Edge() {
    }   
    
}
