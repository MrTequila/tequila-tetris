package Gra;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.ArrayList;
import java.util.Random;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


public class GraphMode extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	final static int[][] O_PIECE = {{1,1}, 
									{1,1}};
	final static int[][] I_PIECE = {{1,1,1,1}};
	final static int[][] L_PIECE = {{0,0,1},
									{1,1,1}};
	final static int[][] J_PIECE = {{1,0,0},
									{1,1,1}};
	final static int[][] S_PIECE = {{0,1,1},
									{1,1,0}};
	final static int[][] Z_PIECE = {{1,1,0},
									{0,1,1}};
	final static int[][] T_PIECE = {{0,1,0},
									{1,1,1}};
	final static int[][][] PIECE_TABLE = {O_PIECE, I_PIECE, L_PIECE, J_PIECE, S_PIECE, Z_PIECE, T_PIECE};
	final static Color[] PIECE_COLOR = {Color.GREEN, Color.MAGENTA, Color.RED, Color.YELLOW, Color.BLUE, Color.CYAN, Color.DARK_GRAY};
	
	boolean isDown=false;
	boolean isLeft=false;
	boolean isRight=false;
	boolean isRotate=false;
	
	boolean isStart=true;

	EmptyBoard board;
	FallingPieces test;
	Tetromino fig;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
	
					JFrame frame= new JFrame();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setBounds(100, 100, 500, 600);
					GraphMode graph = new GraphMode();
					frame.getContentPane().add(graph);
					graph.setFocusable(true);
					graph.requestFocusInWindow();
					frame.setVisible(true);
					
					

	}
		
	/**
	 * Create the frame.
	 */
	public GraphMode() {

		addKeyListener(new KeyAdapter() {
			@Override
			
			public void keyPressed(KeyEvent key) {
				int key1 = key.getKeyCode();
				if(key1==KeyEvent.VK_S){
					isDown = true;
					repaint();
				}
				if(key1==KeyEvent.VK_W){
					isRotate = true;
					repaint();
				}
				if(key1==KeyEvent.VK_A){
					isLeft = true;
					repaint();
				}
				if(key1==KeyEvent.VK_D){
					isRight = true;
					repaint();
				}
			}
			public void keyReleased(KeyEvent key){
				int key1= key.getKeyCode();
				if(key1==KeyEvent.VK_S){
					isDown = false;
				}
				if(key1==KeyEvent.VK_W){
					isRotate = false;
				}
				if(key1==KeyEvent.VK_A){
					isLeft = false;
				}
				if(key1==KeyEvent.VK_D){
					isRight = false;
				}
			}
		});
		
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setLayout(new BorderLayout(0, 0));
		this.setBackground(Color.LIGHT_GRAY);

	}
	public void paintComponent(Graphics g2){
		super.paintComponents(g2);
		Graphics2D g = (Graphics2D) g2.create();
		
		board = new EmptyBoard(g);
		
		if(isStart){
			test = new FallingPieces();
			fig = new Tetromino();
			paintComponent(g);
			repaint();
			isStart=false;
		}

		if(isDown){
			test.fallingMoveDown(g);
			paintComponent(g);
		//	System.out.println("Down ! " + test.currentFalling.getBounds() );
		}
		if(isLeft){
			//test.currentFalling.fallingPieceLeft();
			test.FallingMoveLeft(g);
			paintComponent(g);
		//	System.out.println("Left ! " + test.currentFalling.getBounds() );
		}
		if(isRight){
		//	test.currentFalling.fallingPieceRight();
			test.FallingMoveRight(g);
			paintComponent(g);
		//	System.out.println("Right ! " + test.currentFalling.getBounds() );
		}
		if(isRotate){
			//test.currentFalling.rotateFallingPiece();
			test.fallingMoveRotate(g);
			paintComponent(g);
		//	System.out.println("Rotate ! " + test.currentFalling.getBounds() );
		}
		for(Square iter : fig.tetromino){
			g.setColor(iter.color);
			g.fill(iter.kwadrat);
			g.setColor(Color.BLACK);
			g.draw(iter.kwadrat);
		}


	}
	public void paintComponent(Graphics2D g){
		test.paintFalling(g);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Class for empty board - background.
	 * @author Micha³
	 *
	 */
	public class EmptyBoard extends Area{
		private int rows = 20;
		private int cols = 10;
		
		public EmptyBoard(Graphics2D g){
			this.paintEmptyBoard(g);
		}
		public Area emptyboard = new Area();
		public void paintEmptyBoard(Graphics2D g){
			for (int i=0; i<rows;i++){
				for(int j=0; j<cols;j++){
					Area a1 = new Area(new AreaSquare(j,i,g,Color.ORANGE).areaSquare);  //Need to use Area of each object ! Always !
					emptyboard.add(a1);
				}
				
			}
		}
		
	}
	/**
	 * Generating and manipulating of falling tetromino.
	 * @author Michal
	 *
	 */
	public class TestFallingPiece extends Area{
		int[][] currentPiece = new int[20][10];

		Color pieceColor;
		double rotateX=0;
		double rotateY=0;
		
		boolean moveEnd=false;
		/**
		 * orient variable, to tell which orientation Area has.
		 * 0 - original orientation;
		 * 1 - left orientation;
		 * 2 - upside down orientation
		 * 3 - right orientation
		 */
		int orient=0; 
		
		public TestFallingPiece(){
			Random rand = new Random();
			int nr = rand.nextInt(7);
			currentPiece = PIECE_TABLE[nr];
			pieceColor = PIECE_COLOR[nr];
			for (int i=0; i<currentPiece.length;i++){
			for(int j=0; j<currentPiece[i].length;j++){
				if(currentPiece[i][j]==1){
					this.exclusiveOr(new Area(new AreaSquare(5-currentPiece[i].length/2+j,i).areaSquare));
					}
				}
			}
			orientOrigin();
		}
		
		public void moveFallingPiece(Graphics2D g){
			AffineTransform transform = AffineTransform.getTranslateInstance(0, 25);
			if(moveIsValid(transform) && moveEnd==false)	this.transform(transform);
			else moveEnd=true;
			orientOrigin();

		}
		public void rotateFallingPiece(Graphics2D g){
			AffineTransform rotate = AffineTransform.getRotateInstance(Math.toRadians(-90));
			if(moveIsValid(rotate)){
				this.transform(rotate);
				//orient++;
			//	orient%=4;
			}
			orientOrigin();
		}
		public void fallingPieceLeft(){
			AffineTransform transform = AffineTransform.getTranslateInstance(-25, 0);
			if(moveIsValid(transform)) this.transform(transform);
			orientOrigin();
		}
		public void fallingPieceRight(){
			AffineTransform transform = AffineTransform.getTranslateInstance(25, 0);
			if(moveIsValid(transform))this.transform(transform);
			orientOrigin();
		}
		
		public void rotateFallingPiece(){
			AffineTransform rotate = AffineTransform.getRotateInstance(Math.toRadians(-90),rotateX, rotateY);
			
			if(moveIsValid(rotate)){
				this.transform(rotate);
				orient++;
				orient%=4;
			}
			orientOrigin();
		}
		
		/**
		 * Function calculating point around which we rotate figure.
		 */
		public void orientOrigin(){
			if(this.currentPiece.equals(O_PIECE)){
				rotateX=this.getBounds().getCenterX();
				rotateY=this.getBounds().getCenterY();
			}else if(this.currentPiece.equals(I_PIECE)){
				if(orient==0){
					rotateX=this.getBounds().getCenterX()+12.5;
					rotateY=this.getBounds().getCenterY();
				}
				if(orient==1){
					rotateX=this.getBounds().getCenterX();
					rotateY=this.getBounds().getCenterY()+12.5;
				}
				if(orient==2){
					rotateX=this.getBounds().getCenterX()-12.5;
					rotateY=this.getBounds().getCenterY();
				}
				if(orient==3){
					rotateX=this.getBounds().getCenterX();
					rotateY=this.getBounds().getCenterY()-12.5;
				}
			}
			else{
				if(orient==0){
					rotateX=this.getBounds().getCenterX();
					rotateY=this.getBounds().getCenterY()+12.5;
				}
				if(orient==1){
					rotateX=this.getBounds().getCenterX()+12.5;
					rotateY=this.getBounds().getCenterY();
				}
				if(orient==2){
					rotateX=this.getBounds().getCenterX();
					rotateY=this.getBounds().getCenterY()-12.5;
				}
				if(orient==3){
					rotateX=this.getBounds().getCenterX()-12.5;
					rotateY=this.getBounds().getCenterY();
				}
			}
		}
		/**
		 * Checking if move is allowed.
		 * @param transform
		 * @return
		 */
		public boolean moveIsValid(AffineTransform transform){
			return board.emptyboard.contains(this.createTransformedArea(transform).getBounds());
		}
		
		public void paintTest(Graphics2D g){
			g.setColor(pieceColor);
			g.fill(this);
			g.setColor(Color.BLACK);
			g.draw(this);
			//System.out.println("Maluje! " + pieceColor.toString());
		}
	}
	/**
	 * Class handling falling tetromino, problem with removal of rows.
	 * @author Micha³
	 *
	 */
	public class FallingPieces extends TestFallingPiece{
		ArrayList<TestFallingPiece> fallen = new ArrayList<TestFallingPiece>();
		TestFallingPiece currentFalling;
		Area fallenPieces = new Area();
		boolean rowFull=false;
		
		public FallingPieces(){
			fallen.clear();
			currentFalling=new TestFallingPiece();
		}
		public void endMoveDown(){
			if(currentFalling.moveEnd){
				fallen.add(currentFalling);
				fallenPieces.add(currentFalling);
				System.out.println("Test");
				currentFalling=new TestFallingPiece();
				isRowFull();
			}
		}
		public void moveCurrentPiece(Graphics2D g){
			currentFalling.moveFallingPiece(g);
			endMoveDown();
		}
		//obracanie spadaj¹cej figury
		public void fallingMoveRotate(Graphics2D g){
			AffineTransform rotate = AffineTransform.getRotateInstance(Math.toRadians(-90),currentFalling.rotateX,currentFalling.rotateY);
			if(fallingMoveIsValid(rotate)) currentFalling.rotateFallingPiece();
		}
		//ruch spadaj¹cej figury w lewo
		public void FallingMoveLeft(Graphics2D g){
			AffineTransform transform = AffineTransform.getTranslateInstance(-25, 0);
			if(fallingMoveIsValid(transform)) currentFalling.fallingPieceLeft();;
		}
		//ruch spadaj¹cej figury w prawo
		public void FallingMoveRight(Graphics2D g){
			AffineTransform transform = AffineTransform.getTranslateInstance(25, 0);
			if(fallingMoveIsValid(transform)) currentFalling.fallingPieceRight();;
		}
		//Spadanie figury
		public void fallingMoveDown(Graphics2D g){
			AffineTransform transform = AffineTransform.getTranslateInstance(0, 25);
			if(fallingMoveIsValid(transform))	moveCurrentPiece(g);
			else currentFalling.moveEnd=true;
			endMoveDown();
		}
		//Dodaæ tutaj funkcje do ruchu na boki i obrotu
		public boolean fallingMoveIsValid(AffineTransform transform){
			Area temp = currentFalling.createTransformedArea(transform);
			temp.intersect(fallenPieces);
			return temp.isEmpty();
			
		}
		
		public void isRowFull(){
			Area row = new Area(new Rectangle(50, 50, 250, 25));
			AffineTransform transform = AffineTransform.getTranslateInstance(0, 25);
			Area temp=new Area();
			for(int i=1;i<=20;i++){
				temp.add(fallenPieces);
				temp.intersect(row);
				if(temp.equals(row)){
					rowFull=true;
					System.out.println("Rz¹d nr: " + " Jest pe³ny rz¹d !");
					clearRow(row);
				}
				row.transform(transform);
				System.out.println(row.getBounds2D());
			}
		}
		//usuniêcie rzêdu
		public void clearRow(Area row){
			if(rowFull){
				fallenPieces.subtract(row);
				int i=0;
				for(TestFallingPiece iter : fallen){
					System.out.println(i++ + "  " + iter.getBounds());
					
			}
			}
		}
		public void paintFalling(Graphics2D g){
			currentFalling.paintTest(g);
			//System.out.println(currentFalling.getBounds());
			for(int i=0; i<fallen.size();i++){
				fallen.get(i).paintTest(g);
			//	System.out.println(fallen.get(i).getBounds());
			}
			System.out.println("Koniec iteracji");
		}
	}
	
	
	
	/**
	 * Próbna klasa ze spadaj¹cym tetromino, do usuniêcia.
	 * @author Micha³
	 *
	 */
	
	public class FallingPiece extends Area{
		public Color fallingPieceColor = Color.RED;
		public FallingPiece(Graphics2D g){
			this.generateFallingPiece(g);
		}

		public Area fallingPiece = new Area();
		public void generateFallingPiece(Graphics2D g){
			for (int i=0; i<I_PIECE.length;i++){
				for(int j=0; j<I_PIECE[i].length;j++){
					if(I_PIECE[i][j]==1){
						Area a1 = new Area(new AreaSquare(5-I_PIECE[i].length/2+j,i,g,fallingPieceColor).areaSquare);
					fallingPiece.add(a1);}
				}
			}
		}
		public void paintFallingPiece(Graphics2D g){
			for (int i=0; i<I_PIECE.length;i++){
				for(int j=0; j<I_PIECE[i].length;j++){
					if(I_PIECE[i][j]==1){
						Area a1 = new Area(new AreaSquare(1,i,g,fallingPieceColor).areaSquare);
					fallingPiece.add(a1);}
				}
			}
				
			}
		
		public void moveFallingPiece(Graphics2D g){
			AffineTransform t = new AffineTransform();
			t.translate(0, 25);
			this.fallingPiece.transform(t);
			g.draw(this.fallingPiece);
			g.setColor(fallingPieceColor);
			g.fill(fallingPiece);
		}
	}
	
	public class AreaSquare extends Area{
		private int Height=25;
		private int Width=25;
		private int offsetX = 50;
		private int offsetY = 50;
		private int posX=20;
		private int posY=20;
		private Color color = new Color(33023);
		
		Rectangle a1;
		public Area areaSquare;

		public AreaSquare(int X, int Y, Graphics2D g){
			this.posX=X*this.Width+this.offsetX;
			this.posY=Y*this.Height+this.offsetY;
			a1 = new Rectangle(posX, posY, Width, Height);
			areaSquare = new Area(a1);
			this.paintAreaSquare(g);
		}
		public AreaSquare(int X, int Y){
			this.posX=X*this.Width+this.offsetX;
			this.posY=Y*this.Height+this.offsetY;
			a1 = new Rectangle(posX, posY, Width, Height);
			areaSquare = new Area(a1);
		}
		
		public AreaSquare(int X, int Y, Graphics2D g, Color c){
			this.posX=X*this.Width+this.offsetX;
			this.posY=Y*this.Height+this.offsetY;
			this.setColor(c);
			a1 = new Rectangle(posX, posY, Width, Height);
			areaSquare = new Area(a1);
			this.paintAreaSquare(g);
		}
		public AreaSquare(int X, int Y, int Width, int Height, Graphics2D g, Color c){
			this.posX=X+this.offsetX;
			this.posY=Y+this.offsetY;
			this.Width=Width;
			this.Height=Height;
			this.setColor(c);
			a1 = new Rectangle(posX, posY, Width, Height);
			areaSquare = new Area(a1);
			this.paintAreaSquare(g);
		}
		public void setColor(Color color) {
			this.color = color;
		}
		
		public void paintAreaSquare(Graphics2D g){
			g.setColor(color);
			g.fill(areaSquare);
			g.setColor(Color.BLACK);
			g.draw(areaSquare);

		}
		
	}
	
}
