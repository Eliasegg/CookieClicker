package cookieclicker;

import java.util.ArrayList;

/**
 * Clase que inicia el juego y maneja las mejoras y el cookieManager.
 *
 * Esta clase es la que se encarga de iniciar el juego y de crear las mejoras que se pueden comprar.
 * A su vez, también crea el cookieManager que se encarga de manejar las cookies del jugador.
 * Tiene métodos estáticos para obtener el cookieManager y las mejoras.
 */
public class CookieClicker {

    private static CookieManager cookieManager;
    private static ArrayList<Upgrade> upgrades;

    public static void startGame() {
        upgrades = createUpgrades();
        cookieManager = new CookieManager();
    }

    // TODO: balancear los precios de las mejoras y los cookies por segundo. Hacerlo más elegante también, quizá con un ciclo for y alguna formula.
    private static ArrayList<Upgrade> createUpgrades() {
        ArrayList<Upgrade> upgrades = new ArrayList<>();
        upgrades.add(new Upgrade("Click", 10, 1, "CursorIconTransparent.png"));
        upgrades.add(new Upgrade("Grandma", 100, 5, "GrandmaIconTransparent.png"));
        upgrades.add(new Upgrade("Farm", 500, 10, "FarmIconTransparent.png"));
        upgrades.add(new Upgrade("Mine", 1000, 15, "MineIconTransparent.png"));
        upgrades.add(new Upgrade("Factory", 2000, 20, "FactoryIconTransparent.png"));
        upgrades.add(new Upgrade("Bank", 5000, 25, "BankIconLarge.png"));
        upgrades.add(new Upgrade("Temple", 10000, 30, "TempleIconLarge.png"));
        upgrades.add(new Upgrade("Magic Tower", 50000, 50, "WizardTowerIconLarge.png"));
        upgrades.add(new Upgrade("Shipment", 70000, 100, "ShipmentIconTransparent.png"));
        upgrades.add(new Upgrade("Alchemy Lab", 80000, 200, "AlchemyLabIconTransparent.png"));
        upgrades.add(new Upgrade("Portal", 100000, 300, "PortalIconTransparent.png"));
        upgrades.add(new Upgrade("Time Machine", 103000, 400, "TimeMachineIconTransparent.png"));
        upgrades.add(new Upgrade("Antimatter Condenser", 150000, 500, "AntimatterCondenserIconTransparent.png"));
        upgrades.add(new Upgrade("Prism", 180000, 600, "PrismIconTransparent.png"));
        upgrades.add(new Upgrade("Chancemaker", 200000, 700, "Change.png"));
        upgrades.add(new Upgrade("Fractal Engine", 250000, 800, "Fractal_Engine.png"));
        upgrades.add(new Upgrade("Javascript Console", 500000, 900, "Javascript_console_portrait.png"));
        upgrades.add(new Upgrade("Idleverse", 700000, 1000, "Idleverse_portrait.png"));
        upgrades.add(new Upgrade("Cookieverse", 900000, 1100, "Cortex_Baker_portrait.png"));
        upgrades.add(new Upgrade("Cheated Cookies", 1000000, 1200, "You_portrait.png"));
        return upgrades;
    }

    public static CookieManager getCookieManager() {
        return cookieManager;
    }

    public static ArrayList<Upgrade> getUpgrades() {
        return upgrades;
    }

}