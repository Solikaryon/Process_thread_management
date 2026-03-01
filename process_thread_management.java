import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/* Cambiar el nombre del archivo a Practica3MonjarazLuis, 
   pues a la hora de subirlo le puse 227458_PRACTICA 3_MONJARAZ_LUIS 
   para cumplir con los requisitos de entrega :)*/

public class Practica3MonjarazLuis {

    // ===== Clase Proceso =====
    public static class Proceso extends Thread {
        private int UsoCPU;
        private int UsoMemoria;
        private int Prioridad;

        // Variables estáticas compartidas para Ejercicio 2 y 3
        private static int memoriaDisponible = 2000; // Memoria total disponible (2000 MB)
        private static int usoTotalCPU = 0;          // Acumulador del uso total de CPU
        private static int memoriaMaxUsada = 0;      // Memoria máxima usada en la ejecución
        private static int procesosEjecutados = 0;   // Cantidad de procesos ejecutados

        // Constructor que inicializa cada proceso con sus parámetros
        public Proceso(String Nombre, int UsoCPU, int UsoMemoria, int Prioridad) {
            super(Nombre); // Nombre del proceso (heredado de Thread)
            this.UsoCPU = UsoCPU;
            this.UsoMemoria = UsoMemoria;
            this.Prioridad = Prioridad;
            this.setPriority(Prioridad); // Se asigna la prioridad del hilo
        }

        // Getters para obtener los valores de los procesos
        public int getPrioridad() { return Prioridad; }
        public int getUsoMemoria() { return UsoMemoria; }
        public int getUsoCPU() { return UsoCPU; }

        // ===== Métodos sincronizados para memoria y estadísticas =====

        // Solicita memoria de forma segura usando synchronized
        public static synchronized boolean solicitarMemoria(int cantidad) {
            if (cantidad <= memoriaDisponible) {
                memoriaDisponible -= cantidad;
                memoriaMaxUsada = Math.max(memoriaMaxUsada, 2000 - memoriaDisponible);
                return true;
            }
            return false; // Si no hay suficiente memoria, el proceso debe esperar
        }

        // Libera memoria cuando un proceso termina
        public static synchronized void liberarMemoria(int cantidad) {
            memoriaDisponible += cantidad;
        }

        // Actualiza estadísticas globales (CPU, procesos y memoria máxima)
        public static synchronized void actualizarEstadisticas(int cpu, int memoria) {
            usoTotalCPU += cpu;
            procesosEjecutados++;
            memoriaMaxUsada = Math.max(memoriaMaxUsada, 2000 - memoriaDisponible);
        }

        // Reinicia las estadísticas (se usa al inicio de cada ejercicio)
        public static void resetEstadisticas() {
            memoriaDisponible = 2000;
            usoTotalCPU = 0;
            memoriaMaxUsada = 0;
            procesosEjecutados = 0;
        }

        // Muestra las estadísticas del sistema al finalizar
        public static void mostrarEstadisticas() {
            System.out.println("===== ESTADÍSTICAS DEL SISTEMA =====");
            System.out.println("Uso total de CPU: " + usoTotalCPU + "%");
            System.out.println("Memoria máxima usada: " + memoriaMaxUsada + " MB");
            System.out.println("Número de procesos ejecutados: " + procesosEjecutados);
            System.out.println("====================================");
        }

