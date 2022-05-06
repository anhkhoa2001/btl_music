package uet.myboot.modules.genre.models;

import uet.myboot.modules.music.models.Music;
import uet.myboot.parent.main.Main;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "musicgenre")
public class MusicGenre {

    @Id
    @Basic
    private int id;

    @Basic
    private String name;

    @ManyToMany(mappedBy = "musicGenres", cascade = CascadeType.ALL)
    List<Music> musics;

    @Basic
    private String source;

    @Basic
    private String slogan;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return Main.URL_IP + source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    @Override
    public String toString() {
        return "MusicGenre{" +
                "name='" + name + '\'' +
                ", musics=" + musics +
                ", source='" + source + '\'' +
                '}';
    }
}