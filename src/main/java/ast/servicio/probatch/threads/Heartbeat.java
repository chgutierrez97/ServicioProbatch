package ast.servicio.probatch.threads;

import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ast.servicio.probatch.service.ServicioAgente;

public class Heartbeat extends Thread {

	public static Logger logger = LoggerFactory.getLogger(Heartbeat.class);

	private OutputStream output;
	private long heartbeat;
	private boolean terminarEsteThread;

	public Heartbeat(long heartbeat, OutputStream output) {
		super("HeartBeat Thread");
		this.heartbeat = heartbeat;
		this.output = output;
	}

	@Override
	public void run() {
		try {
			while (!ServicioAgente.terminarThreads && !terminarEsteThread) {
				try {
					logger.debug("CLI <-- <latido/>");
					output.write("<latido/>".getBytes());
					sleep(heartbeat);
				} catch (IOException e) {
					System.err.print("No se pudo escribir en la salida " + e.getMessage());
					// Logs
					logger.error("No se pudo escribir en la salida " + e.getMessage());
					logger.trace(e.getMessage());
					ServicioAgente.connectionStatus = false;
				}
			}
		} catch (InterruptedException e) {
			terminarEsteThread = true;
		}
	}

}
