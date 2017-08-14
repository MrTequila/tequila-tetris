package Gra;

import java.util.ArrayList;
import java.util.Random;



public class Tetris {
	final int WIERSZE = 21;
	final int KOLUMNY = 12;
	final static int[][] O_PIECE = {{1,1}, 
									{1,1}};
	final static int[][] I_PIECE = {{1,1,1,1}};
	final static int[][] L_PIECE = {{1,1,1},
									{1,0,0}};
	final static int[][] J_PIECE = {{1,0,0},
									{1,1,1}};
	final static int[][] S_PIECE = {{0,1,1},
									{1,1,0}};
	final static int[][] Z_PIECE = {{1,1,0},
									{0,1,1}};
	final static int[][] T_PIECE = {{1,1,1},
									{0,1,0}};
	final static int[][][] PIECE_TABLE = {O_PIECE, I_PIECE, L_PIECE, J_PIECE, S_PIECE, Z_PIECE, T_PIECE};

	public class Koordynaty{
		public int KoordWiersz=0;
		public int KoordKolumna=0;
		public Koordynaty(int wier, int kol){
			KoordWiersz=wier;
			KoordKolumna=kol;
		}
	}
	private boolean fullRow=false;
	public int score=0;
	
	public int plansza[][] = new int[WIERSZE][KOLUMNY];
	public int fallingPiece[][] = new int[WIERSZE][KOLUMNY];
	
	
	//Creator
	public Tetris(){
		this.zeruj();
	}
	

	
	//Funkcja zeruj¹ca planszê
	public void zeruj(){
		for(int i=0;i<WIERSZE-1;i++){
			for(int j=1;j<KOLUMNY-1;j++){
				plansza[i][j]=0;
			}
		}
		for (int i=0;i<WIERSZE;i++){
			plansza[i][0]=1;
			plansza[i][KOLUMNY-1]=1;
		}
		for (int i=0;i<KOLUMNY-1;i++) plansza[WIERSZE-1][i]=1;
	}
	public int[][] zeruj(int[][] temp){
		for(int i=0;i<temp.length;i++){
			for(int j=0;j<temp[i].length;j++){
				temp[i][j]=0;
			}
		}
		return temp;
	}
	
	//Funkcja drukuj¹ca aktualn¹ planszê
	public void drukuj(int[][] temp ){
		for(int i=0;i<temp.length;i++){
			for(int j=0;j<temp[i].length;j++){
				System.out.print(temp[i][j]+" ");
			}
			System.out.println();
		}
	}
	
	//dodanie dwóch figur do siebie
	public int[][] add(int[][] a, int[][] b){
		int temp[][] = new int[WIERSZE][KOLUMNY];
		for(int i=0;i<WIERSZE;i++){
			for(int j=0;j<KOLUMNY;j++){
				temp[i][j]=a[i][j]+b[i][j];
			}
		}
		return temp;
	}
	
	//rozpoczêcie nowej spadaj¹cej figury
	public void newPiece(){
		Random generator = new Random();
		int nr = generator.nextInt(7);
		fallingPiece = this.zeruj(fallingPiece);
		for (int i=0; i<PIECE_TABLE[nr][0].length; i++){
			for(int j=0; j<PIECE_TABLE[nr].length;j++){
			fallingPiece[j][fallingPiece[j].length/2-PIECE_TABLE[nr][j].length/2+i] = fallingPiece[j][fallingPiece[j].length/2-PIECE_TABLE[nr][j].length/2+i]+PIECE_TABLE[nr][j][i];
			}
		}
		
	}
	
	//Rusza spadaj¹c¹ figurê w dó³ i sprawdza czy mo¿e to zrobiæ, jak nie - dodaje wczeœniejsz¹ pozycjê do planszy i generuje now¹ figurê
	public void newPieceDown(){
		int[][] temp = new int[WIERSZE][KOLUMNY];
		temp = this.add(temp, fallingPiece);
			for(int i=WIERSZE-2;i>=0;i--){
				for(int j=0;j<KOLUMNY;j++){
					if(temp[i][j]==1){
						temp[i][j]=0;
						temp[i+1][j]=1;
				}
			}
		}
			if(this.moveisValid(temp, plansza)) fallingPiece = temp;
			else{
				plansza = add(plansza,fallingPiece);
				this.fullRow();
				newPiece();
			}
			
	}
	
	//Rusza spadaj¹c¹ figurê w lewo
	public void newPieceLeft(){
		int[][] temp = new int[WIERSZE][KOLUMNY];
		temp = this.add(temp, fallingPiece);
		for(int j=1;j<KOLUMNY-1;j++){	
			for(int i=WIERSZE-2;i>=0;i--){
				if(temp[i][j]==1){
						temp[i][j]=0;
						temp[i][j-1]=1;
				}
			}
		}
			if(this.moveisValid(temp, plansza)) fallingPiece = temp;	
	}
	
