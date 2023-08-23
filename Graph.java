import java.util.ArrayList;
import java.util.Map;
import java.util.Set;


/**
	* Class Graph
	* @author : -
	* @version : 1.0
	* Création de graphe
	* Algortihme de Bellman-Ford
	* Algortihme de Dijkstra

	* Compilation 	: javac -encoding utf8 Graph.java
	* Exécution 	: java Graph
*/

public class Graph
{
	/*-----------*/
	/* ATTRIBUTS */
	/*-----------*/

	/**
		* Définition du serialVersionUID
	*/
	private static final long serialVersionUID = 1L;

	// ArrayList permettant d'accèder à n'importe quelle noeud sans à avoir à refaire tous les noeuds (LinkedList)
	private ArrayList<Node<String>> sNodes; // Informe sur la liste de noeud

	// Pour l'affichage, sauvegarde les valeurs pour les affichers dans le toString
	private ArrayList<ArrayList<Node<String>>> alValuesDisplay; // Pour l'affichage // i=ArrayList, j=ArrayList<Integer>


	/*--------------*/
	/* CONSTRUCTEUR */
	/*--------------*/

	/**
		* Constructeur Graph
	*/
	public Graph()
	{
		this.sNodes = new ArrayList<>();
		this.alValuesDisplay = new ArrayList<ArrayList<Node<String>>>();
	}

	/*--------------------------------------------------*/
	/*						MÉTHODES					*/
	/*--------------------------------------------------*/

	/*--------------*/
	/*	ACCESSEURS	*/
	/*--------------*/

	/**
		* @return Retourne la liste des noeuds du graphe.
	*/
	public ArrayList<Node<String>> getNodes()
	{
		return this.sNodes;
	}

	/**
		* @return Retourne l'indice du noeud de l'array-list qui a la valeur la plus petite
	*/
	private int getIndiceMinNode(ArrayList<Node<String>> lstVertices)
	{
		int indice 	= 0;
		int value 	= lstVertices.get(0).getDistance(); 
		for(int cpt=1; cpt<lstVertices.size(); cpt++)
		{
			if ( lstVertices.get(cpt).getDistance() < value )
			{
				indice = cpt;
				value = lstVertices.get(cpt).getDistance(); 
			}
		}

		return indice;
	}
	
	/*---------------*/
	/* MODIFICATEURS */
	/*---------------*/

	/**
		* @return Retourne true si l'ajout du noeud a été effectué dans la liste des noeuds du graphe et false dans le cas contraire.
		* @param node : Le noeud à ajouter.
	*/
	public boolean addNode(Node<String> node)
	{
		return this.sNodes.add(node);
	}

	/**
		* Méthode pour enregistrer les différentes distances à afficher dans le tableau de la méthode toString()
		* @param alNodes : l'array-list de noeud à enregistrer pour l'affichage dans la méthode toString().
	*/
	private void saveDistanceDisplay(ArrayList<Node<String>> alNodes)
	{
		ArrayList<Node<String>> alSavedNodes = new ArrayList<Node<String>>();

		for(Node<String> node : alNodes)
		{
			Node<String> nodeDisplay = Node.createNode(node);
			alSavedNodes.add( nodeDisplay );
		}

		this.alValuesDisplay.add( alSavedNodes );
	}

