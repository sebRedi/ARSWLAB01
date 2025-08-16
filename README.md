# Laboratorio 1 - ARSW
### Autor: Sebastian Galvis Briceño
## Introducción:
Este ejercicio contiene una introducción a la programación con hilos en Java, además de la aplicación a un caso concreto.

## Parte I - Introducción a Hilos en Java
1. De acuerdo con lo revisado en las lecturas, complete las clases CountThread, para que las mismas definan el ciclo de vida de un hilo que imprima por pantalla los números entre A y B.

   **Desarrollo:** Para crear un hilo que imprima por pantalla dos números recibidos entre A y B, lo hacemos de la forma:

   ```java
   public class CountThread implements Runnable{
    private int startNumber;
    private int endNumber;

    public CountThread(int startNumber, int endNumber) {
        this.startNumber = startNumber;
        this.endNumber = endNumber;
    }

    @Override
    public void run() {
        for(int i = startNumber; i <= endNumber; i++){
            System.out.println(i);
        }
    }
   }
   ```



2. Complete el método main de la clase CountMainThreads para que:
   - Cree 3 hilos de tipo CountThread, asignándole al primero el intervalo [0..99], al segundo [99..199], y al tercero [200..299].
   - Inicie los tres hilos con 'start()'.
   - Ejecute y revise la salida por pantalla.
   - Cambie el incio con 'start()' por 'run()'. Cómo cambia la salida?, por qué?.
   
   **Desarrollo:** Empezamos por intentar efectuar el procedimiento como se indica al inicio:
   ```java
   public class CountThreadsMain {

      public static void main(String a[]){
         CountThread t1 = new CountThread(0, 99);
         CountThread t2 = new CountThread(99, 199);
         CountThread t3 = new CountThread(200, 299);
         Thread thread1 = new Thread(t1);
         Thread thread2 = new Thread(t2);
         Thread thread3 = new Thread(t3);
         thread1.start();
         thread2.start();
         thread3.start();
      }

   }
   ```
   Al desarrollarlo de esta forma, vemos que los números se generaron con prioridades aleatorias en consola, quedando de esta forma, los números del 0 al 299 en desorden.
   
   Ahora, cambiaremos el método start() por run(), dejando el método main de la forma:
   ```java
   public static void main(String a[]){
        CountThread t1 = new CountThread(0, 99);
        CountThread t2 = new CountThread(99, 199);
        CountThread t3 = new CountThread(200, 299);
        Thread thread1 = new Thread(t1);
        Thread thread2 = new Thread(t2);
        Thread thread3 = new Thread(t3);
        thread1.run();
        thread2.run();
        thread3.run();
    }
   ```
   Y al ejecutarlo, podemos apreciar que, ahora sí, los números del 0 al 299 se imprimen en orden.
   
   Es posible **concluir** de esta práctica, que al utilizar el método *run()* cada hilo espera a que el anterior termine su trabajo para empezar el suyo propio. A deferencia de utilizar el método *start()*, donde cada hilo parece preocuparse por ejecutar su tarea sin importarle si hay otro proceso trabajando en el momento.

## Parte II - Ejercicio Black List Search
