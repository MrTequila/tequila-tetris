package Gra;

import java.awt.Color;

import java.util.ArrayList;
import java.util.Random;


public class Tetromino {
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
	
	
	int[][] currentPiece = new int[20][10];
	Color pieceColor;
	
	Point tetrominoBase;
	ArrayList<Square> tetromino = new ArrayList<Square>();
	
	public Tetromino(){
		Random rand = new Random();
		int nr = rand.nextInt(7);
		currentPiece = PIECE_TABLE[nr];
		pieceColor = PIECE_COLOR[nr];
		for (int i=0; i<currentPiece.length;i++){
		for(int j=0; j<currentPiece[i].length;j++){
			if(currentPiece[i][j]==1){
				tetromino.add(new Square((5-currentPiece[i].length/2+j)*25+50,i*25+50,pieceColor));
				}
			}
		}
	}
	
	public static void main(String[] args) {
		Tetromino nowa=new Tetromino();
		for(Square test : nowa.tetromino){
			System.out.println(test.kwadrat.getBounds());
		}
		
	
	}
}
