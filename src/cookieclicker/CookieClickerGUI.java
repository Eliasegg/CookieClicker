package cookieclicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 *
 * @author Elias
 */
public class CookieClickerGUI extends javax.swing.JFrame {

    /**
     * Creates new form CookieClickerGUI
     */
    public CookieClickerGUI() {
        CookieClicker.startGame();
        initComponents();
        setupCookie();
        populateUpgrades();
        setScrollSpeed(20, 100);
        customizeScrollBar();
        refreshCookieCount();
    }

    private void setupCookie() {
        bg.setLayout(null);
        resizeAndSetImage(cookieButton, "src/cookieclicker/images/PerfectCookie.png", 330, 330);

        cookieButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                CookieClicker.getCookieManager().clickCookie();
                cookieCount.setText(CookieClicker.getCookieManager().getCookies() + "");

                JLabel clickLabel = new JLabel("+1");
                clickLabel.setFont(new Font("Arial", Font.BOLD, 24));
                clickLabel.setForeground(Color.WHITE);
                Point clickLocation = SwingUtilities.convertPoint(cookieButton, e.getPoint(), bg);
                clickLabel.setBounds(clickLocation.x, clickLocation.y, 50, 30);
                clickLabel.setFocusable(false);
                clickLabel.setEnabled(false);
                clickLabel.setIgnoreRepaint(true);
                bg.add(clickLabel, 0);
                bg.revalidate();
                bg.repaint();

                new Timer(50, new ActionListener() {
                    int opacity = 255;

                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        opacity -= 5;
                        clickLabel.setForeground(new Color(255, 255, 255, opacity));

                        if (opacity <= 0) {
                            ((Timer) evt.getSource()).stop();
                            bg.remove(clickLabel);
                            bg.revalidate();
                            bg.repaint();
                        }
                    }
                }).start();
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


    private void refreshCookieCount() {
        Timer SimpleTimer = new Timer(1000, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                cookieCount.setText(CookieClicker.getCookieManager().getCookies() + "");
                perSecondText.setText(CookieClicker.getCookieManager().getCookiesPerSecond() + "");
            }
        });
        SimpleTimer.start();
    }

    /**
     * Método para redimensionar y establecer una imagen en un JLabel cuando el componente se redimensiona.
     * @param label - JLabel donde se mostrará la imagen.
     * @param imagePath - Ruta de la imagen.
     */
    private void resizeOnComponentAppear(JLabel label, String imagePath) {
        label.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeAndSetImage(label, "src/cookieclicker/images/CursorIconTransparent.png", 80, 80);
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
     * Poblar las mejoras en el panel de mejoras.
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
            upgradePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            JLabel upgradeImage = new JLabel();
            upgradeImage.setPreferredSize(new Dimension(80, 80));
            upgradePanel.add(upgradeImage, BorderLayout.WEST);
            resizeOnComponentAppear(upgradeImage, "src/cookieclicker/images/CursorIconTransparent.png");

            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel upgradeName = new JLabel(upgrade.getName());
            upgradeName.setFont(new Font("Arial", Font.BOLD, 18));
            textPanel.add(Box.createVerticalStrut(10)); // Espacio antes del texto
            textPanel.add(upgradeName);

            JLabel upgradeCost = new JLabel(upgrade.getCurrentCost() + "");
            upgradeCost.setFont(new Font("Arial", Font.PLAIN, 14));
            upgradeCost.setForeground(new Color(0, 128, 0)); // Green color
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
                    boolean bought = CookieClicker.getCookieManager().buyUpgrade(upgrade);
                    if (bought) {
                        upgradeCount.setText(upgrade.getQuantity() + "");
                        upgradeCost.setText(upgrade.getCurrentCost() + "");
                        perSecondText.setText(CookieClicker.getCookieManager().getCookiesPerSecond() + "");
                    }
                }
            });

            upgradesPanel.add(upgradePanel);
        }

        upgradesScrollPane.setViewportView(upgradesPanel);
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
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg = new javax.swing.JPanel();
        cookieButton = new javax.swing.JLabel();
        cookieLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        cookieCountText = new javax.swing.JLabel();
        cookieCount = new javax.swing.JLabel();
        upgradesScrollPane = new javax.swing.JScrollPane();
        perSecondText = new javax.swing.JLabel();
        perSecondLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1200, 700));
        setResizable(false);
        setSize(new java.awt.Dimension(1200, 700));

        bg.setBackground(new java.awt.Color(255, 255, 255));
        bg.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        bg.add(cookieButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 260, 340, 340));

        cookieLabel.setFont(new java.awt.Font("Samurai Blast", 0, 48)); // NOI18N
        cookieLabel.setText("GALLETAS");
        bg.add(cookieLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, 250, 100));

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator1.setAlignmentX(1.0F);
        bg.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, 340, 20));

        cookieCountText.setFont(new java.awt.Font("Dubai", 1, 28)); // NOI18N
        cookieCountText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cookieCountText.setText("MIL");
        bg.add(cookieCountText, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 130, 210, 40));

        cookieCount.setFont(new java.awt.Font("Dubai", 1, 48)); // NOI18N
        cookieCount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cookieCount.setText("0");
        bg.add(cookieCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 90, 210, 40));

        upgradesScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        bg.add(upgradesScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, -10, 770, 710));

        perSecondText.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        perSecondText.setText("0");
        bg.add(perSecondText, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 180, 170, 20));

        perSecondLabel.setText("Por segundo:");
        bg.add(perSecondLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 180, 80, 20));

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
                new CookieClickerGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg;
    private javax.swing.JLabel cookieButton;
    private javax.swing.JLabel cookieCount;
    private javax.swing.JLabel cookieCountText;
    private javax.swing.JLabel cookieLabel;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel perSecondLabel;
    private javax.swing.JLabel perSecondText;
    private javax.swing.JScrollPane upgradesScrollPane;
    // End of variables declaration//GEN-END:variables
}
