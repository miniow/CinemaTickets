/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package michal.com.domain.model;


import infrastructure.ImageConverter;
import java.awt.Image;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 *
 * @author zymci
 */

@Entity
@Table(name = "rsi_Movies")
public class Movie {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String director;

    @ElementCollection
    private List<String> actors;

    @Column(nullable = false)
    private String description;

    @Column(name = "thumbnail")
    @Lob
    @Convert(converter = ImageConverter.class)
    private Image thumbnail;

    protected Movie() {}

    private Movie(String title, String director, List<String> actors, String description, Image thumbnail) {
        if (title == null || title.isEmpty()) throw new IllegalArgumentException("Title cannot be null or empty");
        if (director == null || director.isEmpty()) throw new IllegalArgumentException("Director cannot be null or empty");
        if (actors == null) throw new IllegalArgumentException("Actors list cannot be null");
        if (description == null) throw new IllegalArgumentException("Description cannot be null");

        this.title = title;
        this.director = director;
        this.actors = Collections.unmodifiableList(actors);
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public static Movie createMovie(String title, String director, List<String> actors, String description, Image thumbnail) {
        return new Movie(title, director, actors, description, thumbnail);
    }

    public Long getId() {
        return id;
    }
    public List<String> getActors(){
        return actors;
    }
    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public String getDescription() {
        return description;
    }

    public Image getImage() {
        return thumbnail;
    }
}
