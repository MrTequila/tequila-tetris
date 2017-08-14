package Gra;

import java.awt.Color;
import java.awt.Rectangle;

public class Square{
	final int rozmiar = 25;
	Point bazowy;
	Rectangle kwadrat;
	Color color;
	
/**
 * Tworzenie kwadratu
 * @param x - koordynaty X
 * @param y - koordynaty Y
 * @param bazowy - okreœla punkt bazowy kwadratu
 */
	public Square(int x, int y, Color color){
		this.color=color;
		bazowy = new Point(x, y);
		kwadrat = new Rectangle(x, y, rozmiar, rozmiar);
	}

	
}


