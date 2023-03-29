package com.company;

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
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Photo[] photos = new Photo[n];
        for (int i = 0; i < n; i++) {
            char orientation = scanner.next().charAt(0);
            int numTags = scanner.nextInt();
            String[] tags = new String[numTags];
            for (int j = 0; j < numTags; j++) {
                tags[j] = scanner.next();
            }
            photos[i] = new Photo(orientation, i, numTags, tags);
        }
        scanner.close();

        ArrayList<Slide> slides = new ArrayList<>();
        boolean[] used = new boolean[n];

        for (int i = 0; i < n; i++) {
            if (!used[i]) {
                if (photos[i].orientation == 'H') {
                    slides.add(new Slide(new int[]{i}, new HashSet<>(Arrays.asList(photos[i].tags))));
                    used[i] = true;
                } else {
                    ArrayList<Integer> pairedPhotos = getPairedPhotos(i, photos);
                    slides.add(new Slide(pairedPhotos.stream().mapToInt(Integer::intValue).toArray(), getTags(pairedPhotos, photos)));
                    for (int pairedPhoto : pairedPhotos) {
                        used[pairedPhoto] = true;
                    }
                }
            }
        }

        ArrayList<Slide> orderedSlides = new ArrayList<>();
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
            orderedSlides.add(slides.get(bestSlideIndex));
            slides.remove(bestSlideIndex);
        }

        System.out.println(orderedSlides.size());
        for (Slide slide : orderedSlides) {
            if (slide.photos.length == 1) {
                System.out.println(slide.photos[0]);
            } else {
                System.out.println(slide.photos[0] + " " + slide.photos[1]);
            }
        }
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


