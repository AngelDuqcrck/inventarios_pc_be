package com.inventarios.pc.inventarios_pc_be.services.implementations;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.repositories.ComputadorRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.DispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.EstadoSolicitudesRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.PropietarioRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.SolicitudRepository;
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

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private EstadoSolicitudesRepository estadoSolicitudesRepository;

    @Autowired
    private PropietarioRepository propietarioRepository;

    @Override
    public DashboardResponse listDashboard(){
        DashboardResponse dashboardResponse = new DashboardResponse();
        dashboardResponse.setNombre1("PC");
        dashboardResponse.setNombre2("Mouse");
        dashboardResponse.setNombre3("Teclado");
        dashboardResponse.setNombre4("Monitor");
        dashboardResponse.setNombre5("Estabilizador");
        dashboardResponse.setNombre6("Parlantes");
        dashboardResponse.setNombre7("Pendientes");
        dashboardResponse.setNombre8("En Proceso");
        dashboardResponse.setNombre9("Completados");

        dashboardResponse.setCantidad1OF(computadorRepository.findByPropietario(propietarioRepository.findById(1).orElse(null)).size()); //EQUIPOS PC LISTAR
        dashboardResponse.setCantidad1CSA(computadorRepository.findByPropietario(propietarioRepository.findById(2).orElse(null)).size());
        dashboardResponse.setCantidad1Total(computadorRepository.findAll().size());

        dashboardResponse.setCantidad2OF(dispositivoRepository.findByTipoDispositivoAndPropietario(tipoDispositivoRepository.findById(2).orElse(null), propietarioRepository.findById(1).orElse(null)).size());
        dashboardResponse.setCantidad2CSA(dispositivoRepository.findByTipoDispositivoAndPropietario(tipoDispositivoRepository.findById(2).orElse(null), propietarioRepository.findById(2).orElse(null)).size());
        dashboardResponse.setCantidad2Total(dispositivoRepository.findByTipoDispositivo(tipoDispositivoRepository.findById(2).orElse(null)).size());


        dashboardResponse.setCantidad3OF(dispositivoRepository.findByTipoDispositivoAndPropietario(tipoDispositivoRepository.findById(1).orElse(null), propietarioRepository.findById(1).orElse(null)).size());
        dashboardResponse.setCantidad3CSA(dispositivoRepository.findByTipoDispositivoAndPropietario(tipoDispositivoRepository.findById(1).orElse(null), propietarioRepository.findById(2).orElse(null)).size());
        dashboardResponse.setCantidad3Total(dispositivoRepository.findByTipoDispositivo(tipoDispositivoRepository.findById(1).orElse(null)).size());

        dashboardResponse.setCantidad4OF(dispositivoRepository.findByTipoDispositivoAndPropietario(tipoDispositivoRepository.findById(3).orElse(null), propietarioRepository.findById(1).orElse(null)).size());
        dashboardResponse.setCantidad4CSA(dispositivoRepository.findByTipoDispositivoAndPropietario(tipoDispositivoRepository.findById(3).orElse(null), propietarioRepository.findById(2).orElse(null)).size());
        dashboardResponse.setCantidad4Total(dispositivoRepository.findByTipoDispositivo(tipoDispositivoRepository.findById(3).orElse(null)).size());
        
        dashboardResponse.setCantidad5(dispositivoRepository.findByTipoDispositivo(tipoDispositivoRepository.findById(6).orElse(null)).size());
        dashboardResponse.setCantidad6(dispositivoRepository.findByTipoDispositivo(tipoDispositivoRepository.findById(7).orElse(null)).size());
        dashboardResponse.setCantidad7(solicitudRepository.findByEstadoSolicitudes(estadoSolicitudesRepository.findById(1).orElse(null)).size());
        dashboardResponse.setCantidad8(solicitudRepository.findByEstadoSolicitudes(estadoSolicitudesRepository.findById(2).orElse(null)).size());
        dashboardResponse.setCantidad9(solicitudRepository.findByEstadoSolicitudes(estadoSolicitudesRepository.findById(3).orElse(null)).size());

        return dashboardResponse;
    }
}
