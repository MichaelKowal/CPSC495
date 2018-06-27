/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.trace;

import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;

/**
 *
 * @author behnish
 */
public abstract class Event implements Serializable {

    private static final long serialVersionUID = -7435622685898555817L;
    double ax;
    double ay;
    double bx;
    double by;
    private double x;
    private double y;
    private int step;
    double ux;
    double uy;
    private int countStep = 1;
    private boolean countChecked = false;
    private int dt;
    private double dividend = 1.0;
    private double ticksMe = 1;

    public void setTicksMe(double ticksMe) {
        this.ticksMe = ticksMe;
    }
    public double getDividend() {
        return dividend;
    }

    public void setDividend(double dividend) {
        this.dividend = dividend;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    protected Event(Point a, Point b, int step) {
        if(a!=null && b!=null)
        {
            setTrajectory(a, b, step);
        }
    }

    public final void setTrajectory(Point a, Point b, int step) {
        if (step == 0) {
            this.step = 1;
        } else {
            this.step = step;
        }
        this.ax = a.getX();
        this.ay = a.getY();
        this.bx = b.getX();
        this.by = b.getY();

        this.x = this.ax;
        this.y = this.ay;

        double distance = 600.0;//a.distance(b);

        this.ux = ((this.bx - this.ax) / distance);
        this.uy = ((this.by - this.ay) / distance);
    }

    public Point currentLocation() {
        Point p = new Point();
        p.setLocation(this.x, this.y);
        return p;
    }


    public void moveForward() {

        double a = this.step * (this.bx - this.ax) / countStep;
        double b = this.step * (this.by - this.ay) /countStep;
        
        this.x += a;
        this.y += b;
    }

    public int getCountStep() {
        return countStep;
    }

    public void setCountStep(int countStep) {
        this.countStep = countStep;
    }

    public void setCountSteps() {
        if (!countChecked) {
            if (this.bx - this.ax == 0) {
                this.countStep = (int) (((this.by - this.ay) / (this.step * this.uy * 1 / dt)) + 0.5);
            } else {
                this.countStep = (int) (((this.bx - this.ax) / (this.step * this.ux * 1 / dt)) + 0.5);
            }            
            this.countChecked = true;
        }
    }

    public void moveBackward() {
        this.x -= this.step * this.ux * 1 / dt;
        this.y -= this.step * this.uy * 1 / dt;
    }

    public boolean isIntoBounds() {
        return (this.x - this.ax) * (this.x - this.bx) + (this.y - this.ay) * (this.y - this.by) <= 0.0D;
    }

    public void reset() {
        this.x = this.ax;
        this.y = this.ay;
    }

    public void end() {
        this.x = this.bx;
        this.y = this.by;
    }

    public abstract void draw(Graphics2D g);

    public int noOfSteps(Event t) {
        int stepsCount = 0;
        boolean check = true;

        while (check) {
            if (t.isIntoBounds()) {
                t.moveForward();
                stepsCount++;
            } else {
                check = false;
            }
        }
        return stepsCount;
    }
}
