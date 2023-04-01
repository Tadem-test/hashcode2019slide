package hashcode2019;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

class Photo {
	char orientation;
	int id, numTags;
	String[] tags;

	public Photo(char orientation, int id, int numTags, String[] tags) {
		this.orientation = orientation;
		this.id = id;
		this.numTags = numTags;
		this.tags = tags;
	}
}

class Slide {
	int[] photos;
	HashSet<String> tags;

	public Slide(int[] photos, HashSet<String> tags) {
		this.photos = photos;
		this.tags = tags;
	}
}

public class Main {
	static int anzahlBilder;
	static int endScore = 0;
	static Photo[] photos;
	static ArrayList<Slide> orderedSlides = new ArrayList<>();

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
	
	 private static void printConventionally() {
	        PrintWriter pWriter = null;
	        String s = "a_output.txt";
	        try {
	            pWriter = new PrintWriter(new FileWriter(s));
	            pWriter.println(orderedSlides.size());
	    		for (Slide slide : orderedSlides) {
	    			if (slide.photos.length == 1) {
	    				pWriter.println(slide.photos[0]);
	    			} else {
	    				pWriter.println(slide.photos[0] + " " + slide.photos[1]);
	    			}
	    		}
	        } catch (IOException ioe) {
	            ioe.printStackTrace();
	        } finally {
	            if (pWriter != null) {
	                pWriter.flush();
	                pWriter.close();
	            }
	        }
	    }

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
			System.out.println("Score = " + endScore);
			orderedSlides.add(slides.get(bestSlideIndex));
			slides.remove(bestSlideIndex);
		}
		printConventionally();
	}

	private static ArrayList<Integer> getPairedPhotos(int i, Photo[] photos) {
		ArrayList<Integer> pairedPhotos = new ArrayList<>();
		for (int j = i + 1; j < photos.length; j++) {
			if (photos[j].orientation == 'V') {
				pairedPhotos.add(j);
				pairedPhotos.add(i);
				break;
			}
		}
		if (pairedPhotos.size() < 2) {
			for (int j = 0; j < photos.length; j++) {
				if (j != i && photos[j].orientation == 'V') {
					pairedPhotos.add(j);
					pairedPhotos.add(i);
					break;
				}
			}
		}
		return pairedPhotos;
	}

	public static int score(Slide s1, Slide s2) {
		HashSet<String> union = new HashSet<>(s1.tags);
		union.addAll(s2.tags);
		int numCommonTags = s1.tags.size() + s2.tags.size() - union.size();
		int numDistinctTagsS1 = s1.tags.size() - numCommonTags;
		int numDistinctTagsS2 = s2.tags.size() - numCommonTags;
		return Math.min(numCommonTags, Math.min(numDistinctTagsS1, numDistinctTagsS2));
	}

	public static HashSet<String> getTags(ArrayList<Integer> photoIds, Photo[] photos) {
		HashSet<String> tags = new HashSet<>();
		for (int id : photoIds) {
			for (String tag : photos[id].tags) {
				tags.add(tag);
			}
		}
		return tags;
	}

}