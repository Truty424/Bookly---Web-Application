package it.unipd.bookly.Resource;

/**
 * Represents an image resource, including the photo and its media type.
 */
public class Image {

    private final byte[] photo;
    private final  String photoMediaType;

    /**
     * Constructs an Image with the specified photo data and media type.
     *
     * @param photo          The byte array representing the photo.
     * @param photoMediaType The media type of the photo.
     */
    public Image(byte[] photo, String photoMediaType) {
        this.photo = photo;
        this.photoMediaType = photoMediaType;
    }

    /**
     * Gets the photo data.
     *
     * @return A byte array representing the photo.
     */
    public final byte[] getPhoto() {
        return photo;
    }

    /**
     * Gets the media type of the photo.
     *
     * @return The media type of the photo (e.g., "image/jpeg").
     */
    public final String getPhotoMediaType() {
        return photoMediaType;
    }

}
