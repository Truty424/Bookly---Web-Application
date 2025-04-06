package it.unipd.bookly.Resource;

public class Author {
    private int author_id;
    private String first_name;
    private String last_name;
    private String biography;
    private String nationality;

    public Author(int author_id, String first_name, String last_name, String biography, String nationality) {
        this.author_id = author_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.biography = biography;
        this.nationality = nationality;
    }

    public Author(String first_name, String last_name, String biography, String nationality) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.biography = biography;
        this.nationality = nationality;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getName() {
        return first_name+ " " + last_name;
    }
}
