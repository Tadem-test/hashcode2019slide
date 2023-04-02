package hashcode2019;

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