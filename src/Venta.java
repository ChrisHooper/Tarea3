
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;


public class Venta extends JPanel{
	
	private static final long serialVersionUID = 1L;
	BufferedImage image;
	Color color;
	Stroke stroke;
	
	public Venta(){
		color = Color.red;
		stroke = new BasicStroke(1f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
		
	}
	
	protected void painComponent(Graphics g){
		
		super.paintComponents(g);
		if(image==null)
			initImage();
		g.drawImage(image,0,0,this);
		
	}
	
	public void draw(Point start, Point end){
		Graphics2D g2 = image.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.setPaint(color);
		g2.setStroke(stroke);
		g2.draw(new Line2D.Double(start, end));
		g2.dispose();
		repaint();
	}
	
	private void clearImage(){
		Graphics g = image.getGraphics();
		g.setColor(getBackground());
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		
		g.dispose();
		repaint();
	
	}
	
	private void initImage(){
		int w = getWidth();
		int h = getHeight();
		image = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = image.createGraphics();
		g2.setPaint(getBackground());
		g2.fillRect(0, 0, w, h);
		g2.dispose();	
	}
	
	private JPanel getColorPanel(){
		Color[] colors ={
				Color.red,Color.green.darker(),Color.blue,Color.orange
		};
		ActionListener L = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JButton button = (JButton)e.getSource();
				color = button.getBackground();
				
				
			}
		};
		JPanel panel = new JPanel(new GridLayout(1,0));
		for(int j = 0; j < colors.length; j++){
			JButton button = new JButton ("  ");
			button.setFocusPainted(false);
			button.setBackground(colors[j]);
			button.addActionListener(L);
			panel.add(button);
			}
		return panel;
		}
	
	private JPanel getControlPanel(){
		
		JButton clear = new JButton("Clear");
		clear.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				clearImage();
			}
		});
		
		final JSlider slider = new JSlider(JSlider.HORIZONTAL,1,6,1);
		slider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				float value = ((Integer)slider.getValue()).floatValue();
				stroke = new BasicStroke(value,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
				
			}
		});
		JPanel panel = new JPanel();
		panel.add(new JLabel("Stroke"));
		panel.add(slider);
		panel.add(clear);
		return panel;
		
	}
	
	
	
	public static void main(String[] args){
		
		Venta venta = new Venta();
		DrawingListener listener = new DrawingListener(venta);
		
		
		venta.addMouseListener(listener);
		venta.addMouseMotionListener(listener);
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(venta.getColorPanel(),"North");
		f.getContentPane().add(venta);
		f.getContentPane().add(venta.getControlPanel(),"South");
		f.setSize(400,400);
		f.setLocation(200,200);
		f.setVisible(true);
	}
	
	class DrawingListener extends MouseInputAdapter{
		Venta venta;
		Point start;
		
		public DrawingListener(Venta vn){
			this.venta = vn;
		}
		
		public void mousePressed(MouseEvent e){
			start = e.getPoint();
		}
		
		public void mouseDragged(MouseEvent e){
			Point p = e.getPoint();
			venta.draw(start, p);
			start = p;
			
		}
	}
}
