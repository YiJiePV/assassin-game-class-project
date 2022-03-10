package hw5.karena;
/*Karena Qian
 * 8.15.2021
 * CS 211 Section C
 * Instructor: Neelakantan Kartha
 * Assassin Manager */
/* This is the class Assassin Manager that runs a game called "Assassin" by storing and printing the
 * "kill ring" and the "graveyard" lists, checking if a given name input is in either list, keeping 
 * track of the game status, updating the lists accordingly after each "kill", and printing the winner 
 * after the game ends.*/
import java.util.*;
public class AssassinManager {

	//reference to the front node of the kill ring (null if empty)
	AssassinNode frontKill = null;
	//a reference to the front node of the graveyard (null if empty)
	AssassinNode frontDed = null;
	
	//Pre-condition: given list is a list of strings (if empty, throw IllegalArgumentException)
	//Post-condition: creates list of nodes from given list
	//constructor: sets up kill ring
	public AssassinManager(List<String> names) {
		if(names.isEmpty()) {
			throw new IllegalArgumentException();
		}
		else {
			frontKill = new AssassinNode(names.get(0));
			for(int i = 1; i < names.size(); i++) {
				AssassinNode temp = frontKill;
				while(temp.next != null) {
					temp = temp.next;
				}
				temp.next = new AssassinNode(names.get(i));
			}
		}
	}
	
	//Post-condition: prints out list of "who is stalking who"
	//prints the current kill ring
	public void printKillRing() {
		AssassinNode temp = frontKill;
		String first = frontKill.name; //first person (for later)
		while(temp.next != null) {
			System.out.println(temp.name + " is stalking " + temp.next.name);
			temp = temp.next;
		}
		System.out.println(temp.name + " is stalking " + first); //complete the kill ring
	}
	
	//Post-condition: print out list of "who was killed by who"
	//prints the current graveyard
	public void printGraveyard() {
		if(frontDed != null) {
			AssassinNode temp = frontDed;
			while(temp != null) {
				System.out.println(temp.name + " was killed by " + temp.killer);
				temp = temp.next;
			}
		}
	}
	
	//Pre-condition: given name is in the frontKill list of nodes
	//Post-condition: return true (else return false)
	//checks if given name is in current kill ring
	public boolean killRingContains(String name) {
		return checkNode(frontKill, name);
	}
	//behavior, parameters, return, pre/post-conditions, and exceptions
	//Pre-condition: given name is in the frontDed list of nodes (or if frontDed is empty, return false)
	//Post-condition: return true (else return false)
	//checks if given name is in current graveyard
	public boolean graveyardContains(String name) {
		if(frontDed == null) {
			return false;
		}
		else {
			return checkNode(frontDed, name);
		}
	}

	//Pre-condition: string name is in the given node list
	//Post-condition: return true (else return false)
	//for name check methods
	private boolean checkNode(AssassinNode front, String name) {
		AssassinNode temp = front;
		while(temp != null) {
			if(name.toLowerCase().equals(temp.name.toLowerCase())) {
				return true;
			}
			temp = temp.next;
		}
		return false;
	}
	
	//Pre-condition: kill ring has one node left
	//Post-condition: return true (if not, return false)
	//checks if one person remains in kill ring (i.e. game over)
	public boolean gameOver() {
		if(frontKill.next == null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//Pre-condition: kill ring has one node left
	//Post-condition: return name of node left in kill ring
	//returns remaining person in kill ring (i.e. the winner)
	public String winner() {
		if(gameOver()) {
			return frontKill.name;
		}
		else {
			return null;
		}
	}
	
	//Pre-condition: game is not over and given name is in the kill ring 
	//(if not, throw IllegalArgumentException)
	//Post-condition: move node with name into graveyard nodes and record previous node as killer
	//transfers given victim from kill ring to graveyard and records name of killer (i.e. the killing)
	public void kill(String name) {
		//checks if game is over and if given name is not in kill ring
		if(gameOver() || !killRingContains(name)) {
			throw new IllegalArgumentException();
		}
		else {
			//find node
			AssassinNode temp = frontKill;
			while(temp.next != null) {
				if(name.toLowerCase().equals(temp.next.name.toLowerCase())) {
					//special transfer method
					recordDeath(temp.next, temp, temp.next.next);
					break;
					
				}
				temp = temp.next;
			}
			//special case: if first person in list is ded
			if(frontKill.name.toLowerCase().equals(name.toLowerCase())) {
				AssassinNode temp2 = frontKill.next;
				recordDeath(frontKill, temp, null);
				frontKill = temp2; //assign front of kill ring to next person
			}
		}
	}
	
	//Post-condition: move ded node to frontDed node list, assign killer node to next node,
	//and record killer name into ded node
	//for kill method
	private void recordDeath(AssassinNode ded, AssassinNode killer, AssassinNode next) {
		//assign killer to next person in kill ring
		killer.next = next;
		//"move" node to the front of frontDed
		ded.next = frontDed;
		frontDed = ded;
		//record name of previous node as the killer
		frontDed.killer = killer.name;
	}
}
