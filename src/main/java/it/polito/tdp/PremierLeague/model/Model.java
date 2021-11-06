package it.polito.tdp.PremierLeague.model;

import java.time.chrono.MinguoChronology;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	PremierLeagueDAO dao;
	Graph<Player, DefaultWeightedEdge> grafo;
	Map<Integer, Player> idMap;
	
	public Model() {
		dao = new PremierLeagueDAO();
		idMap = new HashMap<Integer, Player>();
		this.dao.listAllPlayers(idMap);
	}
	
	public void creaGrafo(int matchId) {
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		//Aggiungo i vertici
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(matchId, idMap));
		
		//aggiungo gli archi
		for(Adiacenza a: this.dao.getAdiacenze(matchId, idMap)) {
			if(a.getPeso() >= 0) {
				//p1 migliore di p2
				if(grafo.containsVertex(a.getP1()) && grafo.containsVertex(a.getP2()))
					Graphs.addEdgeWithVertices(this.grafo, a.getP1(), a.getP2(), a.getPeso());
			}else {
				if(grafo.containsVertex(a.getP1()) && grafo.containsVertex(a.getP2()))
					Graphs.addEdgeWithVertices(this.grafo, a.getP2(), a.getP1(),(-1)*a.getPeso());
			}
		}
		
	}
	
	public List<Match> getAllMatch(){
		List<Match> matches = new ArrayList<>(dao.listAllMatches());
		Collections.sort(matches);
		return matches;
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}

	public int nArchi() {
		return this.grafo.edgeSet().size();
	}

	public GiocatoreMigliore getGiocatoreMigliore() {
		
		if(grafo == null)
			return null;
		
		Player playerBest = null;
		Double maxDelta = (double) Integer.MIN_VALUE;
		
		for(Player p : grafo.vertexSet()) {
			//calcolo la somma dei pesi degli archi uscenti
			double pesoUscenti= 0.0;
			
			for(DefaultWeightedEdge edge: this.grafo.outgoingEdgesOf(p)) {
				pesoUscenti += this.grafo.getEdgeWeight(edge);
			}
			
			//calcolo la somma dei pesi degli archi entranti
			double pesoEntranti= 0.0;
			for(DefaultWeightedEdge edge: this.grafo.incomingEdgesOf(p)) {
				pesoEntranti += this.grafo.getEdgeWeight(edge);
			}
			
			double delta = pesoUscenti - pesoEntranti;
			if(delta > maxDelta) {
				playerBest = p;
				maxDelta = delta;
			}
		}
		
		
		return new GiocatoreMigliore(playerBest, maxDelta);
	}
	
}
