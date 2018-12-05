import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.beans.*;
import java.awt.image.*;
import javax.swing.event.*;
import java.awt.geom.*;
import javax.script.*;

public class Sketchup extends JPanel{
    JDialog jd;
    JProgressBar jpb;
    //public Graphics2D g2d;
    HashMap simp = new HashMap();
    ScriptEngine engine;
    int sc = 35;
    double px=-10.0, py=f(px);
    String exp;
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
    }
    
    public void sketch(){
        BufferedImage output = new BufferedImage(700, 700, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = output.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(Color.white);
        g2d.fillRect(0,0,700,700);

        g2d.setColor(new Color(220,190,0));
        for(int i=0; i<20; i++)
        {
            g2d.drawLine(0, 35*i, 730, 35*i);
            g2d.drawLine(35*i, 0, 35*i, 730);
        }

        g2d.setColor(Color.blue);
        g2d.drawLine(350, 0, 350, 700);
        g2d.drawLine(0, 350, 700, 350);

        AffineTransform aft = new AffineTransform();
        aft.translate(sc*10,sc*10);
        g2d.setTransform(aft);
        g2d.drawString("0", -10, 13);
        
        exp=exp.replace("log","Math.log");
        exp=exp.replace("sin","Math.sin");
        exp=exp.replace("cos","Math.cos");
        exp=exp.replace("tan","Math.tan");
        exp=exp.replace("sqrt","Math.sqrt");
        exp=exp.replace("abs","Math.abs");
        exp=exp.replace("pow","Math.pow");
        exp=exp.replace("E","Math.E");
        exp=exp.replace("PI","Math.PI");

        ScriptEngineManager mgr = new ScriptEngineManager();
        engine = mgr.getEngineByName("JavaScript");

        for(int q=-10; q<=10 ; q++)
        {
            if(q!=0){
                g2d.drawString(""+q, sc*q - 4, 13);
                g2d.drawString(""+q, -12, -sc*q + 6);
            }
        }
        g2d.setColor(Color.black);
        for(double x=-10.0; x<=10.0; x+=0.01)
        {
            double y = f(x);
            if(y!=y || Math.abs(y-py)>=1){
                py=f(px=x+0.01);
            }else{
                g2d.drawLine((int)(sc*px), -(int)(sc*py), (int)(sc*(px=x)), -(int)(sc*(py=y)));
            }
        }
        JOptionPane.showMessageDialog(null, new ImageIcon(output),"Sketchup" ,-1);
    }

    public double f(double x)
    {
        return solve2(exp, x);
    }

    public double solve2(String exp, double x){
        try{
            String final_expression = exp.replace("x",x+"");
            double ans=(double)engine.eval(final_expression);
            return ans;
        }catch(Exception e){}
        return 0;
    }

    public static void main(String args[])
    {
        Sketchup g = new Sketchup();
        g.exp = JOptionPane.showInputDialog(null,"Enter the expression : \nThe following functions are allowed : \nsin(x), cos(x), tan(x)\nlog(x), sqrt(x), abs(x), pow(a,b)\n"+
            "The following constants are allowed: E, PI\n", "Sketchup",-1);
        g.sketch();
    }
}

