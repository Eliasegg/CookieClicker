package cookieclicker;

public class Upgrade {

    private String name;
    private int baseCost;
    private int cookiesPerSecond;
    private int quantity;
    private String imageName;

    public Upgrade(String name, int baseCost, int cookiesPerSecond, String imageName) {
        this.name = name;
        this.baseCost = baseCost;
        this.cookiesPerSecond = cookiesPerSecond;
        this.imageName = imageName;
        this.quantity = 0;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getCurrentCost() {
        return baseCost;
    }

    public int getCookiesPerSecond() {
        return cookiesPerSecond;
    }

    public int getCurrentCost(int quantity) {
        if (quantity == 0) {
            return baseCost;
        } else {
            return (int) (baseCost * Math.pow(1.15, quantity - 1)) + getCurrentCost(quantity - 1);
        }
    }

    public void purchase() {
        quantity++;
    }

    public void sell() {
        quantity--;
    }

    public String getImageName() {
        return imageName;
    }

}