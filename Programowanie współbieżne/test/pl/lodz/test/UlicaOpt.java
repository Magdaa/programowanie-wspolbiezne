package pl.lodz.test;

import java.util.Optional;

public class UlicaOpt {
	private Optional<String> nazwaOpt = Optional.of("Pi�sudskiego");

	public Optional<String> getNazwaOpt() {
		return nazwaOpt;
	}

	public void setNazwaOpt(Optional<String> nazwa) {
		this.nazwaOpt = nazwa;
	}
}
