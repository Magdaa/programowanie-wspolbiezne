package pl.lodz.wspolbiezne.lab07;

public class Protocol {

	public String processInput(String theInput) {
		String theOutput = null;
		if (theInput.equalsIgnoreCase("Who's there?")) {
			theOutput = "nasłuchiwanie trwa";
		}
		return theOutput;
	}
}