	//Rusza spadaj¹c¹ figurê w prawo
	public void newPieceRight(){
		int[][] temp = new int[WIERSZE][KOLUMNY];
		temp = this.add(temp, fallingPiece);
		for(int j=KOLUMNY-2;j>=1;j--){	
			for(int i=WIERSZE-2;i>=0;i--){
				if(temp[i][j]==1){
						temp[i][j]=0;
						temp[i][j+1]=1;
				}
			}
		}
			if(this.moveisValid(temp, plansza)) fallingPiece = temp;	
	}
	
	
	//sprawdzanie czy ruch jest poprawny
	public boolean moveisValid(int[][] a, int[][] b)
	{
		int[][] temp = new int[WIERSZE][KOLUMNY];
		temp=this.add(a, b);
		boolean output=false;
		outerloop:
		for(int i=0;i<temp.length;i++){
			for(int j=0;j<temp[i].length;j++){
				if(temp[i][j]>1 || temp[i][j]<0){
					output = false;
					break outerloop;
				}
				else output = true;
			}
		}
		return output;
	}
	
	/**Funkcja obracaj¹ca spadaj¹c¹ figurê
	 * Wykorzystaæ mno¿enie macierzy, inaczej nie dzia³a !
	*/
	public void rotateFallingPiece(){
		int[][] temp = new int[WIERSZE][KOLUMNY]; //tymczasowa tablica 

		double tempOriginWier=0;
		double tempOriginKol=0;
		this.zeruj(temp);
		ArrayList<Koordynaty> figura = new ArrayList<Koordynaty>();
		for(int i=fallingPiece.length-1;i>=0;i--){
			for(int j=0;j<fallingPiece[i].length;j++){
				if(fallingPiece[i][j]==1){
					figura.add(new Koordynaty(i,j));
				}
			}
		}
		/**Wyliczenie punktu zerowego do obrócenia figury*/ 
		for (Koordynaty koordynaty : figura)
		{
			tempOriginWier+=koordynaty.KoordWiersz;
			tempOriginKol+=koordynaty.KoordKolumna;
		}
		int originWier = (int) Math.round(tempOriginWier/4);
		int originKol = (int) Math.round(tempOriginKol/4);
		ArrayList<Koordynaty> wektor = new ArrayList<Koordynaty>();
		for (int i=0; i<figura.size();i++)
		{
			wektor.add(new Koordynaty(figura.get(i).KoordWiersz-originWier, figura.get(i).KoordKolumna-originKol));
			figura.get(i).KoordWiersz=-1*wektor.get(i).KoordKolumna + originWier;
			figura.get(i).KoordKolumna=1*wektor.get(i).KoordWiersz+originKol;
		}
		for(Koordynaty koordynaty : figura){
			temp[koordynaty.KoordWiersz][koordynaty.KoordKolumna]=1;
		}
		if(this.moveisValid(temp, plansza)) fallingPiece = temp;

	}
	
	/**Ruch figury w dó³*/
	public void moveDown(){
		for(int n=WIERSZE-1;n>=0;n--){
			for(int i=WIERSZE-2;i>=0;i--){
				for(int j=0;j<KOLUMNY;j++){
					if(plansza[i][j]==1 && plansza[i+1][j]==0){
						plansza[i][j]=0;
						plansza[i+1][j]=1;
				}
			}
		}
		}
	}
	
	/**Sprawdzenie czy w planszy jest pe³ny wiersz i wyczyszczenie
	 * */
	public void fullRow(){
		int sumRow=0;
		for(int i=WIERSZE-2;i>0;i--){
			sumRow=0;
			for(int j=1;j<KOLUMNY-1;j++){
				if(plansza[i][j]==1){
					sumRow+=1;
				}
			}
			if(sumRow==10) //je¿eli wszystkie pola w rzêdzie zajête, to usuwamy rz¹d
			{
				for(int j=1;j<KOLUMNY-1;j++){
					plansza[i][j]=0;
				}
				fullRow=true;
				score+=10;
			}
		}
		this.fullRowClear();
		System.out.println(score);
	}
	
	public void fullRowClear(){
		if(fullRow){
			for(int j=WIERSZE-2;j>0;j--){
			for(int i=WIERSZE-2;i>0;i--){
				for(int k=1;k<KOLUMNY-1;k++){
					if(plansza[i-1][k]==1 && plansza[i][k]==0){
						plansza[i-1][k]=0;
						plansza[i][k]=1;
					}
				}
			}
			}
		fullRow=false;
		this.fullRow();
		}
	}

	public static void main(String[] args) {
		Tetris tetr = new Tetris();
		tetr.drukuj(tetr.plansza);
		System.out.println();
		tetr.newPiece();
		tetr.drukuj(tetr.fallingPiece);
		System.out.println();
		for(int i=0; i<3;i++)tetr.newPieceDown();
		System.out.println("*********");
		tetr.drukuj(tetr.fallingPiece);
		System.out.println();
		tetr.rotateFallingPiece();
		tetr.drukuj(tetr.fallingPiece);
	}

	
}
