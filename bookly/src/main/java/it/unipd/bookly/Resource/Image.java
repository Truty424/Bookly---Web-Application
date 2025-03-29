package it.unipd.bookly.Resource;

public class Image {

    private  byte[] photo;
    private  String photoMediaType;

    public Image(byte[] photo, String photoMediaType) {
        this.photo = photo;
        this.photoMediaType = photoMediaType;
    }

    public final byte[] getPhoto() {
        return photo;
    }

    public final String getPhotoMediaType() {
        return photoMediaType;
    }

}
