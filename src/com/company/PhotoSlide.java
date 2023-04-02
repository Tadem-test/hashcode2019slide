package hashcode2019;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;

public abstract class PhotoSlide {
	
	
	
	 static ArrayList<Integer> getPairedPhotos(int i, Photo[] photos) {
			ArrayList<Integer> pairedPhotos = new ArrayList<>();
			for (int j = i + 1; j < photos.length; j++) {
				if (photos[j].orientation == 'V') {
					pairedPhotos.add(i);
					pairedPhotos.add(j);
					break;
				}
			}
			if (pairedPhotos.size() < 2) {
				for (int j = 0; j < photos.length; j++) {
					if (j != i && photos[j].orientation == 'V') {
						pairedPhotos.add(i);
						pairedPhotos.add(j);
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
		
		 static void ausgabeInTxtDatei(ArrayList<Slide> orderedSlides) {
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
}