	/**
		* Algorithme de Bellman-Ford
		* Calcul du plus court chemin
		* @param g : Le graphe.
	*/
	public void bellmanford(Graph g)
	{
		/*-------------------*/
		/* Variables locales */
		/*-------------------*/
		boolean change = true;				// Informe si il faut arrêter algorithme à cause des répétions de lignes du tableau car les distances restent les mêmes
		boolean circuitNegative = false;	// Informe si il y a un circuit négatif

		// Affecte à la source la valeur 0 (d[s] = 0)
		this.sNodes.get(0).setDistance(Integer.valueOf(0));
		
		for(Node<String> node : this.sNodes)
		{
			if ( ! node.equals(this.sNodes.get(0)) )
			{
				node.setDistance(Integer.valueOf(Integer.MAX_VALUE)); // Initialise tous les autres sommets à l'infini (d[v] = +∞)
				node.setPredeccessor(null);
			}
		}

		// Sauvegarde les valeurs pour l'affichage de l'itération initialisation
		this.saveDistanceDisplay(this.sNodes);

		int cpt = 1;
		while( cpt<(this.sNodes.size()-1) && change)
		{
			change = false;
			for(Node<String> node : this.sNodes) // Pour chaque noeud
			{
				Set<Map.Entry<Node<String>, Integer>> st = node.getNeighborNodes().entrySet();
				for(Map.Entry<Node<String>, Integer> entry: st) // Pour chaque voisin du noeud
				{
					if ( entry.getKey().getDistance() >  (node.getDistance() + entry.getValue()) )
					{
						entry.getKey().setDistance( Integer.valueOf( node.getDistance().intValue() + entry.getValue().intValue() ) );
						entry.getKey().setPredeccessor( node );
						change = true;
					}
				}
			}

			// Sauvegarde les valeurs pour l'affichage de l'itération cpt
			this.saveDistanceDisplay(this.sNodes);
		}

		// Après que les ditances ont été fixés, vérification circuit négatif
		for(Node<String> node : this.sNodes) // Pour chaque noeud
		{
			Set<Map.Entry<Node<String>, Integer>> st = node.getNeighborNodes().entrySet();
			for(Map.Entry<Node<String>, Integer> entry: st) // Pour chaque voisin du noeud
			{
				if ( entry.getKey().getDistance() >  (node.getDistance() + entry.getValue()) )
				{
					circuitNegative = true;
					break;
				}
			}
		}

		if ( circuitNegative )
		{
			System.out.println("Circuit négatif");
		}
	}

	/**
		* Algorithme de Dijkstra 
		* Calcul du plus court chemin
		* @param g : Le graphe.
	*/
	public void dijkstra(Graph g)
	{
		for(Node<String> node : this.sNodes)
		{
			node.setDistance(Integer.valueOf(Integer.MAX_VALUE)); // Initialise tous les sommets à l'infini (d[v] = +∞)
		}

		// Affecte à la source la valeur 0 (d[s] = 0)
		this.sNodes.get(0).setDistance(Integer.valueOf(0));

		ArrayList<Node<String>> lstVertices 		= new ArrayList<Node<String>>(); // Liste de sommets (F)
		ArrayList<Node<String>> lstVerticesVisits 	= new ArrayList<Node<String>>(); // Liste de sommets visités (E)

		// Ajout les noeuds du graphe dans la liste de sommets
		for(int cpt=0; cpt<g.getNodes().size(); cpt++)
		{
			lstVertices.add( g.getNodes().get(cpt) );
		}

		// Tant que la liste de sommets (F) n'est pas vide
		while( lstVertices.size() != 0 )
		{
			Node<String> nodeMove = lstVertices.get(this.getIndiceMinNode(lstVertices));
			lstVertices.remove(this.getIndiceMinNode(lstVertices)); // Retire le noeud minimal de la liste de sommets (F)
			lstVerticesVisits.add(nodeMove); // Ajout du noeud à la liste de sommets visités (E)

			Set<Map.Entry<Node<String>, Integer>> st = nodeMove.getNeighborNodes().entrySet();
			for(Map.Entry<Node<String>, Integer> entry: st) // Pour chaque voisin du noeud qui a été déplacé dans la liste de sommets visités (E)
			{
				if ( entry.getKey().getDistance() >  (nodeMove.getDistance() + entry.getValue()) )
				{
					entry.getKey().setDistance( Integer.valueOf( nodeMove.getDistance().intValue() + entry.getValue().intValue() ) );
					entry.getKey().setPredeccessor( nodeMove );
				}
			}
		}
	}

