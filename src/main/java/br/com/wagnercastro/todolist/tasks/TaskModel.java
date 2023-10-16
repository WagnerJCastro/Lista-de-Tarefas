package br.com.wagnercastro.todolist.tasks;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;
    /*
    ID
    usuario (ID_USUARIO)
    descricao
    titulo
    Data de inicio
    Data de termino
    Prioridade
     */

@Data
@Entity(name = "tb_tasks")
public class TaskModel {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private String descricao;
    @Column(length = 50)
    private String titulo;
    private LocalDateTime dataInicio;
    private LocalDateTime dataTermino;
    private String prioridade;
    @CreationTimestamp
    private LocalDateTime dataCriacao;
    private UUID idUser;

    public void setTitulo(String titulo) throws  Exception{
        if(titulo.length() >50){
            throw new Exception("O campo titulo pode conter no maximo 50 caracteres");
        }
        this.titulo = titulo;
    }



}
