package test;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author behnish
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ScratchSpace2 {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                AnimatedPanel panel = new AnimatedPanel();

                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setContentPane(panel);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                panel.animate();
            }
        });
    }

    public static class AnimatedPanel extends JPanel {

        private float progress = 0.0f; // a number between 0.0 and 1.0
        private BufferedImage buffer;

        public AnimatedPanel() {
            setPreferredSize(new Dimension(800, 600));
//            setOpaque(true);
        }

        public void animate() {
            final int animationTime = 1000;
            int framesPerSecond = 100;
            int delay = 1000 / framesPerSecond;
            final long start = System.currentTimeMillis();
            final Timer timer = new Timer(delay, null);
            timer.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    final long now = System.currentTimeMillis();
                    final long elapsed = now - start;

                    int width = getWidth();

                    progress = (float) elapsed / animationTime;

                    updateBuffer();
                    repaint();

                    if (elapsed >= animationTime) {
                        timer.stop();
                    }

                }
            });
            timer.start();
        }

        @Override
        public void invalidate() {
            buffer = null;
            updateBuffer();
            super.invalidate();
        }

        protected void updateBuffer() {

            if (getWidth() > 0 && getHeight() > 0) {

                if (buffer == null) {

                    buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

                }

                Graphics2D g2d = buffer.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
                g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
                int width = getWidth();
                int height = getHeight();
                float progressWidth = width * progress;
                g2d.setColor(Color.BLUE);
                g2d.fill(new Rectangle2D.Float(0, 0, progressWidth, height));
                g2d.setColor(Color.RED);
                g2d.fill(new Rectangle2D.Float(progressWidth, 0, width - progressWidth, height));
                g2d.dispose();

            }

        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            if (buffer != null) {
                g2d.drawImage(buffer, 0, 0, this);
            }
//            int width = getWidth();
//            int progressWidth = (int) (width * progress);
//            g2d.setColor(Color.BLUE);
//            g2d.fillRect(0, 0, progressWidth, getHeight());
//            g2d.setColor(Color.RED);
//            g2d.fillRect(progressWidth, 0, width - progressWidth, getHeight());
        }
    }
}