	/**
		* @return Retourne une chaîne de caractères centrées.
		* Center a string : https://stackoverflow.com/questions/8154366/how-to-center-a-string-using-string-format
	*/
	public static String centerString(int width, String s)
	{
		return String.format("%-" + width + "s", String.format("%" + (s.length() + (width - s.length()) /2) + "s", s ));
	}

	/**
		* @return Retourne le tableau de distances à afficher.
	*/
	public String toString()
	{
		if ( this.alValuesDisplay.isEmpty() )
		{
			return "Aucune valeur enregistrée";
		}

		String sRet = new String();
		int cellInit 	= 14;	// Cellule d'initialisation
		int cellChars 	= 18;	// Cellule de valeurs

		/**
		 	+-----------+-------------------+-------------------+-------------------+-------------------+-------------------+
			|			|		d(s)		|		d(a)		|		d(b)		|		d(c)		|		d(d)		|		
			+-----------+-------------------+-------------------+-------------------+-------------------+-------------------+
			|   init	|       0           |		+∞			|		+∞			|		+∞			|		+∞			|
			+-----------+-------------------+-------------------+-------------------+-------------------+-------------------+
			|	i=1		|		0			|		3			|		+∞			|		5			|		+∞			|
			+-----------+-------------------+-------------------+-------------------+-------------------+-------------------+
			|	i=2		|		0			|		3			|		8			|		4			|		+∞			|
			+-----------+-------------------+-------------------+-------------------+-------------------+-------------------+
			|	i=3		|		0			|		3			|		8			|		4			|		9			|
			+-----------+-------------------+-------------------+-------------------+-------------------+-------------------+
			|	i=4		|		0			|		3			|		8			|		4			|		9			|
			+-----------+-------------------+-------------------+-------------------+-------------------+-------------------+
		*/

		sRet += String.format("%1s", "+");
		for(int cpt=0; cpt<this.sNodes.size()+1; cpt++)
		{
			if ( cpt == 0 )
			{
				sRet += String.format("%"+cellInit+"s", "=").replace(" ", "="); // Répétition du caractère -
				sRet += String.format("%1s", "+");
			}
			else
			{
				sRet += String.format("%"+cellChars+"s", "=").replace(" ", "="); // Répétition du caractère -
				sRet += String.format("%1s", "+");
			}
		}	

		sRet += "\n";

		// Case vide
		sRet += String.format("%1s", "|");
		sRet += String.format("%"+cellInit+"s", "=").replace(" ", "="); // Avec espace pas besoin de répétition du caractère

		sRet += String.format("%-1s", "|");
		for(int cpt=0; cpt<this.sNodes.size(); cpt++)
		{
			String sId = this.alValuesDisplay.get(0).get(cpt).getId();
			if ( sId == null || sId.equals("") )
			{
				sId = "-";
			}

			sRet += Graph.centerString(cellChars, "d("+sId+")");
			sRet += String.format("%1s", "|");
		}

		sRet += "\n";

		sRet += String.format("%-1s", "+");
		for(int cpt=0; cpt<this.sNodes.size()+1; cpt++)
		{
			if ( cpt == 0 )
			{
				sRet += String.format("%"+cellInit+"s", "=").replace(" ", "="); // Répétition du caractère -
				sRet += String.format("%1s", "+");
			}
			else
			{
				sRet += String.format("%"+cellChars+"s", "=").replace(" ", "="); // Répétition du caractère -
				sRet += String.format("%1s", "+");
			}
		}

		sRet += "\n";
		
		int startCharInit=(int) (cellInit/2)-1; // Le première caractère à afficher commence à la moitié de cellInit;
		for(int row=0; row<this.alValuesDisplay.size(); row++) // row = ligne - col : colonne
		{
			sRet += String.format("%-1s", "|");
			for(int col=0; col<this.alValuesDisplay.get(row).size()+1; col++)
			{
				if ( col == 0 )
				{
					if ( row == 0 )
					{
						sRet += Graph.centerString(cellInit, "init");
						sRet += String.format("%1s", "|");
					}
					else
					{
						sRet += Graph.centerString(cellInit, "i=" + row);
						sRet += String.format("%1s", "|");
					}
				}
				else
				{
					String sValue = Integer.toString(this.alValuesDisplay.get(row).get(col-1).getDistance());
					if ( sValue.equals(Integer.toString(Integer.MAX_VALUE)))
					{
						sValue = "+Inf";
					}

					sRet += Graph.centerString(cellChars, sValue);
					sRet += String.format("%1s", "|");
				}
			}
			sRet += "\n";
		}

		sRet += String.format("%1s", "+");
		for(int cpt=0; cpt<this.sNodes.size()+1; cpt++)
		{
			if ( cpt == 0 )
			{
				sRet += String.format("%"+cellInit+"s", "=").replace(" ", "="); // Répétition du caractère -
				sRet += String.format("%1s", "+");
			}
			else
			{
				sRet += String.format("%"+cellChars+"s", "=").replace(" ", "="); // Répétition du caractère -
				sRet += String.format("%1s", "+");
			}
		}

		return sRet;
	}

