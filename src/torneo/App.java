package torneo;

import java.io.FileNotFoundException;

public class App {
	
	public static void main(String[] args) throws FileNotFoundException{
		Torneo arquerosOlimpicos = new Torneo("1000tiros.csv");
		arquerosOlimpicos.getSalida();
	}

}
