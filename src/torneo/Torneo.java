package torneo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.TreeMap;

public class Torneo {
	
	private Map<Integer, PriorityQueue<Integer>> participantes;
	private TreeMap<Integer, LinkedList<Integer>> podio;
	
	public Torneo(String archivo) throws FileNotFoundException{
		
		/*Armamos el constructor asi para que lo resolvamos todo 
		 * desde aqui y poder correrlo desde un main de forma mas practica */
		setParticipantes(archivo);
		setPodio();
	}

	private void setParticipantes(String archivo) throws FileNotFoundException {
		
		//Construimos el TreeMap
		participantes = new HashMap<Integer, PriorityQueue<Integer>>();
		
		//Abrimos el archivo
		Scanner sc = new Scanner(new File(archivo));
		
		//Recorremos el archivo
		while(sc.hasNext()) {
			String[] datos = sc.nextLine().split(",");
			Integer key = Integer.parseInt(datos[0]);
			Integer puntaje = getPuntaje(Double.parseDouble(datos[1]), Double.parseDouble(datos[2]));
			
			
			//Agregamos el puntaje a la lista del participante identificado c/una key
			if (puntaje > 0) {
				if(participantes.containsKey(key)) {
					participantes.get(key).offer(puntaje);
				} else { //Si es la 1era vez que leo el participante, creo la cola de prioridad
					PriorityQueue<Integer> colaP = new PriorityQueue<Integer>(Collections.reverseOrder());//Ordenamos de mayor a menor
					colaP.offer(puntaje);
					participantes.put(key, colaP);
				}
			}
		}
		
		sc.close();
	}
	
	//Sumamos los puntajes de los participantes y lo usamos como Key en podio
	private void setPodio() {
		podio = new TreeMap<Integer, LinkedList<Integer>>(Collections.reverseOrder()); //Lo inicializamos de mayor a menor puntaje
		
		
		//Recorro los participantes, con el metodo para recorrer los Maps
		for(Map.Entry<Integer, PriorityQueue<Integer>> entry : this.participantes.entrySet()) {
			//Vamos a considerar solamente los arqueros con 5 o mas tiros validos
			
			if (entry.getValue().size() >= 5) {
				Integer participante = entry.getKey(); //Tomo el numero de participante con 5 o mas tiros
				Integer suma = 0; 
				for(int i = 0; i < 5; i++) { //Sumos los 5 puntajes de la cola de prioridad (los mejores)
					suma += entry.getValue().poll();
				}
				
				if (podio.containsKey(suma))
					podio.get(suma).add(participante);
				else {
					LinkedList<Integer> listaP = new LinkedList<Integer>();
					listaP.add(participante);
					podio.put(suma, listaP);
				}
			}
		}
		
	}
	
	//Hacemos un iterador para recorrer el podio.
	public void getSalida() throws FileNotFoundException{
		Iterator<Map.Entry<Integer, LinkedList<Integer>>> itr = this.podio.entrySet().iterator();
		
		PrintWriter salida = new PrintWriter(new File("podio.out"));
		
		int i = 1;
		
		while (itr.hasNext() && i <= 3) {
			Map.Entry<Integer, LinkedList<Integer>> entry = itr.next();
			
			
			salida.println(i + " puesto" + entry.getKey() + " puntos, participante: " + entry.getValue());
			
			//Imprimo por consola tambien para verlo en Eclipse
			System.out.println(i + " puesto" + entry.getKey() + " puntos, participante: " + entry.getValue());
			
			i++;
		}
		salida.close();
	}
	
	
	/*Coordenadas x e y del blanco. Pasamos de las 
	 * coordenadas del tiro a un valor discreto*/
	private Integer getPuntaje(Double x, Double y) {
		Integer puntaje = 0;
		Double distanciaAlcentro = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		
		if(distanciaAlcentro <= 10)
			puntaje = 1000;
		else if(distanciaAlcentro <= 20)
			puntaje = 500;
		else if(distanciaAlcentro <= 30)
			puntaje = 200;
		else if(distanciaAlcentro <= 40)
			puntaje = 100;
		else if(distanciaAlcentro <= 50)
			puntaje = 50;
		else if(distanciaAlcentro > 50)
			puntaje = -1; //Tiro invalido
		
		
		return puntaje;
	}

}