	/**
		* @return Retourne le chaîne de caractères informant sur les distances entre les noeuds.
	*/
	public String toStringSentence()
	{
		String sRet = new String();

		// Affichage justifié, récupère la taille maximum de la chaîne de caractères des noeuds
		int maxLengthId 		= this.sNodes.get(0).getId().length();
		int maxLengthDistance 	= Integer.toString(this.sNodes.get(0).getDistance()).length();
		for(int cpt=1; cpt<this.sNodes.size(); cpt++)
		{
			// L'identifiant
			if ( this.sNodes.get(cpt).getId().length() > maxLengthId )
			{
				maxLengthId = this.sNodes.get(cpt).getId().length();
			}

			// La distance
			if ( Integer.toString(this.sNodes.get(cpt).getDistance()).length() > maxLengthDistance )
			{
				maxLengthDistance = this.sNodes.get(cpt).getId().length();
			}
		}

		maxLengthId += 1;
		maxLengthDistance += 1;

		for(int cpt=1; cpt<this.sNodes.size(); cpt++)
		{
			sRet += String.format("%-10s", "Le sommet ") + String.format("%-"+maxLengthId+"s", this.sNodes.get(cpt).getId()) + " ";
			sRet += String.format("%-15s", "est à distance ");
			sRet += String.format("%-"+maxLengthDistance+"s", Integer.toString(this.sNodes.get(cpt).getDistance())) + " ";
			sRet += String.format("%-10s", "du sommet ") + String.format("%-"+maxLengthId+"s", this.sNodes.get(0).getId());
			sRet += "\n";
		}

		return sRet;
	}

