package cookieclicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 *
 * @author Elias
 */
public class CookieClickerGUI extends javax.swing.JFrame {

    private List<JLabel> clickLabels = new ArrayList<>();

    /**
     * Inicializa la interfaz gráfica de Cookie Clicker.
     * Se inicia el juego y se configuran los componentes de la interfaz.
     *
     */
    public CookieClickerGUI() {
        CookieClicker.startGame();
        initComponents();
        setupCookie();
        populateUpgrades();
        setScrollSpeed(20, 100);
        customizeScrollBar();
        refreshCookieCount();
        updateUpgradesAvailability();
    }

    /**
     * Método para mostrar el menú de administrador en la consola.
     * Se pueden añadir galletas y obtener edificios de forma gratuita.
     * Corre en un hilo separado para no bloquear la interfaz gráfica.
     *
     * Útiliza los 3 tipos de print de System.out: print, printf y println como solicitado en el documento.
     */
    private static void menuAdministrador() {
        new Thread(new Runnable() {
            public void run() {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    System.out.println("-------------------- Cookie Clicker --------------------");
                    System.out.println("1. Anadir galletas");
                    System.out.println("2. Obtener edificio");
                    System.out.print("Escoge una opcion: ");
                    int choice = scanner.nextInt();

                    switch (choice) {
                        case 1:
                            System.out.print("Cantidad de galletas: ");
                            int cookies = scanner.nextInt();
                            CookieClicker.getCookieManager().addCookies(cookies);
                            System.out.printf("Se anadieron %d galletas.\n", cookies);
                            break;
                        case 2:
                            System.out.println("Lista de edificios:");
                            ArrayList<Upgrade> upgrades = CookieClicker.getUpgrades();
                            for (int i = 0; i < upgrades.size(); i++) {
                                System.out.printf("%d) %s\n", i + 1, upgrades.get(i).getName());
                            }
                            System.out.print("Edificio a obtener: ");
                            int buildingIndex = scanner.nextInt() - 1;

                            System.out.print("Cantidad: ");
                            int quantity = scanner.nextInt();
                            for (int i = 0; i < quantity; i++) {
                                CookieClicker.getCookieManager().getUpgrade(upgrades.get(buildingIndex));
                            }
                            System.out.printf("Se obtuvieron %d edificios %s.\n", quantity, upgrades.get(buildingIndex).getName());
                            break;
                        default:
                            System.out.println("Opción no valida.");
                    }
                }

            }
        }).start();
    }

    /**
     * Configura el botón de galleta.
     *
     * La galleta se puede clickear para obtener galletas adicionales.
     * La cantidad de galletas que se obtienen por click depende de cuántas mejoras de 'Click' hayas comprado.
     * Se muestra un label con la cantidad de galletas obtenidas por click (que es aleatoria).
     * Se muestra un máximo de 5 labels con la cantidad de galletas obtenidas por click y después desaparecen.
     *
     * Se actualiza la cantidad de galletas en la interfaz.
     */
    private void setupCookie() {
        bg.setLayout(null);
        resizeAndSetImage(cookieButton, "src/cookieclicker/images/PerfectCookie.png", 330, 330);

        cookieButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int cookiesPerClick = CookieClicker.getCookieManager().clickCookie();
                cookieCount.setText(CookieClicker.getCookieManager().getCookiesPrefix() + "");
                cookieCountText.setText(CookieClicker.getCookieManager().getCookiesSuffix());

                JLabel clickLabel = new JLabel("+" + cookiesPerClick);
                clickLabel.setFont(new Font("Arial", Font.BOLD, 34));
                clickLabel.setForeground(Color.WHITE);
                Point clickLocation = SwingUtilities.convertPoint(cookieButton, e.getPoint(), bg);
                clickLabel.setBounds(clickLocation.x, clickLocation.y, 100, 30);
                clickLabel.setFocusable(false);
                clickLabel.setEnabled(false);

                if (clickLabels.size() >= 5) {
                    JLabel oldestLabel = clickLabels.remove(0);
                    bg.remove(oldestLabel);
                }

                clickLabels.add(clickLabel);
                bg.add(clickLabel, 0);
                bg.revalidate();
                bg.repaint();

                Timer fadeTimer = new Timer(50, new ActionListener() {
                    int opacity = 255;

                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        opacity = Math.max(0, opacity - 25); // Decrementar la opacidad en 25. No bajar de 0.
                        clickLabel.setForeground(new Color(255, 255, 255, opacity));

                        if (opacity <= 0) {
                            ((Timer) evt.getSource()).stop();
                            clickLabels.remove(clickLabel);
                            bg.remove(clickLabel);
                            bg.revalidate();
                            bg.repaint();
                        }
                    }
                });
                fadeTimer.start();

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                cookieButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                resizeAndSetImage(cookieButton, "src/cookieclicker/images/PerfectCookie.png", 340, 340);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cookieButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                resizeAndSetImage(cookieButton, "src/cookieclicker/images/PerfectCookie.png", 330, 330);
            }
        });
    }


    /**
     * Actualiza la cantidad de galletas en la interfaz.
     *
     * Se actualiza la cantidad de galletas en la interfaz cada segundo.
     */
    private void refreshCookieCount() {
        Timer cookieTimer = new Timer(1000, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                int cookiesPerSecond = (int) CookieClicker.getCookieManager().getCookiesPerSecond();
                CookieClicker.getCookieManager().addCookies(cookiesPerSecond);

                cookieCount.setText(CookieClicker.getCookieManager().getCookiesPrefix() + "");
                cookieCountText.setText(CookieClicker.getCookieManager().getCookiesSuffix());
                perSecondText.setText(CookieClicker.getCookieManager().getCookiesPerSecond() + "");
            }
        });
        cookieTimer.start();
    }

    /**
     * Actualiza la disponibilidad de las mejoras en la interfaz.
     *
     * Busca las mejoras que están dentro del JScrollPane y actualiza su disponibilidad.
     * Si el jugador tiene suficientes galletas para comprar la mejora, se muestra en verde y se puede clickear.
     * Sino, se muestra en gris y no se puede clickear.
     *
     * Se actualiza cada milisegundo.
     */
    private void updateUpgradesAvailability() {
        Timer upgradeTimer = new Timer(1, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if (upgradesScrollPane.getViewport().getView() instanceof Container) {
                    for (Component component : ((Container) upgradesScrollPane.getViewport().getView()).getComponents()) {
                        if (component instanceof JPanel) {
                            JPanel upgradePanel = (JPanel) component;
                            JPanel textPanel = (JPanel) upgradePanel.getComponent(1);
                            JLabel upgradeCount = (JLabel) upgradePanel.getComponent(2);
                            JLabel upgradeCost = (JLabel) textPanel.getComponent(2);

                            // Esto lo seteamos en la función populateUpgrades.
                            Upgrade upgrade = (Upgrade) textPanel.getClientProperty("upgrade");

                            // Si no se encontró la mejora, continuar con el siguiente loop. Fail-safe.
                            if (upgrade == null) {
                                continue;
                            }

                            upgradeCount.setText(upgrade.getQuantity() + "");
                            upgradeCost.setText(upgrade.getCurrentCost(upgrade.getQuantity()) + "");
                            
                            if (CookieClicker.getCookieManager().getCookies() >= upgrade.getCurrentCost(upgrade.getQuantity())) {
                                upgradePanel.setBackground(new Color(184,216,190));
                                upgradePanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

                                textPanel.setBackground(new Color(184,216,190));
                                textPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                            } else {
                                upgradePanel.setBackground(new Color(176,176,176));
                                upgradePanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                                textPanel.setBackground(new Color(176,176,176));
                                textPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            }
                        }


                    }
                }
            }
        });
        upgradeTimer.start();
    }


    /**
     * Poblar las mejoras en el panel de mejoras.
     *
     * Se añade un MouseListener para comprar la mejora al hacer clic en ella.
     * Se añade un MouseListener para vender la mejora al hacer clic derecho en ella.
     */
    public void populateUpgrades() {
        JPanel upgradesPanel = new JPanel();
        upgradesPanel.setLayout(new BoxLayout(upgradesPanel, BoxLayout.Y_AXIS));

        // Añadir las mejoras al panel
        for (int i = 0; i < CookieClicker.getUpgrades().size(); i++) {
            Upgrade upgrade = CookieClicker.getUpgrades().get(i);

            JPanel upgradePanel = new JPanel();
            upgradePanel.setLayout(new BorderLayout(10, 10));
            upgradePanel.setMaximumSize(new Dimension(760, 80));
            upgradePanel.setBackground(new Color(176,176,176));
            upgradePanel.setBorder(BorderFactory.createLineBorder(Color.black));

            JLabel upgradeImage = new JLabel();
            upgradeImage.setPreferredSize(new Dimension(80, 80));
            upgradePanel.add(upgradeImage, BorderLayout.WEST);
            resizeUpgrade(upgradeImage, upgrade.getImageName());

            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            textPanel.setBackground(new Color(176,176,176));
            JLabel upgradeName = new JLabel(upgrade.getName());
            upgradeName.setFont(new Font("Arial", Font.BOLD, 18));
            textPanel.add(Box.createVerticalStrut(10)); // Espacio antes del texto
            textPanel.add(upgradeName);
            textPanel.putClientProperty("upgrade", upgrade); // Importante: Guardar el objeto Upgrade en el textPanel. Asi es como lo retornamos.
            textPanel.setName("upgradePane" + i);

            JLabel upgradeCost = new JLabel(upgrade.getCurrentCost(upgrade.getQuantity()) + "");
            upgradeCost.setFont(new Font("Arial", Font.PLAIN, 14));
            upgradeCost.setForeground(new Color(0, 128, 0));
            textPanel.add(upgradeCost);

            upgradePanel.add(textPanel, BorderLayout.CENTER);

            JLabel upgradeCount = new JLabel(upgrade.getQuantity() + "");
            upgradeCount.setFont(new Font("Arial", Font.BOLD, 36));
            upgradeCount.setHorizontalAlignment(SwingConstants.RIGHT);
            upgradeCount.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // Margen a la derecha 
            upgradeCount.setPreferredSize(new Dimension(80, 80));
            upgradePanel.add(upgradeCount, BorderLayout.EAST);

            upgradePanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    // Botón izquierdo, comprar la mejora si tiene suficientes galletas.
                    if (e.getButton() == MouseEvent.BUTTON1 && CookieClicker.getCookieManager().getCookies() >= upgrade.getCurrentCost()) {
                        boolean bought = CookieClicker.getCookieManager().buyUpgrade(upgrade);
                        textPanel.setBackground(new Color(176,176,176));

                        // Si se compró la mejora, actualizar la interfaz.
                        if (bought) {
                            cookieCount.setText(CookieClicker.getCookieManager().getCookiesPrefix() + "");
                            cookieCountText.setText(CookieClicker.getCookieManager().getCookiesSuffix());
                            upgradeCount.setText(upgrade.getQuantity() + "");
                            upgradeCost.setText(upgrade.getCurrentCost(upgrade.getQuantity()) + "");
                            perSecondText.setText(CookieClicker.getCookieManager().getCookiesPerSecond() + "");
                        }
                    } else if (e.getButton() == MouseEvent.BUTTON3) { // Botón derecho, vender la mejora.
                        boolean sold = CookieClicker.getCookieManager().sellUpgrade(upgrade);

                        // Si se vendió la mejora, actualizar la interfaz.
                        if (sold) {
                            upgradeCount.setText(upgrade.getQuantity() + "");
                            cookieCount.setText(CookieClicker.getCookieManager().getCookiesPrefix() + "");
                            cookieCountText.setText(CookieClicker.getCookieManager().getCookiesSuffix());
                            perSecondText.setText(CookieClicker.getCookieManager().getCookiesPerSecond() + "");
                        }
                    }

                }
            });
            // Añadir el panel de la mejora al panel de mejoras
            upgradesPanel.add(upgradePanel);
        }

        // Añadir el panel de mejoras al JScrollPane para que sea scrollable
        upgradesScrollPane.setViewportView(upgradesPanel);
    }

    /**
     * Redimensiona una imagen en un JLabel cuando el componente aparece.
     *
     * Útil para redimensionar las imágenes de las mejoras en el panel de mejoras.
     * @param label - JLabel donde se mostrará la imagen.
     * @param imageName - Nombre de la imagen. Basado en el paquete de images.
     */
    private void resizeUpgrade(JLabel label, String imageName) {
        // Redimensionar la imagen cuando el componente aparece
        label.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) { // Se ejecuta cuando el componente aparece.
                resizeAndSetImage(label, "src/cookieclicker/images/" + imageName, 80, 80);
            }
        });
    }

    /**
     * Método para redimensionar y establecer una imagen en un JLabel.
     * @param label - JLabel donde se mostrará la imagen.
     * @param imagePath - Ruta de la imagen.
     */
    private void resizeAndSetImage(JLabel label, String imagePath, int width, int height) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(resizedImage));
    }

    /**
     * Personaliza el estilo del scrollbar del JScrollPane.
     */
    private void customizeScrollBar() {
        JScrollBar verticalScrollBar = upgradesScrollPane.getVerticalScrollBar();
        verticalScrollBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(128, 128, 128);
                this.trackColor = new Color(200, 200, 200);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                jbutton.setMinimumSize(new Dimension(0, 0));
                jbutton.setMaximumSize(new Dimension(0, 0));
                return jbutton;
            }
        });

        verticalScrollBar.setPreferredSize(new Dimension(15, Integer.MAX_VALUE));
    }

    /**
     * Ajusta la velocidad de scroll del JScrollPane.
     * @param unitIncrement - Velocidad de scroll al hacer clic en las flechas de scroll.
     * @param blockIncrement - Velocidad de scroll al hacer clic en el track del scrollbar.
     */
    private void setScrollSpeed(int unitIncrement, int blockIncrement) {
        JScrollBar verticalScrollBar = upgradesScrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(unitIncrement);
        verticalScrollBar.setBlockIncrement(blockIncrement);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg = new javax.swing.JPanel();
        nombreUsuario = new javax.swing.JLabel();
        cookieButton = new javax.swing.JLabel();
        cookieLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        cookieCountText = new javax.swing.JLabel();
        cookieCount = new javax.swing.JLabel();
        perSecondText = new javax.swing.JLabel();
        perSecondLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        upgradesScrollPane = new javax.swing.JScrollPane();
        cookieLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1200, 700));
        setResizable(false);
        setSize(new java.awt.Dimension(1200, 700));

        bg.setBackground(new java.awt.Color(255, 255, 255));
        bg.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        nombreUsuario.setFont(new java.awt.Font("Samurai Blast", 1, 24)); // NOI18N
        nombreUsuario.setForeground(java.awt.Color.white);
        nombreUsuario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bg.add(nombreUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 630, 410, 30));
        bg.add(cookieButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 260, 340, 340));

        cookieLabel.setFont(new java.awt.Font("Samurai Blast", 0, 48)); // NOI18N
        cookieLabel.setForeground(new java.awt.Color(255, 255, 255));
        cookieLabel.setText("GALLETAS");
        bg.add(cookieLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, 250, 100));

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator1.setAlignmentX(1.0F);
        bg.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, 340, 20));

        cookieCountText.setFont(new java.awt.Font("Dubai", 1, 28)); // NOI18N
        cookieCountText.setForeground(java.awt.Color.white);
        cookieCountText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bg.add(cookieCountText, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 130, 210, 40));

        cookieCount.setFont(new java.awt.Font("Dubai", 1, 48)); // NOI18N
        cookieCount.setForeground(new java.awt.Color(255, 255, 255));
        cookieCount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cookieCount.setText("0");
        bg.add(cookieCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, 210, 40));

        perSecondText.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        perSecondText.setForeground(java.awt.Color.white);
        perSecondText.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        perSecondText.setText("0");
        bg.add(perSecondText, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 190, 170, 20));

        perSecondLabel.setFont(new java.awt.Font("OCR A Extended", 0, 12)); // NOI18N
        perSecondLabel.setForeground(java.awt.Color.white);
        perSecondLabel.setText("Por segundo:");
        bg.add(perSecondLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 190, 110, 20));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cookieclicker/images/CookieClicker.jpg"))); // NOI18N
        bg.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 410, 700));

        upgradesScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        bg.add(upgradesScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, -10, 770, 710));

        cookieLabel1.setFont(new java.awt.Font("Samurai Blast", 0, 48)); // NOI18N
        cookieLabel1.setForeground(new java.awt.Color(255, 255, 255));
        cookieLabel1.setText("GALLETAS");
        bg.add(cookieLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, 250, 100));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);

        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CookieClickerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CookieClickerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CookieClickerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CookieClickerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                System.out.println("Iniciando Cookie Clicker...");
                CookieClickerGUI gui = new CookieClickerGUI();
                gui.setVisible(true);

                // Mostrar las instrucciones del juego usando un JOptionPane.
                String instructions = "<html><body style='width: 300px; padding: 20px'>"
                        + "<h2>Bienvenido a Cookie Clicker!</h2>"
                        + "<p>Este es un juego en el que tu objetivo es obtener la mayor cantidad de galletas posible. Aquí te explicamos cómo jugar:</p>"
                        + "<ul style='padding-left: 10px;'>"
                        + "<li style='margin-bottom: 10px;'>Comienzas el juego con 0 galletas y generas 1 galleta por segundo automáticamente.</li>"
                        + "<li style='margin-bottom: 10px;'>Puedes hacer click en la galleta en la pantalla para obtener galletas adicionales. La cantidad de galletas que obtienes por click depende de cuántas mejoras de 'Click' hayas comprado.</li>"
                        + "<li style='margin-bottom: 10px;'>Puedes comprar mejoras para aumentar la cantidad de galletas que generas por segundo. Cada mejora tiene un costo que aumenta cada vez que compras una.</li>"
                        + "<li style='margin-bottom: 10px;'>También puedes vender mejoras al hacer click derecho en una mejora que tengas. Cuando vendes una mejora, obtienes galletas y reduces la cantidad de galletas que generas por segundo.</li>"
                        + "<li style='margin-bottom: 10px;'>En la consola, puedes añadir galletas y obtener mejoras de forma gratuita para probar el juego. Simplemente sigue las instrucciones en la consola.</li>"
                        + "</ul>"
                        + "<p>¡Disfruta el juego y trata de obtener la mayor cantidad de galletas posible!</p>"
                        + "</body></html>";
                JOptionPane.showMessageDialog(gui, instructions, "Instrucciones del juego", JOptionPane.INFORMATION_MESSAGE);
                menuAdministrador(); // Mostrar el menú de administrador en la consola.
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg;
    private javax.swing.JLabel cookieButton;
    private javax.swing.JLabel cookieCount;
    private javax.swing.JLabel cookieCountText;
    private javax.swing.JLabel cookieLabel;
    private javax.swing.JLabel cookieLabel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel nombreUsuario;
    private javax.swing.JLabel perSecondLabel;
    private javax.swing.JLabel perSecondText;
    private javax.swing.JScrollPane upgradesScrollPane;
    // End of variables declaration//GEN-END:variables
}
