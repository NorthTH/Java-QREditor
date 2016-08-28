import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;

import java.awt.Dimension;

import javax.swing.JTextField;

import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ColorValueSliderControl extends JFrame {
  public ColorValueSliderControl() {
    getContentPane().add(new TColor());
    //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(900, 600);
    setVisible(true);
  }
}

class TColor extends JPanel implements ActionListener {
  
  public static final double RV = 0.299;
  public static final double GV = 0.587;
  public static final double BV = 0.114;
  
  TColor.DrawingCanvas canvas = new TColor.DrawingCanvas();
  TColor.DrawingCanvas currentC = new TColor.DrawingCanvas();
  JLabel rgbValue = new JLabel("000000");
  JLabel bValue = new JLabel("0");
  JButton okB;

  JSlider sliderR, sliderG, sliderB, sliderH, sliderS, sliderA,
      sliderAlpha;
  
  JTextField RVTB;
  JTextField GVTB;
  JTextField BVTB;
  
  JTextField HVTB;
  JTextField SVTB;
  JTextField AVTB;

  public TColor() {
    sliderR = getSlider(0, 255, 0, 50, 5);
    sliderG = getSlider(0, 255, 0, 50, 5);
    sliderB = getSlider(0, 255, 0, 50, 5);
    sliderH = getSlider(0, 360, 0, 120, 60);
    sliderS = getSlider(0, 255, 0, 50, 5);
    sliderA = getSlider(0, 255, 0, 50, 5);
    sliderAlpha = getSlider(0, 100, 100, 20, 10);
    
    RVTB = new JTextField("0");
    GVTB = new JTextField("0");
    BVTB = new JTextField("0");
    
    HVTB = new JTextField("0");
    SVTB = new JTextField("0");
    AVTB = new JTextField("0");
    JPanel panel = new JPanel();
    //OKボタン
    okB=new JButton("OK");
    okB.addActionListener(this);
				
    panel.setLayout(new GridLayout(6, 2, 15, 0));

    panel.add(new JLabel("R-G-B Sliders (0 - 255)"));
    panel.add(new JLabel("RGB Value"));
    panel.add(new JLabel("H-S-A Sliders (ex-1)"));
    panel.add(new JLabel("HSA Value"));
    panel.add(sliderR);
    panel.add(RVTB);
    RVTB.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                int key=e.getKeyCode();
                if(e.getSource()==RVTB) {
                    if(key==KeyEvent.VK_ENTER) { 
                        try{
                            int r = Integer.parseInt(RVTB.getText());
                            if(0<=r||r<=255){
                            sliderR.setValue(r);
                            }else if(r>255){
                                RVTB.setText("255"); 
                            }
                        }
                        catch(Exception ex)
                        {
                            sliderR.setValue(0);
                            RVTB.setText("0");
                        }       //System.out.print("");
                    }
                } 
            }

            public void keyTyped(KeyEvent e) {
                
            }