	/**
		* Méthode main
		* @param args
	*/
	public static void main(String[] args)
	{
		System.setProperty("file.encoding", "UTF-8");
		/*----------------*/
		/* INITIALISATION */
		/*----------------*/
		System.out.println("[Phase d'initialisation]");

		// Création du graphe g
		Graph g = new Graph();

		// Création des noeuds du graphe
		Node<String> n1 = Node.createNode("s"); // Chaque distance est à zéro.
		Node<String> n2 = Node.createNode("a");
		Node<String> n3 = Node.createNode("b");
		Node<String> n4 = Node.createNode("c");
		Node<String> n5 = Node.createNode("d");

		// Création des arcs c'est à dire pour un noeud à quelle distance est son voisin/ses voisins
		n1.addNeighbourNode(n2, 3);
		n1.addNeighbourNode(n4, 5);
		n2.addNeighbourNode(n3, 5);
		n2.addNeighbourNode(n4, 1);
		n3.addNeighbourNode(n5, 1);
		n4.addNeighbourNode(n2, 1);
		n4.addNeighbourNode(n3, 5);
		n4.addNeighbourNode(n5, 5);
		n5.addNeighbourNode(n1, 3);
		n5.addNeighbourNode(n3, 3);

		// Ajout des noeuds au graphe
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addNode(n4);
		g.addNode(n5);

		// Création du graphe
		Graph g2 = new Graph();

		// Création des noeuds du graphe
		Node<String> np1 = Node.createNode("z");
		Node<String> np2 = Node.createNode("u");
		Node<String> np3 = Node.createNode("x");
		Node<String> np4 = Node.createNode("y");
		Node<String> np5 = Node.createNode("v");

		// Création des arcs c'est à dire pour un noeud à quelle distance est son voisin/ses voisins
		np1.addNeighbourNode(np2, 6);
		np1.addNeighbourNode(np3, 7);
		np2.addNeighbourNode(np3, 8);
		np2.addNeighbourNode(np4, -4);
		np2.addNeighbourNode(np5, 5);
		np3.addNeighbourNode(np4, 9);
		np3.addNeighbourNode(np5, -3);
		np4.addNeighbourNode(np5, 7);
		np4.addNeighbourNode(np1, 2);
		np5.addNeighbourNode(np2, -2);
		
		// Ajout des noeuds au graphe
		g2.addNode(np1);
		g2.addNode(np2);
		g2.addNode(np3);
		g2.addNode(np4);
		g2.addNode(np5);
		
		/*--------------*/
		/* BELLMAN-FORD */
		/*--------------*/

		/*---------------*/
		/* PHASE DE TEST */
		/*---------------*/
		System.out.println("+===================+");
		System.out.println("|   PHASE DE TEST   |");
		System.out.println("+===================+");
		g.bellmanford(g);
		System.out.println("[EXERCICE 1 : Bellman-Ford]");
		System.out.println(g.toString());
		System.out.println(g.toStringSentence());
		System.out.println();
		System.out.println();

		/*---------------*/
		/* PHASE DE TEST */
		/*---------------*/
		g2.bellmanford(g2);
		System.out.println("[EXERCICE 2 : Bellman-Ford]");
		System.out.println(g2.toString());
		System.out.println(g2.toStringSentence());
		System.out.println();
		System.out.println();

		/*----------*/
		/* DIJKSTRA */
		/*----------*/

		/*---------------*/
		/* PHASE DE TEST */
		/*---------------*/
		g.dijkstra(g);
		System.out.println("[EXERCICE 1 : Dijkstra]");
		System.out.println(g.toString());
		System.out.println(g.toStringSentence());
		System.out.println();
		System.out.println();		
	}


	/*------------------------------------------*/
	/*		INFORMATIONS COMPLÉMENTAIRES		*/
	/*------------------------------------------*/
	
	/*----------------------------*/
	/* Algorithme de Bellman-Ford */
	/*----------------------------*/