        @Override
        public void run() {
            try {
                // Simulación de ejecución del proceso con 5 ciclos
                for (int i = 0; i < 5; i++) {
                    System.out.println("Proceso: " + getName() +
                            " - Uso CPU: " + UsoCPU +
                            " - Uso Memoria: " + UsoMemoria +
                            " - Prioridad: " + Prioridad);
                    Thread.sleep(500); // Espera de 0.5 segundos entre ciclos
                }
                // Al terminar el ciclo, el proceso actualiza estadísticas y libera memoria
                System.out.println("Proceso: " + getName() + " ha terminado.");
                actualizarEstadisticas(UsoCPU, UsoMemoria);
                liberarMemoria(UsoMemoria);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // ===== Lista de procesos fijos =====
    // Puedes alternar entre los conjuntos comentando/descomentando según la prueba
    
    /* Conjunto 1 */
    /*
    public static ArrayList<Proceso> crearProcesosFijos() {
        ArrayList<Proceso> procesos = new ArrayList<>();
        procesos.add(new Proceso("Explorer", 10, 150, 3));
        procesos.add(new Proceso("Chrome", 30, 800, 2));
        procesos.add(new Proceso("Spotify", 15, 400, 4));
        procesos.add(new Proceso("CMD", 5, 50, 5));
        procesos.add(new Proceso("Java", 40, 1200, 1));
        procesos.add(new Proceso("Notepad", 5, 100, 5));
        return procesos;
    }
    */

    /* Conjunto 2 */
    /*
    public static ArrayList<Proceso> crearProcesosFijos() {
        ArrayList<Proceso> procesos = new ArrayList<>();
        procesos.add(new Proceso("Explorer", 15, 200, 4));
        procesos.add(new Proceso("Chrome", 45, 1200, 2));
        procesos.add(new Proceso("Spotify", 20, 600, 5));
        procesos.add(new Proceso("CMD", 8, 100, 6));
        procesos.add(new Proceso("Java", 60, 1500, 1));
        procesos.add(new Proceso("Notepad", 10, 150, 6));
        return procesos;
    }
    */

    /* Conjunto 3 */
    public static ArrayList<Proceso> crearProcesosFijos() {
        ArrayList<Proceso> procesos = new ArrayList<>();
        procesos.add(new Proceso("Explorer", 25, 100, 1));
        procesos.add(new Proceso("Chrome", 70, 900, 3));
        procesos.add(new Proceso("Spotify", 30, 300, 2));
        procesos.add(new Proceso("CMD", 15, 75, 6));
        procesos.add(new Proceso("Java", 80, 1100, 4));
        procesos.add(new Proceso("Notepad", 20, 120, 5));
        return procesos;
    }

    // === EJERCICIO 1: Planificación por prioridad ===
    public static void ejercicio1() {
        System.out.println("\n=== EJERCICIO 1: Planificación por Prioridad ===");
        ArrayList<Proceso> procesos = crearProcesosFijos();

        // Ordenar procesos por prioridad (menor número = mayor prioridad)
        Collections.sort(procesos, Comparator.comparingInt(Proceso::getPrioridad));

        // Iniciar procesos en orden ya definido por prioridad
        for (Proceso p : procesos) {
            p.start();
        }

        // Esperar a que terminen todos los procesos
        for (Proceso p : procesos) {
            try {
                p.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("El proceso principal ha terminado.");
    }

    // === EJERCICIO 2: Control de recursos (memoria) ===
    public static void ejercicio2() {
        System.out.println("\n=== EJERCICIO 2: Control de Recursos ===");
        ArrayList<Proceso> procesos = crearProcesosFijos();
        Proceso.resetEstadisticas();

        for (Proceso p : procesos) {
            // Esperar hasta que haya memoria suficiente para ejecutar
            while (!Proceso.solicitarMemoria(p.getUsoMemoria())) {
                try {
                    Thread.sleep(500); // Espera antes de volver a intentar
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            p.start(); // Inicia el proceso una vez que obtuvo memoria
        }

        // Esperar a que todos los procesos terminen
        for (Proceso p : procesos) {
            try {
                p.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Mostrar estadísticas finales
        Proceso.mostrarEstadisticas();
        System.out.println("El proceso principal ha terminado.");
    }

    // === EJERCICIO 3: Estadísticas del sistema ===
    public static void ejercicio3() {
        System.out.println("\n=== EJERCICIO 3: Estadísticas del Sistema ===");
        Proceso.resetEstadisticas();
        ArrayList<Proceso> procesos = crearProcesosFijos();

        // Ejecutar todos los procesos en paralelo
        for (Proceso p : procesos) {
            p.start();
        }

        // Esperar a que todos finalicen
        for (Proceso p : procesos) {
            try {
                p.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Mostrar estadísticas recolectadas
        Proceso.mostrarEstadisticas();
        System.out.println("El proceso principal ha terminado.");
    }

    // ===== MENÚ PRINCIPAL =====
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion;
        do {
            // Menú de opciones
            System.out.println("\n===== MENÚ PRINCIPAL =====");
            System.out.println("1. Ejercicio 1: Planificación por prioridad");
            System.out.println("2. Ejercicio 2: Control de recursos");
            System.out.println("3. Ejercicio 3: Estadísticas del sistema");
            System.out.println("4. Salir");
            System.out.print("Elige una opción: ");
            opcion = sc.nextInt();

            // Selección de opción
            switch (opcion) {
                case 1: ejercicio1(); break;
                case 2: ejercicio2(); break;
                case 3: ejercicio3(); break;
                case 4: System.out.println("Saliendo..."); break;
                default: System.out.println("Opción inválida");
            }
        } while (opcion != 4); // Repite hasta que el usuario elija salir
        sc.close(); // Cierra el escáner
    }
}
