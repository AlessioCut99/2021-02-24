package it.polito.tdp.PremierLeague.model;

public class GiocatoreMigliore {

	Player player;
	double delta;
	public GiocatoreMigliore(Player player, double delta) {
		super();
		this.player = player;
		this.delta = delta;
	}
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public double getDelta() {
		return delta;
	}
	public void setDelta(double delta) {
		this.delta = delta;
	}
	@Override
	public String toString() {
		return "GiocatoreMigliore: \n"+ player + ", delta efficienza=" + delta;
	}
	
	
	
}