	/** 
		Il permet de trouver les chemins les plus courts entre un sommet source et les autres sommets d'un graphe orienté pondéré. Les valeurs des arcs peuvent être négatives.
		L'algorithme permet également de détecter les circuits absorbants, c'est-à-dire, la somme des poids des arcs du circuit est négative.

		L'algorithme commence par une phase d'initialisation d'un tableau d des distances entre le sommet source s et les autres sommets.
		Ce tableau est indexé par les sommets du graphe. On initialise d[s]=0 et d[v] =+∞ pour tout autre sommet v du graphe.

		Après la phase d'initialisation, il y a la phase de relaxation des arcs qui consiste à mettre à jour les distances entre l'arc initial et les autres arcs.
		Pour chaque arc (u,v), si d(v )> d(u)+w(u, v) alors on met à jour d(u) + w(u, v).
		C'est-à-dire, si on trouve un chemin plus court du sommet source s au sommet u que le chemin précédent, on met à jour la distance de ce chemin La phase de relaxation est itérée le nombre de sommets du graphe – 1. w(u, v) est la valeur de l'arc reliant u à v.
	
		Pseudo-code de l'algorithme :

			Bellman-Ford (G,w,s) // Un graphe G, une matrice des coûts et un sommet sont les données du l'algorithme

			Déclarer un tableau d des coûts depuis le sommet s // le tableau d est indexé par les sommets

			Début
				d[s] = 0 // initialisation du tableau d

				Pour tout sommet v ≠ s faire
					d[v] = +∞
				Finpr

				Pour i variant de 1 à |V| – 1 faire // |V| nombre de sommets
					Pour tout arc (u,v) faire (relaxation de l’arc (u, v))
						Si d(v)>d(u)+w(u, v) alors d(v) = d(u) + w(u, v)
						Finsi
					Finpr
				Finpr
			Fin

		En sortie, le tableau d contient les distances des plus courts chemins du sommet s aux autres sommets.
		
		Afin d'optimiser l'algorithme pour ne pas avoir plusieurs lignes de coûts qui se répètent et trouver les circuits de longueur négative, voici l'algorithme proposé par notre professeur :

			Exemple de lignes de coûts qui se répètent :
			
			+-----------+-------------------+-------------------+-------------------+-------------------+-------------------+
			|			|		d(s)		|		d(a)		|		d(b)		|		d(c)		|		d(d)		|		
			+-----------+-------------------+-------------------+-------------------+-------------------+-------------------+
			|	init	|		0			|		+∞			|		+∞			|		+∞			|		+∞			|
			+-----------+-------------------+-------------------+-------------------+-------------------+-------------------+
			|	i=1		|		0			|		3			|		+∞			|		5			|		+∞			|
			+-----------+-------------------+-------------------+-------------------+-------------------+-------------------+
			|	i=2		|		0			|		3			|		8			|		4			|		+∞			|
			+-----------+-------------------+-------------------+-------------------+-------------------+-------------------+
			|	i=3		|		0			|		3			|		8			|		4			|		9			|
			+-----------+-------------------+-------------------+-------------------+-------------------+-------------------+
			|	i=4		|		0			|		3			|		8			|		4			|		9			| <-- Identique à l'avant dernière
			+-----------+-------------------+-------------------+-------------------+-------------------+-------------------+
			
	*/

	/*------------------------*/
	/* Algorithme de Dijkstra */
	/*------------------------*/

	/**
		L’algorithme de Dijkstra cherche aussi les plus courts chemins entre deux sommets dans un graphe orienté pondéré avec des poids positifs ou nuls.
		La recherche du plus court chemin entre un sommet source s et un sommet destination est basée sur le principe que les chemins du sommet s aux sommets qui composent ce plus court chemin sont aussi les plus courts.
		Le plus court chemin est construit en choisissant à chaque itération parmi les sommets restant à traiter les sommets adjacents les plus proches aux sommets dernièrement visités.

		Pseudo-code de l'algorithme :

			Dijikstra (G,w,s) // Un graphe G, une matrice des coûts et un sommet sont les données du l'algorithme

			Début
				Pour tout sommet v ∈ V faire // Initialisation
					d[v] = +∞
				Finpr

				d[s] = 0
				E = ∅ 		// ensemble des sommets visités
				F = V [G] 	// ensemble des sommets de G

				Tantque F ≠ ∅ faire
					u = v |d[v] = min{d[x] |x ∈ F } // on choisit le sommet avec la plus petite valeur de d
					F = F − {u}
					E = E ∪ {u}

					Pour tout sommet v ∈ adjacent[u] faire // relâchement de l'arc (u, v)
						Si d[v] > d[u] + w(u, v) alors d[v] = d[u] + w(u, v)
						Finsi
					Finpr
				Fintq
			Fin
	*/
}