package util.tsp;

import java.util.Collection;
import java.util.Iterator;

public class IteratorMinFirst implements Iterator<Integer> {

	private Integer[] candidats;
	private int nbCandidats;

	public IteratorMinFirst(Collection<Integer> nonVus, int sommetCrt, long cout[][]) {
		// tri des sommets par couts croissant
		Iterator<Integer> nonVisite = nonVus.iterator();
		Integer temp;
		this.candidats = new Integer[nonVus.size()];
		nbCandidats = 0; // le nombre de sommet qu'on a deja ajoute
		candidats[0] = nonVisite.next().intValue();

		while (nonVisite.hasNext()) {
			int crt = nonVisite.next().intValue();
			for (int i = 0; i < nbCandidats + 1; i++) {

				if (cout[sommetCrt][crt] > cout[sommetCrt][candidats[i]]) {

					temp = crt;
					crt = candidats[i];
					candidats[i] = temp;

				}

			}
			candidats[++nbCandidats] = crt;
		}
		nbCandidats++;
	}

	@Override
	public boolean hasNext() {
		return nbCandidats > 0;
	}

	@Override
	public Integer next() {
		return candidats[--nbCandidats];
	}

}
