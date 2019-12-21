package agh.cs.EvolutionSimulator;

import com.sun.prism.Graphics;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import javax.swing.*;
import java.awt.*;

public class Visualization extends JPanel {
    public WorldMap map;
    public JFrame frame;
    private int width;
    private int height;
    private int widthScale;
    private int heightScale;

    public Visualization(WorldMap map, JFrame frame){
        this.map=map;
        this.frame=frame;
        this.setBounds(0,170, (int) (1 * frame.getWidth()), (int)(0.7 * frame.getHeight()));
        this.width = this.getWidth();
        this.height = this.getHeight();
        this.widthScale = Math.round(width / map.getUpperRight().x);
        this.heightScale = height / map.getUpperRight().y;
    }


    @Override
    protected void paintComponent( java.awt.Graphics g){
        super.paintComponent((java.awt.Graphics) g);
        ((java.awt.Graphics) g).setColor(new Color(170, 224, 103));
        g.fillRect(0, 0, this.width, this.height);

        for(int x= 0; x<=this.map.getUpperRight().x; x++){
            for (int y=0; y<=this.map.getUpperRight().y;y++){
                if(map.insideJungle(new Vector2d(x,y))){
                    ((java.awt.Graphics) g).setColor(new Color(0, 160, 7));
                    ((java.awt.Graphics) g).fillRect(x*widthScale,y*heightScale,widthScale,heightScale);
                }
                if(!map.elementsMap.get(new Vector2d(x, y)).isEmpty() && map.elementsMap.get(new Vector2d(x, y)).get(0) instanceof Grass){
                    ((java.awt.Graphics) g).setColor(new Color(0, 100, 70));
                    ((java.awt.Graphics) g).fillRect(x*widthScale,y*heightScale,widthScale,heightScale);
                }
                if(!map.elementsMap.get(new Vector2d(x, y)).isEmpty() && map.elementsMap.get(new Vector2d(x,y)).get(0) instanceof Animal){
                    ((java.awt.Graphics) g).setColor(((Animal) map.elementsMap.get(new Vector2d(x,y)).get(0)).toColor());
                    ((java.awt.Graphics) g).fillRect(x*widthScale,y*heightScale,widthScale,heightScale);
                }
            }
        }




    }

}
