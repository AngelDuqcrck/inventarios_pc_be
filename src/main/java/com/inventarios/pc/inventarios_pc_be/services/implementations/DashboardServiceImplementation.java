package com.inventarios.pc.inventarios_pc_be.services.implementations;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.repositories.ComputadorRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.DispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IDashboardService;
import com.inventarios.pc.inventarios_pc_be.shared.responses.DashboardResponse;

@Service
public class DashboardServiceImplementation implements IDashboardService{

    @Autowired
    private DispositivoRepository dispositivoRepository;

    @Autowired
    private TipoDispositivoRepository tipoDispositivoRepository;

    @Autowired
    private ComputadorRepository computadorRepository;

    @Override
    public DashboardResponse listDashboard(){
        DashboardResponse dashboardResponse = new DashboardResponse();
        dashboardResponse.setNombre1("PC");
        dashboardResponse.setNombre2("Mouse");
        dashboardResponse.setNombre3("Teclado");
        dashboardResponse.setNombre4("Monitor");
        dashboardResponse.setNombre5("Estabilizador");
        dashboardResponse.setNombre6("Parlantes");

        dashboardResponse.setCantidad1(computadorRepository.findAll().size()); //EQUIPOS PC LISTAR
        dashboardResponse.setCantidad2(dispositivoRepository.findByTipoDispositivo(tipoDispositivoRepository.findById(2).orElse(null)).size());
        dashboardResponse.setCantidad3(dispositivoRepository.findByTipoDispositivo(tipoDispositivoRepository.findById(1).orElse(null)).size());
        dashboardResponse.setCantidad4(dispositivoRepository.findByTipoDispositivo(tipoDispositivoRepository.findById(3).orElse(null)).size());
        dashboardResponse.setCantidad5(dispositivoRepository.findByTipoDispositivo(tipoDispositivoRepository.findById(6).orElse(null)).size());
        dashboardResponse.setCantidad6(dispositivoRepository.findByTipoDispositivo(tipoDispositivoRepository.findById(7).orElse(null)).size());

        return dashboardResponse;
    }
}
