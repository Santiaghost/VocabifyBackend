package com.learningapp.learningapp.service;


import com.learningapp.learningapp.model.Apartado;
import com.learningapp.learningapp.repository.ApartadoRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApartadoService {

    @Autowired
    private ApartadoRepository apartadoRepository;


    public Apartado nuevoApartado(Apartado apartado){
        return apartadoRepository.save(apartado);
    }

    public Apartado actualizarApartado(Apartado apartado){
        Apartado buscar = apartadoRepository.findById(apartado.getId()).orElse(null);
        if(buscar != null){
            return apartadoRepository.save(apartado);
        }
        else{
            return null;
        }
    }

    public boolean borrarApartado(long id){
        try{
            Apartado borrar = devolverApartado(id);
            if(borrar != null){
                apartadoRepository.delete(borrar);
                return true;
            }
        }
        finally {
            return false;
        }
    }
    public void subirImagen(String nombreArchivo,long idApartado){
        Apartado a = devolverApartado(idApartado);
        if(a != null){
            Apartado cambiar = a;
            a.setImagenAdicional(nombreArchivo);
            apartadoRepository.save(a);
        }
    }

    public Apartado devolverApartado(long id){
        return apartadoRepository.findById(id).orElse(null);
    }


}
