package hashcode2019;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Main extends PhotoSlide {
	static int anzahlBilder;
	static int endScore = 0;
	static Photo[] photos;
	static ArrayList<Slide> orderedSlides = new ArrayList<>();

	public static void main(String[] args) {
		load();

		ArrayList<Slide> slides = new ArrayList<>();
		boolean[] used = new boolean[anzahlBilder];

		for (int i = 0; i < anzahlBilder; i++) {
			if (!used[i]) {
				if (photos[i].orientation == 'H') {
					slides.add(new Slide(new int[] { i }, new HashSet<>(Arrays.asList(photos[i].tags))));
					used[i] = true;
				} else {
					ArrayList<Integer> pairedPhotos = getPairedPhotos(i, photos);
					slides.add(new Slide(pairedPhotos.stream().mapToInt(Integer::intValue).toArray(),
							getTags(pairedPhotos, photos)));
					for (int pairedPhoto : pairedPhotos) {
						used[pairedPhoto] = true;
					}
				}
			}
		}

		orderedSlides.add(slides.get(0));
		slides.remove(0);
		while (!slides.isEmpty()) {
			Slide lastSlide = orderedSlides.get(orderedSlides.size() - 1);
			int bestSlideIndex = -1;
			int bestScore = -1;
			for (int i = 0; i < slides.size(); i++) {
				int score = score(lastSlide, slides.get(i));
				if (score > bestScore) {
					bestScore = score;
					bestSlideIndex = i;
				}
			}
			endScore += bestScore;
			orderedSlides.add(slides.get(bestSlideIndex));
			slides.remove(bestSlideIndex);
		}
		System.out.println("Score = " + endScore);
		ausgabeInTxtDatei(orderedSlides);
	}

	public static void load() {
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader("a_example.txt"));
			int line_no = 0;
			while (br.ready()) {
				// Get details
				if (line_no == 0) {
					String line = br.readLine();
					anzahlBilder = Integer.parseInt(line);
					photos = new Photo[anzahlBilder];
				} else {
					for (int j = 0; j < anzahlBilder; j++) {
						String line = br.readLine();
						String[] sp = line.split(" ");
						String[] tags = new String[Integer.parseInt(sp[1])];
						for (int p = 2; p < sp.length; p++) {
							tags[p - 2] = sp[p];
						}
						photos[j] = new Photo(sp[0].charAt(0), j, Integer.parseInt(sp[1]), tags);
					}
				}
				line_no++;
			}
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception ex) {
					System.err.println(ex.getMessage());
				}
			}
		}
	}
}