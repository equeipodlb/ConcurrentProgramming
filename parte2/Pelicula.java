package parte2;

import java.io.Serializable;
import java.util.List;

public class Pelicula implements Serializable {
    // Atributos de la clase Pelicula
    private String titulo;
    private String duracion;
    private double puntuacion;
    private List<String> actores;
    private String fechaLanzamiento;
    private String sinopsis;

    // Constructor de la clase Pelicula
    public Pelicula(String titulo, String duracion, double puntuacion, List<String> actores, String fechaLanzamiento,
            String sinopsis) {
        this.titulo = titulo;
        this.duracion = duracion;
        this.puntuacion = puntuacion;
        this.actores = actores;
        this.fechaLanzamiento = fechaLanzamiento;
        this.sinopsis = sinopsis;
    }

    @Override
    public String toString() {
        return "Título: " + titulo + "\n" +
                "Duración: " + duracion + " minutos\n" +
                "Puntuación: " + puntuacion + "/10\n" +
                "Actores principales: " + actores.toString() + "\n" +
                "Fecha de lanzamiento: " + fechaLanzamiento + "\n" +
                "Sinopsis: " + sinopsis + "\n";
    }

    // Métodos de acceso a los atributos de la clase Pelicula
    public String getTitulo() {
        return titulo;
    }

    public String getDuracion() {
        return duracion;
    }

    public double getPuntuacion() {
        return puntuacion;
    }

    public List<String> getActores() {
        return actores;
    }

    public String getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public String getSinopsis() {
        return sinopsis;
    }
}
