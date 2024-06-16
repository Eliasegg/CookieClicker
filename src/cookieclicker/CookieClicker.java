package cookieclicker;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Scanner;

public class CookieClicker {

    private static CookieManager cookieManager;
    private static ArrayList<Upgrade> upgrades;
    private JPanel panel1;
    private JLabel cookieLabel;

    public static void main(String[] args) {
        upgrades = createUpgrades(); // Las upgrades van afuera de CookieManager puesto que no queremos crear 8000 instancias de cada una.
        cookieManager = new CookieManager();

        // TODO: Preguntarle al lic si esto es válido :shrug:
        Runnable gameTask = new Runnable() {
            @Override
            public void run() {
                startGame(cookieManager);
            }
        };

        // Iniciamos el juego en un nuevo thread. Esto es para que no se me bloquee el main thread.
        Thread gameThread = new Thread(gameTask);
        gameThread.start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("--------- Menu ---------");
            System.out.println("1) Mostrar cuántas galletas y mejoras tienes.");
            System.out.println("2) Mostrar las mejoras y sus precios.");
            System.out.println("3) Comprar mejoras.");
            System.out.println("4) Vender mejoras.");
            System.out.print("Escoge una opción: ");

            int opcion = scanner.nextInt();
            switch(opcion) {
                case 1:
                    printStats();
                    break;
                case 2:
                    showUpgrades();
                    break;
                case 3:
                    buyUpgrades();
                    break;
                case 4:
                    sellUpgrades();
                    break;
                default:
                    System.out.println("Opcion no valida, intente de nuevo.");
                    break;
            }
        }

    }


    // TODO: balancear los precios de las mejoras y los cookies por segundo. Hacerlo más elegante también, quizá con un ciclo for y alguna formula.
    private static ArrayList<Upgrade> createUpgrades() {
        ArrayList<Upgrade> upgrades = new ArrayList<>();
        upgrades.add(new Upgrade("Click", 10, 1));
        upgrades.add(new Upgrade("Grandma", 100, 5));
        upgrades.add(new Upgrade("Farm", 500, 10));
        upgrades.add(new Upgrade("Mine", 1000, 15));
        upgrades.add(new Upgrade("Factory", 2000, 20));
        upgrades.add(new Upgrade("Bank", 5000, 25));
        upgrades.add(new Upgrade("Temple", 10000, 30));
        upgrades.add(new Upgrade("Magic Tower", 50000, 50));
        upgrades.add(new Upgrade("Shipment", 70000, 100));
        upgrades.add(new Upgrade("Alchemy Lab", 80000, 200));
        upgrades.add(new Upgrade("Portal", 100000, 300));
        upgrades.add(new Upgrade("Time Machine", 103000, 400));
        upgrades.add(new Upgrade("Antimatter Condenser", 150000, 500));
        upgrades.add(new Upgrade("Prism", 180000, 600));
        upgrades.add(new Upgrade("Chancemaker", 200000, 700));
        upgrades.add(new Upgrade("Fractal Engine", 250000, 800));
        upgrades.add(new Upgrade("Javascript Console", 500000, 900));
        upgrades.add(new Upgrade("Idleverse", 700000, 1000));
        upgrades.add(new Upgrade("Cookieverse", 900000, 1100));
        upgrades.add(new Upgrade("Cheated Cookies", 1000000, 1200));

        return upgrades;
    }

    // TODO: Preguntarle al licenciado si se puede usar Thread
    private static void startGame(CookieManager cookieManager) {
        boolean running = true;
        while (running) {
            int cookiesPerSecond = (int) cookieManager.getCookiesPerSecond();
            cookieManager.addCookies(cookiesPerSecond);
            try {
                Thread.sleep(1000); // 1 segundo.
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Se interrumpio el juego.");
                running = false;
            }
        }
    }

    /** Los métodos de aquí para abajo serán removidos en favor de la GUI. Es sólo para testear. */
    private static void printStats() {
        System.out.println("Tienes " + cookieManager.getCookies() + " galletas.\nProduces " + cookieManager.getCookiesPerSecond() + " galleta(s)/s.");
        for (Upgrade upgrade : upgrades) {
            if (upgrade.getQuantity() > 0) {
                System.out.println(upgrade.getName() + ": " + upgrade.getQuantity());
            }
        }
    }

    private static void showUpgrades() {
        System.out.println("--------- Mejoras ---------");
        for (int i = 0; i < upgrades.size(); i++) {
            Upgrade upgrade = upgrades.get(i);
            System.out.println(i + ") " + upgrade.getName() + " - Costo: " + upgrade.getCurrentCost() + " - Galletas/s: " + upgrade.getCookiesPerSecond());
        }
    }

    private static void buyUpgrades() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("¿Qué mejora quieres comprar?");
        showUpgrades();
        System.out.println("Escoge una mejora: ");
        int upgradeIndex = scanner.nextInt();

        Upgrade upgrade = upgrades.get(upgradeIndex);
        cookieManager.buyUpgrade(upgrade);
    }

    // TODO para este metodo: chequear que de verdad tenga la mejora antes de venderla.
    private static void sellUpgrades() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("¿Qué mejora quieres vender?");
        showUpgrades();
        System.out.println("Escoge una mejora: ");
        int upgradeIndex = scanner.nextInt();

        Upgrade upgrade = upgrades.get(upgradeIndex);
        cookieManager.sellUpgrade(upgrade);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}