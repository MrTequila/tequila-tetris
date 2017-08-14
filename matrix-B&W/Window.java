package Gra;


import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Timer;

import javax.swing.border.LineBorder;
import javax.swing.JTextField;

public class Window extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private JPanel contentPane;
	boolean isDown=false;
	boolean isLeft=false;
	boolean isRight=false;
	boolean isRotate=false;
	boolean start=true;
	Tetris tetris = new Tetris();
	Timer timer=new Timer(1000, this);
	private JTextField txtScore;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Window window = new Window();
		JFrame app = new JFrame();
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.getContentPane().add(window);
		window.setFocusable(true);
		window.requestFocusInWindow();
		
		app.setSize(500, 700);
		app.setVisible(true);
		
	}

	/**
	 * Create the frame.
	 */
	public Window() {
		super();
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setLayout(null);
		
		txtScore = new JTextField();
		txtScore.setEditable(false);
		txtScore.setText(Integer.toString(tetris.score));
		txtScore.setBounds(379, 40, 86, 20);
		add(txtScore);
		txtScore.setColumns(10);
		timer.start();

		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				int keyCode = arg0.getKeyCode();
				if(keyCode == KeyEvent.VK_S){
					isDown=true;
				
					repaint();
				}
				if(keyCode == KeyEvent.VK_A){
					isLeft=true;
					timer.restart();
					repaint();
				}
					
				if(keyCode == KeyEvent.VK_D){
					isRight=true;
					repaint();

				}
				if(keyCode == KeyEvent.VK_W){
					isRotate=true;
					repaint();
				}
				txtScore.setText(Integer.toString(tetris.score));
					
			}
			@Override
			public void keyReleased(KeyEvent e) {
				int keyCode = e.getKeyCode();
				if(keyCode == KeyEvent.VK_S){
					isDown=false;
				}
				if(keyCode == KeyEvent.VK_A){
					isLeft=false;
				}
					
				if(keyCode == KeyEvent.VK_D){
					isRight=false;
				}
				if(keyCode == KeyEvent.VK_W){
					isRotate=false;
				}
			}
		});
		
		
		
	}
	public void paintComponent(Graphics g){
		final int TETRISHEIGHT = 600;
		final int TETRISWIDTH = 300;
		super.paintComponents(g);
		if(start) {
			tetris.newPiece();
			start=false;
		}
		g.drawRect(0, 0, TETRISWIDTH, TETRISHEIGHT);
		//Obs³uga ruchu spadaj¹cej figury
		if(isDown) tetris.newPieceDown();
		if(isLeft) tetris.newPieceLeft();
		if(isRight) tetris.newPieceRight();
		if(isRotate) tetris.rotateFallingPiece();
		
		//Malowanie planszy
		for(int i=0; i<tetris.WIERSZE-1; i++){
			for (int j=1; j<tetris.KOLUMNY-1; j++){
				g.clearRect((j-1)*30, i*30, 30, 30);
				if(tetris.plansza[i][j]==1) g.fillRect((j-1)*30, i*30, 30, 30);
				else g.drawRect((j-1)*30, i*30, 30, 30);
				if(tetris.fallingPiece[i][j]==1) g.fillRect((j-1)*30, i*30, 30, 30);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==timer)isDown=true;
		repaint();
	}
}
