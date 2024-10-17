package com.inventarios.pc.inventarios_pc_be.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventarios.pc.inventarios_pc_be.exceptions.ComponentNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.LocationNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.MarcaNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.MiscellaneousNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.StateNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypePcNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UserNotFoundException;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IComputadorService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.ComputadorDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HttpResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/computador")
public class ComputadorController {
    
    @Autowired
    private IComputadorService computadorService;

    @PostMapping("/crear")
    public ResponseEntity<HttpResponse> crearComputador(@RequestBody ComputadorDTO computadorDTO) throws TypePcNotFoundException, SelectNotAllowedException, UserNotFoundException,
            LocationNotFoundException, ComponentNotFoundException, MiscellaneousNotFoundException,
            StateNotFoundException, MarcaNotFoundException  {

                
        computadorService.crearComputador(computadorDTO);

         return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Equipo PC creado exitosamente"),
                                HttpStatus.OK);
    }
}
