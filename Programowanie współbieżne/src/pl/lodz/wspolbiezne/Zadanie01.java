package pl.lodz.wspolbiezne;

public class Zadanie01 {

	public static void main(String args[]) {
        new Thread(new W�tek01()).start();
        new Thread(new W�tek02()).start();
    }
}