            public void keyPressed(KeyEvent e) {
                
            }
        });
    panel.add(sliderH);
    panel.add(HVTB);
    HVTB.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                 int key=e.getKeyCode();
                 if(e.getSource()==HVTB) {
                     if(key==KeyEvent.VK_ENTER) { 
                        try{
                            int h = Integer.parseInt(HVTB.getText());
                            if(0<=h||h<=360){
                            sliderH.setValue(h);
                            }else if(h>360){
                                HVTB.setText("360"); 
                            }
                        }
                        catch(Exception ex)
                        {
                            sliderH.setValue(0);
                            HVTB.setText("0");
                        }       //System.out.print("");
                    }
                }
            }

            public void keyTyped(KeyEvent e) {
                 
            }

            public void keyPressed(KeyEvent e) {
                
            }
        });
    panel.add(sliderG);
    panel.add(GVTB);
    GVTB.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                int key=e.getKeyCode();
                if(e.getSource()==GVTB) {
                    if(key==KeyEvent.VK_ENTER) { 
                        try{
                            int g = Integer.parseInt(GVTB.getText());
                            if(0<=g||g<=255){
                            sliderG.setValue(g);
                            }else if(g>255){
                                GVTB.setText("255"); 
                            }
                        }
                        catch(Exception ex)
                        {
                            sliderG.setValue(0);
                            GVTB.setText("0");
                        }       //System.out.print("");
                    }
                }
            }

            public void keyTyped(KeyEvent e) {
                
            }

            public void keyPressed(KeyEvent e) {
                
            }
        });
    panel.add(sliderS);
    panel.add(SVTB);
    SVTB.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                int key=e.getKeyCode();
                if(e.getSource()==SVTB) {
                    if(key==KeyEvent.VK_ENTER) { 
                        try{
                            int s = Integer.parseInt(SVTB.getText());
                            if(0<=s||s<=100){
                            sliderS.setValue(s);
                            }else if(s>100){
                                SVTB.setText("100"); 
                            }
                        }
                        catch(Exception ex)
                        {
                            sliderS.setValue(0);
                            SVTB.setText("0");
                        }       //System.out.print("");
                    }
                }
            }

            public void keyTyped(KeyEvent e) {
                
            }

            public void keyPressed(KeyEvent e) {
                
            }
        });
    panel.add(sliderB);
    panel.add(BVTB);
    BVTB.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                int key=e.getKeyCode();
                if(e.getSource()==BVTB) {
                    if(key==KeyEvent.VK_ENTER) { 
                        try{
                            int b = Integer.parseInt(BVTB.getText());
                            if(0<=b||b<=255){
                            sliderB.setValue(b);
                            }else if(b>255){
                                BVTB.setText("255"); 
                            }
                        }
                        catch(Exception ex)
                        {
                            sliderB.setValue(0);
                            BVTB.setText("0");
                        }       //System.out.print("");
                    }
                }
            }

            public void keyTyped(KeyEvent e) {
                
            }

            public void keyPressed(KeyEvent e) {
                
            }
        });
    panel.add(sliderA);
    panel.add(AVTB);
    AVTB.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                int key=e.getKeyCode();
                if(e.getSource()==AVTB) {
                    if(key==KeyEvent.VK_ENTER) { 
                        try{
                            int a = Integer.parseInt(AVTB.getText());
                            if(0<=a||a<=100){
                            sliderA.setValue(a);
                            }else if(a>100){
                                AVTB.setText("100"); 
                            }
                        }
                        catch(Exception ex)
                        {
                            sliderA.setValue(0);
                            AVTB.setText("0");
                        }       //System.out.print("");
                    }
                }
            }

            public void keyTyped(KeyEvent e) {
                
            }

            public void keyPressed(KeyEvent e) {
                
            }
        });

    panel.add(new JLabel("Alpha Adjustment (0 - 100): ", JLabel.RIGHT));
    panel.add(sliderAlpha);

    panel.add(new JLabel("RGB値: ", JLabel.RIGHT));
    
    rgbValue.setBackground(Color.white);
    rgbValue.setForeground(Color.black);
    rgbValue.setOpaque(true);
    panel.add(rgbValue);
    
    panel.add(new JLabel("輝度値 (MAX:100%): ", JLabel.RIGHT));

    bValue.setBackground(Color.white);
    bValue.setForeground(Color.black);
    bValue.setOpaque(true);
    panel.add(bValue);

    add(panel, BorderLayout.SOUTH);
    add(currentC);
    add(new JLabel("現在の色"));
    add(canvas);
    add(new JLabel("選択色"));
    add(okB);
  }
  ////////////////////////////////////////////イベントメソッド////////////////////////////////////////
	public void actionPerformed(ActionEvent e) {
            Object obj = e.getSource();
        //ボタン(OK)のイベント
			if(obj==okB){
                            double briT;
                            currentC.setBackground(canvas.color);
                            currentC.repaint();
                            EGPanel.rgbValue = canvas.color;
                            briT = Double.parseDouble(bValue.getText());
                            EGPanel.briV = briT;
                            EGPanel.tranValue = sliderAlpha.getValue()/100.0f;
                            /*if(briT>50&&briT<90){
                                if(briT>80){
                                    JOptionPane.showMessageDialog(null, "あなたが選択した色は“明”と設定されます"
                                            + "があるソフトで読み取れない事もありますのでご注意ください。\n"
                                            + "安全の90%以上の輝度値の“明”がおすすめです。");
                                }else if(briT<60){
                                    JOptionPane.showMessageDialog(null, "あなたが選択した色は“暗”と設定されます"
                                            + "があるソフトで読み取れない事もありますのでご注意ください。\n"
                                            + "安全の50%以下の輝度値の“暗”がおすすめです。");
                                }else{
                                    JOptionPane.showMessageDialog(null, "あなたが選択した色は“明”また“暗”と認識"
                                            + "できないようです。\n"
                                            + "安全の輝度値は90%以上の“明”また50%以下の“暗”を選んでください。");
                                }
                            }*/
			}
        }

  public JSlider getSlider(int min, int max, int init, int mjrTkSp, int mnrTkSp) {
    JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, init);
    slider.setPaintTicks(true);
    slider.setMajorTickSpacing(mjrTkSp);
    slider.setMinorTickSpacing(mnrTkSp);
    slider.setPaintLabels(true);
    slider.addChangeListener(new TColor.SliderListener());
    return slider;
  }

  class DrawingCanvas extends Canvas {
    Color color;
    int redValue, greenValue, blueValue;
    int alphaValue = 255;
    float[] hsbValues = new float[3];
    int mode = 0;

    public DrawingCanvas() {
      setSize(100, 100);
      color = new Color(0, 0, 0);
      setBackgroundColor();
    }

    public void setBackgroundColor() {
      color = new Color(redValue, greenValue, blueValue, alphaValue);
      setBackground(color);
    }
  }

  class SliderListener implements ChangeListener {
    public void stateChanged(ChangeEvent e) {
      JSlider slider = (JSlider) e.getSource();
      if (slider == sliderAlpha) {
        
          //canvas.alphaValue = slider.getValue();
        //canvas.setBackgroundColor();
      } else if (slider == sliderR) {
        canvas.redValue = slider.getValue();
        RVTB.setText(Integer.toString(canvas.redValue));
        if(canvas.mode != 2){
            displayRGBColor();
        }
      } else if (slider == sliderG) {
        canvas.greenValue = slider.getValue();
        GVTB.setText(Integer.toString(canvas.greenValue));
        if(canvas.mode != 2){
            displayRGBColor();
        }
      } else if (slider == sliderB) {
        canvas.blueValue = slider.getValue();
        BVTB.setText(Integer.toString(canvas.blueValue));
        if(canvas.mode != 2){
            displayRGBColor();
        }
      } else if (slider == sliderH) {
        canvas.hsbValues[0] = (float) (slider.getValue() / 360.0);
        HVTB.setText(Integer.toString((int) (canvas.hsbValues[0] * 360)));
        if(canvas.mode != 1){
            displayHSBColor();
        }
      } else if (slider == sliderS) {
        canvas.hsbValues[1] = (float) (slider.getValue() / 255.0);
        SVTB.setText(Integer.toString((int) (canvas.hsbValues[1] * 255)));
        if(canvas.mode != 1){
            displayHSBColor();
        }
      } else if (slider == sliderA) {
        canvas.hsbValues[2] = (float) (slider.getValue() / 255.0);
        AVTB.setText(Integer.toString((int) (canvas.hsbValues[2] * 255)));
        if(canvas.mode != 1){
            displayHSBColor();
        }
      }
      double briV = (((double)canvas.redValue*RV)+((double)canvas.greenValue*GV) + ((double)canvas.blueValue*BV))/2.55;
      rgbValue.setText(Integer.toString(canvas.color.getRGB() & 0xffffff, 16));
      bValue.setText(Float.toString((float)briV));
      canvas.repaint();
    }

    public void displayRGBColor() {
      canvas.mode = 1;
      canvas.setBackgroundColor();
      Color.RGBtoHSB(canvas.redValue, canvas.greenValue, canvas.blueValue,canvas.hsbValues);   
      sliderH.setValue((int) (canvas.hsbValues[0] * 360));
      sliderS.setValue((int) (canvas.hsbValues[1] * 255));
      sliderA.setValue((int) (canvas.hsbValues[2] * 255));
      
      canvas.mode = 0;
    }

    public void displayHSBColor() {
      canvas.mode = 2;
      canvas.color = Color.getHSBColor(canvas.hsbValues[0],
          canvas.hsbValues[1], canvas.hsbValues[2]);
      canvas.redValue = canvas.color.getRed();
      canvas.greenValue = canvas.color.getGreen();
      canvas.blueValue = canvas.color.getBlue();
      
      sliderR.setValue(canvas.redValue);
      sliderG.setValue(canvas.greenValue);
      sliderB.setValue(canvas.blueValue);

      canvas.color = new Color(canvas.redValue, canvas.greenValue,
          canvas.blueValue, canvas.alphaValue);
      canvas.setBackground(canvas.color);
      canvas.mode = 0;
    }
  }
}