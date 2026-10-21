package com.casino.dados.service;

import com.casino.dados.dto.IniciarPartidaRequest;
import com.casino.dados.exception.JugadorInactivoException;
import com.casino.dados.exception.PartidaEstadoInvalidaException;
import com.casino.dados.exception.RecursoNoEncontradoException;
import com.casino.dados.exception.SaldoInsuficienteException;
import com.casino.dados.model.EstadoPartida;
import com.casino.dados.model.Jugador;
import com.casino.dados.model.Partida;
import com.casino.dados.repository.JugadorRepository;
import com.casino.dados.repository.PartidaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final JugadorRepository jugadorRepository;
    private final JugadorService jugadorService;

    @Transactional
    public Partida iniciarPartida(IniciarPartidaRequest request) {
        Jugador jugador = jugadorService.obtener(request.jugadorId());

        if (!jugador.isActivo()) {
            throw new JugadorInactivoException("el jugador no esta activo: " + jugador.getNombre());
        }
        if (jugador.getSaldo().compareTo(request.apuesta()) < 0) {
            throw new SaldoInsuficienteException("saldo insuficiente: " + jugador.getSaldo() + ", apuesta: " + request.apuesta());
        }

        jugador.setSaldo(jugador.getSaldo().subtract(request.apuesta()));
        jugadorRepository.save(jugador);

        Partida partida = new Partida();
        partida.setFecha(LocalDateTime.now());
        partida.setEstado(EstadoPartida.JUEGO);
        partida.setJugador(jugador);
        partida.setApuesta(request.apuesta());
        return partidaRepository.save(partida);
    }

    @Transactional(readOnly = true)
    public Partida obtener(Long id) {
        return partidaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("La partida con id " + id + " no existe"));
    }

    @Transactional
    public Partida finalizarPartidaManual(Long id) {
        Partida partida = obtener(id);
        if (partida.getEstado() == EstadoPartida.FINALIZADA) {
            throw new PartidaEstadoInvalidaException("la partida ya finalizo " + id );
        }
        partida.setEstado(EstadoPartida.FINALIZADA);
        return partidaRepository.save(partida);
    }

    @Transactional(readOnly = true)
    public List<Partida> obtenerHistorialPartida(Long jugadorId) {
        jugadorService.obtener(jugadorId); // valida que el jugador exista
        return partidaRepository.findByJugadorIdOrderByFechaDesc(jugadorId);
    }
}
