import java.util.Map;
import java.util.Set;
import java.util.HashMap;

/**
	* Class Node
	* @author : -
	* @version : 1.0
	* Création du noeud
	* E : Identifiant du noeud ( de préférence : Integer, String )

	* Compilation : javac -encoding utf8 Graph.java
*/

public class Node<E>
{
	/*------------------------------------------------------*/
	/*						ATTRIBUTS						*/
	/*------------------------------------------------------*/

	/**
		* Définition du serialVersionUID
	*/
	private static final long serialVersionUID = 1L;

	private E id;										 	// L'identifiant du noeud
	private static Integer nbNodes = Integer.valueOf(0); 	// Numéro séquentiel auto-incrémenté informant sur le nombre de Noeud
	private Integer numNode;							 	// Le numéro du node
	private Integer distance;								// la distance par rapport au noeud source
	private Node<E> predecessor;							// Le noeud parent (le noeud prédécesseur)
	private Map<Node<E>, Integer> hashNeighborNodes;		// la liste de ses voisins <Node, poids>


	/*------------------------------------------------------*/
	/*						CONSTRUCTEUR					*/
	/*------------------------------------------------------*/
	
	/**
	 * Constructeur privée Node
	 * @param identifiant : L'identifiant du noeud
	 * @param distance : La distance associé à ce nom à partir du point d'entrée
	*/
	private Node(E identifiant)
	{
		this.id					= identifiant;
		this.numNode			= ++Node.nbNodes;
		this.distance			= 0;
		this.predecessor		= null;
		this.hashNeighborNodes	= new HashMap<Node<E>, Integer>(); // Ajout des voisins dans les modificateurs
	}
	
	/**
	 * Constructeur privée par recopie
	 * @param node : Le noeud à recopier
	 */
	private Node(Node<E> node)
	{
		this.id					= node.getId();
		this.numNode			= node.getNum();
		this.distance			= node.getDistance();
		this.predecessor		= node.getPredecessor();
		this.hashNeighborNodes	= node.getNeighborNodes();
	}

	/**
		* Design Factory Node pour l'instanciation
		* @param identifiant : l'identifiant.
		* @return Retourne une instance Node.
	*/
	public static <E> Node<E> createNode(E identifiant)
	{
		// Vérification que identifiant n'est pas null
		if ( identifiant == null ) 
		{
			System.err.println("[ERREUR] Identifiant est null !");
			return null;
		}

		return new Node<E>(identifiant);
	}

	/**
		* Design Factory Node pour l'instanciation
		* @param node : le noeud à recopier.
		* @return Retourne une instance Node.
	*/
	public static <E> Node<E> createNode(Node<E> node)
	{
		// Vérification que identifiant n'est pas null
		if ( node == null ) 
		{
			System.err.println("[ERREUR] Le noeud est null !");
			return null;
		}

		return new Node<E>(node);
	}

	/*--------------------------------------------------*/
	/*						MÉTHODES					*/
	/*--------------------------------------------------*/

	/*--------------*/
	/*	ACCESSEURS	*/
	/*--------------*/

	// Noeud
	public E 		getId() 			{ return this.id;			}
	public Integer 	getNum() 			{ return this.numNode;		}
	public Integer 	getDistance() 		{ return this.distance;		}
	public Node<E>	getPredecessor()	{ return this.predecessor;	}

	// Voisins du Noeud
	public Map<Node<E>, Integer> getNeighborNodes() { return this.hashNeighborNodes; }

	/*---------------*/
	/* MODIFICATEURS */
	/*---------------*/

	public void setDistance(Integer distance)			{ this.distance = distance; 		};
	public void setPredeccessor(Node<E> predecessor)	{ this.predecessor = predecessor; 	};

	/**
	 * Ajout un noeud voisin à l'objet courant
	 */
	void addNeighbourNode(Node<E> node, Integer distance)
	{
		this.hashNeighborNodes.put(node, distance);
	}

	/**
		* @return Retourne l'affichage de l'état de l'objet
	*/
	public String toString()
	{
		String sRet = new String();

		sRet += "Node (" + this.id + ") -> d=" + distance + " -> ";
		
		// Utilisation de Set pour récupérer les entrées de la map
		/*Set<Map.Entry<Node<E>, Integer>> st = this.hashNeighborNodes.entrySet();
		for(Map.Entry<Node<E>, Integer> entry: st)
		{
			sRet += "(" + entry.getKey().getId() + ":" + entry.getValue() + ")" + " - ";
		}*/

		return sRet;
	}
}